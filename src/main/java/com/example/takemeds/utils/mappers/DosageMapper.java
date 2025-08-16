package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.DosagePresentationModel;
import com.example.takemeds.utils.StringUtilities;
import org.springframework.stereotype.Component;

@Component
public class DosageMapper {

    public DosagePresentationModel entityToPM(Dosage input) {
        return DosagePresentationModel.builder().frequency(input.getFrequency().toString())
                                                .timesPerDay(input.getTimesPerDay())
                                                .timesToTake(input.getTimesToTake()).build();
    }

    public Dosage PMtoEntity(DosagePresentationModel dosage) throws InvalidFrequencyException {
        Frequency frequency;

        frequency = StringUtilities.frequencyFromString(dosage.getFrequency());

        return Dosage.builder().frequency(frequency)
                     .timesPerDay(dosage.getTimesPerDay())
                     .timesToTake(dosage.getTimesToTake()).build();
    }
}
