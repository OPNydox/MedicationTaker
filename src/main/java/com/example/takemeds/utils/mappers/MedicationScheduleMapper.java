package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import com.example.takemeds.presentationModels.medicationPMs.CreateMedicationDto;
import com.example.takemeds.presentationModels.medicationPMs.MedicationView;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationSchedulePM;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleView;
import com.example.takemeds.presentationModels.medicationSchedulesPMs.MedicationScheduleWithIdsPM;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicationScheduleMapper {

    private final MedicationMapper medicationMapper;
    private final DosageMapper dosageMapper;

    public MedicationScheduleMapper(MedicationMapper medicationMapper, DosageMapper dosageMapper) {
        this.medicationMapper = medicationMapper;
        this.dosageMapper = dosageMapper;
    }

    /**
     * Maps a MedicationSchedule entity to a basic presentation model with IDs.
     * @param entity The MedicationSchedule entity.
     * @return A populated MedicationScheduleWithIdsPM.
     */
    public MedicationScheduleWithIdsPM toMedicationScheduleWithIdsPM(MedicationSchedule entity) {
        if (entity == null) {
            return null;
        }

        return MedicationScheduleWithIdsPM.builder()
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .medicationId(entity.getMedication().getId())
                .userID(entity.getUser().getId())
                .build();
    }

    /**
     * Maps a MedicationSchedule entity to a presentation model with nested medication details.
     * @param entity The MedicationSchedule entity.
     * @return A populated MedicationSchedulePM.
     */
    public MedicationSchedulePM toMedicationSchedulePM(MedicationSchedule entity) {
        if (entity == null) {
            return null;
        }

        MedicationView medicationPM = medicationMapper.mapEntityToPM(entity.getMedication());

        return MedicationSchedulePM.builder()
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .userID(entity.getUser().getId())
                .medication(medicationPM)
                .build();
    }

    /**
     * Maps a MedicationSchedule entity to a detailed view presentation model.
     * @param entity The MedicationSchedule entity.
     * @return A populated MedicationScheduleView.
     */
    public MedicationScheduleView toMedicationScheduleView(MedicationSchedule entity) {
        if (entity == null) {
            return null;
        }

        MedicationSchedulePM medicationSchedulePM = toMedicationSchedulePM(entity);
        BaseDosagePM dosagePM = dosageMapper.mapEntityToPM(entity.getDosage());


        return MedicationScheduleView.builder()
                .id(entity.getId())
                .startDate(medicationSchedulePM.getStartDate())
                .endDate(medicationSchedulePM.getEndDate())
                .userID(medicationSchedulePM.getUserID())
                .medication(medicationSchedulePM.getMedication())
                .dosage(dosagePM)
                .isFinished(entity.isFinished())
                .build();
    }

    /**
     * Maps a list of MedicationSchedule entities to a list of MedicationScheduleViews.
     * @param entities A list of MedicationSchedule entities.
     * @return A list of MedicationScheduleViews.
     */
    public List<MedicationScheduleView> toMedicationScheduleViewList(List<MedicationSchedule> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toMedicationScheduleView)
                .collect(Collectors.toList());
    }

    /**
     * Maps a MedicationScheduleWithIdsPM to a MedicationSchedule entity.
     * This is a reverse mapper, primarily used for creation/updates.
     * Note: This method does NOT fetch related entities, that should be done in the service layer.
     * @param pm The presentation model to map.
     * @return A populated MedicationSchedule entity (with IDs, not full objects).
     */
    public MedicationSchedule toEntity(MedicationScheduleWithIdsPM pm) {
        if (pm == null) {
            return null;
        }
        return MedicationSchedule.builder()
                .startDate(pm.getStartDate())
                .endDate(pm.getEndDate())
                .build();
    }
}
