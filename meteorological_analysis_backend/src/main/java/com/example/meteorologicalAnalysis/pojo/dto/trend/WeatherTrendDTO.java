package com.example.meteorologicalAnalysis.pojo.dto.trend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherTrendDTO {
    private Integer year;
    private Double avgValue;
}