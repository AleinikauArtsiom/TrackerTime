package com.example.timetracker.api.services;

import com.example.timetracker.api.dto.RecordTimeDto;
import com.example.timetracker.api.exception.NotFoundException;
import com.example.timetracker.api.security.entity.UserSecurity;
import com.example.timetracker.api.security.repository.UserSecurityRepository;
import com.example.timetracker.store.entity.Project;
import com.example.timetracker.store.entity.RecordTime;
import com.example.timetracker.store.repository.ProjectRepository;
import com.example.timetracker.store.repository.RecordTimeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecordTimeService {
    private final RecordTimeRepository recordTimeRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final ProjectRepository projectRepository;
    private final Map<Long, LocalDateTime> startTimes = new HashMap<>();

    public RecordTimeService(RecordTimeRepository recordTimeRepository,
                             UserSecurityRepository userSecurityRepository,
                             ProjectRepository projectRepository) {
        this.recordTimeRepository = recordTimeRepository;
        this.userSecurityRepository = userSecurityRepository;
        this.projectRepository = projectRepository;
    }


    public List<RecordTimeDto> getAllRecords() {
        return recordTimeRepository.findAll()
                .stream()
                .map(this::convertRecordToDto)
                .collect(Collectors.toList());
    }


    public RecordTimeDto getRecordById(Long id) {
        RecordTime recordTime = recordTimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Record not found"));
        return convertRecordToDto(recordTime);
    }

    public void startDevelop(Long recordId) {
        RecordTime recordTime = recordTimeRepository.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Запись не найдена"));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = ((UserDetails) authentication.getPrincipal()).getUsername();

        UserSecurity currentUser = userSecurityRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!recordTime.getUserSecurity().getId().equals(currentUser.getId())) {
            throw new SecurityException("Вы не можете управлять этим проектом.");
        }

        if (!startTimes.containsKey(recordId)) {
            startTimes.put(recordId, LocalDateTime.now());
        }
    }

    public void stopDevelop(Long recordId) {
        LocalDateTime startTime = startTimes.get(recordId);
        if (startTime != null) {
            RecordTime recordTime = recordTimeRepository.findById(recordId)
                    .orElseThrow(() -> new NotFoundException("Запись не найдена"));

            // Получаем текущего пользователя
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String login = ((UserDetails) authentication.getPrincipal()).getUsername();

            UserSecurity currentUser = userSecurityRepository.findByLogin(login)
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

            // Проверяем, что текущий пользователь является владельцем проекта
            if (!recordTime.getUserSecurity().getId().equals(currentUser.getId())) {
                throw new SecurityException("Вы не можете управлять этим проектом.");
            }

            Duration timeSpent = Duration.between(startTime, LocalDateTime.now());

            recordTime.setTotalTime(recordTime.getTotalTime().plus(timeSpent));

            recordTimeRepository.save(recordTime);

            startTimes.remove(recordId);
        }
    }

    public List<RecordTimeDto> getUserRecords(String username) {
        UserSecurity user = userSecurityRepository.findByLogin(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        // Возвращаем записи, принадлежащие текущему пользователю
        return recordTimeRepository.findAllByUserSecurity(user).stream()
                .map(this::convertRecordToDto)
                .collect(Collectors.toList());
    }

    // Конвертация объекта RecordTime в DTO и форматирование времени
    private RecordTimeDto convertRecordToDto(RecordTime recordTime) {
        RecordTimeDto dto = new RecordTimeDto();
        dto.setId(recordTime.getId());
        dto.setUserId(recordTime.getUserSecurity().getId());
        dto.setProjectId(recordTime.getProject().getId());

        // Конвертация общего времени в удобочитаемый формат
        Duration totalTime = recordTime.getTotalTime();
        long hours = totalTime.toHours();
        long minutes = totalTime.toMinutes() % 60;
        long seconds = totalTime.getSeconds() % 60;

        dto.setFormattedTime(String.format("%02d:%02d:%02d", hours, minutes, seconds));

        return dto;
    }
}
