package com.example.meteorologicalAnalysis.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherPoint {
    private String label; // 时间轴标签
    private Double value; // 平均值或观测值
}
