package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.BulkIdsRequest;
import com.qlcc.dto.SpecDtos.FeeTypeRequest;
import com.qlcc.entity.FeeType;
import com.qlcc.enums.FeeTypeStatus;
import com.qlcc.enums.FeeTypeType;
import com.qlcc.service.FeeTypeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/** Khoan thu (F-KT-01..08). */
@RestController
@RequestMapping("/api/fee-types")
public class FeeTypeController {

    private final FeeTypeService feeTypeService;

    public FeeTypeController(FeeTypeService feeTypeService) {
        this.feeTypeService = feeTypeService;
    }

    @GetMapping
    public Page<FeeType> list(@RequestParam(required = false) String search,
                              @RequestParam(required = false) FeeTypeType type,
                              @RequestParam(required = false) FeeTypeStatus status,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        return feeTypeService.list(search, type, status, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable Long id) {
        FeeType f = feeTypeService.getById(id);
        Map<String, Object> d = toDto(f);
        // So ban ghi Chua thu se bi xoa theo (hien trong hop xac nhan xoa)
        d.put("unpaidRecordCount", feeTypeService.unpaidRecordCount(id));
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<FeeType> create(@Valid @RequestBody FeeTypeRequest req) {
        return ResponseEntity.ok(feeTypeService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeType> update(@PathVariable Long id,
                                          @Valid @RequestBody FeeTypeRequest req) {
        return ResponseEntity.ok(feeTypeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        feeTypeService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa khoản thu."));
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<Map<String, Object>> bulkDelete(@Valid @RequestBody BulkIdsRequest req) {
        return ResponseEntity.ok(feeTypeService.bulkDelete(req.getIds()));
    }

    private Map<String, Object> toDto(FeeType f) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", f.getId());
        d.put("name", f.getName());
        d.put("type", f.getType());
        d.put("calcMethod", f.getCalcMethod());
        d.put("rate", f.getRate());
        d.put("suggestedAmount", f.getSuggestedAmount());
        d.put("startDate", f.getStartDate());
        d.put("endDate", f.getEndDate());
        d.put("mandatory", f.getMandatory());
        d.put("status", f.getStatus());
        d.put("description", f.getDescription());
        d.put("createdAt", f.getCreatedAt());
        return d;
    }
}
