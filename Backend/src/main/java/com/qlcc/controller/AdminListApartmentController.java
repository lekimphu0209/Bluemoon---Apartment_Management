package com.qlcc.controller;

import com.qlcc.dto.ApartmentDTO;
import com.qlcc.entity.Apartment;
import com.qlcc.entity.Resident;
import com.qlcc.repository.ResidentRepository;
import com.qlcc.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/api/admin/apartment-list")
public class AdminListApartmentController {
    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private ResidentRepository residentRepository;

    // API lấy danh sách tất cả căn hộ
    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();
        return ResponseEntity.ok(apartments);
    }

    // API thêm căn hộ mới
    @PostMapping("/add-apartment")
    public ResponseEntity<String> addApartment(@RequestBody ApartmentDTO apartmentDTO) {
        apartmentService.saveApartment(apartmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Apartment created successfully");
    }

    @GetMapping("/residents/{apartmentId}")
    public ResponseEntity<List<Resident>> getResidentsByApartment(@PathVariable Long apartmentId) {
        Optional<Apartment> apartment = apartmentService.getApartmentById(apartmentId);
        if (apartment.isPresent()) {
            // Schema V2: residents lien ket qua apartment_id FK
            List<Resident> residents = residentRepository.findByApartment_Id(apartmentId);
            return ResponseEntity.ok(residents);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/edit-apartment/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable("id") Long id) {
        Optional<Apartment> apartmentOpt = apartmentService.getApartmentById(id);
        return apartmentOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    @PostMapping("/edit-apartment")
    public ResponseEntity<String> updateApartment(@RequestBody Apartment apartment) {
        ApartmentDTO apartmentDTO = new ApartmentDTO();

        apartmentDTO.setApartmentNumber(apartment.getApartmentNumber());
        apartmentDTO.setArea(apartment.getArea());
        apartmentDTO.setFloor(apartment.getFloor());
        apartmentDTO.setStatus(apartment.getStatus());

        apartmentService.updateApartment(apartmentDTO);
        return ResponseEntity.ok("Apartment updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }
}