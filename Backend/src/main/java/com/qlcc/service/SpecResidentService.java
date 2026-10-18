package com.qlcc.service;

import com.qlcc.dto.SpecDtos.ResidentRequest;
import com.qlcc.dto.SpecDtos.ResidentUpdateRequest;
import com.qlcc.entity.*;
import com.qlcc.enums.*;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Nghiep vu Nguoi dan theo dac ta (F-ND-01..08).
 * - Tao: 1 giao dich luu resident + ghi MOVE_IN + can ho -> OCCUPIED
 * - Sua: khoa Can ho va Trang thai cu tru (doi bang bien dong)
 * - Xoa: chan chu ho con thanh vien; chan nguoi da co lich su cu tru
 */
@Service
public class SpecResidentService {

    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;
    private final ResidencyEventRepository eventRepository;
    private final CurrentUserService currentUserService;
    private final ResidencyEventService residencyEventService;

    public SpecResidentService(ResidentRepository residentRepository,
                               ApartmentRepository apartmentRepository,
                               ResidencyEventRepository eventRepository,
                               CurrentUserService currentUserService,
                               ResidencyEventService residencyEventService) {
        this.residentRepository = residentRepository;
        this.apartmentRepository = apartmentRepository;
        this.eventRepository = eventRepository;
        this.currentUserService = currentUserService;
        this.residencyEventService = residencyEventService;
    }

    public Page<Map<String, Object>> list(String search, Long apartmentId, ResidentStatus status,
                                          Pageable pageable) {
        List<Resident> all = residentRepository.searchResidents(
                (search == null || search.isBlank()) ? null : search, apartmentId, status);
        List<Map<String, Object>> rows = all.stream()
                .sorted(Comparator.comparing(Resident::getId))
                .map(this::toDto).collect(Collectors.toList());
        return FeeTypeService.pageOf(rows, pageable);
    }

    public Map<String, Object> getById(Long id) {
        Resident r = residentRepository.findByIdWithApartments(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        return toDto(r);
    }

    // ---------- F-ND-02: them nguoi dan ----------
    @Transactional
    public Map<String, Object> create(ResidentRequest req) {
        if (residentRepository.existsByIdCard(req.getIdCard())) {
            throw ApiException.fieldError("idCard", "CCCD đã tồn tại.");
        }
        Apartment apt = apartmentRepository.findById(req.getApartmentId())
                .orElseThrow(() -> ApiException.fieldError("apartmentId", "Vui lòng chọn căn hộ."));
        if (req.getRelationship() == Relationship.HEAD) {
            residentRepository.findCurrentHead(apt.getId()).ifPresent(h -> {
                throw ApiException.fieldError("relationship", "Căn hộ đã có chủ hộ.");
            });
        }

        Resident r = new Resident();
        r.setFullName(req.getFullName());
        r.setBirthDate(req.getBirthDate());
        r.setGender(req.getGender());
        r.setIdCard(req.getIdCard());
        r.setPhone(req.getPhone());
        r.setEmail(req.getEmail());
        r.setApartment(apt);
        r.setRelationship(req.getRelationship());
        r.setStatus(ResidentStatus.LIVING);
        r = residentRepository.save(r);

        // Ghi MOVE_IN ngay hom nay trong cung giao dich
        ResidencyEvent ev = new ResidencyEvent();
        ev.setResident(r);
        ev.setApartment(apt);
        ev.setType(ResidencyEventType.MOVE_IN);
        ev.setEventDate(LocalDate.now());
        ev.setCreatedBy(currentUserService.requireCurrentUser());
        eventRepository.save(ev);

        residencyEventService.syncApartmentStatus(apt);
        return toDto(r);
    }

    // ---------- F-ND-04: sua ho so (khoa can ho + trang thai) ----------
    @Transactional
    public Map<String, Object> update(Long id, ResidentUpdateRequest req) {
        Resident r = residentRepository.findByIdWithApartments(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        if (!r.getIdCard().equals(req.getIdCard()) && residentRepository.existsByIdCard(req.getIdCard())) {
            throw ApiException.fieldError("idCard", "CCCD đã tồn tại.");
        }
        if (req.getRelationship() == Relationship.HEAD && r.getRelationship() != Relationship.HEAD) {
            residentRepository.findCurrentHead(r.getApartment().getId()).ifPresent(h -> {
                if (!h.getId().equals(id)) {
                    throw ApiException.fieldError("relationship", "Căn hộ đã có chủ hộ.");
                }
            });
        }

        r.setFullName(req.getFullName());
        r.setBirthDate(req.getBirthDate());
        r.setGender(req.getGender());
        r.setIdCard(req.getIdCard());
        r.setPhone(req.getPhone());
        r.setEmail(req.getEmail());
        r.setRelationship(req.getRelationship()); // F-ND-08: doi chu ho qua day
        return toDto(residentRepository.save(r));
    }

    // ---------- F-ND-05/06: xoa ----------
    @Transactional
    public void delete(Long id) {
        Resident r = residentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));

        if (r.getRelationship() == Relationship.HEAD && r.getStatus() != ResidentStatus.MOVED_OUT
                && residentRepository.countActiveByApartment(r.getApartment().getId()) > 1) {
            throw ApiException.conflict("Không thể xóa chủ hộ khi hộ còn thành viên.");
        }
        long nonMoveIn = eventRepository.findByResident_IdOrderByEventDateDesc(id).stream()
                .filter(e -> e.getType() != ResidencyEventType.MOVE_IN).count();
        if (nonMoveIn > 0) {
            throw ApiException.conflict("Người dân đã có lịch sử cư trú, hãy ghi nhận Chuyển đi thay vì xóa.");
        }
        Apartment apt = r.getApartment();
        eventRepository.findByResident_IdOrderByEventDateDesc(id)
                .forEach(eventRepository::delete);
        residentRepository.delete(r);
        residencyEventService.syncApartmentStatus(apt);
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
                String name = residentRepository.findById(id).map(Resident::getFullName).orElse("#" + id);
                failures.add(name + ": " + e.getMessage());
            }
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", ok);
        res.put("failed", failures.size());
        res.put("failures", failures);
        return res;
    }

    public Map<String, Object> toDto(Resident r) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", r.getId());
        d.put("fullName", r.getFullName());
        d.put("birthDate", r.getBirthDate());
        d.put("gender", r.getGender());
        d.put("idCard", r.getIdCard());
        d.put("phone", r.getPhone());
        d.put("email", r.getEmail());
        Apartment apt = r.getApartment();
        d.put("apartmentId", apt != null ? apt.getId() : null);
        d.put("apartmentNumber", apt != null ? apt.getApartmentNumber() : null);
        d.put("relationship", r.getRelationship());
        d.put("status", r.getStatus());
        d.put("createdAt", r.getCreatedAt());
        return d;
    }
}
