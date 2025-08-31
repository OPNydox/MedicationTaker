package com.example.takemeds.entities;

import com.example.takemeds.entities.enums.Frequency;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "dosages")
public class Dosage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TO DO remove this field
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medication_id", referencedColumnName = "medication_id")
    private Medication medication;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private LocalDateTime scheduledTime;

    @Nullable
    @Positive(message = "timesperday must be a positive number.")
    @Max(value = 126, message = "timesperday must be at most 126")
    private byte timesPerDay;

    @ElementCollection
    @CollectionTable(name = "medication_times", joinColumns = @JoinColumn(name = "medication_id"))
    @Column(name = "time")
    private List<LocalTime> timesToTake;
}
