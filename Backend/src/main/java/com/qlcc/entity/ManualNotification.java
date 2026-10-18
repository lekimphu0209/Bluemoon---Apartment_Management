package com.qlcc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "manual_notifications")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ManualNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "manual_notification_residents",
            joinColumns = @JoinColumn(name = "manual_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "resident_id")
    )
    private List<Resident> residents;

    @Column(nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
