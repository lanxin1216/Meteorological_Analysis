package com.example.meteorologicalAnalysis.pojo.dto.extreme;

import lombok.Data;

@Data
public class ExtremeTemperatureDTO {
    private int year;
    private double maxTemperature;
    private String maxTemperatureTime;
    private double minTemperature;
    private String minTemperatureTime;
}