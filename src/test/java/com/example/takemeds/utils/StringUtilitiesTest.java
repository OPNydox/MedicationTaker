package com.example.takemeds.utils;

import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringUtilitiesTest {
    private final StringUtilities stringUtilities = new StringUtilities();

    @Nested
    @DisplayName("Tests for frequencyFromString()")
    class FrequencyFromStringTests {

        @Test
        @DisplayName("should correctly convert a valid string to a Frequency enum")
        void frequencyFromString_withValidString_shouldReturnCorrectEnum() throws InvalidFrequencyException {
            // Arrange
            String dailyString = "DAILY";
            String weeklyString = "WEEKLY";

            // Act
            Frequency dailyFrequency = stringUtilities.frequencyFromString(dailyString);
            Frequency weeklyFrequency = stringUtilities.frequencyFromString(weeklyString);

            // Assert
            assertThat(dailyFrequency).isEqualTo(Frequency.DAILY);
            assertThat(weeklyFrequency).isEqualTo(Frequency.WEEKLY);
        }

        @Test
        @DisplayName("should correctly handle different casing and whitespace")
        void frequencyFromString_withVariations_shouldReturnCorrectEnum() throws InvalidFrequencyException {
            // Arrange
            String asNeededLowerCase = "as_needed";
            String monthlyWithSpaces = " monthly ";

            // Act
            Frequency asNeededFrequency = stringUtilities.frequencyFromString(asNeededLowerCase);
            Frequency monthlyFrequency = stringUtilities.frequencyFromString(monthlyWithSpaces);

            // Assert
            assertThat(asNeededFrequency).isEqualTo(Frequency.AS_NEEDED);
            assertThat(monthlyFrequency).isEqualTo(Frequency.MONTHLY);
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException for an invalid string")
        void frequencyFromString_withInvalidString_shouldThrowException() {
            // Arrange
            String invalidString = "INVALID_TYPE";

            // Act & Assert
            InvalidFrequencyException thrown = assertThrows(InvalidFrequencyException.class, () -> {
                stringUtilities.frequencyFromString(invalidString);
            });

            assertThat(thrown.getMessage()).isEqualTo("Frequency type: " + invalidString + " does not exist.");
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException for a null string")
        void frequencyFromString_withNullString_shouldThrowException() {
            // Arrange
            String nullString = null;

            // Act & Assert
            InvalidFrequencyException thrown = assertThrows(InvalidFrequencyException.class, () -> {
                stringUtilities.frequencyFromString(nullString);
            });

            assertThat(thrown.getMessage()).contains("does not exist");
        }

        @Test
        @DisplayName("should throw InvalidFrequencyException for a blank string")
        void frequencyFromString_withBlankString_shouldThrowException() {
            // Arrange
            String blankString = " ";

            // Act & Assert
            InvalidFrequencyException thrown = assertThrows(InvalidFrequencyException.class, () -> {
                stringUtilities.frequencyFromString(blankString);
            });

            assertThat(thrown.getMessage()).contains("does not exist");
        }
    }

}
