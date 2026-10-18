package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.VehicleRequest;
import com.qlcc.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** Phuong tien (F-CH-10, v2.0): long trong trang Can ho. */
@RestController
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/api/apartments/{id}/vehicles")
    public ResponseEntity<List<Map<String, Object>>> list(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.listByApartment(id));
    }

    @PostMapping("/api/apartments/{id}/vehicles")
    public ResponseEntity<Map<String, Object>> create(@PathVariable Long id,
                                                      @Valid @RequestBody VehicleRequest req) {
        return ResponseEntity.ok(vehicleService.create(id, req));
    }

    @DeleteMapping("/api/vehicles/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa phương tiện."));
    }
}
