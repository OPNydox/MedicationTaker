package com.example.takemeds.services;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.repositories.DosageRepository;
import com.example.takemeds.utils.mappers.DosageMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DosageServiceTest {

    @Mock
    private DosageRepository dosageRepository;

    @Mock
    private DosageMapper dosageMapper;

    @InjectMocks
    private DosageService dosageService;

    private Dosage createTestDosage() {
        return Dosage.builder().id(1L).build();
    }

    private DosagePresentationModel createTestDosagePM() {
        return DosagePresentationModel.builder().build();
    }

    private BaseDosagePM createTestBaseDosagePM() {
        return BaseDosagePM.builder().build();
    }


    @Nested
    @DisplayName("Tests for createAndMapDosage()")
    class CreateAndMapDosageTests {
        @Test
        @DisplayName("should create a dosage and map it to a presentation model on success")
        void createAndMapDosage_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            BaseDosagePM basePM = createTestBaseDosagePM();
            Dosage newDosage = createTestDosage();
            Dosage savedDosage = createTestDosage();
            DosagePresentationModel expectedPM = createTestDosagePM();

            when(dosageMapper.mapPMToEntity(any(BaseDosagePM.class))).thenReturn(newDosage);
            when(dosageRepository.save(any(Dosage.class))).thenReturn(savedDosage);
            when(dosageMapper.mapEntityToPM(any(Dosage.class))).thenReturn(expectedPM);

            // Act
            DosagePresentationModel result = dosageService.createAndMapDosage(basePM);

            // Assert
            assertThat(result).isNotNull();
            verify(dosageMapper).mapPMToEntity(basePM);
            verify(dosageRepository).save(newDosage);
            verify(dosageMapper).mapEntityToPM(savedDosage);
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException if mapper fails")
        void createAndMapDosage_shouldThrowException_whenMapperFails() throws InvalidFrequencyException {
            // Arrange
            BaseDosagePM basePM = createTestBaseDosagePM();
            when(dosageMapper.mapPMToEntity(any(BaseDosagePM.class))).thenThrow(new InvalidFrequencyException("Invalid frequency"));

            // Act & Assert
            assertThrows(InvalidFrequencyException.class, () -> dosageService.createAndMapDosage(basePM));
            verify(dosageRepository, never()).save(any(Dosage.class));
        }
    }

    @Nested
    @DisplayName("Tests for createDosageEntity()")
    class CreateDosageEntityTests {
        @Test
        @DisplayName("should create and return a Dosage entity on success")
        void createDosageEntity_shouldSucceed() throws InvalidFrequencyException {
            // Arrange
            BaseDosagePM basePM = createTestBaseDosagePM();
            Dosage newDosage = createTestDosage();
            Dosage savedDosage = createTestDosage();

            when(dosageMapper.mapPMToEntity(any(BaseDosagePM.class))).thenReturn(newDosage);
            when(dosageRepository.save(any(Dosage.class))).thenReturn(savedDosage);

            // Act
            Dosage result = dosageService.createDosageEntity(basePM);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(savedDosage);
            verify(dosageMapper).mapPMToEntity(basePM);
            verify(dosageRepository).save(newDosage);
        }
    }

    @Nested
    @DisplayName("Tests for findDosage()")
    class FindDosageTests {
        @Test
        @DisplayName("should find a dosage and map it to a presentation model on success")
        void findDosage_shouldSucceed() {
            // Arrange
            Long id = 1L;
            Dosage foundDosage = createTestDosage();
            DosagePresentationModel expectedPM = createTestDosagePM();

            when(dosageRepository.findById(anyLong())).thenReturn(Optional.of(foundDosage));
            when(dosageMapper.mapEntityToPM(any(Dosage.class))).thenReturn(expectedPM);

            // Act
            DosagePresentationModel result = dosageService.findDosage(id);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(expectedPM);
            verify(dosageRepository).findById(id);
            verify(dosageMapper).mapEntityToPM(foundDosage);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException if dosage is not found")
        void findDosage_shouldThrowException_whenNotFound() {
            // Arrange
            Long id = 99L;
            when(dosageRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> dosageService.findDosage(id))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Dosage with id " + id + " could not be found.");

            verify(dosageRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests for findDosageEntity()")
    class FindDosageEntityTests {
        @Test
        @DisplayName("should find and return a Dosage entity on success")
        void findDosageEntity_shouldSucceed() {
            // Arrange
            Long id = 1L;
            Dosage expectedDosage = createTestDosage();

            when(dosageRepository.findById(anyLong())).thenReturn(Optional.of(expectedDosage));

            // Act
            Dosage result = dosageService.findDosageEntity(id);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(expectedDosage);
            verify(dosageRepository).findById(id);
        }

        @Test
        @DisplayName("should throw EntityNotFoundException if dosage entity is not found")
        void findDosageEntity_shouldThrowException_whenNotFound() {
            // Arrange
            Long id = 99L;
            when(dosageRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> dosageService.findDosageEntity(id))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Dosage with id " + id + " could not be found.");

            verify(dosageRepository).findById(id);
        }
    }
}
