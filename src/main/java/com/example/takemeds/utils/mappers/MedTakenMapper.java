package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MedTakenMapper {
    public LogResponseDto mapToDto(MedicationTakenLog log) {
        if (log == null) {
            return null;
        }

        return LogResponseDto.builder()
                .id(log.getId())
                .scheduleId(log.getMedicationSchedule().getId())
                .timeTaken(log.getTimeTaken())
                .notes(log.getNotes())
                .build();
    }
}
