package com.example.meteorologicalAnalysis.pojo.dto.correlation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempDewPointDiffDTO {
    private Integer tempDiffRange;
    private Integer recordCount;
}