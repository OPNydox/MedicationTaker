package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.Receipt;
import com.example.takemeds.entities.User;
import com.example.takemeds.entities.UserMedication;
import com.example.takemeds.presentationModels.UserMedicationPM;
import com.example.takemeds.repositories.UserMedicationRepository;
import com.example.takemeds.utils.mappers.UserMedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserMedicationService {
    private final UserMedicationRepository userMedicationRepository;

    private final UserMedicationMapper userMedicationMapper;

    public UserMedicationService(UserMedicationRepository userMedicationRepository, UserMedicationMapper userMedicationMapper) {
        this.userMedicationRepository = userMedicationRepository;
        this.userMedicationMapper = userMedicationMapper;
    }

    @Transactional
    protected UserMedicationPM createEntity(Medication medication, Dosage dosage, User user, Receipt receipt, LocalDate endDate) {
        UserMedication userMedication = UserMedication.builder().build();

        userMedication.setMedication(medication);
        userMedication.setDosage(dosage);
        userMedication.setUser(user);
        userMedication.setReceipt(receipt);
        userMedication.setEndDate(endDate);
        userMedication.setFinished(false);

        userMedicationRepository.save(userMedication);

        return userMedicationMapper.toUserMedicationPM(userMedication);
    }

    @Transactional
    public UserMedicationPM updateMedication(Long id, Medication newMedication) {
        UserMedication userMedication = findUserMedicationById(id);

        userMedication.setMedication(newMedication);

        return userMedicationMapper.toUserMedicationPM(userMedication);
    }

    @Transactional
    protected UserMedicationPM updateDosage(Long id, Dosage newDosage) {
        UserMedication userMedication = findUserMedicationById(id);

        userMedication.setDosage(newDosage);

        return userMedicationMapper.toUserMedicationPM(userMedication);

    }

    protected List<UserMedication> findNonFinishedUserMedicationBy(Long userId) {
        return userMedicationRepository.findByUser_IdAndIsFinishedFalse(userId);
    }

    private UserMedication findUserMedicationById(Long id) {
        Optional<UserMedication> userMedication = userMedicationRepository.findById(id);

        if (userMedication.isEmpty()) {
            throw new EntityNotFoundException("Medication assignment with id " + id + " does not exist");
        }

        return userMedication.get();
    }
}
