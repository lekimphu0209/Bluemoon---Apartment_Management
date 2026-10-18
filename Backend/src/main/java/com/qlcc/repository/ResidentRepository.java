package com.qlcc.repository;

import com.qlcc.entity.Resident;
import com.qlcc.enums.ResidentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Resident findByFullName(String fullName);

    boolean existsByFullName(String fullName);

    boolean existsByEmail(String email);

    Optional<Resident> findById(Long Id);

    Set<Resident> findByIdIn(Set<Long> residentIds);

    // Schema V2: age -> birth_date
    @Modifying
    @Transactional
    @Query("UPDATE Resident r SET r.fullName = :fullName, r.email = :email, r.birthDate = :birthDate, r.phone = :phone WHERE r.id = :id")
    void updateResidentInfo(@Param("id") Long id,
                            @Param("fullName") String fullName,
                            @Param("email") String email,
                            @Param("birthDate") LocalDate birthDate,
                            @Param("phone") String phone);

    @Query("SELECT r FROM Resident r LEFT JOIN FETCH r.apartment WHERE r.id = :id")
    Optional<Resident> findByIdWithApartments(@Param("id") Long id);

    List<Resident> findByFullNameContainingIgnoreCase(String name);

    // Cac query qua apartment (FK) thay cho element collection cu
    @Query("SELECT r FROM Resident r JOIN r.apartment a WHERE a.apartmentNumber LIKE %:apartmentNumber%")
    List<Resident> findByApartmentNumberContaining(@Param("apartmentNumber") String apartmentNumber);

    @Query("SELECT DISTINCT r FROM Resident r JOIN r.apartment apt WHERE apt.floor = :floor")
    List<Resident> findByFloor(@Param("floor") Integer floor);

    List<Resident> findByStatusIn(List<ResidentStatus> statuses);
    List<Resident> findAll();

    @Query("SELECT r FROM Resident r WHERE r.email = :username")
    Resident findByUsername(@Param("username") String username);

    @Query("SELECT r FROM Resident r WHERE r.email = :email")
    Resident findByEmail(@Param("email") String email);

    @Query("SELECT r FROM Resident r WHERE r.phone = :phoneNumber")
    Resident findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT r FROM Resident r JOIN r.apartment a WHERE a.apartmentNumber = :apartmentNumber")
    List<Resident> findByApartmentNumber(@Param("apartmentNumber") String apartmentNumber);

    List<Resident> findByApartment_Id(Long apartmentId);

    // ---- Spec endpoints ----
    boolean existsByIdCard(String idCard);

    Optional<Resident> findByApartment_IdAndRelationship(Long apartmentId, com.qlcc.enums.Relationship relationship);

    @Query("SELECT r FROM Resident r WHERE r.apartment.id = :apartmentId AND r.relationship = 'HEAD' AND r.status <> 'MOVED_OUT'")
    Optional<Resident> findCurrentHead(@Param("apartmentId") Long apartmentId);

    @Query("SELECT COUNT(r) FROM Resident r WHERE r.apartment.id = :apartmentId AND r.status <> 'MOVED_OUT'")
    long countActiveByApartment(@Param("apartmentId") Long apartmentId);

    @Query("SELECT r FROM Resident r WHERE r.apartment.id = :apartmentId AND r.status <> 'MOVED_OUT' ORDER BY CASE WHEN r.relationship = 'HEAD' THEN 0 ELSE 1 END, r.fullName")
    List<Resident> findActiveByApartment(@Param("apartmentId") Long apartmentId);

    @Query("SELECT r FROM Resident r LEFT JOIN FETCH r.apartment a WHERE " +
           "(:search IS NULL OR LOWER(r.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(r.idCard) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(r.phone) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:apartmentId IS NULL OR r.apartment.id = :apartmentId) AND " +
           "(:status IS NULL OR r.status = :status)")
    List<Resident> searchResidents(
        @Param("search") String search,
        @Param("apartmentId") Long apartmentId,
        @Param("status") ResidentStatus status
    );

    long countByStatusNot(ResidentStatus status);

    List<Resident> findAllById(Iterable<Long> ids);

    @Query("SELECT r FROM Resident r JOIN User u ON r.id = u.residentId " +
           "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<Resident> findByUserNameContaining(@Param("username") String username);

    @Query("SELECT r FROM Resident r WHERE r.apartment.apartmentNumber IN :apartments")
    List<Resident> findByApartmentNumbersIn(@Param("apartments") List<String> apartments);

    @Query("SELECT DISTINCT r FROM Resident r JOIN r.apartment apt WHERE apt.floor IN :floors")
    List<Resident> findByFloors(@Param("floors") List<Integer> floors);

    @Query("SELECT r FROM Resident r JOIN User u ON r.id = u.residentId " +
           "WHERE u.role = :role")
    List<Resident> findByUserRole(@Param("role") String role);

    @Query("SELECT r FROM Resident r JOIN User u ON r.id = u.residentId " +
           "WHERE u.activation = :status")
    List<Resident> findByUserStatus(@Param("status") Boolean status);

    @Query("SELECT DISTINCT r FROM Resident r LEFT JOIN User u ON r.id = u.residentId " +
           "WHERE (:fullName IS NULL OR LOWER(r.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))) " +
           "AND (:username IS NULL OR (u IS NOT NULL AND LOWER(u.name) LIKE LOWER(CONCAT('%', :username, '%'))))")
    List<Resident> findByFilters(
        @Param("fullName") String fullName,
        @Param("username") String username
    );
}
