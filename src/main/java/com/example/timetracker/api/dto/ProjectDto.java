package com.example.timetracker.api.dto;

import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private String projectName;
    private Long userId;
}
