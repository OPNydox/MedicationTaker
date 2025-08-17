package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.medicationPMs.MedicationPresentationModel;
import com.example.takemeds.repositories.MedicationRepository;
import com.example.takemeds.utils.mappers.DosageMapper;
import com.example.takemeds.utils.mappers.MedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    private final DosageService dosageService;

    private final UserService userService;

    private final MedicationMapper medicationMapper;

    private final DosageMapper dosageMapper;

    public MedicationService(MedicationRepository medicationRepository, DosageService dosageService, UserService userService, MedicationMapper medicationMapper, DosageMapper dosageMapper) {
        this.medicationRepository = medicationRepository;
        this.dosageService = dosageService;
        this.userService = userService;
        this.medicationMapper = medicationMapper;
        this.dosageMapper = dosageMapper;
    }

    @Transactional
    public MedicationPresentationModel createMedication(MedicationPresentationModel medicationPM) throws InvalidFrequencyException {
        Medication createdMedication = medicationMapper.presentationModelToEntity(medicationPM);
        Dosage dosage;
        if (medicationPM.getDefaultDosage() != null) {
            try {
                dosage =  dosageMapper.mapDosagePMToEntity(dosageService.findDosage(createdMedication.getDosage().getId()));
            } catch (EntityNotFoundException ex) {
                dosage = dosageMapper.mapDosagePMToEntity(dosageService.createAndMapDosage(medicationPM.getDefaultDosage()));
            }
            createdMedication.setDosage(dosage);
        }
        return medicationMapper.mapEntityToPM(medicationRepository.save(createdMedication));
    }

    public Medication findMedication(long id) {
        Optional<Medication> medication = medicationRepository.findById(id);

        if (medication.isEmpty()) {
            throw new EntityNotFoundException("Medication with id " + id + " does not exist");
        }

        return medication.get();
    }

    public MedicationPresentationModel selfAssignMedication(UserDetails userDetails, Long medId) {
        User user = userService.getUser(userDetails.getUsername());
        Medication medication = findMedication(medId);

        return medicationMapper.mapEntityToPM(assignMedicationToUser(medication, user));
    }

    private Medication assignMedicationToUser(Medication medication, User user) {
        user.getMedicationToTake().add(medication);
        medication.getUsers().add(user);
        userService.saveUser(user);

        return medicationRepository.save(medication);
    }

    public MedicationPresentationModel editMedication(UserDetails userDetails, MedicationPresentationModel medicationUpdate) {
        User user = userService.getUser(userDetails.getUsername());
        Medication medication = user.getMedicationToTake().stream()
                .filter(entity -> entity.getId() == medicationUpdate.getId()).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Medication with id " + medicationUpdate.getId() + "does not exist or is not assigned."));
        medication.setName(medicationUpdate.getName());
        medication.setDescription(medicationUpdate.getDescription());

        medication = medicationRepository.save(medication);

        return medicationMapper.mapEntityToPM(medication);
    }

    public List<MedicationPresentationModel> findMedicationsForUser(UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        return medicationMapper.mapMedicationsToPM(user.getMedicationToTake());
    }

    public boolean addDefaultDosageToMedication(Dosage dosage, Long medicationId) {
        if (medicationId == null || dosage == null) {
            return false;
        }

        Medication medication = findMedication(medicationId);
        medication.setDosage(dosage);

        medicationRepository.save(medication);

        return true;
    }
}
