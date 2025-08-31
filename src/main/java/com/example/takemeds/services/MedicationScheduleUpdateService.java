package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.UpdateDateDto;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MedicationScheduleUpdateService {
    private final MedicationScheduleMapper scheduleMapper;

    private final MedicationScheduleReadService scheduleReadService;

    private final DosageService dosageService;

    private final MedicationService medicationService;

    public MedicationScheduleUpdateService(MedicationScheduleMapper scheduleMapper, MedicationScheduleReadService scheduleReadService, DosageService dosageService, MedicationService medicationService) {
        this.scheduleMapper = scheduleMapper;
        this.scheduleReadService = scheduleReadService;
        this.dosageService = dosageService;
        this.medicationService = medicationService;
    }

    @Transactional
    public MedicationScheduleView swapMedication(Long scheduleId, Long medicationId, UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationSchedule schedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        Medication medication = medicationService.findMedication(medicationId);

        schedule.setMedication(medication);

        return scheduleMapper.toMedicationScheduleView(schedule);
    }

    @Transactional
    public MedicationScheduleView editDosage(Long scheduleId, BaseDosagePM dosagePM, UserDetails userDetails) throws UnauthorizedAccessException, InvalidFrequencyException {
        MedicationSchedule schedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        Dosage newDosage = dosageService.createDosageEntity(dosagePM);

        schedule.setDosage(newDosage);

        return scheduleMapper.toMedicationScheduleView(schedule);
    }

    @Transactional
    public MedicationScheduleView updateDates(Long scheduleId, UpdateDateDto dates, UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationSchedule schedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        schedule.setStartDate(dates.getStartDate());
        schedule.setEndDate(dates.getEndDate());

        return scheduleMapper.toMedicationScheduleView(schedule);
    }

    @Transactional
    public MedicationScheduleView finishMedicationSchedule(Long scheduleId, UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationSchedule medicationSchedule = scheduleReadService.validateAndGetSchedule(scheduleId, userDetails);

        medicationSchedule.setFinished(true);

        return scheduleMapper.toMedicationScheduleView(medicationSchedule);
    }


}
