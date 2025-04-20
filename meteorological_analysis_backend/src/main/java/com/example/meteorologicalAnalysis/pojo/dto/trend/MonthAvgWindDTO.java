package com.example.meteorologicalAnalysis.pojo.dto.trend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthAvgWindDTO {
    private Integer month;
    private Double avgSpeed;
}