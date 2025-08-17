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

    private StringUtilities stringUtilities;

    public DosageMapper(StringUtilities stringUtilities) {
        this.stringUtilities = stringUtilities;
    }

    public DosagePresentationModel mapEntityToDosagePM(Dosage dosage) {
        if (dosage == null) {
            return null;
        }

        DosagePresentationModel dosagePM = DosagePresentationModel.builder().build();

        mapBasePMtoEntity(dosage, dosagePM);

        return dosagePM;
    }

    public CreateDosagePM mapEntityToCreateDosagePM(Dosage dosage) {
        if (dosage == null) {
            return null;
        }

        CreateDosagePM dosagePM = CreateDosagePM.builder().build();

        mapBasePMtoEntity(dosage, dosagePM);
        dosagePM.setMedicationId(dosagePM.getMedicationId());

        return dosagePM;
    }

    private void mapBasePMtoEntity(Dosage dosage, BaseDosagePM target) {
        target.setFrequency(dosage.getFrequency().toString());
        target.setTimesPerDay(dosage.getTimesPerDay());
        target.setTimesToTake(dosage.getTimesToTake());
    }

    public Dosage mapCreateDosagePMToEntity(CreateDosagePM dosagePM) throws InvalidFrequencyException {
        if (dosagePM == null) {
            return null;
        }

        Dosage dosage = Dosage.builder().build();

        mapBasePMToEntity(dosagePM, dosage);

        return dosage;
    }

    public Dosage mapDosagePMToEntity(DosagePresentationModel dosagePM) throws InvalidFrequencyException {
        if (dosagePM == null) {
            return null;
        }

        Dosage dosage = Dosage.builder().build();

        mapBasePMToEntity(dosagePM, dosage);

        return dosage;
    }

    public Dosage mapDosagePMToEntity(BaseDosagePM dosagePM) throws InvalidFrequencyException {
        if (dosagePM == null) {
            return null;
        }

        Dosage dosage = Dosage.builder().build();

        mapBasePMToEntity(dosagePM, dosage);

        return dosage;
    }

    private void mapBasePMToEntity(BaseDosagePM dosagePM, Dosage dosage) throws InvalidFrequencyException {
        dosage.setFrequency(stringUtilities.frequencyFromString(dosagePM.getFrequency()));
        dosage.setTimesToTake(dosagePM.getTimesToTake());
        dosage.setTimesPerDay(dosagePM.getTimesPerDay());
    }
}
