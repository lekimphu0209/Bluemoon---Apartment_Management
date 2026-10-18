package com.qlcc.repository;
import com.qlcc.entity.Apartment;
import com.qlcc.enums.ApartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long>, JpaSpecificationExecutor<Apartment> {
    boolean existsByApartmentNumber(String apartmentNumber);

    @Query("SELECT a FROM Apartment a WHERE a.apartmentNumber = :apartmentNumber")
    Apartment findByApartmentNumber(@Param("apartmentNumber") String apartmentNumber);

    Optional<Apartment> findById(Long apartmentId);

    // Schema V2: quan he resident->apartment la FK, khong con @ElementCollection
    @Query("SELECT a FROM Apartment a JOIN a.residents r WHERE r.id = :residentId")
    List<Apartment> findByResidentIdsContaining(@Param("residentId") Long residentId);

    List<Apartment> findByFloor(Integer floorNumber);
    List<Apartment> findByApartmentNumberContainingIgnoreCase(String apartmentNumber);
    List<Apartment> findByFloorIn(List<Integer> floors);
    List<Apartment> findByAreaGreaterThanEqual(Double minArea);
    List<Apartment> findByAreaLessThanEqual(Double maxArea);
    List<Apartment> findByStatusIn(List<ApartmentStatus> statuses);

    // Schema V2: da bo room_number/type -> loc con number/area/floor/status
    @Query("SELECT a FROM Apartment a WHERE " +
           "(:apartmentNumber IS NULL OR LOWER(a.apartmentNumber) LIKE LOWER(CONCAT('%', :apartmentNumber, '%'))) AND " +
           "(:minArea IS NULL OR a.area >= :minArea) AND " +
           "(:maxArea IS NULL OR a.area <= :maxArea)")
    List<Apartment> findByFilters(
        @Param("apartmentNumber") String apartmentNumber,
        @Param("minArea") Double minArea,
        @Param("maxArea") Double maxArea
    );
}
