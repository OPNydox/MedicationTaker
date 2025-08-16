package com.example.takemeds.presentationModels;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
public class DosagePresentationModel {
    private MedicationPresentationModel medicationId;

    private String frequency;

    private byte timesPerDay;

    private List<LocalTime> timesToTake;

}
