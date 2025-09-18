package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.User;
import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    private UserService userService;
    @Mock
    private MedicationMapper medicationMapper;
    @Mock
    private DosageService dosageService;
    @Mock
    private UserDetails userDetails;

    private final String MEDICATION_NAME = "TestMed";

    private final String MEDICATION_DESC = "TestDesc";

    private final Long MEDICATION_ID = 1L;

    @InjectMocks
    private MedicationService medicationService;

    // Helper methods for creating test data
    private User createTestUser(String username) {
        return User.builder().id(1L).email(username).medications(new ArrayList<>()).build();
    }

    private Medication createTestMedication() {
        return Medication.builder().id(MEDICATION_ID).name(MEDICATION_NAME).description(MEDICATION_DESC).build();
    }

    private CreateMedicationDto createTestCreateMedicationDto() {

        return CreateMedicationDto.builder().name(MEDICATION_NAME).description(MEDICATION_DESC).build();
    }

    private BaseDosagePM createDosagePM() {
        return BaseDosagePM.builder()
                .frequency("DAILY")
                .timesPerDay((byte) 2)
                .timesToTake(List.of(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .build();
    }

    private Dosage createDosage() {
        return Dosage.builder()
                .frequency(Frequency.DAILY)
                .scheduledTime(LocalDateTime.now())
                .timesPerDay((byte) 2)
                .timesToTake(List.of(LocalTime.of(9, 0), LocalTime.of(17, 0)))
                .build();
    }

    private MedicationView createTestMedicationView() {
        return MedicationView.builder().name(MEDICATION_NAME).description(MEDICATION_DESC).build();
    }

    @Nested
    @DisplayName("Tests for createMedication method")
    class CreateMedicationTests {

        @Test
        @DisplayName("should create a new medication with a default dosage and assign it to the user")
        void createMedication_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            User user = createTestUser(username);
            BaseDosagePM dosageDto = createDosagePM();
            CreateMedicationDto medicationDto = createTestCreateMedicationDto();
            medicationDto.setDefaultDosagePM(dosageDto);
            Dosage dosage = createDosage();
            Medication medication = createTestMedication();
            MedicationView medicationView = createTestMedicationView();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(medicationMapper.mapPMToEntity(any(CreateMedicationDto.class))).thenReturn(medication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);
            when(dosageService.createDosageEntity(dosageDto)).thenReturn(dosage);
            when(medicationRepository.save(any(Medication.class))).thenReturn(medication);


            // Act
            MedicationView result = medicationService.createMedication(medicationDto, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(user.getMedications()).contains(medication);
            assertThat(medication.getDefaultDosage()).isEqualTo(dosage);
            assertThat(dosage.getMedication()).isEqualTo(medication);
            verify(userService).getUser(username);
            verify(medicationRepository, times(1)).save(medication);
        }

        @Test
        @DisplayName("should create a medication with an existing dosage and assign it to the user")
        void createMedication_withExistingDosageId_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            User user = createTestUser(username);
            CreateMedicationDto medicationDto = createTestCreateMedicationDto();
            Dosage existingDosage = new Dosage();
            medicationDto.setDosageId(1L);
            Medication medication = createTestMedication();
            MedicationView medicationView = createTestMedicationView();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(medicationMapper.mapPMToEntity(any(CreateMedicationDto.class))).thenReturn(medication);
            when(dosageService.findDosageEntity(1L)).thenReturn(existingDosage);
            when(medicationRepository.save(any(Medication.class))).thenReturn(medication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);

            // Act
            MedicationView result = medicationService.createMedication(medicationDto, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(user.getMedications()).contains(medication);
            assertThat(medication.getDefaultDosage()).isEqualTo(existingDosage);
            assertThat(existingDosage.getMedication()).isEqualTo(medication);
            verify(dosageService, never()).createDosageEntity(any());
        }

        @Test
        @DisplayName("should create a medication without a dosage")
        void createMedication_withoutDosage_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            User user = createTestUser(username);
            CreateMedicationDto medicationDto = createTestCreateMedicationDto();
            Medication medication = createTestMedication();
            MedicationView medicationView = createTestMedicationView();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(medicationMapper.mapPMToEntity(any(CreateMedicationDto.class))).thenReturn(medication);
            when(medicationRepository.save(any(Medication.class))).thenReturn(medication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);

            // Act
            MedicationView result = medicationService.createMedication(medicationDto, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(user.getMedications()).contains(medication);
            assertThat(result.getDefaultDosage()).isNull();
            verify(dosageService, never()).createDosageEntity(any());
            verify(dosageService, never()).findDosageEntity(any());
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException if dosage creation fails")
        void createMedication_shouldThrowException_whenDosageCreationFails() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            User user = createTestUser(username);
            CreateMedicationDto medicationDto = createTestCreateMedicationDto();
            BaseDosagePM baseDosagePM = createDosagePM();
            baseDosagePM.setFrequency("Invalid frequency");
            medicationDto.setDefaultDosagePM(baseDosagePM);

            when(userService.getUser(username)).thenReturn(user);
            when(userDetails.getUsername()).thenReturn(username);
            when(dosageService.createDosageEntity(baseDosagePM)).thenThrow(new InvalidFrequencyException("Invalid frequency"));

            // Act & Assert
            assertThatThrownBy(() -> medicationService.createMedication(medicationDto, userDetails))
                    .isInstanceOf(InvalidFrequencyException.class);

            verify(medicationRepository, never()).save(any(Medication.class));
        }
    }

    @Nested
    @DisplayName("Tests for deleteMedication method")
    class DeleteMedicationTests {

        @Test
        @DisplayName("should successfully delete a medication")
        void deleteMedication_shouldSucceed() {
            // Arrange
            Long medicationId = 1L;
            String username = "testuser";
            User user = createTestUser(username);
            Medication medicationToDelete = createTestMedication();
            user.getMedications().add(medicationToDelete);

            when(userService.getUser(anyString())).thenReturn(user);
            when(userService.findUserMedication(medicationId, user)).thenReturn(medicationToDelete);
            when(userDetails.getUsername()).thenReturn("");

            // Act
            medicationService.deleteMedication(medicationId, userDetails);

            // Assert
            assertThat(user.getMedications()).doesNotContain(medicationToDelete);
            verify(medicationRepository, times(1)).delete(medicationToDelete);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when medication does not exist or is not assigned to the user")
        void deleteMedication_shouldThrowException() {
            // Arrange
            Long medicationId = 99L;
            String username = "testuser";
            User user = createTestUser(username);

            when(userService.getUser(anyString())).thenReturn(user);
            when(userService.findUserMedication(medicationId, user)).thenThrow(new EntityNotFoundException("Medication not found"));
            when(userDetails.getUsername()).thenReturn(username);

            // Act & Assert
            assertThatThrownBy(() -> medicationService.deleteMedication(medicationId, userDetails))
                    .isInstanceOf(EntityNotFoundException.class);
            verify(medicationRepository, never()).delete(any(Medication.class));
        }
    }

    @Nested
    @DisplayName("Tests for findMedication method")
    class FindMedication {

        @Test
        @DisplayName("should successfully find a medication")
        void deleteMedication_shouldSucceed() {
            // Arrange
            long medicationId = 1L;
            Medication medication = createTestMedication();
            Medication foundMedication;

            when(medicationRepository.findById(medicationId)).thenReturn(Optional.of(medication));

            // Act
            foundMedication = medicationService.findMedication(medicationId);

            // Assert
            assertThat(foundMedication).isEqualTo(medication);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when medication does not exist")
        void deleteMedication_shouldThrowException() {
            // Arrange
            long medicationId = 99L;
            Medication foundMedication = null;

            when(medicationRepository.findById(medicationId)).thenReturn(Optional.empty());

            // Assert & Act
            assertThatThrownBy(() -> medicationService.findMedication(medicationId))
                    .isInstanceOf(EntityNotFoundException.class);

            assertThat(foundMedication).isNull();
        }
    }

    @Nested
    @DisplayName("Tests for editMedication method")
    class EditMedicationTests {

        @Test
        @DisplayName("should successfully edit a medication")
        void editMedication_shouldSucceed() {
            // Arrange
            String username = "testuser";
            String updatedName = "Updated Name";
            String updatedDescription = "Updated Description";
            long medicationId = 1L;
            User user = createTestUser(username);
            CreateMedicationDto updateDto = CreateMedicationDto.builder().name(updatedName)
                                                                         .description(updatedDescription).build();
            Medication expectedMedication = Medication.builder().name(updatedName)
                                                        .description(updatedDescription).build();

            Medication oldMedication = createTestMedication();
            MedicationView medicationView = MedicationView.builder().name(updatedName)
                                                                    .description(updatedDescription).build();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(userService.findUserMedication(medicationId, user)).thenReturn(oldMedication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);


            // Act
            MedicationView result = medicationService.editMedication(medicationId, updateDto, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(oldMedication.getName()).isEqualTo(expectedMedication.getName());
            assertThat(oldMedication.getDescription()).isEqualTo(expectedMedication.getDescription());
        }
    }

    @Nested
    @DisplayName("Tests for setDefaultDosage method")
    class SetDosageTests {

        @Test
        @DisplayName("should successfully set a default dosage for a medication")
        void setDefaultDosage_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            long medicationId = 1L;
            User user = createTestUser(username);
            Dosage dosage = createDosage();
            CreateDosagePM createDosage = CreateDosagePM.builder().medicationId(medicationId)
                    .frequency("DAILY")
                    .timesPerDay((byte) 2).build();
            Medication foundMedication = createTestMedication();
            MedicationView medicationView = createTestMedicationView();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(userService.findUserMedication(medicationId, user)).thenReturn(foundMedication);
            when(dosageService.createDosageEntity(any(BaseDosagePM.class))).thenReturn(dosage);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);


            // Act
            MedicationView result = medicationService.setDefaultDosage(createDosage, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(foundMedication.getDefaultDosage()).isNotNull();
        }

        @Test
        @DisplayName("should successfully set a default dosage for a medication")
        void setDefautlDosage_dosageId_shouldSuccede() throws InvalidFrequencyException {
            // Arrange
            String username = "testuser";
            long medicationId = 1L;
            long dosageId = 1L;
            User user = createTestUser(username);
            Dosage foundDosage = createDosage();
            Medication foundMedication = createTestMedication();
            MedicationView medicationView = createTestMedicationView();

            when(userDetails.getUsername()).thenReturn(username);
            when(userService.getUser(username)).thenReturn(user);
            when(userService.findUserMedication(medicationId, user)).thenReturn(foundMedication);
            when(medicationMapper.mapEntityToPM(any(Medication.class))).thenReturn(medicationView);
            when(dosageService.findDosageEntity(dosageId)).thenReturn(foundDosage);

            // Act
            MedicationView result = medicationService.setDefaultDosage(medicationId, dosageId, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(foundMedication.getDefaultDosage()).isNotNull();
        }
    }
}
