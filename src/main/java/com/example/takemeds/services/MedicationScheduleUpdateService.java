package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class MedicationScheduleUpdateService {
    private final MedicationScheduleRepository medicationScheduleRepository;

    private final MedicationScheduleMapper medicationScheduleMapper;

    private final MedicationScheduleReadService medicationScheduleReadService;

    public MedicationScheduleUpdateService(MedicationScheduleRepository medicationScheduleRepository, MedicationScheduleMapper medicationScheduleMapper, MedicationScheduleReadService medicationScheduleReadService) {
        this.medicationScheduleRepository = medicationScheduleRepository;
        this.medicationScheduleMapper = medicationScheduleMapper;
        this.medicationScheduleReadService = medicationScheduleReadService;
    }

    @Transactional
    public MedicationSchedulePM updateMedication(Long id, Medication newMedication) {
        MedicationSchedule medicationSchedule = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationSchedule.setMedication(newMedication);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedule);
    }

    @Transactional
    protected MedicationSchedulePM updateDosage(Long id, Dosage newDosage) {
        MedicationSchedule medicationSchedule = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationSchedule.setDosage(newDosage);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedule);

    }

    @Transactional
    protected MedicationSchedulePM updateStartDate(Long id, LocalDate startDate) {
        MedicationSchedule medicationSchedulePM = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationSchedulePM.setStartDate(startDate);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedulePM);
    }

    @Transactional
    protected MedicationSchedulePM updateEndDate(Long id, LocalDate endDate) {
        MedicationSchedule medicationSchedulePM = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationSchedulePM.setEndDate(endDate);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedulePM);
    }

    @Transactional
    protected MedicationSchedulePM finishMedicationSchedule(Long id) {
        MedicationSchedule medicationSchedule = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationSchedule.setFinished(true);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedule);
    }
}
