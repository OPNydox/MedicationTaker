package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.CreateMedicationScheduleRequest;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicationScheduleManagementService {
    private final MedicationScheduleRepository medicationScheduleRepository;

    private final MedicationScheduleMapper medicationScheduleMapper;

    private final MedicationScheduleReadService scheduleReadService;

    private final UserService userService;

    private final MedicationService medicationService;

    private final DosageService dosageService;

    public MedicationScheduleManagementService(MedicationScheduleRepository medicationScheduleRepository, MedicationScheduleMapper medicationScheduleMapper, MedicationScheduleReadService scheduleReadService, UserService userService, MedicationService medicationService, DosageService dosageService) {
        this.medicationScheduleRepository = medicationScheduleRepository;
        this.medicationScheduleMapper = medicationScheduleMapper;
        this.scheduleReadService = scheduleReadService;
        this.userService = userService;
        this.medicationService = medicationService;
        this.dosageService = dosageService;
    }

    @Transactional
    public MedicationScheduleView createMedicationSchedule(CreateMedicationScheduleRequest createSchedulePM, UserDetails userDetails) throws InvalidFrequencyException, InvalidRequestException {
        User user = userService.getUser(userDetails.getUsername());
        Medication medication = getMedicationForSchedule(createSchedulePM);
        Dosage dosage = getDosageForSchedule(createSchedulePM, medication);

        MedicationSchedule result = createEntity(medication, dosage, user, createSchedulePM);

        return medicationScheduleMapper.toMedicationScheduleView(result);
    }

    private Medication getMedicationForSchedule(CreateMedicationScheduleRequest scheduleRequest) throws InvalidRequestException {
        if (scheduleRequest.getMedicationId() != null) {
            return medicationService.findMedication(scheduleRequest.getMedicationId());
        } else if (scheduleRequest.getMedicationPM() != null) {
            return medicationService.createMedicationEntity(scheduleRequest.getMedicationPM());
        } else {
            throw new InvalidRequestException("Medication schedule must have an attached medication.");
        }
    }

    private Dosage getDosageForSchedule(CreateMedicationScheduleRequest scheduleRequest, Medication medication) throws InvalidRequestException, InvalidFrequencyException {
        if (scheduleRequest.getDosage() != null) {
            return dosageService.createDosageEntity(scheduleRequest.getDosage());
        } else if (medication.getDefaultDosage() != null) {
            return medication.getDefaultDosage();
        } else {
            throw new InvalidRequestException("Medication schedule must have an attached dosage.");
        }
    }

    private MedicationSchedule createEntity(Medication medication, Dosage dosage, User user, CreateMedicationScheduleRequest schedulePM) {
        MedicationSchedule medicationSchedule = MedicationSchedule.builder().build();

        medicationSchedule.setMedication(medication);
        medicationSchedule.setDosage(dosage);
        medicationSchedule.setUser(user);
        medicationSchedule.setEndDate(schedulePM.getEndDate());
        medicationSchedule.setStartDate(schedulePM.getStartDate());
        medicationSchedule.setFinished(false);

        return medicationScheduleRepository.save(medicationSchedule);
    }


    @Transactional
    public void deleteMedicationSchedule(Long scheduleId, UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationSchedule schedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        medicationScheduleRepository.delete(schedule);
    }
}
