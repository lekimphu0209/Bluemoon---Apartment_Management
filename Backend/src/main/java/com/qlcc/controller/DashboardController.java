package com.qlcc.controller;

import com.qlcc.enums.ApartmentStatus;
import com.qlcc.enums.BillStatus;
import com.qlcc.enums.ResidentStatus;
import com.qlcc.repository.ApartmentRepository;
import com.qlcc.repository.FeeRecordRepository;
import com.qlcc.repository.ResidentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * F-TQ-01: 4 the so lieu tong quan.
 * So lieu khop voi trang Thu phi (unpaidAmount/paidAmount = tong theo status).
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final FeeRecordRepository feeRecordRepository;

    public DashboardController(ApartmentRepository apartmentRepository,
                               ResidentRepository residentRepository,
                               FeeRecordRepository feeRecordRepository) {
        this.apartmentRepository = apartmentRepository;
        this.residentRepository = residentRepository;
        this.feeRecordRepository = feeRecordRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> summary() {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("apartmentCount", apartmentRepository.count());
        d.put("residentCount", residentRepository.countByStatusNot(ResidentStatus.MOVED_OUT));
        d.put("unpaidAmount", feeRecordRepository.sumAmountByStatus(BillStatus.UNPAID));
        d.put("paidAmount", feeRecordRepository.sumAmountByStatus(BillStatus.PAID));
        d.put("occupiedCount", apartmentRepository.findByStatusIn(
                java.util.List.of(ApartmentStatus.OCCUPIED)).size());
        return ResponseEntity.ok(d);
    }
}
