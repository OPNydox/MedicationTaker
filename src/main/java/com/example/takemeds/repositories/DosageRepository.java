package com.example.takemeds.repositories;

import com.example.takemeds.entities.Dosage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DosageRepository extends JpaRepository<Dosage, Long> {
}
