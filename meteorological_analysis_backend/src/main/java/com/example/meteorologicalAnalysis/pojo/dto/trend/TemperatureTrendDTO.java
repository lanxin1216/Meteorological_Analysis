package com.example.meteorologicalAnalysis.pojo.dto.trend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TemperatureTrendDTO {
    private Integer month;  // 1-12
    private Double avgTemp;
}