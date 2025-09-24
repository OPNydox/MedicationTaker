package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.MedicationTakenLog;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.takenLogPMs.CreateLogDto;
import com.example.takemeds.presentationModels.takenLogPMs.LogResponseDto;
import com.example.takemeds.repositories.MedicationTakenLogRepository;
import com.example.takemeds.utils.mappers.MedTakenMapper;
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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicationTakenLogServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private MedicationTakenLogRepository logRepository;
    @Mock
    private MedicationScheduleReadService scheduleReadService;
    @Mock
    private MedTakenMapper logMapper;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MedicationTakenLogService service;

    private User testUser;
    private User otherUser;
    private MedicationSchedule testSchedule;
    private MedicationTakenLog testLog;
    private LogResponseDto logResponseDto;
    private CreateLogDto createLogDto;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).email("user@test.com").build();
        otherUser = User.builder().id(2L).email("other@test.com").build();
        testSchedule = MedicationSchedule.builder().id(1L).user(testUser).isFinished(false).build();
        testLog = MedicationTakenLog.builder().id(1L).medicationSchedule(testSchedule).build();
        logResponseDto = LogResponseDto.builder().id(1L).build();
        createLogDto = CreateLogDto.builder().scheduleId(1L).build();
    }

    @Nested
    @DisplayName("Tests for createLog()")
    class CreateLogTests {
        @Test
        @DisplayName("should create a log with a valid schedule and user")
        void shouldCreateLog_withValidScheduleAndUser() throws InvalidRequestException, UnauthorizedAccessException {
            // Arrange
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(scheduleReadService.findMedicationScheduleEntityById(anyLong())).thenReturn(testSchedule);
            when(logRepository.save(any(MedicationTakenLog.class))).thenReturn(testLog);
            when(logMapper.mapToDto(any(MedicationTakenLog.class))).thenReturn(logResponseDto);

            // Act
            LogResponseDto result = service.createLog(createLogDto, userDetails);

            // Assert
            assertThat(result).isNotNull();
            verify(logRepository).save(any(MedicationTakenLog.class));
            verify(logMapper).mapToDto(testLog);
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when user is not the owner of the schedule")
        void shouldThrowUnauthorizedAccessException_whenUserIsNotOwner() {
            // Arrange
            when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(otherUser);
            when(scheduleReadService.findMedicationScheduleEntityById(anyLong())).thenReturn(testSchedule);

            // Act & Assert
            assertThatThrownBy(() -> service.createLog(createLogDto, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("You have not been assigned this medication");

            verify(logRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw InvalidRequestException when schedule is already finished")
        void shouldThrowInvalidRequestException_whenScheduleIsFinished() {
            // Arrange
            testSchedule.setFinished(true);
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(scheduleReadService.findMedicationScheduleEntityById(anyLong())).thenReturn(testSchedule);

            // Act & Assert
            assertThatThrownBy(() -> service.createLog(createLogDto, userDetails))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("already complete");

            verify(logRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests for getLogById()")
    class GetLogByIdTests {
        @Test
        @DisplayName("should return a log when it exists and the user is the owner")
        void shouldReturnLog_whenUserIsOwner() throws UnauthorizedAccessException {
            // Arrange
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(logRepository.findById(anyLong())).thenReturn(Optional.of(testLog));
            when(logMapper.mapToDto(any(MedicationTakenLog.class))).thenReturn(logResponseDto);

            // Act
            LogResponseDto result = service.getLogById(1L, userDetails);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(logRepository).findById(1L);
            verify(logMapper).mapToDto(testLog);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when log does not exist")
        void shouldThrowEntityNotFoundException_whenLogDoesNotExist() {
            // Arrange
            when(logRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.getLogById(99L, userDetails))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("not found with ID: 99");
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when user is not the owner of the log")
        void shouldThrowUnauthorizedAccessException_whenUserIsNotOwner() {
            // Arrange
            when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(otherUser);
            when(logRepository.findById(anyLong())).thenReturn(Optional.of(testLog));

            // Act & Assert
            assertThatThrownBy(() -> service.getLogById(1L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("You do not have access to this resource");
        }
    }

    @Nested
    @DisplayName("Tests for getLogsByScheduleId()")
    class GetLogsByScheduleIdTests {
        @Test
        @DisplayName("should return logs when user is the owner of the schedule")
        void shouldReturnLogs_whenUserIsOwner() throws UnauthorizedAccessException {
            // Arrange
            List<MedicationTakenLog> logs = List.of(testLog);
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(scheduleReadService.findMedicationScheduleEntityById(anyLong())).thenReturn(testSchedule);
            when(logRepository.findByMedicationScheduleId(anyLong())).thenReturn(logs);
            when(logMapper.mapToDto(any(MedicationTakenLog.class))).thenReturn(logResponseDto);

            // Act
            List<LogResponseDto> result = service.getLogsByScheduleId(1L, userDetails);

            // Assert
            assertThat(result).hasSize(1);
            verify(logRepository).findByMedicationScheduleId(1L);
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when user is not the owner")
        void shouldThrowUnauthorizedAccessException_whenUserIsNotOwner() {
            // Arrange
            when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(otherUser);
            when(scheduleReadService.findMedicationScheduleEntityById(anyLong())).thenReturn(testSchedule);

            // Act & Assert
            assertThatThrownBy(() -> service.getLogsByScheduleId(1L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("You do not have access to this resource");
        }
    }

    @Nested
    @DisplayName("Tests for deleteLog()")
    class DeleteLogTests {
        @Test
        @DisplayName("should delete a log when it exists and the user is the owner")
        void shouldDeleteLog_whenUserIsOwner() throws UnauthorizedAccessException {
            // Arrange
            when(userDetails.getUsername()).thenReturn(testUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(testUser);
            when(logRepository.findById(anyLong())).thenReturn(Optional.of(testLog));
            doNothing().when(logRepository).deleteById(anyLong());

            // Act
            service.deleteLog(1L, userDetails);

            // Assert
            verify(logRepository).deleteById(1L);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException when log does not exist")
        void shouldThrowEntityNotFoundException_whenLogDoesNotExist() {
            // Arrange
            when(logRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> service.deleteLog(99L, userDetails))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("not found with ID: 99");

            verify(logRepository, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("should throw UnauthorizedAccessException when user is not the owner")
        void shouldThrowUnauthorizedAccessException_whenUserIsNotOwner() {
            // Arrange
            when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
            when(userService.getUser(anyString())).thenReturn(otherUser);
            when(logRepository.findById(anyLong())).thenReturn(Optional.of(testLog));

            // Act & Assert
            assertThatThrownBy(() -> service.deleteLog(1L, userDetails))
                    .isInstanceOf(UnauthorizedAccessException.class)
                    .hasMessageContaining("You do not have access to this resource");

            verify(logRepository, never()).deleteById(anyLong());
        }
    }
}

