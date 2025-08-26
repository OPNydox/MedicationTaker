package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleWithIdsPM;
import com.example.takemeds.utils.mappers.DosageMapper;
import com.example.takemeds.utils.mappers.MedicationMapper;
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

    public UserActionService(UserService userService, MedicationService medicationService, MedicationMapper medicationMapper, DosageService dosageService, DosageMapper dosageMapper) {
        this.userService = userService;
        this.medicationService = medicationService;
        this.medicationMapper = medicationMapper;
        this.dosageService = dosageService;
        this.dosageMapper = dosageMapper;
    }

    @Transactional
    public List<MedicationDosagePM> showMyMedication(String username) {
        User user = userService.getUser(username);
        List<Medication> medications = user.getMedications();

        return medicationMapper.mapMedicationsToPM(medications);
    }

    @Transactional
    public MedicationDosagePM editMedication(Long medicationId, MedicationDosagePM medicationUpdate, UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Medication medication = findUserMedication(medicationId, user);

        MedicationDosagePM result = medicationService.editMedication(medication, medicationUpdate);

        if (medication.getDosage() != null) {
            DosagePresentationModel dosage = dosageService.findDosage(medication.getDosage().getId());
            result.setDefaultDosagePM(dosage);
        }

        return medicationMapper.mapEntityToPM(medication);
    }

    @Transactional
    public MedicationDosagePM createMedication(BaseMedicationPM medicationPM, UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Medication createdMedication = medicationService.createMedicationEntity(medicationPM);
        user.getMedications().add(createdMedication);

        userService.saveUser(user);

        return medicationMapper.mapEntityToPM(createdMedication);
    }

    @Transactional
    public MedicationDosagePM createMedicationWithDosage(MedicationDosagePM medicationDosagePM, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());

        Medication createdMedication = medicationService.createMedicationEntity(medicationDosagePM);
        Dosage createdDosage = dosageService.createDosageEntity(medicationDosagePM.getDefaultDosagePM());

        createdMedication.setDosage(createdDosage);
        createdDosage.setMedication(createdMedication);

        user.getMedications().add(createdMedication);

        userService.saveUser(user);

        MedicationDosagePM result = medicationMapper.mapEntityToPM(createdMedication);
        result.setDefaultDosagePM(dosageMapper.mapBaseEntityToPM(createdDosage));

        return result;
    }

    @Transactional
    public void deleteMedication(Long id, UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Medication medicationToDelete = findUserMedication(id, user);

        user.getMedications().remove(medicationToDelete);

        medicationService.deleteMedication(id);
    }

    @Transactional
    public MedicationDosagePM setDefaultDosage(CreateDosagePM dosagePM, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());
        Dosage createdDosage = dosageService.createDosageEntity(dosagePM);
        Medication medication = findUserMedication(dosagePM.getMedicationId(), user);

        medication.setDosage(createdDosage);

        MedicationDosagePM result = medicationMapper.mapEntityToPM(medication);
        result.setDefaultDosagePM(dosageMapper.mapBaseEntityToPM(createdDosage));

        return result;
    }

    private Medication findUserMedication(Long medicationId, User user) {
        return user.getMedications().stream()
                .filter(entity -> Objects.equals(entity.getId(), medicationId)).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Medication with id " + medicationId + "does not exist or is not assigned."));
    }

    private MedicationSchedulePM createMedicationSchedule(MedicationScheduleWithIdsPM medicationScheduleWithIdsPM, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());
        Medication medication = medicationService.findMedication(medicationScheduleWithIdsPM.getMedicationId());

        return null;
    }
}

