package com.qlcc.repository;

import com.qlcc.entity.ResidencyEvent;
import com.qlcc.enums.ResidencyEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResidencyEventRepository extends JpaRepository<ResidencyEvent, Long> {
    List<ResidencyEvent> findByResident_IdOrderByEventDateDesc(Long residentId);
    List<ResidencyEvent> findByApartment_IdOrderByEventDateDesc(Long apartmentId);
    List<ResidencyEvent> findByType(ResidencyEventType type);
    List<ResidencyEvent> findByEventDateBetween(LocalDate from, LocalDate to);

    @Query("SELECT e FROM ResidencyEvent e WHERE " +
           "(:type IS NULL OR e.type = :type) AND " +
           "(:from IS NULL OR e.eventDate >= :from) AND " +
           "(:to IS NULL OR e.eventDate <= :to) " +
           "ORDER BY e.eventDate DESC")
    List<ResidencyEvent> findByFilters(
        @Param("type") ResidencyEventType type,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );
}
