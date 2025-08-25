package com.example.takemeds.presentationModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MedLogPresentationModel {
    String medicationName;

    String username;

    LocalDateTime timeTaken;

    public MedLogPresentationModel() {
    }

    public MedLogPresentationModel(String medicationName, String username, LocalDateTime timeTaken) {
        this.medicationName = medicationName;
        this.username = username;
        this.timeTaken = timeTaken;
    }
}
