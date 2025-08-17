package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.medicationPMs.MedicationPresentationModel;
import com.example.takemeds.services.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/med/medication")
public class MedicationController {
    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<MedicationPresentationModel> createMedication(@RequestBody @Valid MedicationPresentationModel medicationPM) throws InvalidFrequencyException {
       MedicationPresentationModel createdMedication = medicationService.createMedication(medicationPM);
       return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }
}
