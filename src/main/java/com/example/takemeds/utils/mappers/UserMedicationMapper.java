package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Receipt;
import com.example.takemeds.entities.UserMedication;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.UserMedicationPresentationModel;
import com.example.takemeds.services.ReceiptService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMedicationMapper {

    /**
     * Maps a UserMedication entity to its presentation model.
     * In a real application, you'd likely fetch details from User, Medication, and Dosage entities
     * that are eagerly loaded or fetched within a transaction here to populate the names/details.
     */
    public UserMedicationPresentationModel toUserMedicationPresentationModel(UserMedication userMedication) {
        if (userMedication == null) {
            return null;
        }

        return UserMedicationPresentationModel.builder()
                .id(userMedication.getId())
                .userID(userMedication.getUser().getId()).build();


    }

    @SneakyThrows
    public UserMedication toUserMedicationEntity(UserMedicationPresentationModel userMedication) {
        if (userMedication == null) {
            return null;
        }

        return UserMedication.builder().receipt(Receipt.builder().id(userMedication.getReceiptId()).build()).build();
    }


    @SneakyThrows
    public List<UserMedication> toUserMedicationEntities(List<UserMedicationPresentationModel> userMedications) {
        List<UserMedication> userMedicationList;

        userMedicationList = userMedications.stream()
                .map(this::toUserMedicationEntity)
                .toList();

        return userMedicationList;
    }
}
