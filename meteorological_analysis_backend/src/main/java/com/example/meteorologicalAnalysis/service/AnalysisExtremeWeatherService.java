package com.example.meteorologicalAnalysis.service;

import com.example.meteorologicalAnalysis.pojo.dto.extreme.ExtremeTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.extreme.StrongWindDaysDTO;

import java.util.List;

/**
 * 极端天气分析
 */
public interface AnalysisExtremeWeatherService {

    /**
     * 获取年度极端温度数据
     * @param year 年份
     * @return 极端温度数据
     */
    ExtremeTemperatureDTO getExtremeTemperatures(int year);

    /**
     * 获取每年强风天数统计 (风速阈值: 10m/s)
     * @return 年份和对应天数的列表
     */
    List<StrongWindDaysDTO> getStrongWindDaysStatistics();
}
