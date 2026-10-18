package com.qlcc.controller;

import com.qlcc.entity.Apartment;
import com.qlcc.entity.Bill;
import com.qlcc.entity.Resident;
import com.qlcc.repository.BillRepository;
import com.qlcc.repository.ResidentRepository;
import com.qlcc.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user/apartment-detail")
@CrossOrigin
public class UserApartmentDetailController {

@Autowired
private ApartmentService apartmentService;

@Autowired
private BillRepository billRepository;

@Autowired
private ResidentRepository residentRepository;

@GetMapping
public ResponseEntity<?> getApartmentDetail(@RequestParam("apartmentNumber") String apartmentNumber) {
Apartment apartment = apartmentService.getApartmentByNumber(apartmentNumber);

if (apartment == null) {
return ResponseEntity.notFound().build();
}

// Schema V2: bills lien ket qua cot apartment_number (legacy_bills),
// residents lien ket qua apartment_id FK
List<Bill> bills = billRepository.findByApartmentNumber(apartmentNumber);
List<Resident> residents = residentRepository.findByApartment_Id(apartment.getId());

Map<String, Object> response = new HashMap<>();
response.put("apartment", apartment);
response.put("bills", bills);
response.put("residents", residents);

return ResponseEntity.ok(response);
}
}
