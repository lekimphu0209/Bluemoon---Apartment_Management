package com.qlcc.service;

import com.qlcc.dto.SpecDtos.ResidencyEventRequest;
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
 * Nghiep vu Bien dong (F-BD-01..06).
 * - Trang thai cu tru luon suy ra tu su kien moi nhat co tac dung doi trang thai
 * - Nguoi cuoi cung chuyen di -> can ho EMPTY; nguoi dau chuyen vao -> OCCUPIED
 * - Sua/xoa chi cho su kien MOI NHAT cua nguoi do, sau do tinh lai trang thai
 */
@Service
public class ResidencyEventService {

    private final ResidencyEventRepository eventRepository;
    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;
    private final CurrentUserService currentUserService;

    public ResidencyEventService(ResidencyEventRepository eventRepository,
                                 ResidentRepository residentRepository,
                                 ApartmentRepository apartmentRepository,
                                 CurrentUserService currentUserService) {
        this.eventRepository = eventRepository;
        this.residentRepository = residentRepository;
        this.apartmentRepository = apartmentRepository;
        this.currentUserService = currentUserService;
    }

    public Page<Map<String, Object>> list(String search, Long apartmentId, ResidencyEventType type,
                                          Pageable pageable) {
        List<ResidencyEvent> all = eventRepository.findAll().stream()
                .filter(e -> apartmentId == null || e.getApartment().getId().equals(apartmentId))
                .filter(e -> type == null || e.getType() == type)
                .filter(e -> search == null || search.isBlank()
                        || e.getResident().getFullName().toLowerCase().contains(search.toLowerCase()))
                .sorted(Comparator.comparing(ResidencyEvent::getEventDate).reversed()
                        .thenComparing(Comparator.comparing(ResidencyEvent::getId).reversed()))
                .collect(Collectors.toList());
        return FeeTypeService.pageOf(all, pageable).map(this::toDto);
    }

    public List<Map<String, Object>> listByResident(Long residentId) {
        return eventRepository.findByResident_IdOrderByEventDateDesc(residentId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public List<Map<String, Object>> listByApartment(Long apartmentId) {
        return eventRepository.findByApartment_IdOrderByEventDateDesc(apartmentId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> create(ResidencyEventRequest req) {
        Resident resident = residentRepository.findById(req.getResidentId())
                .orElseThrow(() -> ApiException.fieldError("residentId", "Người dân không tồn tại."));
        Apartment apartment = resolveApartment(req, resident);

        validateTransition(resident, req);
        validateFields(req);

        ResidencyEvent e = new ResidencyEvent();
        e.setResident(resident);
        e.setApartment(apartment);
        e.setType(req.getType());
        e.setEventDate(req.getEventDate());
        e.setFromDate(req.getFromDate());
        e.setToDate(req.getToDate());
        e.setDestination(req.getDestination());
        e.setNote(req.getNote());
        e.setCreatedBy(currentUserService.requireCurrentUser());
        e = eventRepository.save(e);

        applyToResident(resident, apartment, req);
        syncApartmentStatus(apartment);
        return toDto(e);
    }

    private Apartment resolveApartment(ResidencyEventRequest req, Resident resident) {
        if (req.getType() == ResidencyEventType.MOVE_IN) {
            // Chuyen vao: cho phep chon can ho khac (nguoi cu quay lai)
            if (req.getApartmentId() != null) {
                return apartmentRepository.findById(req.getApartmentId())
                        .orElseThrow(() -> ApiException.fieldError("apartmentId", "Căn hộ không tồn tại."));
            }
        }
        Apartment apt = resident.getApartment();
        if (apt == null) {
            throw ApiException.fieldError("apartmentId", "Người dân chưa thuộc căn hộ nào.");
        }
        return apt;
    }

    private void validateFields(ResidencyEventRequest req) {
        switch (req.getType()) {
            case MOVE_OUT -> {
                if (req.getDestination() == null || req.getDestination().isBlank()) {
                    throw ApiException.fieldError("destination", "Vui lòng nhập nơi chuyển đến.");
                }
            }
            case TEMP_RESIDENCE, TEMP_ABSENCE -> {
                if (req.getFromDate() == null) throw ApiException.fieldError("fromDate", "Vui lòng chọn Từ ngày.");
                if (req.getToDate() == null) throw ApiException.fieldError("toDate", "Vui lòng chọn Đến ngày.");
                if (req.getToDate().isBefore(req.getFromDate())) {
                    throw ApiException.fieldError("toDate", "Đến ngày phải sau hoặc bằng Từ ngày.");
                }
            }
            case INFO_CHANGE -> {
                if (req.getNote() == null || req.getNote().isBlank()) {
                    throw ApiException.fieldError("note", "Vui lòng nhập nội dung thay đổi.");
                }
            }
            default -> { }
        }
    }

    private void validateTransition(Resident resident, ResidencyEventRequest req) {
        ResidentStatus cur = resident.getStatus();
        switch (req.getType()) {
            case MOVE_IN -> {
                if (cur == ResidentStatus.LIVING) {
                    throw ApiException.conflict("Người dân đang ở, không thể ghi nhận Chuyển vào.");
                }
            }
            case MOVE_OUT -> {
                if (cur == ResidentStatus.MOVED_OUT) {
                    throw ApiException.conflict("Người dân đã chuyển đi.");
                }
            }
            case TEMP_RESIDENCE -> {
                if (cur != ResidentStatus.LIVING) {
                    throw ApiException.conflict("Chỉ ghi nhận Tạm trú khi người dân đang ở.");
                }
            }
            case TEMP_ABSENCE -> {
                if (cur != ResidentStatus.LIVING) {
                    throw ApiException.conflict("Chỉ ghi nhận Tạm vắng khi người dân đang ở.");
                }
            }
            case INFO_CHANGE -> { /* moi trang thai */ }
        }
    }

    private void applyToResident(Resident resident, Apartment apartment, ResidencyEventRequest req) {
        switch (req.getType()) {
            case MOVE_IN -> {
                resident.setStatus(ResidentStatus.LIVING);
                if (!resident.getApartment().getId().equals(apartment.getId())) {
                    resident.setApartment(apartment);
                }
            }
            case MOVE_OUT -> resident.setStatus(ResidentStatus.MOVED_OUT);
            case TEMP_RESIDENCE -> resident.setStatus(ResidentStatus.TEMP_RESIDENCE);
            case TEMP_ABSENCE -> resident.setStatus(ResidentStatus.TEMP_ABSENCE);
            case INFO_CHANGE -> { /* giu nguyen */ }
        }
        residentRepository.save(resident);
    }

    /** Can ho OCCUPIED khi con nguoi != MOVED_OUT, EMPTY khi het. */
    public void syncApartmentStatus(Apartment apartment) {
        long active = residentRepository.countActiveByApartment(apartment.getId());
        ApartmentStatus next = active > 0 ? ApartmentStatus.OCCUPIED : ApartmentStatus.EMPTY;
        if (apartment.getStatus() != next) {
            apartment.setStatus(next);
            apartmentRepository.save(apartment);
        }
    }

    // ---------- F-BD-06: sua/xoa su kien moi nhat ----------
    @Transactional
    public Map<String, Object> update(Long id, ResidencyEventRequest req) {
        ResidencyEvent e = eventRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        requireLatest(e);
        validateFields(req);
        e.setEventDate(req.getEventDate());
        e.setFromDate(req.getFromDate());
        e.setToDate(req.getToDate());
        e.setDestination(req.getDestination());
        e.setNote(req.getNote());
        e = eventRepository.save(e);
        recomputeResidentStatus(e.getResident());
        syncApartmentStatus(e.getApartment());
        return toDto(e);
    }

    @Transactional
    public void delete(Long id) {
        ResidencyEvent e = eventRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        requireLatest(e);
        Resident resident = e.getResident();
        Apartment apartment = e.getApartment();
        eventRepository.delete(e);
        recomputeResidentStatus(resident);
        syncApartmentStatus(apartment);
    }

    private void requireLatest(ResidencyEvent e) {
        ResidencyEvent latest = latestOf(e.getResident().getId());
        if (latest == null || !latest.getId().equals(e.getId())) {
            throw ApiException.conflict("Chỉ được sửa hoặc xóa sự kiện mới nhất của người dân này.");
        }
    }

    private ResidencyEvent latestOf(Long residentId) {
        return eventRepository.findByResident_IdOrderByEventDateDesc(residentId).stream()
                .max(Comparator.comparing(ResidencyEvent::getEventDate)
                        .thenComparing(ResidencyEvent::getId))
                .orElse(null);
    }

    /** Tinh lai trang thai cu tru tu su kien moi nhat co tac dung doi trang thai. */
    private void recomputeResidentStatus(Resident resident) {
        List<ResidencyEvent> events = eventRepository
                .findByResident_IdOrderByEventDateDesc(resident.getId());
        ResidencyEvent latestStatusEvent = events.stream()
                .filter(e -> e.getType() != ResidencyEventType.INFO_CHANGE)
                .max(Comparator.comparing(ResidencyEvent::getEventDate)
                        .thenComparing(ResidencyEvent::getId))
                .orElse(null);
        if (latestStatusEvent == null) {
            resident.setStatus(ResidentStatus.LIVING); // chua co su kien -> mac dinh dang o
        } else {
            switch (latestStatusEvent.getType()) {
                case MOVE_IN -> resident.setStatus(ResidentStatus.LIVING);
                case MOVE_OUT -> resident.setStatus(ResidentStatus.MOVED_OUT);
                case TEMP_RESIDENCE -> resident.setStatus(ResidentStatus.TEMP_RESIDENCE);
                case TEMP_ABSENCE -> resident.setStatus(ResidentStatus.TEMP_ABSENCE);
                default -> { }
            }
        }
        residentRepository.save(resident);
    }

    public Map<String, Object> toDto(ResidencyEvent e) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", e.getId());
        d.put("residentId", e.getResident().getId());
        d.put("residentName", e.getResident().getFullName());
        d.put("apartmentId", e.getApartment().getId());
        d.put("apartmentNumber", e.getApartment().getApartmentNumber());
        d.put("type", e.getType());
        d.put("eventDate", e.getEventDate());
        d.put("fromDate", e.getFromDate());
        d.put("toDate", e.getToDate());
        d.put("destination", e.getDestination());
        d.put("note", e.getNote());
        d.put("createdByName", e.getCreatedBy() != null ? e.getCreatedBy().getFullName() : null);
        d.put("createdAt", e.getCreatedAt());
        return d;
    }
}
