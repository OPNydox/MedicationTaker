package com.example.takemeds.controllers;

import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.services.MedicationLogService;
import com.example.takemeds.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/med/user")
public class UserActionController {

    private final MedicationLogService medicationLogService;

    private final UserService userService;

    public UserActionController(MedicationLogService medicationLogService, UserService userService) {
        this.medicationLogService = medicationLogService;
        this.userService = userService;
    }

    @PostMapping("/take/{id}")
    public ResponseEntity<MedLogPresentationModel> takeMedication(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable Long id) {
        MedLogPresentationModel logPresentationModel = this.medicationLogService.takeMedication(userDetails.getUsername(), id);
        return new ResponseEntity<>(logPresentationModel, HttpStatus.OK);
    }

    @PostMapping("/show/my/medication")
    public ResponseEntity<List<MedicationView>> showMyMedication(@AuthenticationPrincipal UserDetails userDetails) {
        List<MedicationView> medications = userService.showMyMedication(userDetails);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }
}
