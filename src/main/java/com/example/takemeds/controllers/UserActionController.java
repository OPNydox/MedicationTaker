package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.MedLogPresentationModel;
import com.example.takemeds.presentationModels.medicationPMs.BaseMedicationPM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.MedicationDosageRefPM;
import com.example.takemeds.services.MedicationLogService;
import com.example.takemeds.services.MedicationService;
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

    private final UserActionService userActionService;

    private final MedicationService medicationService;

    private final UserService userService;

    public UserActionController(MedicationLogService medicationLogService, UserActionService userActionService, MedicationService medicationService, UserService userService) {
        this.medicationLogService = medicationLogService;
        this.userActionService = userActionService;
        this.medicationService = medicationService;
        this.userService = userService;
    }

    @PostMapping("/take/{id}")
    public ResponseEntity<MedLogPresentationModel> takeMedication(@AuthenticationPrincipal UserDetails userDetails,  @PathVariable Long id) {
        MedLogPresentationModel logPresentationModel = this.medicationLogService.takeMedication(userDetails.getUsername(), id);
        return new ResponseEntity<>(logPresentationModel, HttpStatus.OK);
    }

    @PostMapping("/assign/medication/{id}")
    public ResponseEntity<MedicationDosagePM> assignMedication(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        //MedicationDosagePM medication = medicationService.selfAssignMedication(userDetails, id);
        return null;
    }

    @PostMapping("/show/my/medication/")
    public ResponseEntity<List<MedicationDosagePM>> showMyMedication(@AuthenticationPrincipal UserDetails userDetails) {
        List<MedicationDosagePM> medications = userActionService.showMyMedication(userDetails.getUsername());
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @PutMapping("/edit/my/medication/{id}")
    public ResponseEntity<MedicationDosagePM> editMyMedication(@PathVariable Long id, @RequestBody MedicationDosagePM medication, @AuthenticationPrincipal UserDetails userDetails) {
        MedicationDosagePM result = userActionService.editMedication(id, medication, userDetails);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/show/my/medication")
    public ResponseEntity<List<MedicationDosagePM>> showMyMedications(@AuthenticationPrincipal UserDetails userDetails) {
        List<MedicationDosagePM> result = userActionService.showMyMedication(userDetails.getUsername());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<MedicationDosagePM> createMedication(@RequestBody @Valid BaseMedicationPM medicationPM, @AuthenticationPrincipal UserDetails userDetails) {
        MedicationDosagePM createdMedication = userActionService.createMedication(medicationPM, userDetails);
        return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }

    @PostMapping("/create/with-dosage")
    public ResponseEntity<MedicationDosagePM> createMedicationWithDosage(@RequestBody @Valid MedicationDosagePM medication, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException {
        MedicationDosagePM createdMedication = userActionService.createMedicationWithDosage(medication, userDetails);
        return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }

    @PostMapping("/create/with-dosage-ref")
    public ResponseEntity<MedicationDosagePM> createMedicationWithDosageRef(@RequestBody @Valid MedicationDosageRefPM medication, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException {
        MedicationDosagePM createdMedication = userActionService.createMedicationWithDosageRef(medication, userDetails);
        return new ResponseEntity<>(createdMedication, HttpStatus.OK);
    }

    @DeleteMapping("/delete/medication/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        userActionService.deleteMedication(id, userDetails);

        return ResponseEntity.noContent().build();
    }

}
