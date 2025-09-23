package com.example.takemeds.presentationModels.takenLogPMs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LogResponseDto {
    private Long id;
    private Long scheduleId;
    private LocalDateTime timeTaken;
    private String notes;
}
