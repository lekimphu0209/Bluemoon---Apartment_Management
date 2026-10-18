package com.qlcc.controller;

import com.qlcc.dto.SpecDtos.ResidencyEventRequest;
import com.qlcc.enums.ResidencyEventType;
import com.qlcc.service.ResidencyEventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Bien dong (F-BD-01..06). */
@RestController
@RequestMapping("/api/residency-events")
public class ResidencyEventController {

    private final ResidencyEventService eventService;

    public ResidencyEventController(ResidencyEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Page<Map<String, Object>> list(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) Long apartmentId,
                                          @RequestParam(required = false) ResidencyEventType type,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return eventService.list(search, apartmentId, type, PageRequest.of(page, size));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ResidencyEventRequest req) {
        return ResponseEntity.ok(eventService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id,
                                                      @Valid @RequestBody ResidencyEventRequest req) {
        return ResponseEntity.ok(eventService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa biến động."));
    }
}
