package com.qlcc.repository;

import com.qlcc.entity.ApartmentFeeUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentFeeUnitRepository extends JpaRepository<ApartmentFeeUnit, Long> {
} 