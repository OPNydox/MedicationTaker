package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
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

    private final UserService userService;

    private final MedicationMapper medicationMapper;

    private final DosageService dosageService;

    public MedicationService(MedicationRepository medicationRepository, UserService userService, MedicationMapper medicationMapper, DosageService dosageService) {
        this.medicationRepository = medicationRepository;
        this.userService = userService;
        this.medicationMapper = medicationMapper;
        this.dosageService = dosageService;
    }

    @Transactional
    public MedicationView createMedication(CreateMedicationDto medicationDto, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());

        Medication createdMedication = createMedicationEntity(medicationDto);
        Dosage dosage = getDosage(medicationDto);

        if (dosage != null) {
            dosage.setMedication(createdMedication);
            createdMedication.setDefaultDosage(dosage);
        }

        user.getMedications().add(createdMedication);

        return medicationMapper.mapEntityToPM(createdMedication);
    }

    @Transactional
    protected Medication createMedicationEntity(CreateMedicationDto medicationPM) {
        Medication createdMedication = medicationMapper.mapPMToEntity(medicationPM);

        return medicationRepository.save(createdMedication);
    }

    private Dosage getDosage(CreateMedicationDto medicationDto) throws InvalidFrequencyException {
        if (medicationDto.getDefaultDosagePM() != null) {
            return dosageService.createDosageEntity(medicationDto.getDefaultDosagePM());
        } else if (medicationDto.getDosageId() != null) {
            return dosageService.findDosageEntity(medicationDto.getDosageId());
        }
        return null;
    }

    public Medication findMedication(long id) {
        Optional<Medication> medication = medicationRepository.findById(id);

        if (medication.isEmpty()) {
            throw new EntityNotFoundException("Medication with id " + id + " does not exist");
        }

        return medication.get();
    }

    @Transactional
    public void deleteMedication(Long id, UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Medication medicationToDelete = userService.findUserMedication(id, user);

        user.getMedications().remove(medicationToDelete);

        medicationRepository.delete(medicationToDelete);
    }

    @Transactional
    public MedicationView editMedication(Long medicationId, CreateMedicationDto medicationUpdate, UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());

        Medication medication = userService.findUserMedication(medicationId, user);

        medication.setName(medicationUpdate.getName());
        medication.setDescription(medicationUpdate.getDescription());

        return medicationMapper.mapEntityToPM(medication);
    }

    @Transactional
    public MedicationView setDefaultDosage(CreateDosagePM dosagePM, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());
        Dosage createdDosage = dosageService.createDosageEntity(dosagePM);
        Medication medication = userService.findUserMedication(dosagePM.getMedicationId(), user);

        medication.setDefaultDosage(createdDosage);

        return medicationMapper.mapEntityToPM(medication);
    }

    @Transactional
    public MedicationView setDefaultDosage(Long medicationId, Long dosageId, UserDetails userDetails) throws InvalidFrequencyException {
        User user = userService.getUser(userDetails.getUsername());
        Dosage createdDosage = dosageService.findDosageEntity(dosageId);
        Medication medication = userService.findUserMedication(medicationId, user);

        medication.setDefaultDosage(createdDosage);

        return medicationMapper.mapEntityToPM(medication);
    }

    public List<MedicationView> getMyMedications(UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        List<Medication> medicationEntities = user.getMedications();

        return medicationMapper.mapMedicationsToPM(medicationEntities);
    }
}
