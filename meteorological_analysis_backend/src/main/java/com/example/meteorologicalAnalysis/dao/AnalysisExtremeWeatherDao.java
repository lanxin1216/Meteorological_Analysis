package com.example.meteorologicalAnalysis.dao;

import java.util.List;
import java.util.Map;

/**
 * 极端天气分析
 */
public interface AnalysisExtremeWeatherDao {
    /**
     * 获取年度极端温度数据
     * @param year 年份
     * @return 包含最高温和最低温及其出现时间的数据
     */
    List<Map<String, Object>> getExtremeTemperatures(int year);

    /**
     * 查询每年风速超过阈值的天数
     *
     * @param windSpeedThreshold 风速阈值(单位:m/s)
     * @return 年份和对应天数的列表
     */
    List<Map<String, Object>> findStrongWindDaysByYear(double windSpeedThreshold);
}
