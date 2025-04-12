package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidDosageException;
import com.example.takemeds.presentationModels.DosagePresentationModel;
import com.example.takemeds.services.DosageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/med/dosage")
public class DosageController {

    private DosageService dosageService;

    public DosageController(DosageService dosageService) {
        this.dosageService = dosageService;
    }

    @PostMapping("/create")
    public ResponseEntity<DosagePresentationModel> createDosage(@RequestBody @Valid DosagePresentationModel input) throws InvalidDosageException {
        DosagePresentationModel createdDosage = dosageService.createDosagePM(input);
        return new ResponseEntity<>(createdDosage, HttpStatus.OK);
    }
}
