package com.qlcc.repository;

import com.qlcc.entity.FeeType;
import com.qlcc.enums.FeeTypeStatus;
import com.qlcc.enums.FeeTypeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeTypeRepository extends JpaRepository<FeeType, Long> {
    Optional<FeeType> findByName(String name);
    boolean existsByName(String name);
    List<FeeType> findByStatus(FeeTypeStatus status);
    List<FeeType> findByType(FeeTypeType type);
    List<FeeType> findByTypeAndStatus(FeeTypeType type, FeeTypeStatus status);
}
