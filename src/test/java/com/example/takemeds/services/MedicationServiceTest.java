package com.example.takemeds.services;

import com.example.takemeds.entities.Medication;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.repositories.MedicationRepository;
import com.example.takemeds.utils.mappers.MedicationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTest {
    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicationMapper medicationMapper;

    @Mock
    private DosageService dosageService;

    @InjectMocks
    private MedicationService medicationService;

    private Medication createTestMedication(Long id, String name, String description) {
        return Medication.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    private CreateMedicationDto createTestBasePM(String name, String description) {
        return CreateMedicationDto.builder()
                .name(name)
                .description(description)
                .build();
    }

    private MedicationView createTestMedicationPM(String name, String description) {
        return MedicationView.builder()
                .name(name)
                .description(description)
                .build();
    }

    @Nested
    @DisplayName("Tests for createMedication method")
    class CreateMedicationPMTests {

        @Test
        @DisplayName("should successfully create a medication and return a presentation model")
        void createMedicationPM_shouldSucceed() {
            // Arrange
            CreateMedicationDto basePM = createTestBasePM("Test Medication", "Test Description");
            Medication medicationToSave = createTestMedication(null, "Test Medication", "Test Description");
            Medication savedMedication = createTestMedication(1L, "Test Medication", "Test Description");
            MedicationView expectedPM = createTestMedicationPM("Test Medication", "Test Description");

            when(medicationMapper.mapPMToEntity(any(CreateMedicationDto.class))).thenReturn(medicationToSave);
            when(medicationRepository.save(any(Medication.class))).thenReturn(savedMedication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(expectedPM);

            // Act
            MedicationView resultPM = medicationService.createMedication(basePM);

            // Assert
            assertThat(resultPM).isEqualTo(expectedPM);
            verify(medicationRepository, times(1)).save(medicationToSave);
        }
    }

    @Nested
    @DisplayName("Tests for createMedicationEntity method")
    class CreateMedicationEntityTests {

        @Test
        @DisplayName("should successfully create a medication and return an Entity")
        void createMedicationPM_shouldSucceed() {
            // Arrange
            CreateMedicationDto basePM = createTestBasePM("Test Medication", "Test Description");
            Medication medicationToSave = createTestMedication(null, "Test Medication", "Test Description");
            Medication savedMedication = createTestMedication(1L, "Test Medication", "Test Description");
            Medication expectedEntity = createTestMedication(1L, "Test Medication", "Test Description");;

            when(medicationMapper.mapPMToEntity(any(CreateMedicationDto.class))).thenReturn(medicationToSave);
            when(medicationRepository.save(any(Medication.class))).thenReturn(savedMedication);

            // Act
            Medication resultPM = medicationService.createMedicationEntity(basePM);

            // Assert
            assertThat(expectedEntity.getId()).isEqualTo(resultPM.getId());
            assertThat(expectedEntity.getName()).isEqualTo(resultPM.getName());
            assertThat(expectedEntity.getDescription()).isEqualTo(resultPM.getDescription());
            verify(medicationRepository, times(1)).save(medicationToSave);
        }
    }

    @Nested
    @DisplayName("Tests for findMedication method")
    class FindMedicationTests {

        @Test
        @DisplayName("should find and return a medication when a valid ID is provided")
        void findMedication_withValidId_shouldReturnMedication() {
            // Arrange
            Long medicationId = 1L;
            Medication expectedMedication = createTestMedication(medicationId, "Test Med", "Desc");
            when(medicationRepository.findById(medicationId)).thenReturn(Optional.of(expectedMedication));

            // Act
            Medication foundMedication = medicationService.findMedication(medicationId);

            // Assert
            assertThat(foundMedication).isEqualTo(expectedMedication);
            verify(medicationRepository, times(1)).findById(medicationId);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException if medication ID does not exist")
        void findMedication_withInvalidId_shouldThrowException() {
            // Arrange
            Long medicationId = 999L;
            when(medicationRepository.findById(medicationId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> medicationService.findMedication(medicationId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Medication with id " + medicationId + " does not exist");
        }
    }

    @Nested
    @DisplayName("Tests for editMedication method")
    class EditMedicationTests {

        @Test
        @DisplayName("should successfully edit a medication and return the updated presentation model")
        void editMedication_shouldSucceed() {
            // Arrange
            Long medicationId = 1L;
            Medication oldMedication = createTestMedication(medicationId, "Old Name", "Old Desc");
            MedicationView updatePM = MedicationView.builder()
                    .name("New Name")
                    .description("New Desc")
                    .build();
            Medication updatedMedication = createTestMedication(medicationId, "New Name", "New Desc");
            MedicationView expectedPM = createTestMedicationPM("New Name", "New Desc");

            when(medicationRepository.save(any(Medication.class))).thenReturn(updatedMedication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(expectedPM);

            // Act
            MedicationDosageDto resultPM = medicationService.editMedication(oldMedication, updatePM);

            // Assert
            assertThat(resultPM).isEqualTo(expectedPM);
            assertThat(oldMedication.getName()).isEqualTo("New Name");
            assertThat(oldMedication.getDescription()).isEqualTo("New Desc");
            verify(medicationRepository, times(1)).save(oldMedication);
        }
    }

    @Nested
    @DisplayName("Tests for deleteMedication method")
    class DeleteMedicationTests {

        @Test
        @DisplayName("should successfully delete a medication when a valid ID is provided")
        void deleteMedication_withValidId_shouldSucceed() {
            // Arrange
            Long medicationId = 1L;
            Medication medicationToDelete = createTestMedication(medicationId, "To Delete", "Desc");
            when(medicationRepository.findById(medicationId)).thenReturn(Optional.of(medicationToDelete));
            doNothing().when(medicationRepository).delete(medicationToDelete);

            // Act
            medicationService.deleteMedication(medicationId);

            // Assert
            verify(medicationRepository, times(1)).delete(medicationToDelete);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when trying to delete a non-existent medication")
        void deleteMedication_withInvalidId_shouldThrowException() {
            // Arrange
            Long medicationId = 999L;
            when(medicationRepository.findById(medicationId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> medicationService.deleteMedication(medicationId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Medication with id " + medicationId + " does not exist");
        }
    }
}
