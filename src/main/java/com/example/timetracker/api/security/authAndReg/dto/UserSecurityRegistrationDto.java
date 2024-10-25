package com.example.timetracker.api.security.authAndReg.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserSecurityRegistrationDto {
    @NotNull
    @Size(min = 2, max = 20)
    private String login;

    @NotNull
    @Size(min = 2, max = 30)
    private String password;
}
