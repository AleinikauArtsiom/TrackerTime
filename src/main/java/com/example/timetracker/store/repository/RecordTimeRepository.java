package com.example.timetracker.store.repository;

import com.example.timetracker.api.security.entity.UserSecurity;
import com.example.timetracker.store.entity.Project;
import com.example.timetracker.store.entity.RecordTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordTimeRepository extends JpaRepository<RecordTime, Long> {
    List<RecordTime> findAllByUserSecurity(UserSecurity userSecurity);

    List<RecordTime> findByProject(Project project);

    List<RecordTime> findByProjectId(Long id);
}
