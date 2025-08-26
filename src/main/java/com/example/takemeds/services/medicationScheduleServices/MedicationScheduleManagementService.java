package com.example.takemeds.services.medicationScheduleServices;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.Receipt;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    protected MedicationSchedulePM createEntity(Medication medication, Dosage dosage, User user, Receipt receipt, LocalDate endDate, LocalDate startDate) {
        MedicationSchedule medicationSchedule = MedicationSchedule.builder().build();

        medicationSchedule.setMedication(medication);
        medicationSchedule.setDosage(dosage);
        medicationSchedule.setUser(user);
        medicationSchedule.setReceipt(receipt);
        medicationSchedule.setEndDate(endDate);
        medicationSchedule.setStartDate(startDate);
        medicationSchedule.setFinished(false);

        medicationScheduleRepository.save(medicationSchedule);

        return medicationScheduleMapper.toMedicationSchedulePM(medicationSchedule);
    }

    @Transactional
    protected void deleteMedicationSchedule(Long id) {
        MedicationSchedule medicationSchedule = medicationScheduleReadService.findMedicationScheduleById(id);

        medicationScheduleRepository.delete(medicationSchedule);
    }
}
