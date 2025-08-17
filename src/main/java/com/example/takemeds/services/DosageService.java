package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.repositories.DosageRepository;
import com.example.takemeds.utils.mappers.DosageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DosageService {

    private final DosageRepository dosageRepository;

    private final DosageMapper dosageMapper;

    public DosageService(DosageRepository dosageRepository, DosageMapper dosageMapper) {
        this.dosageRepository = dosageRepository;
        this.dosageMapper = dosageMapper;
    }


    public DosagePresentationModel createAndMapDosage(BaseDosagePM dosagePM) throws InvalidFrequencyException {
        Dosage newDosage = createDosageEntity(dosagePM);

        return dosageMapper.mapEntityToDosagePM(dosageRepository.save(newDosage));
    }

    public Dosage createDosageEntity(BaseDosagePM dosagePM) throws InvalidFrequencyException {
        Dosage newDosage = dosageMapper.mapDosagePMToEntity(dosagePM);

        return dosageRepository.save(newDosage);
    }

    public DosagePresentationModel findDosage(Long id) {
        Optional<Dosage> foundDosage = dosageRepository.findById(id);

        if (foundDosage.isEmpty()) {
            throw new EntityNotFoundException("Dosage with id " + id + " could not be found.");
        }
        return dosageMapper.mapEntityToDosagePM(foundDosage.get());
    }
}
