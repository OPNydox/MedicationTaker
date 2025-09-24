package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.presentationModels.takenLogPMs.CreateLogDto;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import com.example.takemeds.repositories.MedicationTakenLogRepository;
import com.example.takemeds.utils.mappers.MedTakenMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationTakenLogService {
    private final MedicationTakenLogRepository logRepository;
    
    private final MedicationScheduleReadService scheduleReadService;
    
    private final MedTakenMapper logMapper;

    public MedicationTakenLogService(MedicationTakenLogRepository logRepository, MedicationScheduleReadService scheduleReadService, MedTakenMapper logMapper) {
        this.logRepository = logRepository;
        this.scheduleReadService = scheduleReadService;
        this.logMapper = logMapper;
    }

    /**
     * Creates a new medication taken log entry.
     */
    @Transactional
    public LogResponseDto createLog(CreateLogDto createLogDto) {
        MedicationSchedule schedule = scheduleReadService.findMedicationScheduleEntityById(createLogDto.getScheduleId());

        MedicationTakenLog newLog = new MedicationTakenLog();
        newLog.setMedicationSchedule(schedule);
        newLog.setTimeTaken(createLogDto.getTimeTaken() != null ? createLogDto.getTimeTaken() : LocalDateTime.now());
        newLog.setNotes(createLogDto.getNotes());

        MedicationTakenLog savedLog = logRepository.save(newLog);
        return logMapper.mapToDto(savedLog);
    }

    /**
     * Retrieves a single log entry by its ID.
     */
    public LogResponseDto getLogById(Long logId) {
        MedicationTakenLog log = logRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("MedicationTakenLog not found with ID: " + logId));
        return logMapper.mapToDto(log);
    }

    /**
     * Retrieves all log entries for a specific medication schedule.
     */
    public List<LogResponseDto> getLogsByScheduleId(Long scheduleId) {
        List<MedicationTakenLog> logs = logRepository.findByMedicationScheduleId(scheduleId);

        return logs.stream().map(logMapper::mapToDto).collect(Collectors.toList());
    }

    /**
     * Deletes a log entry by its ID.
     */
    @Transactional
    public void deleteLog(Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new EntityNotFoundException("MedicationTakenLog not found with ID: " + logId);
        }
        logRepository.deleteById(logId);
    }
}
