package com.example.takemeds.presentationModels.medicationSchedulesPMs;

import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
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
public class MedicationSchedulePM extends BaseMedicationSchedulePM {

    private CreateMedicationDto medication;

}
