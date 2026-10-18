package com.qlcc.service;

import com.qlcc.dto.SpecDtos.*;
import com.qlcc.entity.*;
import com.qlcc.enums.*;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Nghiep vu Thu phi theo dac ta (F-TP-01..09) + Thong ke (F-TH).
 * - generate: tao fee_records cho can ho OCCUPIED, bo qua ban ghi da co (dedup_key)
 * - collect: Chua thu -> Da thu, ghi paidDate + collectedBy; 409 neu vua bi thu
 * - contribution: tao thang Da thu, contribution=1 (khong ap dedup)
 * - uncollect: Da thu -> Chua thu; khoan dong gop khong duoc huy
 */
@Service
public class FeeRecordService {

    private final FeeRecordRepository feeRecordRepository;
    private final FeeTypeRepository feeTypeRepository;
    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final VehicleRepository vehicleRepository;
    private final FeeTypeService feeTypeService;
    private final CurrentUserService currentUserService;

    public FeeRecordService(FeeRecordRepository feeRecordRepository,
                            FeeTypeRepository feeTypeRepository,
                            ApartmentRepository apartmentRepository,
                            ResidentRepository residentRepository,
                            VehicleRepository vehicleRepository,
                            FeeTypeService feeTypeService,
                            CurrentUserService currentUserService) {
        this.feeRecordRepository = feeRecordRepository;
        this.feeTypeRepository = feeTypeRepository;
        this.apartmentRepository = apartmentRepository;
        this.residentRepository = residentRepository;
        this.vehicleRepository = vehicleRepository;
        this.feeTypeService = feeTypeService;
        this.currentUserService = currentUserService;
    }

    // ---------- F-TP-01: xem va loc ----------
    public Page<Map<String, Object>> list(String search, Long apartmentId, Long feeTypeId,
                                          String period, String fromPeriod, String toPeriod,
                                          BillStatus status, Pageable pageable) {
        List<FeeRecord> all = feeRecordRepository.findAll().stream()
                .filter(fr -> apartmentId == null || fr.getApartment().getId().equals(apartmentId))
                .filter(fr -> feeTypeId == null || fr.getFeeType().getId().equals(feeTypeId))
                .filter(fr -> period == null || fr.getPeriod().equals(period))
                .filter(fr -> fromPeriod == null || fr.getPeriod().compareTo(fromPeriod) >= 0)
                .filter(fr -> toPeriod == null || fr.getPeriod().compareTo(toPeriod) <= 0)
                .filter(fr -> status == null || fr.getStatus() == status)
                .filter(fr -> matchesSearch(fr, search))
                .sorted(Comparator.comparing(FeeRecord::getPeriod).reversed()
                        .thenComparing(fr -> fr.getApartment().getApartmentNumber()))
                .collect(Collectors.toList());
        return FeeTypeService.pageOf(all, pageable).map(this::toDto);
    }

    private boolean matchesSearch(FeeRecord fr, String search) {
        if (search == null || search.isBlank()) return true;
        String s = search.toLowerCase();
        if (fr.getApartment().getApartmentNumber().toLowerCase().contains(s)) return true;
        return residentRepository.findCurrentHead(fr.getApartment().getId())
                .map(h -> h.getFullName().toLowerCase().contains(s))
                .orElse(false);
    }

    public Map<String, Object> getById(Long id) {
        FeeRecord fr = feeRecordRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        return toDto(fr);
    }

    // ---------- F-TP-02: lap danh sach thu ----------
    @Transactional
    public Map<String, Object> generate(GenerateRequest req) {
        List<FeeType> feeTypes = req.getFeeTypeIds().stream()
                .map(id -> feeTypeRepository.findById(id)
                        .orElseThrow(() -> ApiException.fieldError("feeTypeIds", "Khoản thu không tồn tại.")))
                .collect(Collectors.toList());
        feeTypes.forEach(feeTypeService::validateGeneratable);

        List<Apartment> occupied = apartmentRepository.findByStatusIn(List.of(ApartmentStatus.OCCUPIED));
        if (occupied.isEmpty()) {
            return Map.of("created", 0, "skipped", 0,
                    "message", "Không có căn hộ nào đang ở để lập danh sách thu.");
        }

        int created = 0, skipped = 0;
        for (Apartment apt : occupied) {
            for (FeeType ft : feeTypes) {
                if (feeRecordRepository.existsByApartment_IdAndFeeType_IdAndPeriod(
                        apt.getId(), ft.getId(), req.getPeriod())) {
                    skipped++;
                    continue;
                }
                FeeRecord fr = new FeeRecord();
                fr.setApartment(apt);
                fr.setFeeType(ft);
                fr.setPeriod(req.getPeriod());
                fr.setStatus(BillStatus.UNPAID);
                fr.setContribution(false);
                fr.setAmount(computeAmount(apt, ft));
                feeRecordRepository.save(fr);
                created++;
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("created", created);
        res.put("skipped", skipped);
        res.put("message", "Đã tạo " + created + " bản ghi. Đã bỏ qua " + skipped + " bản ghi đã tồn tại.");
        return res;
    }

    private BigDecimal computeAmount(Apartment apt, FeeType ft) {
        return switch (ft.getCalcMethod()) {
            case PER_AREA -> apt.getArea() != null && ft.getRate() != null
                    ? ft.getRate().multiply(BigDecimal.valueOf(apt.getArea()))
                        .setScale(0, RoundingMode.HALF_UP)
                    : null;
            case PER_VEHICLE -> {
                VehicleType vt = ft.getType() == FeeTypeType.PARKING_CAR
                        ? VehicleType.CAR : VehicleType.MOTORBIKE;
                long n = vehicleRepository.findByApartment_Id(apt.getId()).stream()
                        .filter(v -> v.getType() == vt && v.getStatus() == VehicleStatus.ACTIVE)
                        .count();
                yield ft.getRate() != null ? ft.getRate().multiply(BigDecimal.valueOf(n)) : null;
            }
            case MANUAL_PER_APARTMENT, VOLUNTARY -> null; // nhap tay tren dong (F-TP-09)
        };
    }

    // ---------- F-TP-03: thu mot ho ----------
    @Transactional
    public Map<String, Object> collect(Long id, CollectRequest req) {
        FeeRecord fr = feeRecordRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        doCollect(fr, req.getPaidDate(), req.getNote());
        return toDto(fr);
    }

    private void doCollect(FeeRecord fr, LocalDate paidDate, String note) {
        if (fr.getStatus() == BillStatus.PAID) {
            throw ApiException.conflict("Khoản thu đã được thu.");
        }
        if (paidDate == null) throw ApiException.fieldError("paidDate", "Chọn ngày thu.");
        if (paidDate.isAfter(LocalDate.now())) {
            throw ApiException.fieldError("paidDate", "Ngày thu không được ở tương lai.");
        }
        if (fr.getAmount() == null) {
            throw ApiException.badRequest("Chưa nhập số tiền cho bản ghi này.");
        }
        fr.setStatus(BillStatus.PAID);
        fr.setPaidDate(paidDate);
        fr.setCollectedBy(currentUserService.requireCurrentUser());
        if (note != null) fr.setNote(note);
        feeRecordRepository.save(fr);
    }

    // ---------- F-TP-04: thu nhieu ho ----------
    @Transactional
    public Map<String, Object> collectBulk(CollectBulkRequest req) {
        LocalDate paidDate = req.getPaidDate() != null ? req.getPaidDate() : LocalDate.now();
        List<FeeRecord> targets = resolveBulkTargets(req);
        int ok = 0;
        List<String> failures = new ArrayList<>();
        for (FeeRecord fr : targets) {
            try {
                doCollect(fr, paidDate, null);
                ok++;
            } catch (ApiException e) {
                failures.add(label(fr) + ": " + e.getMessage());
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", ok);
        res.put("skipped", failures.size());
        res.put("failures", failures);
        return res;
    }

    private List<FeeRecord> resolveBulkTargets(CollectBulkRequest req) {
        if (req.getIds() != null && !req.getIds().isEmpty()) {
            return feeRecordRepository.findByIdIn(req.getIds());
        }
        if (req.getFilter() != null) {
            Map<String, Object> f = req.getFilter();
            return feeRecordRepository.findByFilters(
                    (String) f.get("apartmentNumber"),
                    f.get("feeTypeId") != null ? Long.valueOf(f.get("feeTypeId").toString()) : null,
                    (String) f.get("period"),
                    f.get("status") != null ? BillStatus.valueOf(f.get("status").toString()) : null);
        }
        throw ApiException.badRequest("Chưa chọn dòng nào.");
    }

    private String label(FeeRecord fr) {
        return fr.getApartment().getApartmentNumber() + "/" + fr.getFeeType().getName() + "/" + fr.getPeriod();
    }

    // ---------- F-TP-05: ghi nhan dong gop ----------
    @Transactional
    public Map<String, Object> createContribution(ContributionRequest req) {
        Apartment apt = apartmentRepository.findById(req.getApartmentId())
                .orElseThrow(() -> ApiException.fieldError("apartmentId", "Căn hộ không tồn tại."));
        FeeType ft = feeTypeRepository.findById(req.getFeeTypeId())
                .orElseThrow(() -> ApiException.fieldError("feeTypeId", "Khoản đóng góp không tồn tại."));
        if (ft.getType() != FeeTypeType.DONATION) {
            throw ApiException.fieldError("feeTypeId", "Khoản thu này không phải khoản đóng góp.");
        }
        if (ft.getStatus() != FeeTypeStatus.ACTIVE) {
            throw ApiException.fieldError("feeTypeId", "Khoản đóng góp đã ngừng áp dụng.");
        }
        if (req.getPaidDate().isAfter(LocalDate.now())) {
            throw ApiException.fieldError("paidDate", "Ngày thu không được ở tương lai.");
        }
        if (ft.getStartDate() != null && req.getPaidDate().isBefore(ft.getStartDate())
                || ft.getEndDate() != null && req.getPaidDate().isAfter(ft.getEndDate())) {
            throw ApiException.fieldError("paidDate", "Ngày thu ngoài khoảng áp dụng của khoản đóng góp.");
        }

        FeeRecord fr = new FeeRecord();
        fr.setApartment(apt);
        fr.setFeeType(ft);
        fr.setPeriod(String.format("%04d-%02d", req.getPaidDate().getYear(), req.getPaidDate().getMonthValue()));
        fr.setAmount(req.getAmount());
        fr.setStatus(BillStatus.PAID);
        fr.setContribution(true);
        fr.setPaidDate(req.getPaidDate());
        fr.setCollectedBy(currentUserService.requireCurrentUser());
        fr.setNote(req.getNote());
        return toDto(feeRecordRepository.save(fr));
    }

    // ---------- F-TP-07: huy thu ----------
    @Transactional
    public Map<String, Object> uncollect(Long id) {
        FeeRecord fr = feeRecordRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        if (Boolean.TRUE.equals(fr.getContribution())) {
            throw ApiException.conflict("Không thể hủy thu khoản đóng góp.");
        }
        if (fr.getStatus() == BillStatus.UNPAID) {
            throw ApiException.conflict("Bản ghi đã ở trạng thái Chưa thu.");
        }
        fr.setStatus(BillStatus.UNPAID);
        fr.setPaidDate(null);
        fr.setCollectedBy(null);
        return toDto(feeRecordRepository.save(fr));
    }

    // ---------- F-TP-09: nhap so tien (v2.0) ----------
    @Transactional
    public Map<String, Object> setAmount(Long id, AmountRequest req) {
        FeeRecord fr = feeRecordRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        if (fr.getStatus() == BillStatus.PAID) {
            throw ApiException.conflict("Không thể sửa số tiền bản ghi đã thu.");
        }
        fr.setAmount(req.getAmount());
        if (req.getNote() != null) fr.setNote(req.getNote());
        return toDto(feeRecordRepository.save(fr));
    }

    // ---------- F-TH-02: thong ke ----------
    public Map<String, Object> statistics(String fromPeriod, String toPeriod, FeeTypeType type) {
        if (fromPeriod == null || toPeriod == null) {
            throw ApiException.badRequest("Chọn Từ kỳ và Đến kỳ.");
        }
        if (fromPeriod.compareTo(toPeriod) > 0) {
            throw ApiException.fieldError("fromPeriod", "Từ kỳ phải trước hoặc bằng Đến kỳ.");
        }
        List<FeeRecord> records = feeRecordRepository.findByPeriodRangeAndType(fromPeriod, toPeriod, type);

        Map<Long, List<FeeRecord>> byType = records.stream()
                .collect(Collectors.groupingBy(fr -> fr.getFeeType().getId(), LinkedHashMap::new, Collectors.toList()));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Map.Entry<Long, List<FeeRecord>> e : byType.entrySet()) {
            List<FeeRecord> rs = e.getValue();
            FeeType ft = rs.get(0).getFeeType();
            long paid = rs.stream().filter(r -> r.getStatus() == BillStatus.PAID).count();
            long expected = ft.getType() == FeeTypeType.DONATION ? paid : rs.size();
            BigDecimal paidAmt = sum(rs, BillStatus.PAID);
            BigDecimal unpaidAmt = sum(rs, BillStatus.UNPAID);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("feeTypeId", ft.getId());
            row.put("feeTypeName", ft.getName());
            row.put("expected", expected);
            row.put("paid", paid);
            row.put("unpaid", expected - paid);
            row.put("paidAmount", paidAmt);
            row.put("unpaidAmount", unpaidAmt);
            rows.add(row);
        }

        Map<String, Object> total = new LinkedHashMap<>();
        total.put("expected", rows.stream().mapToLong(r -> (long) r.get("expected")).sum());
        total.put("paid", rows.stream().mapToLong(r -> (long) r.get("paid")).sum());
        total.put("unpaid", rows.stream().mapToLong(r -> (long) r.get("unpaid")).sum());
        total.put("paidAmount", rows.stream().map(r -> (BigDecimal) r.get("paidAmount"))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        total.put("unpaidAmount", rows.stream().map(r -> (BigDecimal) r.get("unpaidAmount"))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Map<String, Object> res = new LinkedHashMap<>();
        res.put("rows", rows);
        res.put("total", total);
        return res;
    }

    private BigDecimal sum(List<FeeRecord> rs, BillStatus status) {
        return rs.stream()
                .filter(r -> r.getStatus() == status && r.getAmount() != null)
                .map(FeeRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------- DTO ----------
    public Map<String, Object> toDto(FeeRecord fr) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", fr.getId());
        Apartment apt = fr.getApartment();
        d.put("apartmentId", apt.getId());
        d.put("apartmentNumber", apt.getApartmentNumber());
        d.put("headName", residentRepository.findCurrentHead(apt.getId())
                .map(Resident::getFullName).orElse(null));
        d.put("feeTypeId", fr.getFeeType().getId());
        d.put("feeTypeName", fr.getFeeType().getName());
        d.put("feeTypeType", fr.getFeeType().getType());
        d.put("period", fr.getPeriod());
        d.put("amount", fr.getAmount());
        d.put("status", fr.getStatus());
        d.put("contribution", fr.getContribution());
        d.put("paidDate", fr.getPaidDate());
        d.put("collectedByName", fr.getCollectedBy() != null ? fr.getCollectedBy().getFullName() : null);
        d.put("note", fr.getNote());
        d.put("createdAt", fr.getCreatedAt());
        return d;
    }
}
