package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.Receipt;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.repositories.MedicationScheduleRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationScheduleReadServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private MedicationScheduleRepository medicationScheduleRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicationScheduleReadService service;

    private User testUser;
    private MedicationSchedule testSchedule;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).email("user@test.com").build();
        testSchedule = MedicationSchedule.builder().id(1L).user(testUser).isFinished(false).build();
    }

    @Nested
    @DisplayName("Tests for findNonFinishedUserMedicationBy()")
    class FindNonFinishedTests {
        @Test
        @DisplayName("should call repository with correct userId and return a list of schedules")
        void shouldCallRepository_withCorrectUserId() {
            // Arrange
            List<MedicationSchedule> expectedList = List.of(testSchedule);
            when(medicationScheduleRepository.findByUser_IdAndIsFinishedFalse(anyLong())).thenReturn(expectedList);

            // Act
            List<MedicationSchedule> result = service.findNonFinishedUserMedicationBy(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            verify(medicationScheduleRepository).findByUser_IdAndIsFinishedFalse(1L);
        }
    }

    @Nested
    @DisplayName("Tests for findMedicationScheduleById()")
    class FindByIdTests {
        @Test
        @DisplayName("should return a schedule when one is found by ID")
        void shouldReturnSchedule_whenFound() {
            // Arrange
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.of(testSchedule));

            // Act
            MedicationSchedule result = service.findMedicationScheduleById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(medicationScheduleRepository).findById(1L);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when schedule is not found")
        void shouldThrowException_whenNotFound() {
            // Arrange
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.findMedicationScheduleById(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Medication assignment with id 99 does not exist");

            verify(medicationScheduleRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Tests for validateAndGetSchedule()")
    class ValidateAndGetScheduleTests {

        @Test
        @DisplayName("should return schedule when user is owner, and schedule is not finalized")
        void shouldReturnSchedule_whenValidAndAuthorized() throws UnauthorizedAccessException {
            // Arrange
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.of(testSchedule));

            // Act
            MedicationSchedule result = service.validateAndGetSchedule(1L, userDetails);

            // Assert
            assertThat(result).isEqualTo(testSchedule);
            verify(userService).getUser(testUser.getEmail());
            verify(medicationScheduleRepository).findById(1L);
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when user is not the owner")
        void shouldThrowException_whenUserIsNotOwner() {
            // Arrange
            User otherUser = User.builder().id(2L).email("other@test.com").build();
            when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(otherUser);
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.of(testSchedule));

            // Act & Assert
            assertThatThrownBy(() -> service.validateAndGetSchedule(1L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("You are not allowed to alter this resource.");
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when a receipt is present")
        void shouldThrowException_whenReceiptIsPresent() {
            // Arrange
            Receipt testReceipt = new Receipt();
            MedicationSchedule scheduleWithReceipt = MedicationSchedule.builder().id(2L).user(testUser).receipt(testReceipt).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.of(scheduleWithReceipt));

            // Act & Assert
            assertThatThrownBy(() -> service.validateAndGetSchedule(2L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("Cannot edit a medication schedule that has been recorded by a receipt.");
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when schedule is finished")
        void shouldThrowException_whenScheduleIsFinished() {
            // Arrange
            MedicationSchedule finishedSchedule = MedicationSchedule.builder().id(3L).user(testUser).isFinished(true).build();
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(medicationScheduleRepository.findById(anyLong())).thenReturn(Optional.of(finishedSchedule));

            // Act & Assert
            assertThatThrownBy(() -> service.validateAndGetSchedule(3L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("Finished schedule cannot be altered.");
        }
    }
}
