package com.example.meteorologicalAnalysis.pojo.dto.trend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeasonTemperatureDTO {
    private String season;  // 季节名称：春季、夏季、秋季、冬季
    private Double avgTemp; // 平均温度
    private Integer year;   // 年份
}