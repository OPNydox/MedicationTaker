package com.example.takemeds.presentationModels;

import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserMedicationPresentationModel {
    private Long id;

    private Long userID;
    private MedicationDosagePM medication;
    private DosagePresentationModel dosage;


    private Long receiptId;
}
