package com.example.takemeds.services;

import com.example.takemeds.entities.Receipt;
import com.example.takemeds.exceptions.FinalizedReceiptException;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.repositories.ReceiptRepository;
import com.example.takemeds.utils.mappers.ReceiptMapper;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    private final MedicationScheduleMapper medicationScheduleMapper;

    public ReceiptService(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper, MedicationScheduleMapper medicationScheduleMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.medicationScheduleMapper = medicationScheduleMapper;
    }

    /**
     * Creates a new receipt.
     *
     * @param presentationModel input model.
     * @return The created Receipt entity.
     */
    @Transactional
    public ReceiptPresentationModel createReceipt(ReceiptPresentationModel presentationModel) throws InvalidFrequencyException {
        Receipt receipt = receiptMapper.toEntity(presentationModel);

        return receiptMapper.toPresentationModel(receiptRepository.save(receipt));
    }

    /**
     * Retrieves a receipt by its ID.
     *
     * @param id The ID of the receipt to retrieve.
     * @return An Optional containing the Receipt if found, or empty if not.
     */
    public ReceiptPresentationModel getReceiptById(Long id) {
        Optional<Receipt> foundReceipt = receiptRepository.findById(id);

        if (foundReceipt.isEmpty()) {
            throw new EntityNotFoundException("Receipt with id " + id + " does not exist");
        }
        return receiptMapper.toPresentationModel(foundReceipt.get());
    }

    /**
     * Retrieves all receipts.
     *
     * @return A list of all Receipt entities.
     */
    public List<ReceiptPresentationModel> getAllReceipts() {
        List<Receipt> foundReceipts =  receiptRepository.findAll();

        return receiptMapper.toPresentationModelList(foundReceipts);
    }

    /**
     * Adds a UserMedication to an existing receipt.
     *
     * @param receiptId The ID of the receipt to add the medication to.
     * @param userMedication The UserMedication to add.
     * @return The updated Receipt entity.
     * @throws FinalizedReceiptException If the receipt is already finalized.
     * @throws IllegalArgumentException If the receipt with the given ID is not found.
     */
    @Transactional
    public ReceiptPresentationModel addUserMedicationToReceipt(Long receiptId, MedicationSchedulePM userMedication) throws FinalizedReceiptException, InvalidFrequencyException {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Receipt with ID " + receiptId + " not found."));

        receipt.AddUserMedication(medicationScheduleMapper.toUserMedicationEntity(userMedication));
        return receiptMapper.toPresentationModel(receiptRepository.save(receipt));
    }

    /**
     * Finalizes a receipt, preventing further modifications to its user medications.
     *
     * @param receiptId The ID of the receipt to finalize.
     * @return The finalized Receipt entity.
     * @throws IllegalArgumentException If the receipt with the given ID is not found.
     */
    @Transactional
    public Receipt finalizeReceipt(Long receiptId) {
        Receipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Receipt with ID " + receiptId + " not found."));

        receipt.setFinalized(true);
        return receiptRepository.save(receipt);
    }

    /**
     * Deletes a receipt by its ID.
     *
     * @param id The ID of the receipt to delete.
     */
    @Transactional
    public void deleteReceipt(Long id) {
        receiptRepository.deleteById(id);
    }

    /**
     * Finds receipts that are not finalized.
     *
     * @return A list of non-finalized Receipt entities.
     */
    public List<Receipt> findNonFinalizedReceipts() {
        // Assuming you add a custom method to your ReceiptRepository:
        // List<Receipt> findByIsFinalizedFalse();
        return receiptRepository.findByIsFinalizedFalse();
    }
}
