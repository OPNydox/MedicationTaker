package com.example.takemeds.presentationModels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserMedicationPresentationModel {
    private Long id;

    private Long userID;
    private MedicationPresentationModel medication;
    private DosagePresentationModel dosage;


    private Long receiptId;
}
