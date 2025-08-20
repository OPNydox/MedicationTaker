package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosageRefPM;
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
    public ResponseEntity<MedicationDosagePM> createMedication(@RequestBody @Valid BaseMedicationPM medicationPM) {
       MedicationDosagePM createdMedication = medicationService.createMedication(medicationPM);
       return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }

    @PostMapping("/create/with-dosage")
    public ResponseEntity<MedicationDosagePM> createMedicationWithDosage(@RequestBody @Valid MedicationDosagePM medication) throws InvalidFrequencyException {
        MedicationDosagePM createdMedication = medicationService.createMedication(medication);
        return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }

    @PostMapping("/create/with-dosage-reference")
    public ResponseEntity<MedicationDosagePM> createMedicationWithDosageRef(@RequestBody @Valid MedicationDosageRefPM medication) throws InvalidFrequencyException {
        MedicationDosagePM createdMedication = medicationService.createMedication(medication);
        return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }
}
