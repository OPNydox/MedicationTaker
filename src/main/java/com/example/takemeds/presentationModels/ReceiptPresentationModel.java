package com.example.takemeds.presentationModels;

import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ReceiptPresentationModel {
    private Long id;
    private LocalDateTime receiptTime;
    private String description;
    private List<MedicationSchedulePM> userMedications; // Changed to a list of presentation models
    private boolean isFinalized;
}
