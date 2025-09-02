package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MedicationMapper {
    private DosageMapper dosageMapper;

    public MedicationMapper(DosageMapper dosageMapper) {
        this.dosageMapper = dosageMapper;
    }

    public MedicationView mapEntityToPM(Medication medication) {
        if (medication == null) {
            return null;
        }

        return MedicationView.builder().id(medication.getId())
                                                      .name(medication.getName())
                                                      .description(medication.getDescription())
                                                      .defaultDosage(dosageMapper.mapBaseEntityToPM(medication.getDefaultDosage())).build();
    }

    public List<MedicationView> mapMedicationsToPM(List<Medication> input) {
        List<MedicationView> result = new ArrayList<>();

        for (Medication medication : input) {
            result.add(mapEntityToPM(medication));
        }

        return result;
    }

    public Medication mapPMToEntity(CreateMedicationDto medicationPM)  {
        if (medicationPM == null) {
            return null;
        }

        return Medication.builder().name(medicationPM.getName())
                                   .description(medicationPM.getDescription()).build();
    }
}
