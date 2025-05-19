package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.MedicationPresentationModel;

import java.util.ArrayList;
import java.util.List;

public class MedicationMapper {

    public static MedicationPresentationModel mapEntityToPM(Medication input) {
        MedicationPresentationModel output = new MedicationPresentationModel();

        if (input == null) {
            return output;
        }

        output.setId(input.getId());
        output.setName(input.getName());
        output.setDescription(input.getDescription());

        if (input.getDosage() != null) {
            output.setDosage(input.getDosage().getId());
        }

        return output;
    }
}
