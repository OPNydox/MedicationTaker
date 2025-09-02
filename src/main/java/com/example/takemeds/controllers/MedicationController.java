package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.services.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<MedicationView> createMedication(@RequestBody @Valid CreateMedicationDto medicationPM, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException {
        MedicationView createdMedication = medicationService.createMedication(medicationPM, userDetails);
        return new ResponseEntity<>(createdMedication, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        medicationService.deleteMedication(id, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/my/medication/{id}")
    public ResponseEntity<MedicationView> editMyMedication(@PathVariable Long id, @RequestBody CreateMedicationDto medication, @AuthenticationPrincipal UserDetails userDetails) {
        MedicationView result = medicationService.editMedication(id, medication, userDetails);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/set-default-dosage")
    public ResponseEntity<MedicationView> setDefaultDosage(@RequestBody @Valid CreateDosagePM dosagePM, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException {
        MedicationView updatedMedication = medicationService.setDefaultDosage(dosagePM, userDetails);
        return new ResponseEntity<>(updatedMedication, HttpStatus.OK);
    }

    @PutMapping("{medicationId}/set-default-dosage/{dosageId}")
    public ResponseEntity<MedicationView> setDefaultDosageWithId(@PathVariable Long medicationId, @PathVariable Long dosageId, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException {
        MedicationView updatedMedication = medicationService.setDefaultDosage(medicationId, dosageId, userDetails);
        return new ResponseEntity<>(updatedMedication, HttpStatus.OK);
    }
}
