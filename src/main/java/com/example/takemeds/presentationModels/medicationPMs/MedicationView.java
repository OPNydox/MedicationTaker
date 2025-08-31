package com.example.takemeds.presentationModels.medicationPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MedicationView {
    private Long id;

    private String name;

    private String description;

    private BaseDosagePM defaultDosage;
}
