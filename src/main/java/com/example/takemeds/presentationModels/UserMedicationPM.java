package com.example.takemeds.presentationModels;

import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserMedicationPM {
    private Long id;

    private Long userID;
    private BaseMedicationPM medication;
    private DosagePresentationModel dosage;
    private Long receiptId;
    private LocalDate endDate;
    private boolean isFinished;
}
