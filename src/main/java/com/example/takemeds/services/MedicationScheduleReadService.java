package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationScheduleReadService {
    private final MedicationScheduleRepository medicationScheduleRepository;


    public MedicationScheduleReadService(MedicationScheduleRepository medicationScheduleRepository) {
        this.medicationScheduleRepository = medicationScheduleRepository;
    }

    protected List<MedicationSchedule> findNonFinishedUserMedicationBy(Long userId) {
        return medicationScheduleRepository.findByUser_IdAndIsFinishedFalse(userId);
    }

    protected MedicationSchedule findMedicationScheduleById(Long id) {
        Optional<MedicationSchedule> userMedication = medicationScheduleRepository.findById(id);

        if (userMedication.isEmpty()) {
            throw new EntityNotFoundException("Medication assignment with id " + id + " does not exist");
        }

        return userMedication.get();
    }
}
