package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.BaseMedicationSchedulePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicationScheduleManagementService {
    private final MedicationScheduleRepository medicationScheduleRepository;

    private final MedicationScheduleMapper medicationScheduleMapper;

    private final MedicationScheduleReadService medicationScheduleReadService;

    public MedicationScheduleManagementService(MedicationScheduleRepository medicationScheduleRepository, MedicationScheduleMapper medicationScheduleMapper, MedicationScheduleReadService medicationScheduleReadService) {
        this.medicationScheduleRepository = medicationScheduleRepository;
        this.medicationScheduleMapper = medicationScheduleMapper;
        this.medicationScheduleReadService = medicationScheduleReadService;
    }

    @Transactional
    protected MedicationScheduleView createEntity(Medication medication, Dosage dosage, User user, BaseMedicationSchedulePM schedulePM) {
        MedicationSchedule medicationSchedule = MedicationSchedule.builder().build();

        medicationSchedule.setMedication(medication);
        medicationSchedule.setDosage(dosage);
        medicationSchedule.setUser(user);
        medicationSchedule.setEndDate(schedulePM.getEndDate());
        medicationSchedule.setStartDate(schedulePM.getStartDate());
        medicationSchedule.setFinished(false);

        medicationScheduleRepository.save(medicationSchedule);

        return medicationScheduleMapper.toMedicationScheduleView(medicationSchedule);
    }

    @Transactional
    protected void deleteMedicationSchedule(Long id) {
        MedicationSchedule medicationSchedule = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationScheduleRepository.delete(medicationSchedule);
    }
}
