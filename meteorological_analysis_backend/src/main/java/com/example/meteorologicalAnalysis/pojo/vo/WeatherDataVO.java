package com.example.meteorologicalAnalysis.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class WeatherDataVO {
    private Timestamp dataTime;
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