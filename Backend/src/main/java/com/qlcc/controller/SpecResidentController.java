package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.BulkIdsRequest;
import com.qlcc.dto.SpecDtos.ResidentRequest;
import com.qlcc.dto.SpecDtos.ResidentUpdateRequest;
import com.qlcc.enums.ResidentStatus;
import com.qlcc.service.ResidencyEventService;
import com.qlcc.service.SpecResidentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Nguoi dan theo dac ta (F-ND-01..08).
 * URL root /api/residents dung chung voi ResidentController cu
 * (no chi chiem /filter, /admin/filter, /a5e324/update/apartment — khong xung dot).
 */
@RestController
@RequestMapping("/api/residents")
public class SpecResidentController {

    private final SpecResidentService residentService;
    private final ResidencyEventService eventService;

    public SpecResidentController(SpecResidentService residentService,
                                  ResidencyEventService eventService) {
        this.residentService = residentService;
        this.eventService = eventService;
    }

    @GetMapping
    public Page<Map<String, Object>> list(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) Long apartmentId,
                                          @RequestParam(required = false) ResidentStatus status,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return residentService.list(search, apartmentId, status, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ResidentRequest req) {
        return ResponseEntity.ok(residentService.create(req));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<Map<String, Object>> bulkDelete(@Valid @RequestBody BulkIdsRequest req) {
        return ResponseEntity.ok(residentService.bulkDelete(req.getIds()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable Long id) {
        return ResponseEntity.ok(residentService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @Valid @RequestBody ResidentUpdateRequest req) {
        return ResponseEntity.ok(residentService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        residentService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa người dân."));
    }

    // ---- Khoi Lich su bien dong tren trang ho so (F-ND-03) ----
    @GetMapping("/{id}/events")
    public ResponseEntity<List<Map<String, Object>>> events(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.listByResident(id));
    }
}
