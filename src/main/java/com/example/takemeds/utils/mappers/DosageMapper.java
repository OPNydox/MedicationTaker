package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.utils.StringUtilities;
import org.springframework.stereotype.Component;

@Component
public class DosageMapper {

    private final StringUtilities stringUtilities;

    public DosageMapper(StringUtilities stringUtilities) {
        this.stringUtilities = stringUtilities;
    }

    public DosagePresentationModel mapEntityToPM(Dosage dosage) {
        if (dosage == null) {
            return null;
        }

        return DosagePresentationModel.builder()
                .frequency(dosage.getFrequency().toString())
                .timesPerDay(dosage.getTimesPerDay())
                .timesToTake(dosage.getTimesToTake())
                .build();
    }

    public CreateDosagePM mapEntityToCreatePM(Dosage dosage) {
        if (dosage == null) {
            return null;
        }

        return CreateDosagePM.builder()
                .frequency(dosage.getFrequency().toString())
                .timesPerDay(dosage.getTimesPerDay())
                .timesToTake(dosage.getTimesToTake())
                .build();
    }

    public Dosage mapPMToEntity(BaseDosagePM dosagePM) throws InvalidFrequencyException {
        if (dosagePM == null) {
            return null;
        }

        Dosage dosage = Dosage.builder().build();
        dosage.setFrequency(stringUtilities.frequencyFromString(dosagePM.getFrequency()));
        dosage.setTimesPerDay(dosagePM.getTimesPerDay());
        dosage.setTimesToTake(dosagePM.getTimesToTake());

        return dosage;
    }
}
