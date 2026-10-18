package com.qlcc.service;

import com.qlcc.dto.SpecDtos.VehicleRequest;
import com.qlcc.entity.Apartment;
import com.qlcc.entity.Vehicle;
import com.qlcc.exception.ApiException;
import com.qlcc.repository.ApartmentRepository;
import com.qlcc.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Phuong tien trong trang Can ho (F-CH-10, v2.0). */
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ApartmentRepository apartmentRepository;

    public VehicleService(VehicleRepository vehicleRepository, ApartmentRepository apartmentRepository) {
        this.vehicleRepository = vehicleRepository;
        this.apartmentRepository = apartmentRepository;
    }

    public List<Map<String, Object>> listByApartment(Long apartmentId) {
        return vehicleRepository.findByApartment_Id(apartmentId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> create(Long apartmentId, VehicleRequest req) {
        Apartment apt = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        if (vehicleRepository.existsByPlate(req.getPlate())) {
            throw ApiException.fieldError("plate", "Biển số đã tồn tại.");
        }
        Vehicle v = new Vehicle();
        v.setApartment(apt);
        v.setType(req.getType());
        v.setPlate(req.getPlate());
        return toDto(vehicleRepository.save(v));
    }

    @Transactional
    public void delete(Long id) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy dữ liệu."));
        vehicleRepository.delete(v);
    }

    private Map<String, Object> toDto(Vehicle v) {
        Map<String, Object> d = new LinkedHashMap<>();
        d.put("id", v.getId());
        d.put("apartmentId", v.getApartment().getId());
        d.put("type", v.getType());
        d.put("plate", v.getPlate());
        d.put("status", v.getStatus());
        return d;
    }
}
