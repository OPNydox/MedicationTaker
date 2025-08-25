package com.example.takemeds.presentationModels.medicationPMs;

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
public class MedicationDosageRefPM extends BaseMedicationPM {
    private Long dosageId;

}
