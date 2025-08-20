package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationMapper {
    public MedicationMapper() {
    }

    public MedicationDosagePM mapEntityToPM(Medication medication) {
        if (medication == null) {
            return null;
        }

        return MedicationDosagePM.builder().id(medication.getId())
                                                         .name(medication.getName())
                                                         .description(medication.getDescription()).build();
    }

    public List<MedicationDosagePM> mapMedicationsToPM(List<Medication> input) {
        List<MedicationDosagePM> result = new ArrayList<>();

        for (Medication medication : input) {
            result.add(mapEntityToPM(medication));
        }

        return result;
    }

    public Medication presentationModelToEntity(BaseMedicationPM medicationPM)  {
        if (medicationPM == null) {
            return null;
        }

        return Medication.builder().id(medicationPM.getId())
                                   .name(medicationPM.getName())
                                   .description(medicationPM.getDescription()).build();
    }
}
