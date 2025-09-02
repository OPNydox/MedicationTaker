package com.example.takemeds.repositories;

import com.example.takemeds.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.medications m
       LEFT JOIN FETCH m.baseDosages
       WHERE u.email = :email
       """)
    Optional<User> findWithMedicationsByEmail(String email);

}
