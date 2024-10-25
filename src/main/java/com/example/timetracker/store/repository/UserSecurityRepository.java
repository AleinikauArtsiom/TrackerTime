package com.example.timetracker.store.repository;

import com.example.timetracker.store.entity.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {

    Optional<UserSecurity> findByLogin(String login);

    boolean existsByLogin(String login);
}
