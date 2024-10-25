package com.example.timetracker.api.controllers;

import com.example.timetracker.api.dto.ProjectDto;
import com.example.timetracker.api.services.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Transactional
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    // Retrieves all projects; accessible by ADMIN role.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllProject")
    public List<ProjectDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    //Retrieves a project by ID; accessible by ADMIN role.
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getId/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    // Создаёт новый проект, доступно только для роли ADMIN.
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProject")
    public ProjectDto createProject(@RequestBody ProjectDto projectDto) {
        return projectService.createProject(projectDto);
    }

    //Deletes a project by ID; accessible by ADMIN role.
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteProject/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // Обновляет проект по ID, доступно только для роли ADMIN.
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateProject/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        ProjectDto updatedProject = projectService.updateProject(id, projectDto);
        return ResponseEntity.ok(updatedProject);
    }
}
