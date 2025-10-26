package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.takenLogPMs.CreateLogDto;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import com.example.takemeds.repositories.MedicationTakenLogRepository;
import com.example.takemeds.utils.mappers.MedTakenMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicationTakenLogService {
    private final UserService userService;

    private final MedicationTakenLogRepository logRepository;
    
    private final MedicationScheduleReadService scheduleReadService;
    
    private final MedTakenMapper logMapper;

    public MedicationTakenLogService(UserService userService, MedicationTakenLogRepository logRepository, MedicationScheduleReadService scheduleReadService, MedTakenMapper logMapper) {
        this.userService = userService;
        this.logRepository = logRepository;
        this.scheduleReadService = scheduleReadService;
        this.logMapper = logMapper;
    }

    /**
     * Creates a new medication taken log entry.
     */
    @Transactional
    public LogResponseDto createLog(CreateLogDto createLogDto, UserDetails userDetails) throws InvalidRequestException, UnauthorizedAccessException {
        MedicationSchedule schedule = scheduleReadService.findMedicationScheduleEntityById(createLogDto.getScheduleId());
        User user = userService.getUser(userDetails.getUsername());

        if (user.getId() != schedule.getUser().getId()) {
            throw new UnauthorizedAccessException("You have not been assigned this medication");
        }

        MedicationTakenLog newLog = MedicationTakenLog.builder().build();
        newLog.setMedicationSchedule(schedule);
        newLog.setTimeTaken(createLogDto.getTimeTaken() != null ? createLogDto.getTimeTaken() : LocalDateTime.now());
        newLog.setNotes(createLogDto.getNotes());

        MedicationTakenLog savedLog = logRepository.save(newLog);
        return logMapper.mapToDto(savedLog);
    }

    /**
     * Retrieves a single log entry by its ID.
     */
    public LogResponseDto getLogById(Long logId, UserDetails userDetails) throws UnauthorizedAccessException {
        User user = userService.getUser(userDetails.getUsername());

        MedicationTakenLog log = logRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("MedicationTakenLog not found with ID: " + logId));

        if (user.getId() != log.getMedicationSchedule().getUser().getId()) {
            throw new UnauthorizedAccessException("You do not have access to this resource");
        }

        return logMapper.mapToDto(log);
    }

    /**
     * Retrieves all log entries for a specific medication schedule.
     */
    public List<LogResponseDto> getLogsByScheduleId(Long scheduleId, UserDetails userDetails) throws UnauthorizedAccessException {
        User user = userService.getUser(userDetails.getUsername());
        MedicationSchedule schedule = scheduleReadService.findMedicationScheduleEntityById(scheduleId);

        if (user.getId() != schedule.getUser().getId()) {
            throw new UnauthorizedAccessException("You do not have access to this resource");
        }

        List<MedicationTakenLog> logs = logRepository.findByMedicationScheduleId(scheduleId);

        return logs.stream().map(logMapper::mapToDto).collect(Collectors.toList());
    }

    /**
     * Deletes a log entry by its ID.
     */
    @Transactional
    public void deleteLog(Long logId, UserDetails userDetails) throws UnauthorizedAccessException {
        User user = userService.getUser(userDetails.getUsername());
        Optional<MedicationTakenLog> log = logRepository.findById(logId);

        if (log.isEmpty()) {
            throw new EntityNotFoundException("MedicationTakenLog not found with ID: " + logId);
        }

        if (user.getId() != log.get().getMedicationSchedule().getUser().getId()) {
            throw new UnauthorizedAccessException("You do not have access to this resource");
        }

        logRepository.deleteById(logId);
    }
}
