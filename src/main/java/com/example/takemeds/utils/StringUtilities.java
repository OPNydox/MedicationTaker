package com.example.takemeds.utils;

import com.example.takemeds.entities.enums.Frequency;

public class StringUtilities {

    public static Frequency frequencyFromString(String value) throws IllegalArgumentException {
        return Frequency.valueOf(value.toUpperCase().trim().replace(" ", "_"));
    }
}
