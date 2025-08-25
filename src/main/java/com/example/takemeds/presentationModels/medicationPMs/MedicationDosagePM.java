package com.example.takemeds.presentationModels.medicationPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
public class MedicationDosagePM extends BaseMedicationPM {
    @NotNull(message = "Dosage is required.")
    @Valid
    private BaseDosagePM defaultDosagePM;
}
