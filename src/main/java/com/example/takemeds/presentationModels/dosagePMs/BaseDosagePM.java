package com.example.takemeds.presentationModels.dosagePMs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseDosagePM {

    private String frequency;

    private byte timesPerDay;

    private List<LocalTime> timesToTake;
}
