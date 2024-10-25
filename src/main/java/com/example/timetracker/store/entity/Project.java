package com.example.timetracker.store.entity;

import com.example.timetracker.api.security.entity.UserSecurity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "project_name", nullable = false, length = 255)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserSecurity userSecurity;
}
