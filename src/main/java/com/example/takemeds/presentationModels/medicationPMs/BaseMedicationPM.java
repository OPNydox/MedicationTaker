package com.example.takemeds.presentationModels.medicationPMs;

import com.example.takemeds.presentationModels.dosagePMs.DosagePresentationModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMedicationPM {
    private long id;

    @NotBlank(message = "Medication name is required.")
    @Size(min = 1, max = 100, message = "Medication name must be between 1 and 100 characters")
    private String name;

    //TO DO make field more complex Structure the data into separate fields (e.g., "Uses," "Side Effects," "Dosage") instead of one long string.
    @Size(max = 500, message = "Medication description cannot exceed 500 symbols")
    private String description;

    private DosagePresentationModel defaultDosage;
}
