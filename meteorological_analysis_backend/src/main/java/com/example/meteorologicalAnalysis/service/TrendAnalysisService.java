package com.example.meteorologicalAnalysis.service;

import com.example.meteorologicalAnalysis.pojo.dto.trend.MonthAvgWindDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.SeasonTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.TemperatureTrendDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.WeatherTrendDTO;

import java.util.List;

/**
 * 趋势分析
 */
public interface TrendAnalysisService {
    /**
     * 获取月度平均温度趋势
     * @param year 查询年份
     * @return 每月平均温度
     */
    List<TemperatureTrendDTO> getMonthlyTemperatureTrend(int year);

    /**
     * 获取年度气压变化趋势
     * @param startYear 起始年份
     * @param endYear 结束年份
     * @return 每年平均气压
     */
    List<WeatherTrendDTO> getYearlyPressureTrend(int startYear, int endYear);

    /**
     * 获取月度平均风速
     * @param year 查询年份
     * @return 每月平均风速
     */
    List<MonthAvgWindDTO> getMonthlyAvgWindSpeed(int year);

    /**
     * 获取指定年份的季节平均温度对比
     * @param year 查询年份
     * @return 季节温度数据列表
     */
    List<SeasonTemperatureDTO> getSeasonalTemperature(int year);
}
