package com.example.takemeds.services;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.MedicationPresentationModel;
import com.example.takemeds.repositories.MedicationRepository;
import com.example.takemeds.utils.mappers.MedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicationService {
    MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public MedicationPresentationModel createMedicationPM(MedicationPresentationModel medicationPM) {
        Medication createdMedication = createMedication(medicationPM);
        return MedicationMapper.mapEntityToPM(createdMedication);
    }

    public Medication createMedication(MedicationPresentationModel medicationPM) {
        Medication medication = new Medication();

        medication.setName(medicationPM.getName());
        medication.setDescription(medicationPM.getDescription());

        return medicationRepository.save(medication);
    }

    public Medication findMedication(long id) {
        Optional<Medication> medication = medicationRepository.findById(id);

        if (medication.isEmpty()) {
            throw new EntityNotFoundException("Medication with id " + id + " does not exist");
        }

        return medication.get();
    }
}
