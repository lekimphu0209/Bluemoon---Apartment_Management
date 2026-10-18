package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.*;
import com.qlcc.enums.BillStatus;
import com.qlcc.service.FeeRecordService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Thu phi (F-TP-01..09). */
@RestController
@RequestMapping("/api/fee-records")
public class FeeRecordController {

    private final FeeRecordService feeRecordService;

    public FeeRecordController(FeeRecordService feeRecordService) {
        this.feeRecordService = feeRecordService;
    }

    @GetMapping
    public Page<Map<String, Object>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long apartmentId,
            @RequestParam(required = false) Long feeTypeId,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String fromPeriod,
            @RequestParam(required = false) String toPeriod,
            @RequestParam(required = false) BillStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return feeRecordService.list(search, apartmentId, feeTypeId, period,
                fromPeriod, toPeriod, status, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> detail(@PathVariable Long id) {
        return ResponseEntity.ok(feeRecordService.getById(id));
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generate(@Valid @RequestBody GenerateRequest req) {
        return ResponseEntity.ok(feeRecordService.generate(req));
    }

    @PostMapping("/{id}/collect")
    public ResponseEntity<Map<String, Object>> collect(@PathVariable Long id,
                                                       @Valid @RequestBody CollectRequest req) {
        return ResponseEntity.ok(feeRecordService.collect(id, req));
    }

    @PostMapping("/collect-bulk")
    public ResponseEntity<Map<String, Object>> collectBulk(@RequestBody CollectBulkRequest req) {
        return ResponseEntity.ok(feeRecordService.collectBulk(req));
    }

    @PostMapping("/contributions")
    public ResponseEntity<Map<String, Object>> contribution(@Valid @RequestBody ContributionRequest req) {
        return ResponseEntity.ok(feeRecordService.createContribution(req));
    }

    @PostMapping("/{id}/uncollect")
    public ResponseEntity<Map<String, Object>> uncollect(@PathVariable Long id) {
        return ResponseEntity.ok(feeRecordService.uncollect(id));
    }

    @PutMapping("/{id}/amount")
    public ResponseEntity<Map<String, Object>> setAmount(@PathVariable Long id,
                                                         @Valid @RequestBody AmountRequest req) {
        return ResponseEntity.ok(feeRecordService.setAmount(id, req));
    }
}
