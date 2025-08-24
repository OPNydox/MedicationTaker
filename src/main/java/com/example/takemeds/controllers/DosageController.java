package com.example.takemeds.controllers;

import com.example.takemeds.entities.Dosage;
import com.example.takemeds.exceptions.InvalidDosageException;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import com.example.takemeds.services.DosageService;
import com.example.takemeds.services.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/med/dosage")
public class DosageController {

    private final DosageService dosageService;

    private final MedicationService medicationService;

    public DosageController(DosageService dosageService, MedicationService medicationService) {
        this.dosageService = dosageService;
        this.medicationService = medicationService;
    }

    @PostMapping("/create")
    public ResponseEntity<DosagePresentationModel> createDosage(@RequestBody @Valid DosagePresentationModel dosagePM) throws InvalidDosageException, InvalidFrequencyException {
        DosagePresentationModel createdDosage = dosageService.createAndMapDosage(dosagePM);
        return new ResponseEntity<>(createdDosage, HttpStatus.OK);
    }

    @PostMapping("/createWithMedication")
    public ResponseEntity<DosagePresentationModel> createDosageWithMedication(@RequestBody @Valid CreateDosagePM dosagePM) throws InvalidDosageException, InvalidFrequencyException {
        //Dosage createdDosage = dosageService.createDosageEntity(dosagePM);
//
        //medicationService.addDefaultDosageToMedication(createdDosage, dosagePM.getMedicationId());
//
        //DosagePresentationModel resultDosage = dosageService.findDosage(createdDosage.getId());
//
        //return new ResponseEntity<>(resultDosage, HttpStatus.OK);

        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
}
