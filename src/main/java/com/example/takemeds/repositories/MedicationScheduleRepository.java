package com.example.takemeds.repositories;

import com.example.takemeds.entities.MedicationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {

    List<MedicationSchedule> findByUser_IdAndIsFinishedFalse(Long userId);
}
