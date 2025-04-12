package com.example.takemeds.presentationModels;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DosagePresentationModel {
    @NotNull(message = "Medication id is required")
    private long medicationId;

    private String frequency;

    private LocalDateTime scheduledTime;
}
