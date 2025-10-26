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

import java.util.ArrayList;
import java.util.List;

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

        MedicationSchedule result = medicationScheduleRepository.save(createScheduleEntity(createSchedulePM, user));

        return medicationScheduleMapper.toMedicationScheduleView(result);
    }

    @Transactional
    public List<MedicationSchedule> createScheduleEntitiesFromRequests(List<CreateMedicationScheduleRequest> scheduleRequests, User patient) throws InvalidRequestException, InvalidFrequencyException {
        if (scheduleRequests == null || scheduleRequests.isEmpty() || patient == null) {
            return new ArrayList<>();
        }

        List<MedicationSchedule> createSchedule = new ArrayList<>();

        for (CreateMedicationScheduleRequest request : scheduleRequests) {
            createSchedule.add(createScheduleEntity(request, patient));
        }

        return createSchedule;
    }

    private Medication getMedicationForSchedule(CreateMedicationScheduleRequest scheduleRequest) throws InvalidRequestException {
        if (scheduleRequest.getMedicationId() != null) {
            return medicationService.findMedication(scheduleRequest.getMedicationId());
        } else if (scheduleRequest.getMedication() != null) {
            return medicationService.createMedicationEntity(scheduleRequest.getMedication());
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

    private MedicationSchedule createScheduleEntity(CreateMedicationScheduleRequest schedulePM, User user) throws InvalidRequestException, InvalidFrequencyException {
        Medication medication = getMedicationForSchedule(schedulePM);
        Dosage dosage = getDosageForSchedule(schedulePM, medication);

        return MedicationSchedule.builder()
                .medication(medication)
                .dosage(dosage)
                .user(user)
                .startDate(schedulePM.getStartDate())
                .endDate(schedulePM.getEndDate())
                .build();
    }

    @Transactional
    public void deleteMedicationSchedule(Long scheduleId, UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationSchedule schedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        medicationScheduleRepository.delete(schedule);
    }
}
