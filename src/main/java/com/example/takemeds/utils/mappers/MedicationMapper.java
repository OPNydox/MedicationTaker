package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.medicationPMs.MedicationPresentationModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationMapper {
    public MedicationMapper() {
    }

    public MedicationPresentationModel mapEntityToPM(Medication medication) {
        if (medication == null) {
            return null;
        }

        return MedicationPresentationModel.builder().id(medication.getId())
                                                    .name(medication.getName())
                                                    .description(medication.getDescription()).build();
    }

    public List<MedicationPresentationModel> mapMedicationsToPM(List<Medication> input) {
        List<MedicationPresentationModel> result = new ArrayList<>();

        for (Medication medication : input) {
            result.add(mapEntityToPM(medication));
        }

        return result;
    }

    public Medication presentationModelToEntity(MedicationPresentationModel medicationPM)  {
        if (medicationPM == null) {
            return null;
        }

        return Medication.builder().id(medicationPM.getId())
                                   .name(medicationPM.getName())
                                   .description(medicationPM.getDescription()).build();
    }
}
