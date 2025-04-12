package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidDosageException;
import com.example.takemeds.presentationModels.DosagePresentationModel;
import com.example.takemeds.repositories.DosageRepository;
import com.example.takemeds.utils.StringUtilities;
import com.example.takemeds.utils.mappers.DosageMappers;
import org.springframework.stereotype.Service;

@Service
public class DosageService {

    private final DosageRepository dosageRepository;

    private final MedicationService medicationService;

    public DosageService(DosageRepository dosageRepository, MedicationService medicationService) {
        this.dosageRepository = dosageRepository;
        this.medicationService = medicationService;
    }

    public DosagePresentationModel createDosagePM(DosagePresentationModel dosagePM) throws InvalidDosageException {
        Dosage newDosage = createDosage(dosagePM);

        return DosageMappers.entityToPM(newDosage);
    }

    public Dosage createDosage(DosagePresentationModel dosagePM) throws InvalidDosageException {
        Dosage dosage = new Dosage();
        Frequency frequency;

        Medication medication = medicationService.findMedication(dosagePM.getMedicationId());

        try {
            frequency = StringUtilities.frequencyFromString(dosagePM.getFrequency());
        } catch (IllegalArgumentException ex) {
            throw new InvalidDosageException("Frequency type: " + dosagePM.getFrequency() + " does not exist.");
        }

        dosage.setFrequency(frequency);
        dosage.setScheduledTime(dosagePM.getScheduledTime());
        dosage.setMedication(medication);

        return dosageRepository.save(dosage);
    }
}
