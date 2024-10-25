package com.example.timetracker.api.controllers;

import com.example.timetracker.api.dto.RecordTimeDto;
import com.example.timetracker.api.services.RecordTimeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@SecurityRequirement(name = "Bearer Authentication")
@Transactional
@RestController
@RequestMapping("/api/v1/records")
public class RecordTimeControllers {

    private final RecordTimeService recordTimeService;

    public RecordTimeControllers(RecordTimeService recordTimeService) {
        this.recordTimeService = recordTimeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("getAllRecords")
    public ResponseEntity<List<RecordTimeDto>> getAllRecords() {
        List<RecordTimeDto> records = recordTimeService.getAllRecords();
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRecordById/{id}")
    public ResponseEntity<RecordTimeDto> getRecordById(@PathVariable Long id) {
        RecordTimeDto record = recordTimeService.getRecordById(id);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{id}/start")
    public ResponseEntity<Void> startDevelop(@PathVariable Long id) {
        try {
            recordTimeService.startDevelop(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SecurityException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{id}/stop")
    public ResponseEntity<Void> stopDevelop(@PathVariable Long id) {
        try {
            recordTimeService.stopDevelop(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SecurityException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/my-records")
    public ResponseEntity<List<RecordTimeDto>> getUserRecords(Principal principal) {
        String username = principal.getName();
        List<RecordTimeDto> records = recordTimeService.getUserRecords(username);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }
}
