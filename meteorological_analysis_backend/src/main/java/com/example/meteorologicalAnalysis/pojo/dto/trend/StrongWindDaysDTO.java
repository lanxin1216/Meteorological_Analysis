package com.example.meteorologicalAnalysis.pojo.dto.trend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StrongWindDaysDTO {
    private Integer year;
    private Integer days;
}