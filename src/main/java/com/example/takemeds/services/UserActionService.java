package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.utils.mappers.DosageMapper;
import com.example.takemeds.utils.mappers.MedicationMapper;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserActionService {
    private final UserService userService;

    private final MedicationService medicationService;

    private final MedicationMapper medicationMapper;

    private final DosageService dosageService;

    private final DosageMapper dosageMapper;

    private final MedicationScheduleManagementService medicationScheduleManagementService;

    private final MedicationScheduleReadService medicationScheduleReadService;

    private final MedicationScheduleMapper scheduleMapper;

    public UserActionService(UserService userService, MedicationService medicationService, MedicationMapper medicationMapper, DosageService dosageService, DosageMapper dosageMapper, MedicationScheduleManagementService medicationScheduleManagementService, MedicationScheduleReadService medicationScheduleReadService, MedicationScheduleMapper scheduleMapper) {
        this.userService = userService;
        this.medicationService = medicationService;
        this.medicationMapper = medicationMapper;
        this.dosageService = dosageService;
        this.dosageMapper = dosageMapper;
        this.medicationScheduleManagementService = medicationScheduleManagementService;
        this.medicationScheduleReadService = medicationScheduleReadService;
        this.scheduleMapper = scheduleMapper;
    }

    @Transactional
    public List<MedicationView> showMyMedication(String username) {
        User user = userService.getUser(username);
        List<Medication> medications = user.getMedications();

        return medicationMapper.mapMedicationsToPM(medications);
    }
}

