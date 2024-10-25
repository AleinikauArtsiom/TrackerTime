package com.example.timetracker.store.repository;

import com.example.timetracker.store.entity.Project;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph
    List<Project> findByUserSecurityId(Long userId);


}
