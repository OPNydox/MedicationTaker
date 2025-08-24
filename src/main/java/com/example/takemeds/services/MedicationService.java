package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import com.example.takemeds.repositories.MedicationRepository;
import com.example.takemeds.utils.mappers.MedicationMapper;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    @Transactional
    public MedicationDosagePM createMedicationPM(BaseMedicationPM medicationDosageRefPM) {
        Medication createdMedication = medicationMapper.mapPMToEntity(medicationDosageRefPM);

        return medicationMapper.mapEntityToPM(medicationRepository.save(createdMedication));
    }

    @Transactional
    protected Medication createMedicationEntity(BaseMedicationPM medicationPM) {
        Medication createdMedication = medicationMapper.mapPMToEntity(medicationPM);

        return medicationRepository.save(createdMedication);
    }

    public Medication findMedication(long id) {
        Optional<Medication> medication = medicationRepository.findById(id);

        if (medication.isEmpty()) {
            throw new EntityNotFoundException("Medication with id " + id + " does not exist");
        }

        return medication.get();
    }

    protected void deleteMedication(Long medicationId) {
        Medication medicationToDelete = findMedication(medicationId);
        medicationRepository.delete(medicationToDelete);
    }

    @Transactional
    public MedicationDosagePM editMedication(Medication oldMedication, MedicationDosagePM medicationUpdate) {
        oldMedication.setName(medicationUpdate.getName());
        oldMedication.setDescription(medicationUpdate.getDescription());

        Medication updatedMedication = medicationRepository.save(oldMedication);

        return medicationMapper.mapEntityToPM(updatedMedication);
    }
}
