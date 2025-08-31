package com.example.takemeds.services;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.User;
import com.example.takemeds.exceptions.UnauthorizedAccessException;
import com.example.takemeds.repositories.MedicationScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MedicationScheduleReadService {
    private final UserService userService;

    private final MedicationScheduleRepository medicationScheduleRepository;

    public MedicationScheduleReadService(UserService userService, MedicationScheduleRepository medicationScheduleRepository) {
        this.userService = userService;
        this.medicationScheduleRepository = medicationScheduleRepository;
    }

    protected List<MedicationSchedule> findNonFinishedUserMedicationBy(Long userId) {
        return medicationScheduleRepository.findByUser_IdAndIsFinishedFalse(userId);
    }

    protected MedicationSchedule findMedicationScheduleById(Long id) {
        Optional<MedicationSchedule> userMedication = medicationScheduleRepository.findById(id);

        if (userMedication.isEmpty()) {
            throw new EntityNotFoundException("Medication assignment with id " + id + " does not exist");
        }

        return userMedication.get();
    }

    protected MedicationSchedule validateAndGetSchedule(Long scheduleId, UserDetails userDetails) throws UnauthorizedAccessException {
        User user = userService.getUser(userDetails.getUsername());
        MedicationSchedule schedule = findMedicationScheduleById(scheduleId);

        if (!Objects.equals(schedule.getUser().getId(), user.getId())) {
            throw new UnauthorizedAccessException("You are not allowed to alter this resource.");
        }

        if (schedule.getReceipt() != null) {
            throw new UnauthorizedAccessException("Cannot edit a medication schedule that has been recorded by a receipt.");
        }

        if (schedule.isFinished()) {
            throw new UnauthorizedAccessException("Finished schedule cannot be altered.");
        }

        return schedule;
    }
}
