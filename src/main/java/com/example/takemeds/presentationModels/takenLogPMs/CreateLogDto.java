package com.example.takemeds.presentationModels.takenLogPMs;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateLogDto {
    @NotNull
    private Long scheduleId;
    private LocalDateTime timeTaken;
    private String notes;
}
