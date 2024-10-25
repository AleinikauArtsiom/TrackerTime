package com.example.timetracker.api.controllers;

import com.example.timetracker.api.security.authAndReg.dto.AuthRequestDto;
import com.example.timetracker.api.security.authAndReg.dto.AuthResponseDto;
import com.example.timetracker.api.security.authAndReg.dto.UserSecurityRegistrationDto;
import com.example.timetracker.api.services.SecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityRequirement(name = "Bearer Authentication")
@Transactional
@RestController
@RequestMapping("/api/v1/security")
public class SecurityController {
    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    // Регистрация нового пользователя
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserSecurityRegistrationDto registrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        securityService.registerUser(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Генерация JWT токена на основе логина и пароля
    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequestDto authRequestDto) {
        try {
            Optional<String> token = securityService.generateToken(authRequestDto);
            if (token.isPresent()) {
                return ResponseEntity.ok(new AuthResponseDto(token.get()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные учетные данные");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка");
        }
    }
}
