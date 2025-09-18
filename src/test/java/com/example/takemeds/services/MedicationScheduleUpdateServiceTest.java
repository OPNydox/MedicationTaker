package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.entities.Medication;
import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.UpdateDateDto;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicationScheduleUpdateServiceTest {


    @Mock
    private MedicationScheduleMapper scheduleMapper;
    @Mock
    private MedicationScheduleReadService scheduleReadService;
    @Mock
    private DosageService dosageService;
    @Mock
    private MedicationService medicationService;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicationScheduleUpdateService service;

    private MedicationSchedule testSchedule;
    private MedicationScheduleView scheduleView;

    @BeforeEach
    void setUp() {
        testSchedule = MedicationSchedule.builder().id(1L).isFinished(false).build();
        scheduleView = MedicationScheduleView.builder().id(1L).isFinished(false).build();
    }

    @Nested
    @DisplayName("Tests for swapMedication()")
    class SwapMedicationTests {
        @Test
        @DisplayName("should swap medication when authorized and medication is found")
        void shouldSwapMedication_whenAuthorized_andMedicationIsFound() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            Long medicationId = 2L;
            Medication newMedication = new Medication();
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenReturn(testSchedule);
            when(medicationService.findMedication(anyLong())).thenReturn(newMedication);
            when(scheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.swapMedication(scheduleId, medicationId, userDetails);

            // Assert
            assertThat(testSchedule.getMedication()).isEqualTo(newMedication);
            assertThat(result).isEqualTo(scheduleView);
            verify(scheduleReadService).validateAndGetSchedule(scheduleId, userDetails);
            verify(medicationService).findMedication(medicationId);
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when not authorized")
        void shouldThrowException_whenNotAuthorized() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            Long medicationId = 2L;
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenThrow(new UnauthorizedAccessException("Not authorized."));

            // Act & Assert
            assertThatThrownBy(() -> service.swapMedication(scheduleId, medicationId, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when medication is not found")
        void shouldThrowException_whenMedicationNotFound() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            Long medicationId = 99L;
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenReturn(testSchedule);
            when(medicationService.findMedication(anyLong())).thenThrow(new EntityNotFoundException("Medication not found."));

            // Act & Assert
            assertThatThrownBy(() -> service.swapMedication(scheduleId, medicationId, userDetails))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Tests for editDosage()")
    class EditDosageTests {
        @Test
        @DisplayName("should edit dosage when authorized and dosage is created")
        void shouldEditDosage_whenAuthorized_andDosageCreated() throws UnauthorizedAccessException, InvalidFrequencyException {
            // Arrange
            Long scheduleId = 1L;
            BaseDosagePM dosagePM = BaseDosagePM.builder().build();
            Dosage newDosage = new Dosage();
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenReturn(testSchedule);
            when(dosageService.createDosageEntity(any(BaseDosagePM.class))).thenReturn(newDosage);
            when(scheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.editDosage(scheduleId, dosagePM, userDetails);

            // Assert
            assertThat(testSchedule.getDosage()).isEqualTo(newDosage);
            assertThat(result).isEqualTo(scheduleView);
            verify(scheduleReadService).validateAndGetSchedule(scheduleId, userDetails);
            verify(dosageService).createDosageEntity(dosagePM);
        }
    }

    @Nested
    @DisplayName("Tests for updateDates()")
    class UpdateDatesTests {
        @Test
        @DisplayName("should update dates when authorized")
        void shouldUpdateDates_whenAuthorized() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            LocalDate newStartDate = LocalDate.of(2025, 1, 1);
            LocalDate newEndDate = LocalDate.of(2025, 1, 31);
            UpdateDateDto dates = new UpdateDateDto();
            dates.setStartDate(newStartDate);
            dates.setEndDate(newEndDate);
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenReturn(testSchedule);
            when(scheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.updateDates(scheduleId, dates, userDetails);

            // Assert
            assertThat(testSchedule.getStartDate()).isEqualTo(newStartDate);
            assertThat(testSchedule.getEndDate()).isEqualTo(newEndDate);
            assertThat(result).isEqualTo(scheduleView);
            verify(scheduleReadService).validateAndGetSchedule(scheduleId, userDetails);
        }
    }

    @Nested
    @DisplayName("Tests for finishMedicationSchedule()")
    class FinishMedicationScheduleTests {
        @Test
        @DisplayName("should finish medication schedule when authorized")
        void shouldFinishSchedule_whenAuthorized() throws UnauthorizedAccessException {
            // Arrange
            Long scheduleId = 1L;
            when(scheduleReadService.validateAndGetSchedule(anyLong(), any(UserDetails.class))).thenReturn(testSchedule);
            when(scheduleMapper.toMedicationScheduleView(any(MedicationSchedule.class))).thenReturn(scheduleView);

            // Act
            MedicationScheduleView result = service.finishMedicationSchedule(scheduleId, userDetails);

            // Assert
            assertThat(testSchedule.isFinished()).isTrue();
            assertThat(result).isEqualTo(scheduleView);
            verify(scheduleReadService).validateAndGetSchedule(scheduleId, userDetails);
        }
    }

}
