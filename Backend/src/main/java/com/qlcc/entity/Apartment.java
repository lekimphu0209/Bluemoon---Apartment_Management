package com.qlcc.entity;

import com.qlcc.enums.ApartmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "residents")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Schema V2: cot `number` (truoc day la apartment_number).
    // Giu ten property `apartmentNumber` de cac derived query/JPQL cu van chay.
    @Column(name = "number", nullable = false, unique = true)
    private String apartmentNumber;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "area")
    private Double area;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApartmentStatus status = ApartmentStatus.EMPTY;

    // Quan he that: residents.apartment_id -> apartments.id (schema V2)
    @OneToMany(mappedBy = "apartment")
    @JsonIgnore // tranh vong lap khi serialize Apartment
    private Set<Resident> residents = new HashSet<>();

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /**
     * Compat getter: code cu dung apartment.getResidentIds() khi quan he
     * con la @ElementCollection. Gio derive tu residents.
     */
    @JsonIgnore
    public Set<Long> getResidentIds() {
        if (residents == null) return new HashSet<>();
        return residents.stream().map(Resident::getId).collect(Collectors.toSet());
    }
}
