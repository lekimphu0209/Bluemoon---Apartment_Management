package com.qlcc.entity;

import com.qlcc.enums.FeeCalcMethod;
import com.qlcc.enums.FeeTypeStatus;
import com.qlcc.enums.FeeTypeType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fee_types")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FeeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private FeeTypeType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "calc_method", nullable = false, length = 30)
    private FeeCalcMethod calcMethod;

    @Column(name = "rate", precision = 15, scale = 2)
    private BigDecimal rate;

    @Column(name = "suggested_amount", precision = 15, scale = 2)
    private BigDecimal suggestedAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "mandatory", nullable = false)
    private Boolean mandatory = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private FeeTypeStatus status = FeeTypeStatus.ACTIVE;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
