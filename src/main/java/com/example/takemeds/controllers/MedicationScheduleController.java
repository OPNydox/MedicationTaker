package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.CreateMedicationScheduleRequest;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleWithIdsPM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.UpdateDateDto;
import com.example.takemeds.services.MedicationScheduleManagementService;
import com.example.takemeds.services.MedicationScheduleUpdateService;
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
@RequestMapping("/api/med/schedule")
public class MedicationScheduleController {
    private final MedicationScheduleManagementService managementService;

    private final MedicationScheduleUpdateService updateService;

    public MedicationScheduleController(MedicationScheduleManagementService managementService, MedicationScheduleUpdateService updateService) {
        this.managementService = managementService;
        this.updateService = updateService;
    }

    @PostMapping("/create/")
    public ResponseEntity<MedicationScheduleView> createMedicationSchedule(@RequestBody @Valid CreateMedicationScheduleRequest scheduleRequestPM, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException, InvalidRequestException {
        MedicationScheduleView scheduleView = managementService.createMedicationSchedule(scheduleRequestPM, userDetails);
        return new ResponseEntity<>(scheduleView, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MedicationScheduleView> deleteMedicationSchedule(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        managementService.deleteMedicationSchedule(id, userDetails);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/swap-medication/schedule/{scheduleId}/medication/{medicationId}")
    public ResponseEntity<MedicationScheduleView> scheduleSwapMedication(@PathVariable Long scheduleId, @PathVariable Long medicationId, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationScheduleView scheduleView = updateService.swapMedication(scheduleId, medicationId, userDetails);
        return new ResponseEntity<>(scheduleView, HttpStatus.CREATED);
    }

    @PutMapping("/edit-dosage/schedule/{id}")
    public ResponseEntity<MedicationScheduleView> scheduleEditDosage(@PathVariable Long scheduleId, @RequestBody @Valid BaseDosagePM dosagePM, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException, InvalidFrequencyException {
        MedicationScheduleView scheduleView = updateService.editDosage(scheduleId, dosagePM, userDetails);
        return new ResponseEntity<>(scheduleView, HttpStatus.CREATED);
    }

    @PutMapping("/update-dates/{id}")
    public ResponseEntity<MedicationScheduleView> updateMedicationScheduleDates(@PathVariable Long id, @RequestBody UpdateDateDto updateDto, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationScheduleView updatedSchedule = updateService.updateDates(id, updateDto, userDetails);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<MedicationScheduleView> finishMedicationSchedule(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws UnauthorizedAccessException {
        MedicationScheduleView updatedSchedule = updateService.finishMedicationSchedule(id, userDetails);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }
}
