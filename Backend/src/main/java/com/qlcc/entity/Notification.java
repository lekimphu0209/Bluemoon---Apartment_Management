package com.qlcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @Column(nullable = false)
    private String message;

    @Column
    private String linkHeader;
    @Column
    private String linkAPI;


    @Column(name = "is_read", nullable = false)
    private boolean read;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
