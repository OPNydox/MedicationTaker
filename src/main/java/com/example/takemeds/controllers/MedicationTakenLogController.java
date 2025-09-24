package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.takenLogPMs.CreateLogDto;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import com.example.takemeds.services.MedicationTakenLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/med/user/logs")
public class MedicationTakenLogController {

    private final MedicationTakenLogService logService;

    public MedicationTakenLogController(MedicationTakenLogService logService) {
        this.logService = logService;
    }

    @PostMapping("/create")
    public ResponseEntity<LogResponseDto> createLog(@RequestBody CreateLogDto createLogDto, @AuthenticationPrincipal UserDetails userDetails) throws InvalidRequestException, UnauthorizedAccessException {
        LogResponseDto createdLog = logService.createLog(createLogDto, userDetails);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/{logId}")
    public ResponseEntity<LogResponseDto> getLogById(@PathVariable Long logId, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        return ResponseEntity.ok(logService.getLogById(logId, userDetails));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<LogResponseDto>> getLogsBySchedule(@PathVariable Long scheduleId, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        return ResponseEntity.ok(logService.getLogsByScheduleId(scheduleId, userDetails));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        logService.deleteLog(logId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
