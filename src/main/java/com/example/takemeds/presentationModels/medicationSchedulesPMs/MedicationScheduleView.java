package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationScheduleView extends MedicationSchedulePM {
    private Long id;

    private Long receiptId;

    private boolean isFinished;
}
