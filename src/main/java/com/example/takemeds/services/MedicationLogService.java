package com.example.takemeds.services;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.repositories.MedicationTakenLogRepository;
import com.example.takemeds.utils.mappers.MedTakenMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MedicationLogService {

    UserService userService;

    MedicationService medicationService;

    MedicationTakenLogRepository medicationTakenLogRepository;

    MedTakenMapper medTakenMapper;

    public MedicationLogService(UserService userService, MedicationService medicationService, MedicationTakenLogRepository medicationTakenLogRepository, MedTakenMapper medTakenMapper) {
        this.userService = userService;
        this.medicationService = medicationService;
        this.medicationTakenLogRepository = medicationTakenLogRepository;
        this.medTakenMapper = medTakenMapper;
    }

    public MedLogPresentationModel takeMedication(String username, Long medicationId) {
        //TO DO
        throw new RuntimeException("Method is not implemented");
    }

    private MedicationTakenLog createMedicationLog(User user, Medication medication) {
        MedicationTakenLog medicationTakenLog = new MedicationTakenLog();

        medicationTakenLog.setMedication(medication);
        medicationTakenLog.setUser(user);
        medicationTakenLog.setTimeTaken(LocalDateTime.now());

        return medicationTakenLog;
    }
}
