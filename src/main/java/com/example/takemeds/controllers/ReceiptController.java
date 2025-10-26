package com.example.takemeds.controllers;

import com.example.takemeds.exceptions.FinalizedReceiptException;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.services.ReceiptService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/med/receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/doc/create")
    public ResponseEntity<ReceiptPresentationModel> createReceipt(@RequestBody @Valid ReceiptPresentationModel receipt, @AuthenticationPrincipal UserDetails userDetails) throws InvalidFrequencyException, InvalidRequestException {
        ReceiptPresentationModel newReceipt = receiptService.createReceipt(receipt, userDetails);

        return new ResponseEntity<>(newReceipt, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptPresentationModel> getReceipt(@PathVariable Long id) {
        ReceiptPresentationModel foundReceipt = receiptService.getReceiptById(id);


        return new ResponseEntity<>(foundReceipt, HttpStatus.OK);
    }

    @GetMapping("/allReceipts")
    public ResponseEntity<List<ReceiptPresentationModel>> getAllReceipts() {
        List<ReceiptPresentationModel> foundReceipts = receiptService.getAllReceipts();

        return new ResponseEntity<>(foundReceipts, HttpStatus.OK);
    }

    @PostMapping("/add/med/{id}")
    public ResponseEntity<ReceiptPresentationModel> addMedicineToReceipt(@PathVariable Long id,
                                                                         @RequestBody @Valid MedicationSchedulePM userMedication) throws FinalizedReceiptException, InvalidFrequencyException {
        //ReceiptPresentationModel editedReceipt = receiptService.addUserMedicationToReceipt(id, userMedication);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
