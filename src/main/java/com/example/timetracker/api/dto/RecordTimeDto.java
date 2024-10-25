package com.example.timetracker.api.dto;

import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordTimeDto {
    private Long id;
    private Long projectId;
    private Long userId;
    private String formattedTime;

}
