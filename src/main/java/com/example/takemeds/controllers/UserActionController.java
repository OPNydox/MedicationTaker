package com.example.takemeds.controllers;

import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.presentationModels.MedicationPresentationModel;
import com.example.takemeds.services.MedicationLogService;
import com.example.takemeds.services.MedicationService;
import com.example.takemeds.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/med/user")
public class UserActionController {

    private final MedicationLogService medicationLogService;

    private final MedicationService medicationService;

    private final UserService userService;

    public UserActionController(MedicationLogService medicationLogService, MedicationService medicationService, UserService userService) {
        this.medicationLogService = medicationLogService;
        this.medicationService = medicationService;
        this.userService = userService;
    }

    @PostMapping("/take/{id}")
    private ResponseEntity<MedLogPresentationModel> takeMedication(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable Long id) {
        MedLogPresentationModel logPresentationModel = this.medicationLogService.takeMedication(userDetails.getUsername(), id);
        return new ResponseEntity<>(logPresentationModel, HttpStatus.OK);
    }

    @PostMapping("/assign/medication/{id}")
    private ResponseEntity<MedicationPresentationModel> assignMedication(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        MedicationPresentationModel medication = medicationService.selfAssignMedication(userDetails, id);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    @PostMapping("/show/my/medication/")
    private ResponseEntity<List<MedicationPresentationModel>> showMyMedication(@AuthenticationPrincipal UserDetails userDetails) {
        List<MedicationPresentationModel> medications = userService.showMedicationForUser(userDetails.getUsername());
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @PutMapping("/edit/my/medication")
    private ResponseEntity<MedicationPresentationModel> editMyMedication(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MedicationPresentationModel medication) {
        MedicationPresentationModel result = medicationService.editMedication(userDetails, medication);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
