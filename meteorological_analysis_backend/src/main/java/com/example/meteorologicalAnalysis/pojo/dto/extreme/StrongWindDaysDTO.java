package com.example.meteorologicalAnalysis.pojo.dto.extreme;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StrongWindDaysDTO {
    private int year;
    private int days;
}