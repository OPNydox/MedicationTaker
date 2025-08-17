package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.User;
import com.example.takemeds.entities.UserMedication;
import com.example.takemeds.presentationModels.UserMedicationPresentationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserMedicationMapperTest {

    private UserMedicationMapper userMedicationMapper;

    @BeforeEach
    void setUp() {
        userMedicationMapper = new UserMedicationMapper();
    }

    @Test
    @DisplayName("Should correctly map a UserMedication entity to a UserMedicationPresentationModel")
    void toUserMedicationPresentationModel_validInput_shouldMapCorrectly() {
        // Given
        User user = new User();
        user.setId(1L);

        UserMedication userMedication = UserMedication.builder()
                .id(1L)
                .user(user)
                .build();

        // When
        UserMedicationPresentationModel presentationModel = userMedicationMapper.toUserMedicationPresentationModel(userMedication);

        // Then
        assertNotNull(presentationModel);
        assertEquals(1L, presentationModel.getId());
        assertEquals(1L, presentationModel.getUserID());
    }

    @Test
    @DisplayName("Should return null when mapping a null UserMedication entity to a PM")
    void toUserMedicationPresentationModel_nullInput_shouldReturnNull() {
        // Given
        UserMedication userMedication = null;

        // When
        UserMedicationPresentationModel presentationModel = userMedicationMapper.toUserMedicationPresentationModel(userMedication);

        // Then
        assertNull(presentationModel);
    }

    @Test
    @DisplayName("Should correctly map a UserMedicationPresentationModel to a UserMedication entity")
    void toUserMedicationEntity_validInput_shouldMapCorrectly() {
        // Given
        UserMedicationPresentationModel userMedicationPM = UserMedicationPresentationModel.builder()
                .id(1L)
                .receiptId(10L)
                .build();

        // When
        UserMedication entity = userMedicationMapper.toUserMedicationEntity(userMedicationPM);

        // Then
        assertNotNull(entity);
        assertEquals(10L, entity.getReceipt().getId());
    }

    @Test
    @DisplayName("Should return null when mapping a null UserMedicationPresentationModel to an entity")
    void toUserMedicationEntity_nullInput_shouldReturnNull() {
        // Given
        UserMedicationPresentationModel userMedicationPM = null;

        // When
        UserMedication entity = userMedicationMapper.toUserMedicationEntity(userMedicationPM);

        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should correctly map a list of UserMedicationPresentationModels to a list of UserMedication entities")
    void toUserMedicationEntities_validList_shouldMapCorrectly() {
        // Given
        UserMedicationPresentationModel userMedicationPM1 = UserMedicationPresentationModel.builder().id(1L).receiptId(10L).build();
        UserMedicationPresentationModel userMedicationPM2 = UserMedicationPresentationModel.builder().id(2L).receiptId(11L).build();
        List<UserMedicationPresentationModel> presentationModels = new ArrayList<>();
        presentationModels.add(userMedicationPM1);
        presentationModels.add(userMedicationPM2);

        // When
        List<UserMedication> entities = userMedicationMapper.toUserMedicationEntities(presentationModels);

        // Then
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(10L, entities.get(0).getReceipt().getId());
        assertEquals(11L, entities.get(1).getReceipt().getId());
    }

    @Test
    @DisplayName("Should return an empty list when mapping an empty list of PMs to entities")
    void toUserMedicationEntities_emptyList_shouldReturnEmptyList() {
        // Given
        List<UserMedicationPresentationModel> emptyList = Collections.emptyList();

        // When
        List<UserMedication> resultList = userMedicationMapper.toUserMedicationEntities(emptyList);

        // Then
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }
}
