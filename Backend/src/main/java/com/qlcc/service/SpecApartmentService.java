package com.qlcc.service;

import com.qlcc.dto.SpecDtos.ApartmentRequest;
import com.qlcc.entity.*;
import com.qlcc.enums.*;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Nghiep vu Can ho theo dac ta (F-CH-01..10).
 * - Trang thai suy ra: EMPTY khi tao, OCCUPIED khi co nguoi dau tien
 * - Xoa: chan khi con nguoi o (E1) hoac da co ban ghi thu phi (E2)
 * - Tim theo so can hoac ten chu ho
 */
@Service
public class SpecApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final FeeRecordRepository feeRecordRepository;
    private final ResidencyEventRepository eventRepository;

    public SpecApartmentService(ApartmentRepository apartmentRepository,
                                ResidentRepository residentRepository,
                                FeeRecordRepository feeRecordRepository,
                                ResidencyEventRepository eventRepository) {
        this.apartmentRepository = apartmentRepository;
        this.residentRepository = residentRepository;
        this.feeRecordRepository = feeRecordRepository;
        this.eventRepository = eventRepository;
    }

    // ---------- F-CH-01: danh sach ----------
    public Page<Map<String, Object>> list(String search, ApartmentStatus status, Pageable pageable) {
        List<Apartment> all = apartmentRepository.findAll().stream()
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> matchesSearch(a, search))
                .sorted(Comparator.comparing(Apartment::getApartmentNumber))
                .collect(Collectors.toList());
        List<Map<String, Object>> rows = all.stream().map(this::toSummary).collect(Collectors.toList());
        return FeeTypeService.pageOf(rows, pageable);
    }

    private boolean matchesSearch(Apartment a, String search) {
        if (search == null || search.isBlank()) return true;
        String s = search.toLowerCase();
        if (a.getApartmentNumber().toLowerCase().contains(s)) return true;
        return residentRepository.findCurrentHead(a.getId())
                .map(h -> h.getFullName() != null && h.getFullName().toLowerCase().contains(s))
                .orElse(false);
    }

    // ---------- F-CH-03: chi tiet 4 khoi (tung khoi doc lap) ----------
    public Map<String, Object> getById(Long id) {
        Apartment a = apartmentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        return toDetail(a);
    }

    public List<Map<String, Object>> residentsOf(Long id) {
        requireExists(id);
        return residentRepository.findActiveByApartment(id).stream()
                .map(r -> {
                    Map<String, Object> d = new LinkedHashMap<>();
                    d.put("id", r.getId());
                    d.put("fullName", r.getFullName());
                    d.put("relationship", r.getRelationship());
                    d.put("status", r.getStatus());
                    d.put("phone", r.getPhone());
                    return d;
                }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> recentFeeRecords(Long id) {
        requireExists(id);
        return feeRecordRepository.findTop5ByApartment_IdOrderByPeriodDesc(id).stream()
                .map(fr -> {
                    Map<String, Object> d = new LinkedHashMap<>();
                    d.put("id", fr.getId());
                    d.put("feeTypeName", fr.getFeeType().getName());
                    d.put("period", fr.getPeriod());
                    d.put("amount", fr.getAmount());
                    d.put("status", fr.getStatus());
                    return d;
                }).collect(Collectors.toList());
    }

    // ---------- F-CH-02: tao ----------
    @Transactional
    public Map<String, Object> create(ApartmentRequest req) {
        if (apartmentRepository.existsByApartmentNumber(req.getApartmentNumber())) {
            throw ApiException.fieldError("apartmentNumber", "Số căn hộ đã tồn tại.");
        }
        Apartment a = new Apartment();
        a.setApartmentNumber(req.getApartmentNumber());
        a.setFloor(req.getFloor());
        a.setArea(req.getArea());
        a.setStatus(ApartmentStatus.EMPTY); // moi tao luon trong, tu chuyen khi co nguoi
        return toDetail(apartmentRepository.save(a));
    }

    // ---------- F-CH-04: sua ----------
    @Transactional
    public Map<String, Object> update(Long id, ApartmentRequest req) {
        Apartment a = apartmentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        Apartment sameNumber = apartmentRepository.findByApartmentNumber(req.getApartmentNumber());
        if (sameNumber != null && !sameNumber.getId().equals(id)) {
            throw ApiException.fieldError("apartmentNumber", "Số căn hộ đã tồn tại.");
        }
        a.setApartmentNumber(req.getApartmentNumber());
        a.setFloor(req.getFloor());
        a.setArea(req.getArea());
        return toDetail(apartmentRepository.save(a));
    }

    // ---------- F-CH-05/06: xoa ----------
    @Transactional
    public void delete(Long id) {
        Apartment a = apartmentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        if (residentRepository.countActiveByApartment(id) > 0) {
            throw ApiException.conflict("Không thể xóa căn hộ đang có người ở.");
        }
        if (feeRecordRepository.existsByApartment_Id(id)) {
            throw ApiException.conflict("Không thể xóa căn hộ đã có bản ghi thu phí.");
        }
        // Don bien dong tro toi can ho truoc khi xoa (tranh loi FK)
        eventRepository.findByApartment_IdOrderByEventDateDesc(id)
                .forEach(eventRepository::delete);
        apartmentRepository.delete(a);
    }

    @Transactional
    public Map<String, Object> bulkDelete(List<Long> ids) {
        List<String> failures = new ArrayList<>();
        int ok = 0;
        for (Long id : ids) {
            try {
                delete(id);
                ok++;
            } catch (ApiException e) {
                String name = apartmentRepository.findById(id).map(Apartment::getApartmentNumber).orElse("#" + id);
                failures.add(name + ": " + e.getMessage());
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", ok);
        res.put("failed", failures.size());
        res.put("failures", failures);
        return res;
    }

    private void requireExists(Long id) {
        if (!apartmentRepository.existsById(id)) {
            throw ApiException.notFound("Không tìm thấy dữ liệu.");
        }
    }

    private Map<String, Object> toSummary(Apartment a) {
        Map<String, Object> d = toDetail(a);
        return d;
    }

    private Map<String, Object> toDetail(Apartment a) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", a.getId());
        d.put("apartmentNumber", a.getApartmentNumber());
        d.put("floor", a.getFloor());
        d.put("area", a.getArea());
        d.put("status", a.getStatus());
        d.put("headName", residentRepository.findCurrentHead(a.getId())
                .map(Resident::getFullName).orElse(null));
        d.put("residentCount", residentRepository.countActiveByApartment(a.getId()));
        d.put("createdAt", a.getCreatedAt());
        d.put("updatedAt", a.getUpdatedAt());
        return d;
    }
}
