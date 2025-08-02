package com.example.takemeds.utils;

import com.example.takemeds.entities.enums.Frequency;
import com.example.takemeds.exceptions.InvalidFrequencyException;

public class StringUtilities {

    public static Frequency frequencyFromString(String value) throws InvalidFrequencyException {
        try {
            return Frequency.valueOf(value.toUpperCase().trim().replace(" ", "_"));
        } catch (IllegalArgumentException exception) {
            throw new InvalidFrequencyException("Frequency type: " + value + " does not exist.");
        }
    }
}
