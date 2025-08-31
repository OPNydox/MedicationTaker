package com.example.takemeds.presentationModels.medicationPMs;

import com.example.takemeds.presentationModels.dosagePMs.BaseDosagePM;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateMedicationDto {
    @NotBlank(message = "Medication name is required.")
    @Size(min = 1, max = 100, message = "Medication name must be between 1 and 100 characters")
    private String name;

    //TO DO make field more complex Structure the data into separate fields (e.g., "Uses," "Side Effects," "Dosage") instead of one long string.
    @Size(max = 500, message = "Medication description cannot exceed 500 symbols")
    private String description;

    @Valid
    private BaseDosagePM defaultDosagePM;

    private Long dosageId;
}
