package com.example.timetracker.api.controllers;

import com.example.timetracker.api.dto.UserDtoUpdate;
import com.example.timetracker.api.dto.UserSecurityDto;
import com.example.timetracker.store.entity.UserSecurity;
import com.example.timetracker.api.services.UserSecurityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "Bearer Authentication")
@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/security")
public class UserSecurityController {

    private final UserSecurityService userSecurityService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserSecurityDto>> getAllUsers() {
        List<UserSecurityDto> users = userSecurityService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserSecurity> getUserById(@PathVariable Long id) {
        UserSecurity userSecurity = userSecurityService.getUserById(id);
        return ResponseEntity.ok(userSecurity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserSecurity> createUser(@RequestBody UserSecurity user) {
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<UserSecurity> updateUser(@PathVariable Long id, @RequestBody UserDtoUpdate userDtoUpdate) {
        return new ResponseEntity<>(userSecurityService.updateUser(userDtoUpdate) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userSecurityService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/current")
    public ResponseEntity<UserSecurityDto> getInfoAboutCurrentUser(Principal principal) {
        Optional<UserSecurityDto> userSecurityDto = userSecurityService.getInfoAboutCurrentUser(principal.getName());

        if (userSecurityDto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(userSecurityDto.get(), HttpStatus.OK);
    }
}
