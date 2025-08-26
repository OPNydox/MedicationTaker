package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMedicationSchedulePM {

    private LocalDate endDate;
    private LocalDate startDate;
    private Long userID;
    private BaseDosagePM dosage;
}
