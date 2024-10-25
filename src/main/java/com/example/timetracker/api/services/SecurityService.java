package com.example.timetracker.api.services;


import com.example.timetracker.api.exception.SameUserInDataBase;
import com.example.timetracker.store.entity.Roles;
import com.example.timetracker.api.security.service.JwtUtils;
import org.springframework.stereotype.Service;
import com.example.timetracker.store.entity.UserSecurity;
import com.example.timetracker.api.security.authAndReg.dto.AuthRequestDto;
import com.example.timetracker.api.security.authAndReg.dto.UserSecurityRegistrationDto;
import com.example.timetracker.store.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SecurityService {
    private final UserSecurityRepository userSecurityRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityService(UserSecurityRepository userSecurityRepository,
                           JwtUtils jwtUtils,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userSecurityRepository = userSecurityRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    //регистрация юзера
    @Transactional(rollbackFor = Exception.class)
    public void registerUser(UserSecurityRegistrationDto registrationDto) {
        if (userSecurityRepository.existsByLogin(registrationDto.getLogin())) {
            throw new SameUserInDataBase("Пользователь с таким логином уже существует.");
        }

        UserSecurity userSecurity = UserSecurity.builder()
                .login(registrationDto.getLogin())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Roles.USER)
                .build();
        userSecurityRepository.save(userSecurity);
    }

    // Генерация токена на основе логина и пароля
    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        // Аутентификация пользователя с помощью логина и пароля
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getLogin(), authRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Генерация JWT токена
        return Optional.of(jwtUtils.generateJwtToken(authRequestDto.getLogin()));
    }
}
