package com.qlcc.controller;

import com.qlcc.enums.FeeTypeType;
import com.qlcc.service.FeeRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Thong ke (F-TH-01..04). */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final FeeRecordService feeRecordService;

    public StatisticsController(FeeRecordService feeRecordService) {
        this.feeRecordService = feeRecordService;
    }

    @GetMapping("/fees")
    public ResponseEntity<Map<String, Object>> fees(
            @RequestParam(required = false) String fromPeriod,
            @RequestParam(required = false) String toPeriod,
            @RequestParam(required = false) FeeTypeType type) {
        return ResponseEntity.ok(feeRecordService.statistics(fromPeriod, toPeriod, type));
    }
}
