package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.utils.StringUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DosageMapperTest {

    private DosageMapper dosageMapper;

    private StringUtilities stringUtilities;

    @BeforeEach
    void setUp() {
        stringUtilities = new StringUtilities();
        dosageMapper = new DosageMapper(stringUtilities);
    }


    @Nested
    @DisplayName("When mapping from Dosage entity to Presentation Model")
    class EntityToPresentationModelTests {

        @Test
        @DisplayName("should correctly map a Dosage to DosagePresentationModel")
        void mapEntityToDosagePM_shouldMapCorrectly() {
            // Arrange
            Dosage dosage = Dosage.builder()
                    .frequency(Frequency.DAILY)
                    .timesPerDay((byte) 2)
                    .timesToTake(List.of(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                    .build();

            // Act
            DosagePresentationModel dosagePM = dosageMapper.mapEntityToDosagePM(dosage);

            // Assert
            assertThat(dosagePM).isNotNull();
            assertThat(dosagePM.getFrequency()).isEqualTo("DAILY");
            assertThat(dosagePM.getTimesPerDay()).isEqualTo((byte) 2);
            assertThat(dosagePM.getTimesToTake()).containsExactly(LocalTime.of(9, 0), LocalTime.of(17, 0));
        }

        @Test
        @DisplayName("should correctly map a Dosage to CreateDosagePM")
        void mapEntityToCreateDosagePM_shouldMapCorrectly() {
            // Arrange
            Dosage dosage = Dosage.builder()
                    .frequency(Frequency.WEEKLY)
                    .timesPerDay((byte) 1)
                    .timesToTake(List.of(LocalTime.of(12, 0)))
                    .build();

            // Act
            CreateDosagePM createDosagePM = dosageMapper.mapEntityToCreateDosagePM(dosage);

            // Assert
            assertThat(createDosagePM).isNotNull();
            assertThat(createDosagePM.getFrequency()).isEqualTo("WEEKLY");
            assertThat(createDosagePM.getTimesPerDay()).isEqualTo((byte) 1);
            assertThat(createDosagePM.getTimesToTake()).containsExactly(LocalTime.of(12, 0));
        }
    }

    @Nested
    @DisplayName("When mapping from Presentation Model to Dosage entity")
    class PresentationModelToEntityTests {

        @Test
        @DisplayName("should correctly map a CreateDosagePM to a Dosage entity")
        void mapCreateDosagePMToEntity_shouldMapCorrectly() throws InvalidFrequencyException {
            // Arrange
            CreateDosagePM createDosagePM = CreateDosagePM.builder()
                    .frequency("DAILY")
                    .timesPerDay((byte) 3)
                    .timesToTake(List.of(LocalTime.of(8, 0), LocalTime.of(14, 0), LocalTime.of(20, 0)))
                    .build();

            // Act
            Dosage dosage = dosageMapper.mapCreateDosagePMToEntity(createDosagePM);

            // Assert
            assertThat(dosage).isNotNull();
            assertThat(dosage.getFrequency()).isEqualTo(Frequency.DAILY);
            assertThat(dosage.getTimesPerDay()).isEqualTo((byte) 3);
            assertThat(dosage.getTimesToTake()).containsExactly(LocalTime.of(8, 0), LocalTime.of(14, 0), LocalTime.of(20, 0));
        }

        @Test
        @DisplayName("should correctly map a DosagePresentationModel to a Dosage entity")
        void mapDosagePMToEntity_shouldMapCorrectly() throws InvalidFrequencyException {
            // Arrange
            DosagePresentationModel dosagePM = DosagePresentationModel.builder()
                    .frequency("AS_NEEDED")
                    .timesPerDay((byte) 0)
                    .timesToTake(List.of())
                    .build();

            // Act
            Dosage dosage = dosageMapper.mapDosagePMToEntity(dosagePM);

            // Assert
            assertThat(dosage).isNotNull();
            assertThat(dosage.getFrequency()).isEqualTo(Frequency.AS_NEEDED);
            assertThat(dosage.getTimesPerDay()).isEqualTo((byte) 0);
            assertThat(dosage.getTimesToTake()).isEmpty();
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException for an invalid frequency string")
        void mapToEntity_withInvalidFrequency_shouldThrowException() {
            // Arrange
            CreateDosagePM createDosagePM = CreateDosagePM.builder()
                    .frequency("INVALID_FREQ")
                    .timesPerDay((byte) 1)
                    .build();

            // Mock the static method to throw the exception
            assertThrows(InvalidFrequencyException.class, () -> {
                when(stringUtilities.frequencyFromString("INVALID_FREQ")).thenThrow(new InvalidFrequencyException("Invalid frequency"));
                dosageMapper.mapCreateDosagePMToEntity(createDosagePM);
            });
        }
    }

    @Nested
    @DisplayName("When handling null input")
    class NullHandlingTests {

        @Test
        @DisplayName("mapEntityToDosagePM should return null for a null Dosage entity")
        void mapEntityToDosagePM_shouldReturnNull_forNullInput() {
            assertThat(dosageMapper.mapEntityToDosagePM(null)).isNull();
        }

        @Test
        @DisplayName("mapEntityToCreateDosagePM should return null for a null Dosage entity")
        void mapEntityToCreateDosagePM_shouldReturnNull_forNullInput() {
            assertThat(dosageMapper.mapEntityToCreateDosagePM(null)).isNull();
        }

        @Test
        @DisplayName("mapCreateDosagePMToEntity should return null for a null CreateDosagePM")
        void mapCreateDosagePMToEntity_shouldReturnNull_forNullInput() throws InvalidFrequencyException {
            assertThat(dosageMapper.mapCreateDosagePMToEntity(null)).isNull();
        }

        @Test
        @DisplayName("mapDosagePMToEntity should return null for a null DosagePresentationModel")
        void mapDosagePMToEntity_shouldReturnNull_forNullInput() throws InvalidFrequencyException {
            assertThat(dosageMapper.mapDosagePMToEntity(null)).isNull();
        }
    }
}
