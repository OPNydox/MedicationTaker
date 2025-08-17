package com.example.takemeds.presentationModels.dosagePMs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDosagePM extends BaseDosagePM {
    private Long medicationId;
}
