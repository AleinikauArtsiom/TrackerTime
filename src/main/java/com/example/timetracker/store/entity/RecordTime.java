package com.example.timetracker.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "record_time")
public class RecordTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;  // Связывание с таблицей Project через project_id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserSecurity userSecurity;  // Связывание с таблицей UserSecurity через user_id

    @Builder.Default
    @Column(name = "total_time")
    private Duration totalTime = Duration.ZERO;  // Время по умолчанию — 0
}
