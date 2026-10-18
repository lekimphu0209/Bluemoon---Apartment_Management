package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.ApartmentRequest;
import com.qlcc.dto.SpecDtos.BulkIdsRequest;
import com.qlcc.enums.ApartmentStatus;
import com.qlcc.service.SpecApartmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Can ho theo dac ta (F-CH-01..09).
 * URL root /api/apartments dung chung voi ApartmentAPIController cu
 * (no chi chiem /list, /filter, /floors, /batch-update-status — khong xung dot).
 */
@RestController
@RequestMapping("/api/apartments")
public class SpecApartmentController {

    private final SpecApartmentService apartmentService;

    public SpecApartmentController(SpecApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @GetMapping
    public Page<Map<String, Object>> list(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) ApartmentStatus status,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return apartmentService.list(search, status, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ApartmentRequest req) {
        return ResponseEntity.ok(apartmentService.create(req));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<Map<String, Object>> bulkDelete(@Valid @RequestBody BulkIdsRequest req) {
        return ResponseEntity.ok(apartmentService.bulkDelete(req.getIds()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable Long id) {
        return ResponseEntity.ok(apartmentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @Valid @RequestBody ApartmentRequest req) {
        return ResponseEntity.ok(apartmentService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        apartmentService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa căn hộ."));
    }

    // ---- Khoi Nhan khau tren trang chi tiet (F-CH-03) ----
    @GetMapping("/{id}/residents")
    public ResponseEntity<List<Map<String, Object>>> residents(@PathVariable Long id) {
        return ResponseEntity.ok(apartmentService.residentsOf(id));
    }

    // ---- Khoi Khoan thu gan day (F-CH-03) ----
    @GetMapping("/{id}/fee-records")
    public ResponseEntity<List<Map<String, Object>>> feeRecords(@PathVariable Long id) {
        return ResponseEntity.ok(apartmentService.recentFeeRecords(id));
    }
}
