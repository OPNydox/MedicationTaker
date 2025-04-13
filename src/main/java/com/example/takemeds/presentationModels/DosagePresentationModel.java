package com.example.takemeds.presentationModels;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DosagePresentationModel {
    @NotNull(message = "Medication id is required")
    private long medicationId;

    private String frequency;

    private byte timesPerDay;

    private List<LocalTime> timesToTake;

    public DosagePresentationModel() {
        setTimesToTake(new ArrayList<>());
    }
}
