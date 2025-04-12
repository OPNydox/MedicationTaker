package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.presentationModels.DosagePresentationModel;

public class DosageMappers {

    public static DosagePresentationModel entityToPM(Dosage input) {
        DosagePresentationModel output = new DosagePresentationModel();

        output.setFrequency(input.getFrequency().toString());
        output.setMedicationId(input.getMedication().getId());
        output.setScheduledTime(input.getScheduledTime());

        return output;
    }
}
