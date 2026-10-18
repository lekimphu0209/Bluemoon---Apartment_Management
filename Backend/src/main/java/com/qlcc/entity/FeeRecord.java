package com.qlcc.entity;

import com.qlcc.enums.BillStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fee_records")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FeeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fee_type_id", nullable = false)
    private FeeType feeType;

    // Dinh dang YYYY-MM, CHECK trong DB (varchar(7))
    @Column(name = "period", nullable = false, length = 7)
    private String period;

    // NULL = chua nhap (dien, nuoc o v2.0)
    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private BillStatus status = BillStatus.UNPAID;

    @Column(name = "contribution", nullable = false)
    private Boolean contribution = false;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by")
    private User collectedBy;

    @Column(name = "note", length = 500)
    private String note;

    // Cot sinh trong DB (dedup_key) - chi doc
    @Column(name = "dedup_key", insertable = false, updatable = false)
    private String dedupKey;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
