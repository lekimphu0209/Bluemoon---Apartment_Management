package com.qlcc.repository;

import com.qlcc.entity.Vehicle;
import com.qlcc.enums.VehicleStatus;
import com.qlcc.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(String plate);
    boolean existsByPlate(String plate);
    List<Vehicle> findByApartment_Id(Long apartmentId);
    List<Vehicle> findByApartment_IdAndStatus(Long apartmentId, VehicleStatus status);
    List<Vehicle> findByTypeAndStatus(VehicleType type, VehicleStatus status);
}
