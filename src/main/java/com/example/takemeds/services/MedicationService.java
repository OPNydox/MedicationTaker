package com.example.takemeds.services;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.MedicationPresentationModel;
import com.example.takemeds.repositories.MedicationRepository;
import com.example.takemeds.utils.mappers.MedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    private final UserService userService;

    public MedicationService(MedicationRepository medicationRepository, UserService userService) {
        this.medicationRepository = medicationRepository;
        this.userService = userService;
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

    public MedicationPresentationModel selfAssignMedication(UserDetails userDetails, Long medId) {
        User user = userService.getUser(userDetails.getUsername());
        Medication medication = findMedication(medId);

        return MedicationMapper.mapEntityToPM(assignMedicationToUser(medication, user));
    }

    private Medication assignMedicationToUser(Medication medication, User user) {
        user.getMedicationToTake().add(medication);
        medication.getUsers().add(user);
        userService.saveUser(user);

        return medicationRepository.save(medication);
    }
}
