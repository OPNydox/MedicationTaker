package com.example.takemeds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationTakenLogRepository extends JpaRepository<MedicationTakenLogRepository, Long> {
}
