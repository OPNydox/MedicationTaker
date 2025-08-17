package com.example.takemeds.presentationModels.dosagePMs;

import com.example.takemeds.presentationModels.medicationPMs.MedicationPresentationModel;
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
public class DosagePresentationModel  extends BaseDosagePM {
    private MedicationPresentationModel medication;
}
