package com.example.takemeds.utils;

import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import org.springframework.stereotype.Component;

@Component
public class StringUtilities {

    public Frequency frequencyFromString(String value) throws InvalidFrequencyException {
        if (checkIfStringIsNullOrEmpty(value)){
            throwFrequencyException(value);
        }
        try {
            return Frequency.valueOf(value.toUpperCase().trim().replace(" ", "_"));
        } catch (IllegalArgumentException exception) {
            throwFrequencyException(value);
        }
        return null;
    }

    public boolean checkIfStringIsNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }

    private void throwFrequencyException(String value) throws InvalidFrequencyException {
        throw new InvalidFrequencyException("Frequency type: " + value + " does not exist.");
    }
}
