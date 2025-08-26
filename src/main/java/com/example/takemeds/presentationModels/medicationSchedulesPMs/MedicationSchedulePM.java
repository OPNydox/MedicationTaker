package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class MedicationSchedulePM extends BaseMedicationSchedulePM {

    private BaseMedicationPM medication;

}
