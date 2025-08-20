package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.utils.StringUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DosageMapperTest {
    @InjectMocks
    private DosageMapper dosageMapper;

    @Mock
    private StringUtilities stringUtilities;

    @Nested
    @DisplayName("When mapping from an entity to a presentation model")
    class EntityToPMTests {

        @Test
        @DisplayName("should correctly map a Dosage entity to a DosagePresentationModel")
        void mapEntityToPM_shouldMapCorrectly() {
            // Arrange
            Dosage dosage = Dosage.builder()
                    .frequency(Frequency.DAILY)
                    .timesPerDay((byte) 2)
                    .timesToTake(List.of(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                    .build();

            // Act
            DosagePresentationModel dosagePM = dosageMapper.mapEntityToPM(dosage);

            // Assert
            assertThat(dosagePM).isNotNull();
            assertThat(dosagePM.getFrequency()).isEqualTo("DAILY");
            assertThat(dosagePM.getTimesPerDay()).isEqualTo((byte) 2);
            assertThat(dosagePM.getTimesToTake()).containsExactly(LocalTime.of(9, 0), LocalTime.of(17, 0));
        }

        @Test
        @DisplayName("should correctly map a Dosage entity to a CreateDosagePM")
        void mapEntityToCreatePM_shouldMapCorrectly() {
            // Arrange
            Dosage dosage = Dosage.builder()
                    .frequency(Frequency.WEEKLY)
                    .timesPerDay((byte) 1)
                    .timesToTake(List.of(LocalTime.of(12, 0)))
                    .build();

            // Act
            CreateDosagePM createDosagePM = dosageMapper.mapEntityToCreatePM(dosage);

            // Assert
            assertThat(createDosagePM).isNotNull();
            assertThat(createDosagePM.getFrequency()).isEqualTo("WEEKLY");
            assertThat(createDosagePM.getTimesPerDay()).isEqualTo((byte) 1);
            assertThat(createDosagePM.getTimesToTake()).containsExactly(LocalTime.of(12, 0));
        }
    }

    @Nested
    @DisplayName("When mapping from a presentation model to an entity")
    class PMToEntityTests {

        @Test
        @DisplayName("should correctly map a BaseDosagePM to a Dosage entity")
        void mapPMToEntity_shouldMapCorrectly_fromBaseDosagePM() throws InvalidFrequencyException {
            // Arrange
            BaseDosagePM baseDosagePM = BaseDosagePM.builder()
                    .frequency("AS_NEEDED")
                    .timesPerDay((byte) 0)
                    .timesToTake(List.of())
                    .build();

            // Mock the dependency call
            when(stringUtilities.frequencyFromString("AS_NEEDED")).thenReturn(Frequency.AS_NEEDED);

            // Act
            Dosage dosage = dosageMapper.mapPMToEntity(baseDosagePM);

            // Assert
            assertThat(dosage).isNotNull();
            assertThat(dosage.getFrequency()).isEqualTo(Frequency.AS_NEEDED);
            assertThat(dosage.getTimesPerDay()).isEqualTo((byte) 0);
            assertThat(dosage.getTimesToTake()).isEmpty();
        }

        @Test
        @DisplayName("should correctly map a CreateDosagePM to a Dosage entity")
        void mapPMToEntity_shouldMapCorrectly_fromCreateDosagePM() throws InvalidFrequencyException {
            // Arrange
            CreateDosagePM createDosagePM = CreateDosagePM.builder()
                    .medicationId(1L)
                    .frequency("DAILY")
                    .timesPerDay((byte) 3)
                    .timesToTake(List.of(LocalTime.of(8, 0), LocalTime.of(14, 0), LocalTime.of(20, 0)))
                    .build();

            // Mock the dependency call
            when(stringUtilities.frequencyFromString("DAILY")).thenReturn(Frequency.DAILY);

            // Act
            Dosage dosage = dosageMapper.mapPMToEntity(createDosagePM);

            // Assert
            assertThat(dosage).isNotNull();
            assertThat(dosage.getFrequency()).isEqualTo(Frequency.DAILY);
            assertThat(dosage.getTimesPerDay()).isEqualTo((byte) 3);
            assertThat(dosage.getTimesToTake()).containsExactly(LocalTime.of(8, 0), LocalTime.of(14, 0), LocalTime.of(20, 0));
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException for an invalid frequency string")
        void mapPMToEntity_withInvalidFrequency_shouldThrowException() throws InvalidFrequencyException {
            // Arrange
            BaseDosagePM baseDosagePM = BaseDosagePM.builder()
                    .frequency("INVALID_FREQ")
                    .build();

            // Mock the dependency to throw the exception
            when(stringUtilities.frequencyFromString("INVALID_FREQ")).thenThrow(new InvalidFrequencyException("Invalid frequency string."));

            // Act & Assert
            assertThrows(InvalidFrequencyException.class, () -> {
                dosageMapper.mapPMToEntity(baseDosagePM);
            });
        }
    }

    @Nested
    @DisplayName("When handling null input")
    class NullHandlingTests {

        @Test
        @DisplayName("mapEntityToPM should return null for a null Dosage entity")
        void mapEntityToPM_shouldReturnNull_forNullInput() {
            assertThat(dosageMapper.mapEntityToPM(null)).isNull();
        }

        @Test
        @DisplayName("mapEntityToCreatePM should return null for a null Dosage entity")
        void mapEntityToCreatePM_shouldReturnNull_forNullInput() {
            assertThat(dosageMapper.mapEntityToCreatePM(null)).isNull();
        }

        @Test
        @DisplayName("mapPMToEntity should return null for a null BaseDosagePM")
        void mapPMToEntity_shouldReturnNull_forNullInput() throws InvalidFrequencyException {
            assertThat(dosageMapper.mapPMToEntity(null)).isNull();
        }
    }
}
