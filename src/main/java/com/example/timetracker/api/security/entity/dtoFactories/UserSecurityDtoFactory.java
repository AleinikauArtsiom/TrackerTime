package com.example.timetracker.api.security.entity.dtoFactories;

import com.example.timetracker.api.security.entity.UserSecurity;
import com.example.timetracker.api.dto.UserSecurityDto;
import org.springframework.stereotype.Component;

@Component
public class UserSecurityDtoFactory {
    public UserSecurityDto makeUserSecurityFactory(UserSecurity userSecurity) {
        return UserSecurityDto.builder()
                .id(userSecurity.getId())
                .login(userSecurity.getLogin())
                .role(String.valueOf(userSecurity.getRole()))
                .build();
    }
}
