package com.example.takemeds.controllers;

import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.presentationModels.MedicationPresentationModel;
import com.example.takemeds.services.MedicationLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/med/medication")
public class UserActionController {

    private final MedicationLogService medicationLogService;

    public UserActionController(MedicationLogService medicationLogService) {
        this.medicationLogService = medicationLogService;
    }

    @PostMapping("/take/{id}")
    private ResponseEntity<MedLogPresentationModel> takeMedication(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable Long id) {
        MedLogPresentationModel logPresentationModel = this.medicationLogService.takeMedication(userDetails.getUsername(), id);
        return new ResponseEntity<>(logPresentationModel, HttpStatus.OK);
    }
}
