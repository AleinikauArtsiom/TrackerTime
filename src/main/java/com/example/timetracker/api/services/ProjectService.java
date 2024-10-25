package com.example.timetracker.api.services;

import com.example.timetracker.api.dto.ProjectDto;
import com.example.timetracker.api.exception.NotFoundException;
import com.example.timetracker.api.exception.UserHasProjectsException;
import com.example.timetracker.api.security.entity.UserSecurity;
import com.example.timetracker.store.entity.Project;
import com.example.timetracker.store.entity.RecordTime;
import com.example.timetracker.store.repository.ProjectRepository;
import com.example.timetracker.store.repository.RecordTimeRepository;
import com.example.timetracker.api.security.repository.UserSecurityRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final RecordTimeRepository recordTimeRepository;
    private final UserSecurityRepository userSecurityRepository;

    public ProjectService(ProjectRepository projectRepository, RecordTimeRepository recordTimeRepository, UserSecurityRepository userSecurityRepository) {
        this.projectRepository = projectRepository;
        this.recordTimeRepository = recordTimeRepository;
        this.userSecurityRepository = userSecurityRepository;
    }

    //Retrieves all projects.
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    //Retrieves a project by ID
    public ProjectDto getProjectById(Long id) {
        Project project = getProjectOrThrowException(id);
        return convertToDto(project);
    }

    //Throws exception if project is not found by ID
    private Project getProjectOrThrowException(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project with id ->" + id + " doesn't exist"));
    }


    //Creates a new project and an associated RecordTime entry.
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setProjectName(projectDto.getProjectName());
        project.setUserSecurity(userSecurityRepository.findById(projectDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found")));
        project = projectRepository.save(project);
        // Создание записи в RecordTime для этого проекта
        RecordTime recordTime = new RecordTime();
        recordTime.setProject(project);
        recordTime.setUserSecurity(project.getUserSecurity()); // Устанавливаем пользователя из проекта
        recordTimeRepository.save(recordTime);

        return convertToDto(project);
    }

    //Deletes a project by ID, checking if it is associated with a user.
    public void deleteProject(Long id) {
        Optional<Project> project = projectRepository.findById(id);

        if (project.isEmpty()) {
            throw new NotFoundException("Project with ID " + id + " not found.");
        }

        if (project.get().getUserSecurity() != null) {
            throw new UserHasProjectsException("Unable to delete the project because it is associated with a user.");
        }

        projectRepository.deleteById(id);
    }

    // Converts a Project entity to a ProjectDto
    private ProjectDto convertToDto(Project project) {
        return ProjectDto.builder().projectName(project.getProjectName()).userId(project.getUserSecurity().getId()).build();
    }
   // Updates project information, including resetting RecordTime if user is changed.
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project project = getProjectOrThrowException(id);

        // Проверяем, изменился ли пользователь в проекте
        UserSecurity newUser = userSecurityRepository.findById(projectDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        if (!project.getUserSecurity().equals(newUser)) {
            project.setUserSecurity(newUser);

            List<RecordTime> recordTimes = recordTimeRepository.findByProjectId(project.getId());
            for (RecordTime recordTime : recordTimes) {
                recordTime.setUserSecurity(newUser);
                recordTime.setTotalTime(Duration.ZERO);
                recordTimeRepository.save(recordTime);
            }
        }

        // Обновляем другие поля проекта
        project.setProjectName(projectDto.getProjectName());

        projectRepository.save(project);

        return convertToDto(project);
    }


}