package com.example.meteorologicalAnalysis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @TableName weather_analysis
 */

@Data
@AllArgsConstructor
public class WeatherAnalysis implements Serializable {

    private DateTime dataTime;

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer hour;

    private Double temperature;

    private Double dewPoint;

    private Double pressure;

    private Double windDirection;

    private Double windSpeed;

    private Integer pYear;
}

