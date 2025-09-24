package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.CreateMedicationScheduleRequest;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationScheduleManagementServiceTest {

    @Mock
    private MedicationScheduleRepository medicationScheduleRepository;
    @Mock
    private MedicationScheduleMapper medicationScheduleMapper;
    @Mock
    private MedicationScheduleReadService scheduleReadService;
    @Mock
    private UserService userService;
    @Mock
    private MedicationService medicationService;
    @Mock
    private DosageService dosageService;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicationScheduleManagementService service;

    private User testUser;
    private Medication testMedication;
    private Dosage testDosage;
    private MedicationSchedule testSchedule;
    private CreateMedicationScheduleRequest request;
    private MedicationScheduleView scheduleView;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).email("user@test.com").build();
        testDosage = Dosage.builder().id(1L).build();
        testMedication = Medication.builder().id(1L).defaultDosage(testDosage).build();
        testSchedule = MedicationSchedule.builder().id(1L).user(testUser).medication(testMedication).dosage(testDosage).build();
        request = CreateMedicationScheduleRequest.builder().medication(CreateMedicationDto.builder().build()).dosage(new BaseDosagePM()).build();
        scheduleView = MedicationScheduleView.builder().id(1L).build();
    }

    @Nested
    @DisplayName("Tests for createMedicationSchedule()")
    class CreateTests {

        @Test
        @DisplayName("should create a schedule with a new medication and new dosage")
        void shouldCreateSchedule_withNewMedication_andNewDosage() throws InvalidFrequencyException, InvalidRequestException {
            // Arrange
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(testUser.getEmail())).thenReturn(testUser);
            when(medicationService.createMedicationEntity(any(CreateMedicationDto.class))).thenReturn(testMedication);
            when(dosageService.createDosageEntity(any(BaseDosagePM.class))).thenReturn(testDosage);
            when(medicationScheduleRepository.save(any(MedicationSchedule.class))).thenReturn(testSchedule);
            when(medicationScheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.createMedicationSchedule(request, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(medicationService).createMedicationEntity(request.getMedication());
            verify(dosageService).createDosageEntity(request.getDosage());
            verify(medicationScheduleRepository).save(any(MedicationSchedule.class));
        }

        @Test
        @DisplayName("should create a schedule with existing medication and new dosage")
        void shouldCreateSchedule_withExistingMedication_andNewDosage() throws InvalidFrequencyException, InvalidRequestException {
            // Arrange
            request = CreateMedicationScheduleRequest.builder().medicationId(1L).dosage(new BaseDosagePM()).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(testUser.getEmail())).thenReturn(testUser);
            when(medicationService.findMedication(1L)).thenReturn(testMedication);
            when(dosageService.createDosageEntity(any(BaseDosagePM.class))).thenReturn(testDosage);
            when(medicationScheduleRepository.save(any(MedicationSchedule.class))).thenReturn(testSchedule);
            when(medicationScheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.createMedicationSchedule(request, userDetails);

            // Assert
            assertThat(result).isNotNull();
            verify(medicationService).findMedication(1L);
            verify(medicationService, never()).createMedicationEntity(any());
            verify(dosageService).createDosageEntity(request.getDosage());
        }

        @Test
        @DisplayName("should use default dosage if no dosage provided in request")
        void shouldUseDefaultDosage_whenNoneProvided() throws InvalidFrequencyException, InvalidRequestException {
            // Arrange
            request = CreateMedicationScheduleRequest.builder().medicationId(1L).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(testUser.getEmail())).thenReturn(testUser);
            when(medicationService.findMedication(1L)).thenReturn(testMedication);
            when(medicationScheduleRepository.save(any(MedicationSchedule.class))).thenReturn(testSchedule);
            when(medicationScheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.createMedicationSchedule(request, userDetails);

            // Assert
            assertThat(result).isNotNull();
            verify(medicationService).findMedication(1L);
            verify(dosageService, never()).createDosageEntity(any());
        }

        @Test
        @DisplayName("should throw InvalidRequestException if no medication provided")
        void shouldThrowException_whenNoMedicationProvided() {
            // Arrange
            request = CreateMedicationScheduleRequest.builder().dosage(new BaseDosagePM()).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(testUser.getEmail())).thenReturn(testUser);

            // Act & Assert
            assertThatThrownBy(() -> service.createMedicationSchedule(request, userDetails))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("must have an attached medication.");
        }

        @Test
        @DisplayName("should throw InvalidRequestException if no dosage provided and medication has no default")
        void shouldThrowException_whenNoDosageProvided_andMedicationHasNoDefault() {
            // Arrange
            Medication medicationWithoutDosage = Medication.builder().id(2L).defaultDosage(null).build();
            request = CreateMedicationScheduleRequest.builder().medicationId(2L).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(testUser.getEmail())).thenReturn(testUser);
            when(medicationService.findMedication(2L)).thenReturn(medicationWithoutDosage);

            // Act & Assert
            assertThatThrownBy(() -> service.createMedicationSchedule(request, userDetails))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("must have an attached dosage.");
        }
    }

    @Nested
    @DisplayName("Tests for deleteMedicationSchedule()")
    class DeleteTests {

        @Test
        @DisplayName("should successfully delete a medication schedule when authorized")
        void shouldDeleteSchedule_whenAuthorized() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            when(scheduleReadService.validateAndGetSchedule(scheduleId, userDetails)).thenReturn(testSchedule);

            // Act
            service.deleteMedicationSchedule(scheduleId, userDetails);

            // Assert
            verify(medicationScheduleRepository).delete(testSchedule);
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when not authorized")
        void shouldThrowException_whenNotAuthorized() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            when(scheduleReadService.validateAndGetSchedule(scheduleId, userDetails)).thenThrow(new UnauthorizedAccessException("Not authorized."));

            // Act & Assert
            assertThatThrownBy(() -> service.deleteMedicationSchedule(scheduleId, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class);

            verify(medicationScheduleRepository, never()).delete(any());
        }
    }
}
