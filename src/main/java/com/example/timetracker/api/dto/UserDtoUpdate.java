package com.example.timetracker.api.dto;

import lombok.*;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoUpdate {
    private Long id;
    private String login;
    private String password;
}
