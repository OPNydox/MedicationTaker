package com.example.takemeds.repositories;

import com.example.takemeds.entities.UserMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMedicationRepository extends JpaRepository<UserMedication, Long> {

    List<UserMedication> findByUser_IdAndIsFinishedFalse(Long userId);
}
