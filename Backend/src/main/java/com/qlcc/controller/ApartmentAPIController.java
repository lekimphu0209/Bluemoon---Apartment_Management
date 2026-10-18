package com.qlcc.controller;

import com.qlcc.entity.Apartment;
import com.qlcc.entity.Resident;
import com.qlcc.enums.ApartmentStatus;
import com.qlcc.repository.ResidentRepository;
import com.qlcc.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentAPIController {

    @Autowired
    private ApartmentService apartmentService;
    @Autowired
    private ResidentRepository residentRepository;

    @GetMapping("/list")
    public List<Map<String, Object>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();
        return apartments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Schema V2: bo tham so roomNumber/type (2 cot da bi xoa khoi apartments)
    @GetMapping("/filter")
    public List<Map<String, Object>> filterApartments(
            @RequestParam(value = "apartmentNumber", required = false) String apartmentNumber,
            @RequestParam(value = "floors", required = false) String floor,
            @RequestParam(value = "minArea", required = false) Double minArea,
            @RequestParam(value = "maxArea", required = false) Double maxArea,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "filterLogic", defaultValue = "AND") String filterLogic) {

        Map<String, Object> filterParams = new HashMap<>();

        if (apartmentNumber != null && !apartmentNumber.isEmpty()) {
            filterParams.put("apartmentNumber", apartmentNumber);
        }

        if (floor != null && !floor.isEmpty()) {
            filterParams.put("floors", parseMultipleValues(floor));
        }

        if (minArea != null) {
            filterParams.put("minArea", minArea);
        }

        if (maxArea != null) {
            filterParams.put("maxArea", maxArea);
        }

        if (status != null && !status.isEmpty()) {
            List<ApartmentStatus> statusList = parseStatusValues(status);
            if (!statusList.isEmpty()) {
                filterParams.put("status", statusList);
            }
        }

        filterParams.put("filterLogic", filterLogic);

        List<Apartment> apartments = apartmentService.filterApartments(filterParams);

        return apartments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private List<Integer> parseMultipleValues(String values) {
        if (values == null || values.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(values.split("\\|"))
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return Integer.parseInt(s.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());
    }

    private List<ApartmentStatus> parseStatusValues(String status) {
        if (status == null || status.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(status.split("\\|"))
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return ApartmentStatus.valueOf(s.trim());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(value -> value != null)
                .collect(Collectors.toList());
    }

    private Map<String, Object> convertToDto(Apartment apartment) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", apartment.getId());
        dto.put("apartmentNumber", apartment.getApartmentNumber());
        dto.put("floor", apartment.getFloor());
        dto.put("area", apartment.getArea());
        dto.put("status", apartment.getStatus() != null ? apartment.getStatus().name() : null);
        // Schema V2: lay residents qua quan he apartment_id thay vi collection ids
        List<String> residentName = residentRepository.findByApartment_Id(apartment.getId()).stream()
                .map(Resident::getFullName)
                .collect(Collectors.toList());
        dto.put("residentName", String.join(", ", residentName));

        return dto;
    }

    @GetMapping("/floors")
    public List<Integer> getAvailableFloors() {
        return apartmentService.getAllApartments().stream()
                .map(Apartment::getFloor)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @PostMapping("/batch-update-status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            String status = (String) request.get("status");

            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.badRequest().body("Danh sách ID căn hộ không được trống");
            }

            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body("Trạng thái không được trống");
            }

            ApartmentStatus apartmentStatus;
            try {
                apartmentStatus = ApartmentStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
            }

            List<Apartment> apartments = apartmentService.findAllById(ids);
            for (Apartment apartment : apartments) {
                apartment.setStatus(apartmentStatus);
            }

            apartmentService.save(apartments);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Đã cập nhật trạng thái cho " + apartments.size() + " căn hộ",
                "updatedCount", apartments.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }
}
