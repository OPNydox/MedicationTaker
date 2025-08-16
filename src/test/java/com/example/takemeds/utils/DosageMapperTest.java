package com.example.takemeds.utils;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.DosagePresentationModel;
import com.example.takemeds.utils.mappers.DosageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DosageMapperTest {

    private DosageMapper dosageMapper;

    @BeforeEach
    void setUp() {
        dosageMapper = new DosageMapper();
    }

    @Test
    @DisplayName("Should correctly map a Dosage entity to a DosagePresentationModel")
    void entityToPM_validInput_shouldMapCorrectly() {
        // Given
        List<LocalTime> timesToTake = Arrays.asList(LocalTime.of(8, 0), LocalTime.of(20, 0));
        Dosage entity = Dosage.builder()
                .frequency(Frequency.DAILY)
                .timesPerDay((byte) 2)
                .timesToTake(timesToTake)
                .build();

        // When
        DosagePresentationModel presentationModel = dosageMapper.entityToPM(entity);

        // Then
        assertNotNull(presentationModel);
        assertEquals("DAILY", presentationModel.getFrequency());
        assertEquals(2, presentationModel.getTimesPerDay());
        assertEquals(timesToTake, presentationModel.getTimesToTake());
    }

    @Test
    @DisplayName("Should correctly map a DosagePresentationModel to a Dosage entity")
    void PMtoEntity_validInput_shouldMapCorrectly() throws InvalidFrequencyException {
        // Given
        List<LocalTime> timesToTake = Arrays.asList(LocalTime.of(8, 0), LocalTime.of(20, 0));
        DosagePresentationModel presentationModel = DosagePresentationModel.builder()
                .frequency("DAILY")
                .timesPerDay((byte) 2)
                .timesToTake(timesToTake)
                .build();

        // When
        Dosage entity = dosageMapper.PMtoEntity(presentationModel);

        // Then
        assertNotNull(entity);
        assertEquals(Frequency.DAILY, entity.getFrequency());
        assertEquals(2, entity.getTimesPerDay());
        assertEquals(timesToTake, entity.getTimesToTake());
    }

    @Test
    @DisplayName("Should throw InvalidFrequencyException for an invalid frequency string")
    void PMtoEntity_invalidFrequency_shouldThrowException() {
        // Given
        DosagePresentationModel presentationModel = DosagePresentationModel.builder()
                .frequency("INVALID_FREQUENCY")
                .timesPerDay((byte) 2)
                .build();

        // When & Then
        assertThrows(InvalidFrequencyException.class, () -> {
            dosageMapper.PMtoEntity(presentationModel);
        });
    }

    @Test
    @DisplayName("Should handle a null timesToTake list gracefully in entityToPM")
    void entityToPM_nullTimesToTake_shouldHandleGracefully() {
        // Given
        Dosage entity = Dosage.builder()
                .frequency(Frequency.DAILY)
                .timesPerDay((byte) 2)
                .timesToTake(null)
                .build();

        // When
        DosagePresentationModel presentationModel = dosageMapper.entityToPM(entity);

        // Then
        assertNotNull(presentationModel);
        assertNull(presentationModel.getTimesToTake());
    }

    @Test
    @DisplayName("Should handle a null timesToTake list gracefully in PMtoEntity")
    void PMtoEntity_nullTimesToTake_shouldHandleGracefully() throws InvalidFrequencyException {
        // Given
        DosagePresentationModel presentationModel = DosagePresentationModel.builder()
                .frequency("DAILY")
                .timesPerDay((byte) 2)
                .timesToTake(null)
                .build();

        // When
        Dosage entity = dosageMapper.PMtoEntity(presentationModel);

        // Then
        assertNotNull(entity);
        assertNull(entity.getTimesToTake());
    }
}
