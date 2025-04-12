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
        output.setDosages(getDosages(input));

        if (input.getUser() != null) {
            output.setUser(input.getUser().getId());
        }

        return output;
    }

    private static List<Long> getDosages(Medication input) {
        List<Long> dosageIds = new ArrayList<>();

        if (input == null || input.getDosages() == null) {
            return dosageIds;
        }

        for (Dosage dosage : input.getDosages()) {
            dosageIds.add(dosage.getId());
        }

        return dosageIds;
    }
}
