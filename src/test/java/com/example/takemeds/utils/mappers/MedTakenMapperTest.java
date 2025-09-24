package com.example.takemeds.utils.mappers;


import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MedTakenMapperTest {
    private final MedTakenMapper medTakenMapper = new MedTakenMapper();

    // Helper method for creating a test entity
    private MedicationTakenLog createTestEntity() {
        return MedicationTakenLog.builder()
                .id(1L)
                .medicationSchedule(MedicationSchedule.builder().id(5L).build())
                .timeTaken(LocalDateTime.of(2025, 9, 24, 10, 30))
                .notes("Took with breakfast.")
                .build();
    }

    @Nested
    @DisplayName("Tests for mapToDto method")
    class MapToDtoTests {

        @Test
        @DisplayName("should correctly map a MedicationTakenLog entity to a LogResponseDto")
        void mapToDto_shouldMapCorrectly() {
            // Arrange
            MedicationTakenLog log = createTestEntity();

            // Act
            LogResponseDto dto = medTakenMapper.mapToDto(log);

            // Assert
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isEqualTo(log.getId());
            assertThat(dto.getScheduleId()).isEqualTo(log.getMedicationSchedule().getId());
            assertThat(dto.getTimeTaken()).isEqualTo(log.getTimeTaken());
            assertThat(dto.getNotes()).isEqualTo(log.getNotes());
        }

        @Test
        @DisplayName("should return null when the input entity is null")
        void mapToDto_shouldReturnNull_forNullInput() {
            // Act
            LogResponseDto dto = medTakenMapper.mapToDto(null);

            // Assert
            assertThat(dto).isNull();
        }
    }
}
