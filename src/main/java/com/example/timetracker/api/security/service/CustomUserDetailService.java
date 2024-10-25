package com.example.timetracker.api.security.service;

import com.example.timetracker.store.entity.UserSecurity;
import com.example.timetracker.store.repository.UserSecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserSecurityRepository userSecurityRepository;

    @Autowired
    public CustomUserDetailService(UserSecurityRepository userSecurityRepository) {
        this.userSecurityRepository = userSecurityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserSecurity> securityInfoOptional = userSecurityRepository.findByLogin(login);
        if (securityInfoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден: " + login);
        }

        UserSecurity userSecurity = securityInfoOptional.get();

        // Используем объект User из Spring Security для построения UserDetails
        return User.builder().username(userSecurity.getLogin())  // Используем логин в качестве username
                .password(userSecurity.getPassword())
                .roles(userSecurity.getRole().toString())
                .build();
    }
}
