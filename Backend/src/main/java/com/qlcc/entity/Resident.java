package com.qlcc.entity;

import com.qlcc.enums.Relationship;
import com.qlcc.enums.ResidentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "residents")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "apartment")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "id_card", unique = true)
    private String idCard;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    // Quan he that: residents.apartment_id -> apartments.id (schema V2)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    @JsonIgnore // tranh vong lap khi serialize Resident
    private Apartment apartment;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship", nullable = false)
    private Relationship relationship = Relationship.OTHER;

    // Schema V2: cot `residency_status` (truoc day la status voi enum THUONGTRU/TAMTRU/TAMVANG)
    @Enumerated(EnumType.STRING)
    @Column(name = "residency_status", nullable = false)
    private ResidentStatus status = ResidentStatus.LIVING;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /**
     * Compat getter: code cu dung resident.getApartmentNumbers() (Set<String>).
     * Schema moi chi co 1 apartment -> tra ve set 1 phan tu.
     */
    public Set<String> getApartmentNumbers() {
        if (apartment != null && apartment.getApartmentNumber() != null) {
            Set<String> result = new HashSet<>();
            result.add(apartment.getApartmentNumber());
            return result;
        }
        return new HashSet<>(apartmentNumbersInput);
    }

    /**
     * Compat setter: code cu gan resident.setApartmentNumbers(Set<String>).
     * Voi schema moi (1 apartment/resident), ta chi luu apartment DAU TIEN
     * cua set vao mot field tam; service se resolve sang Apartment entity.
     */
    @Transient
    private Set<String> apartmentNumbersInput = new HashSet<>();

    public void setApartmentNumbers(Set<String> apartmentNumbers) {
        this.apartmentNumbersInput = apartmentNumbers != null ? apartmentNumbers : new HashSet<>();
    }

    public Set<String> getApartmentNumbersInput() {
        return apartmentNumbersInput;
    }

    /**
     * Compat getter: code cu dung resident.getAge() (Long).
     * Schema moi luu birth_date -> tinh age tu birthDate.
     */
    public Long getAge() {
        if (birthDate == null) return null;
        return (long) java.time.Period.between(birthDate, LocalDate.now()).getYears();
    }
}
