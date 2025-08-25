package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.entities.User;
import com.example.takemeds.presentationModels.MedLogPresentationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MedTakenMapperTest {
    private MedTakenMapper medTakenMapper;

    @BeforeEach
    public void setup() {
        medTakenMapper = new MedTakenMapper();
    }

    @Test
    @DisplayName("Should correctly map a MedicationTakenLog entity to a MedLogPresentationModel")
    void entityToPM_validInput_shouldMapCorrectly() {
        // Given
        // We use mock objects to simulate the dependencies
        Medication medication = Medication.builder().build();
        medication.setName("Aspirin");

        User user = new User();
        user.setEmail("testuser@example.com");

        LocalDateTime timeTaken = LocalDateTime.now();

        MedicationTakenLog medicationTakenLog = new MedicationTakenLog();
        medicationTakenLog.setMedication(medication);
        medicationTakenLog.setUser(user);
        medicationTakenLog.setTimeTaken(timeTaken);

        // When
        MedLogPresentationModel presentationModel = medTakenMapper.entityToPM(medicationTakenLog);

        // Then
        assertNotNull(presentationModel);
        assertEquals("testuser@example.com", presentationModel.getUsername());
        assertEquals("Aspirin", presentationModel.getMedicationName());
        assertEquals(timeTaken, presentationModel.getTimeTaken());
    }

    @Test
    @DisplayName("Should throw NullPointerException when MedicationTakenLog is null")
    void entityToPM_nullInput_shouldThrowNullPointerException() {
        // Given
        MedicationTakenLog medicationTakenLog = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> medTakenMapper.entityToPM(medicationTakenLog));
    }

    @Test
    @DisplayName("Should throw NullPointerException when Medication in MedicationTakenLog is null")
    void entityToPM_nullMedication_shouldThrowNullPointerException() {
        // Given
        User user = new User();
        user.setEmail("testuser@example.com");
        LocalDateTime timeTaken = LocalDateTime.now();

        MedicationTakenLog medicationTakenLog = new MedicationTakenLog();
        medicationTakenLog.setMedication(null); // Medication is null
        medicationTakenLog.setUser(user);
        medicationTakenLog.setTimeTaken(timeTaken);

        // When & Then
        assertThrows(NullPointerException.class, () -> medTakenMapper.entityToPM(medicationTakenLog));
    }

    @Test
    @DisplayName("Should throw NullPointerException when User in MedicationTakenLog is null")
    void entityToPM_nullUser_shouldThrowNullPointerException() {
        // Given
        Medication medication = Medication.builder().build();
        medication.setName("Aspirin");
        LocalDateTime timeTaken = LocalDateTime.now();

        MedicationTakenLog medicationTakenLog = new MedicationTakenLog();
        medicationTakenLog.setMedication(medication);
        medicationTakenLog.setUser(null); // User is null
        medicationTakenLog.setTimeTaken(timeTaken);

        // When & Then
        assertThrows(NullPointerException.class, () -> medTakenMapper.entityToPM(medicationTakenLog));
    }
}
