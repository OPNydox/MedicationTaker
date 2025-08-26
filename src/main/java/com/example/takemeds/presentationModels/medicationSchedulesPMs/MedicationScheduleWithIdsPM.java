package com.example.takemeds.presentationModels.medicationSchedulesPMs;

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
public class MedicationScheduleWithIdsPM extends BaseMedicationSchedulePM {
    private Long medicationId;
}
