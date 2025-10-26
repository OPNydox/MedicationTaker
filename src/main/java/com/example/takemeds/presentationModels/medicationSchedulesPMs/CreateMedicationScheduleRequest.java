package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateMedicationScheduleRequest {
    private Long medicationId;

    @NotNull(message = "Medication is required.")
    private CreateMedicationDto medication;

    private BaseDosagePM dosage;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long userId;
}
