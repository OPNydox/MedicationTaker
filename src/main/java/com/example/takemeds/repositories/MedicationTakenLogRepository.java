package com.example.takemeds.repositories;

import com.example.takemeds.entities.MedicationTakenLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationTakenLogRepository extends JpaRepository<MedicationTakenLog, Long> {

    List<MedicationTakenLog> findByMedicationScheduleId(Long scheduleId);
}
