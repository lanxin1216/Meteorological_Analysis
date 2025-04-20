package com.example.meteorologicalAnalysis.dao;

import java.util.List;
import java.util.Map;

/**
 * 趋势分析
 */
public interface TrendAnalysisDao {
    /**
     * 查询每月平均温度趋势
     */
    List<Map<String, Object>> findMonthlyAvgTemperature(int year);

    /**
     * 查询年度气压变化趋势
     */
    List<Map<String, Object>> findYearlyPressureTrend(int startYear, int endYear);

    /**
     * 查询各月平均风速
     */
    List<Map<String, Object>> findMonthlyAvgWindSpeed(int year);

    /**
     * 查询指定年份的季节平均温度
     */
    List<Map<String, Object>> findSeasonalTemperature(int year);
}
