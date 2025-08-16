package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.presentationModels.MedLogPresentationModel;
import org.springframework.stereotype.Component;

@Component
public class MedTakenMapper {
    public MedLogPresentationModel entityToPM(MedicationTakenLog medicationTakenLog) {
        MedLogPresentationModel pm = new MedLogPresentationModel();

        pm.setUsername(medicationTakenLog.getUser().getEmail());
        pm.setMedicationName(medicationTakenLog.getMedication().getName());
        pm.setTimeTaken(medicationTakenLog.getTimeTaken());

        return pm;
    }
}
