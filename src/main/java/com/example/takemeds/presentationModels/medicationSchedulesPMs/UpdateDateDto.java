package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateDateDto {
    private LocalDate startDate;
    private LocalDate endDate;
}
