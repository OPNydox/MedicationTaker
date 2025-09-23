package com.example.takemeds.controllers;

import com.example.takemeds.presentationModels.takenLogPMs.CreateLogDto;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import com.example.takemeds.services.MedicationTakenLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/med/user/logs")
public class MedicationTakenLogController {

    private final MedicationTakenLogService logService;

    public MedicationTakenLogController(MedicationTakenLogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<LogResponseDto> createLog(@RequestBody CreateLogDto createLogDto) {
        LogResponseDto createdLog = logService.createLog(createLogDto);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/{logId}")
    public ResponseEntity<LogResponseDto> getLogById(@PathVariable Long logId) {
        return ResponseEntity.ok(logService.getLogById(logId));
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<LogResponseDto>> getLogsBySchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(logService.getLogsByScheduleId(scheduleId));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId) {
        logService.deleteLog(logId);
        return ResponseEntity.noContent().build();
    }
}
