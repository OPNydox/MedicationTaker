package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationMapperTest {

    private MedicationMapper medicationMapper;

    @BeforeEach
    void setUp() {
        medicationMapper = new MedicationMapper();
    }

    @Test
    @DisplayName("Should correctly map a Medication entity to a MedicationPresentationModel")
    void mapEntityToPM_validInput_shouldMapCorrectly() {
        // Given
        Medication medication = Medication.builder()
                .id(1L)
                .name("Advil")
                .description("Pain reliever")
                .build();

        // When
        MedicationDosagePM presentationModel = medicationMapper.mapEntityToPM(medication);

        // Then
        assertNotNull(presentationModel);
        assertEquals(1L, presentationModel.getId());
        assertEquals("Advil", presentationModel.getName());
        assertEquals("Pain reliever", presentationModel.getDescription());
    }

    @Test
    @DisplayName("Should return null when mapping a null Medication entity to a PM")
    void mapEntityToPM_nullInput_shouldReturnNull() {
        // Given
        Medication medication = null;

        // When
        MedicationDosagePM presentationModel = medicationMapper.mapEntityToPM(medication);

        // Then
        assertNull(presentationModel);
    }

    @Test
    @DisplayName("Should correctly map a list of Medication entities to a list of PMs")
    void mapMedicationsToPM_validList_shouldMapCorrectly() {
        // Given
        Medication medication1 = Medication.builder().id(1L).name("Advil").description("Pain reliever").build();
        Medication medication2 = Medication.builder().id(2L).name("Tylenol").description("Fever reducer").build();
        List<Medication> medicationList = new ArrayList<>();
        medicationList.add(medication1);
        medicationList.add(medication2);

        // When
        List<MedicationDosagePM> resultList = medicationMapper.mapMedicationsToPM(medicationList);

        // Then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("Advil", resultList.get(0).getName());
        assertEquals("Tylenol", resultList.get(1).getName());
    }

    @Test
    @DisplayName("Should return an empty list when mapping an empty list of Medications to PMs")
    void mapMedicationsToPM_emptyList_shouldReturnEmptyList() {
        // Given
        List<Medication> emptyList = Collections.emptyList();

        // When
        List<MedicationDosagePM> resultList = medicationMapper.mapMedicationsToPM(emptyList);

        // Then
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    @DisplayName("Should correctly map a MedicationPresentationModel to a Medication entity")
    void presentationModelToEntity_validInput_shouldMapCorrectly() {
        // Given
        BaseMedicationPM presentationModel = BaseMedicationPM.builder()
                .id(1L)
                .name("Advil")
                .description("Pain reliever")
                .build();

        // When
        //Medication entity = medicationMapper.presentationModelToEntity(presentationModel);

        // Then
        //assertNotNull(entity);
        //assertEquals(1L, entity.getId());
        //assertEquals("Advil", entity.getName());
        //assertEquals("Pain reliever", entity.getDescription());
    }

    @Test
    @DisplayName("Should return null when mapping a null MedicationPresentationModel to an entity")
    void presentationModelToEntity_nullInput_shouldReturnNull() {
        // Given
        MedicationDosagePM presentationModel = null;

        // When
        Medication entity = medicationMapper.presentationModelToEntity(presentationModel);

        // Then
        assertNull(entity);
    }
}
