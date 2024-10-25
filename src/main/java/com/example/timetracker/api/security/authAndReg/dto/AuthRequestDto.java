package com.example.timetracker.api.security.authAndReg.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AuthRequestDto {
    private String login;
    private String password;
}