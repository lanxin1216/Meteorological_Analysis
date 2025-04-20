package com.example.meteorologicalAnalysis.pojo.dto.correlation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorrelationDTO {
    private Integer year;
    private Integer month;
    private Double correlationValue;
}