package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicationMapperTest {
    @Mock
    private DosageMapper dosageMapper;

    @InjectMocks
    private MedicationMapper medicationMapper;

    // Helper methods for creating test data
    private Medication createTestMedication(Long id, String name, String description, Dosage dosage) {
        return Medication.builder()
                .id(id)
                .name(name)
                .description(description)
                .defaultDosage(dosage)
                .build();
    }

    private Dosage createTestDosage(Long id, Frequency frequency) {
        return Dosage.builder()
                .id(id)
                .frequency(frequency)
                .build();
    }

    private BaseDosagePM createTestDosagePM() {
        return BaseDosagePM.builder()
                .frequency("DAILY")
                .timesPerDay((byte) 1)
                .build();
    }

    private CreateMedicationDto createTestCreateMedicationDto(String name, String description) {
        return CreateMedicationDto.builder()
                .name(name)
                .description(description)
                .build();
    }

    // --- Entity to Presentation Model Mapping Tests ---

    @Nested
    @DisplayName("Tests for mapEntityToPM method")
    class MapEntityToPMTests {

        @Test
        @DisplayName("should correctly map a Medication entity to a MedicationView")
        void mapEntityToPM_shouldMapCorrectly() {
            // Arrange
            Dosage dosage = createTestDosage(1L, Frequency.DAILY);
            Medication medication = createTestMedication(1L, "Aspirin", "Pain reliever", dosage);
            BaseDosagePM dosagePM = createTestDosagePM();

            when(dosageMapper.mapBaseEntityToPM(any(Dosage.class))).thenReturn(dosagePM);

            // Act
            MedicationView medicationView = medicationMapper.mapEntityToPM(medication);

            // Assert
            assertThat(medicationView).isNotNull();
            assertThat(medicationView.getId()).isEqualTo(medication.getId());
            assertThat(medicationView.getName()).isEqualTo(medication.getName());
            assertThat(medicationView.getDescription()).isEqualTo(medication.getDescription());
            assertThat(medicationView.getDefaultDosage()).isEqualTo(dosagePM);
        }

        @Test
        @DisplayName("should return null when the input Medication entity is null")
        void mapEntityToPM_shouldReturnNull_forNullInput() {
            // Act
            MedicationView medicationView = medicationMapper.mapEntityToPM(null);

            // Assert
            assertThat(medicationView).isNull();
        }
    }

    // --- List Mapping Tests ---

    @Nested
    @DisplayName("Tests for mapMedicationsToPM method")
    class MapMedicationsToPMTests {

        @Test
        @DisplayName("should correctly map a list of Medication entities to a list of MedicationViews")
        void mapMedicationsToPM_shouldMapListCorrectly() {
            // Arrange
            List<Medication> medications = List.of(
                    createTestMedication(1L, "Aspirin", "Pain reliever", createTestDosage(1L, Frequency.DAILY)),
                    createTestMedication(2L, "Ibuprofen", "Anti-inflammatory", createTestDosage(2L, Frequency.AS_NEEDED))
            );
            List<MedicationView> expectedViews = List.of(
                    MedicationView.builder().id(1L).name("Aspirin").description("Pain reliever").defaultDosage(createTestDosagePM()).build(),
                    MedicationView.builder().id(2L).name("Ibuprofen").description("Anti-inflammatory").defaultDosage(createTestDosagePM()).build()
            );

            when(dosageMapper.mapBaseEntityToPM(any(Dosage.class))).thenReturn(createTestDosagePM());

            // Act
            List<MedicationView> resultViews = medicationMapper.mapMedicationsToPM(medications);

            // Assert
            assertThat(resultViews).hasSize(2);
            assertThat(resultViews.get(0).getName()).isEqualTo("Aspirin");
            assertThat(resultViews.get(1).getName()).isEqualTo("Ibuprofen");
        }

        @Test
        @DisplayName("should return an empty list when the input list is empty")
        void mapMedicationsToPM_shouldReturnEmptyList_forEmptyInput() {
            // Act
            List<MedicationView> resultViews = medicationMapper.mapMedicationsToPM(Collections.emptyList());

            // Assert
            assertThat(resultViews).isEmpty();
        }

        @Test
        @DisplayName("should return null when the input list is null")
        void mapMedicationsToPM_shouldReturnNull_forNullInput() {
            // Act
            List<MedicationView> resultViews = medicationMapper.mapMedicationsToPM(null);

            // Assert
            assertThat(resultViews).isEmpty();
        }
    }

    // --- Presentation Model to Entity Mapping Tests ---

    @Nested
    @DisplayName("Tests for mapPMToEntity method")
    class MapPMToEntityTests {

        @Test
        @DisplayName("should correctly map a CreateMedicationDto to a Medication entity")
        void mapPMToEntity_shouldMapCorrectly() {
            // Arrange
            CreateMedicationDto dto = createTestCreateMedicationDto("TestMed", "TestDesc");

            // Act
            Medication medication = medicationMapper.mapPMToEntity(dto);

            // Assert
            assertThat(medication).isNotNull();
            assertThat(medication.getId()).isNull(); // ID should be null for a new entity
            assertThat(medication.getName()).isEqualTo(dto.getName());
            assertThat(medication.getDescription()).isEqualTo(dto.getDescription());
        }

        @Test
        @DisplayName("should return null when the input CreateMedicationDto is null")
        void mapPMToEntity_shouldReturnNull_forNullInput() {
            // Act
            Medication medication = medicationMapper.mapPMToEntity(null);

            // Assert
            assertThat(medication).isNull();
        }
    }
}
