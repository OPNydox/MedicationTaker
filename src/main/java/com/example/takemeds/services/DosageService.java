package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.DosagePresentationModel;
import com.example.takemeds.repositories.DosageRepository;
import com.example.takemeds.utils.mappers.DosageMappers;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DosageService {

    private final DosageRepository dosageRepository;

    private final DosageMappers dosageMapper;

    public DosageService(DosageRepository dosageRepository, DosageMappers dosageMapper) {
        this.dosageRepository = dosageRepository;
        this.dosageMapper = dosageMapper;
    }

    public DosagePresentationModel createDosage(DosagePresentationModel dosagePM) throws InvalidFrequencyException {
        Dosage newDosage = dosageMapper.PMtoEntity(dosagePM);

        return dosageMapper.entityToPM(dosageRepository.save(newDosage));
    }

    public DosagePresentationModel findDosage(Long id) {
        Optional<Dosage> foundDosage = dosageRepository.findById(id);

        if (foundDosage.isEmpty()) {
            throw new EntityNotFoundException("Dosage with id " + id + " could not be found.");
        }
        return dosageMapper.entityToPM(foundDosage.get());
    }
}
