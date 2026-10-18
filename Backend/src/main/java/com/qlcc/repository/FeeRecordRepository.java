package com.qlcc.repository;

import com.qlcc.entity.FeeRecord;
import com.qlcc.enums.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRecordRepository extends JpaRepository<FeeRecord, Long>, JpaSpecificationExecutor<FeeRecord> {
    List<FeeRecord> findByApartment_Id(Long apartmentId);
    List<FeeRecord> findByApartment_ApartmentNumber(String apartmentNumber);
    List<FeeRecord> findByFeeType_Id(Long feeTypeId);
    List<FeeRecord> findByPeriod(String period);
    List<FeeRecord> findByStatus(BillStatus status);
    List<FeeRecord> findByStatusAndPeriod(BillStatus status, String period);
    List<FeeRecord> findByApartment_IdAndPeriod(Long apartmentId, String period);
    List<FeeRecord> findByApartment_IdAndStatus(Long apartmentId, BillStatus status);

    @Query("SELECT fr FROM FeeRecord fr WHERE fr.status = :status AND fr.period <= :period")
    List<FeeRecord> findOverdue(@Param("status") BillStatus status, @Param("period") String period);

    @Query("SELECT fr FROM FeeRecord fr JOIN fr.apartment a WHERE " +
           "(:apartmentNumber IS NULL OR LOWER(a.apartmentNumber) LIKE LOWER(CONCAT('%', :apartmentNumber, '%'))) AND " +
           "(:feeTypeId IS NULL OR fr.feeType.id = :feeTypeId) AND " +
           "(:period IS NULL OR fr.period = :period) AND " +
           "(:status IS NULL OR fr.status = :status)")
    List<FeeRecord> findByFilters(
        @Param("apartmentNumber") String apartmentNumber,
        @Param("feeTypeId") Long feeTypeId,
        @Param("period") String period,
        @Param("status") BillStatus status
    );

    // ---- Spec endpoints ----
    boolean existsByApartment_IdAndFeeType_IdAndPeriod(Long apartmentId, Long feeTypeId, String period);

    long countByFeeType_IdAndStatus(Long feeTypeId, BillStatus status);

    boolean existsByFeeType_IdAndStatus(Long feeTypeId, BillStatus status);

    boolean existsByApartment_Id(Long apartmentId);

    List<FeeRecord> findTop5ByApartment_IdOrderByPeriodDesc(Long apartmentId);

    List<FeeRecord> findByIdIn(List<Long> ids);

    @Query("SELECT fr FROM FeeRecord fr WHERE fr.period BETWEEN :from AND :to " +
           "AND (:type IS NULL OR fr.feeType.type = :type)")
    List<FeeRecord> findByPeriodRangeAndType(
        @Param("from") String fromPeriod,
        @Param("to") String toPeriod,
        @Param("type") com.qlcc.enums.FeeTypeType type
    );

    @Query("SELECT COALESCE(SUM(fr.amount), 0) FROM FeeRecord fr WHERE fr.status = :status")
    java.math.BigDecimal sumAmountByStatus(@Param("status") BillStatus status);
}
