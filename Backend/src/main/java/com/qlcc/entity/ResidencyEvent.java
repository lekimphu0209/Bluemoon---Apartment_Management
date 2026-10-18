package com.qlcc.entity;

import com.qlcc.enums.ResidencyEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "residency_events")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResidencyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ResidencyEventType type;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "destination")
    private String destination;

    @Column(name = "note", length = 500)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
