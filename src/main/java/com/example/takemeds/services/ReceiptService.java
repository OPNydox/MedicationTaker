package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.Receipt;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.FinalizedReceiptException;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.exceptions.InvalidRequestException;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.CreateMedicationScheduleRequest;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.repositories.ReceiptRepository;
import com.example.takemeds.utils.mappers.ReceiptMapper;
import com.example.takemeds.utils.mappers.MedicationScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptMapper receiptMapper;

    private final UserService userService;

    private final MedicationScheduleManagementService scheduleManagementService;

    private final MedicationScheduleMapper medicationScheduleMapper;

    public ReceiptService(ReceiptRepository receiptRepository, ReceiptMapper receiptMapper, UserService userService, MedicationScheduleManagementService scheduleManagementService, MedicationScheduleMapper medicationScheduleMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.userService = userService;
        this.scheduleManagementService = scheduleManagementService;
        this.medicationScheduleMapper = medicationScheduleMapper;
    }

    /**
     * Creates a new receipt.
     *
     * @param presentationModel input model.
     * @return The created Receipt entity.
     */
    @Transactional
    public ReceiptPresentationModel createReceipt(ReceiptPresentationModel presentationModel, UserDetails userDetails) throws InvalidFrequencyException, InvalidRequestException {
        Receipt receipt = receiptMapper.toEntity(presentationModel);

        User doctor = userService.getUser(userDetails.getUsername());
        User patient = userService.getUserEntityById(presentationModel.getPatientId());

        receipt.setDoctor(doctor);
        receipt.setPatient(patient);

        List<MedicationSchedule> schedules = scheduleManagementService.createScheduleEntitiesFromRequests(presentationModel.getUserMedications(), patient);

        schedules.forEach(schedule -> schedule.setReceipt(receipt));

        return receiptMapper.toPresentationModel(receiptRepository.save(receipt));
    }

    /**
     * Retrieves a receipt by its ID.
     *
     * @param id The ID of the receipt to retrieve.
     * @return A presentation model of the found receipt
     */
    public ReceiptPresentationModel getReceiptById(Long id) {
        return receiptMapper.toPresentationModel(findReceiptEntity(id));
    }

    protected Receipt findReceiptEntity(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new IllegalArgumentException("Receipt with id " + receiptId + " does not exist"));

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
    public ReceiptPresentationModel addUserMedicationToReceipt(Long receiptId, CreateMedicationScheduleRequest userMedication) throws FinalizedReceiptException, InvalidFrequencyException {
        Receipt receipt = findReceiptEntity(receiptId);

        //receipt.AddUserMedication(scheduleManagementService.createMedicationSchedule(userMedication, receipt.getUser()));
        return receiptMapper.toPresentationModel(receiptRepository.save(receipt));
    }

    private List<MedicationSchedule> prepareSchedules(Receipt receipt) {
        List<MedicationSchedule> schedules = new ArrayList<>();

        for (MedicationSchedule medicationSchedule : receipt.getMedicationSchedules()) {
            medicationSchedule.setUser(receipt.getPatient());
            medicationSchedule.setReceipt(receipt);
            schedules.add(medicationSchedule);
        }

        return schedules;
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
