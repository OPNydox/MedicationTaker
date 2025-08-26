package com.example.takemeds.entities;

import com.example.takemeds.exceptions.FinalizedReceiptException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime receiptTime;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "receipt",
                cascade = CascadeType.ALL,
                orphanRemoval = true,
                fetch = FetchType.LAZY)
    private List<MedicationSchedule> medicationSchedules = new ArrayList<>();

    @Column
    boolean isFinalized;

    public void AddUserMedication(MedicationSchedule medicationSchedule) throws FinalizedReceiptException {
        if (isFinalized) {
            throw new FinalizedReceiptException("Cannot add medication to finalized receipt.");
        }

        this.medicationSchedules.add(medicationSchedule);
    }
}
