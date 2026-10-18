package com.qlcc.service;

import com.qlcc.dto.SpecDtos.FeeTypeRequest;
import com.qlcc.entity.FeeType;
import com.qlcc.enums.*;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.FeeRecordRepository;
import com.qlcc.repository.FeeTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Nghiep vu Khoan thu theo dac ta (F-KT-01..08).
 * - Loai khoa cung sau khi tao (E1: "Không thể đổi loại khoản thu.")
 * - Xoa: ban ghi Da thu -> 409; ban ghi Chua thu bi xoa theo trong cung giao dich
 */
@Service
public class FeeTypeService {

    private final FeeTypeRepository feeTypeRepository;
    private final FeeRecordRepository feeRecordRepository;

    public FeeTypeService(FeeTypeRepository feeTypeRepository, FeeRecordRepository feeRecordRepository) {
        this.feeTypeRepository = feeTypeRepository;
        this.feeRecordRepository = feeRecordRepository;
    }

    public Page<FeeType> list(String search, FeeTypeType type, FeeTypeStatus status, Pageable pageable) {
        List<FeeType> all = feeTypeRepository.findAll().stream()
                .filter(f -> search == null || search.isBlank()
                        || f.getName().toLowerCase().contains(search.toLowerCase()))
                .filter(f -> type == null || f.getType() == type)
                .filter(f -> status == null || f.getStatus() == status)
                .sorted(Comparator.comparing(FeeType::getId))
                .collect(Collectors.toList());
        return pageOf(all, pageable);
    }

    public FeeType getById(Long id) {
        return feeTypeRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
    }

    /** So ban ghi Chua thu se bi xoa theo (dung trong hop xac nhan xoa). */
    public long unpaidRecordCount(Long id) {
        return feeRecordRepository.countByFeeType_IdAndStatus(id, BillStatus.UNPAID);
    }

    @Transactional
    public FeeType create(FeeTypeRequest req) {
        if (feeTypeRepository.existsByName(req.getName())) {
            throw ApiException.fieldError("name", "Tên khoản thu đã tồn tại.");
        }
        FeeType f = new FeeType();
        f.setName(req.getName());
        f.setType(req.getType());
        f.setCalcMethod(calcMethodFor(req.getType()));
        applyFields(f, req, true);
        return feeTypeRepository.save(f);
    }

    @Transactional
    public FeeType update(Long id, FeeTypeRequest req) {
        FeeType f = getById(id);
        if (req.getType() != null && req.getType() != f.getType()) {
            throw ApiException.fieldError("type", "Không thể đổi loại khoản thu.");
        }
        feeTypeRepository.findByName(req.getName()).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw ApiException.fieldError("name", "Tên khoản thu đã tồn tại.");
            }
        });
        applyFields(f, req, false);
        return feeTypeRepository.save(f);
    }

    @Transactional
    public void delete(Long id) {
        FeeType f = getById(id);
        if (feeRecordRepository.existsByFeeType_IdAndStatus(id, BillStatus.PAID)) {
            throw ApiException.conflict("Không thể xóa khoản thu đã có bản ghi thu phí.");
        }
        feeRecordRepository.findByFeeType_Id(id).forEach(feeRecordRepository::delete);
        feeTypeRepository.delete(f);
    }

    /** Bulk delete: mot dong loi khong lam hong ca lo (L-06). */
    @Transactional
    public Map<String, Object> bulkDelete(List<Long> ids) {
        List<String> failures = new ArrayList<>();
        int ok = 0;
        for (Long id : ids) {
            try {
                delete(id);
                ok++;
            } catch (ApiException e) {
                String name = feeTypeRepository.findById(id).map(FeeType::getName).orElse("#" + id);
                failures.add(name + ": " + e.getMessage());
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", ok);
        res.put("failed", failures.size());
        res.put("failures", failures);
        return res;
    }

    private void applyFields(FeeType f, FeeTypeRequest req, boolean isCreate) {
        FeeCalcMethod method = isCreate ? f.getCalcMethod() : calcMethodFor(f.getType());
        f.setCalcMethod(method);

        if (method == FeeCalcMethod.PER_AREA) {
            if (req.getRate() == null || req.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw ApiException.fieldError("rate", "Mức thu phải lớn hơn 0.");
            }
            f.setRate(req.getRate());
        } else if (method == FeeCalcMethod.PER_VEHICLE) {
            if (req.getRate() == null || req.getRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw ApiException.fieldError("rate", "Mức thu phải lớn hơn 0.");
            }
            f.setRate(req.getRate());
        } else if (method == FeeCalcMethod.VOLUNTARY) {
            f.setRate(null);
            if (req.getStartDate() == null) throw ApiException.fieldError("startDate", "Vui lòng chọn ngày bắt đầu.");
            if (req.getEndDate() == null) throw ApiException.fieldError("endDate", "Vui lòng chọn ngày kết thúc.");
            if (req.getEndDate().isBefore(req.getStartDate())) {
                throw ApiException.fieldError("endDate", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
            }
        }
        if (req.getStartDate() != null) f.setStartDate(req.getStartDate());
        if (req.getEndDate() != null) f.setEndDate(req.getEndDate());
        if (req.getStartDate() != null && req.getEndDate() != null
                && req.getEndDate().isBefore(req.getStartDate())) {
            throw ApiException.fieldError("endDate", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu.");
        }

        f.setSuggestedAmount(req.getSuggestedAmount());
        f.setMandatory(req.getMandatory() != null ? req.getMandatory()
                : f.getType() != FeeTypeType.DONATION);
        f.setStatus(req.getStatus() != null ? req.getStatus() : FeeTypeStatus.ACTIVE);
        f.setDescription(req.getDescription());
    }

    private FeeCalcMethod calcMethodFor(FeeTypeType type) {
        return switch (type) {
            case SERVICE, MANAGEMENT -> FeeCalcMethod.PER_AREA;
            case DONATION -> FeeCalcMethod.VOLUNTARY;
            case PARKING_MOTORBIKE, PARKING_CAR -> FeeCalcMethod.PER_VEHICLE;
            case ELECTRICITY, WATER, INTERNET -> FeeCalcMethod.MANUAL_PER_APARTMENT;
        };
    }

    /** Khoan hop le de lap danh sach thu: ACTIVE + mandatory + PER_AREA/PER_VEHICLE/MANUAL. */
    public void validateGeneratable(FeeType f) {
        if (f.getStatus() != FeeTypeStatus.ACTIVE) {
            throw ApiException.fieldError("feeTypeIds", "Khoản thu '" + f.getName() + "' đã ngừng áp dụng.");
        }
        if (!Boolean.TRUE.equals(f.getMandatory())) {
            throw ApiException.fieldError("feeTypeIds", "Khoản thu '" + f.getName() + "' không bắt buộc.");
        }
        if (f.getCalcMethod() == FeeCalcMethod.VOLUNTARY) {
            throw ApiException.fieldError("feeTypeIds", "Khoản đóng góp không lập danh sách thu.");
        }
    }

    static <T> Page<T> pageOf(List<T> items, Pageable pageable) {
        int from = (int) Math.min(pageable.getOffset(), items.size());
        int to = Math.min(from + pageable.getPageSize(), items.size());
        return new PageImpl<>(items.subList(from, to), pageable, items.size());
    }
}
