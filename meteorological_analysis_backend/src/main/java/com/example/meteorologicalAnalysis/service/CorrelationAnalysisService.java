package com.example.meteorologicalAnalysis.service;


import com.example.meteorologicalAnalysis.pojo.dto.correlation.CorrelationDTO;
import com.example.meteorologicalAnalysis.pojo.dto.correlation.TempDewPointDiffDTO;

import java.util.List;

public interface CorrelationAnalysisService {
    /**
     * 获取指定年份温度与气压的月度相关系数
     * @param year 指定年份
     */
    List<CorrelationDTO> getTempPressureCorrelation(Integer year);

    /**
     * 获取指定年份露点温度与温度差值的分布
     * @param year 指定年份
     */
    List<TempDewPointDiffDTO> getTempDewPointDistribution(Integer year);
}