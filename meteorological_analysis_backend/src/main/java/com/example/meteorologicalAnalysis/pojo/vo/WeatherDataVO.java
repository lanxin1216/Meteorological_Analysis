package com.example.meteorologicalAnalysis.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WeatherDataVO {
    private LocalDateTime dataTime;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hour;
    private Double temperature;
    private Double dewPoint;
    private Double pressure;
    private Double windDirection;
    private Double windSpeed;
}