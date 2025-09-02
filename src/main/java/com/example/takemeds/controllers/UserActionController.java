package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.presentationModels.dosagePMs.CreateDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.services.MedicationLogService;
import com.example.takemeds.services.UserActionService;
import com.example.takemeds.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final UserService userService;

    private final UserActionService userActionService;

    public UserActionController(MedicationLogService medicationLogService, UserService userService, UserActionService userActionService) {
        this.medicationLogService = medicationLogService;
        this.userService = userService;
        this.userActionService = userActionService;
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
