package com.example.meteorologicalAnalysis.dao;

import java.util.List;
import java.util.Map;

/**
 * 相关性分析 DAO
 */
public interface CorrelationAnalysisDao {
    /**
     * 查询指定年份温度与气压的月度相关系数
     * @param year 指定年份
     */
    List<Map<String, Object>> findTempPressureCorrelation(Integer year);

    /**
     * 查询指定年份露点温度与温度差值的分布
     * @param year 指定年份
     */
    List<Map<String, Object>> findTempDewPointDistribution(Integer year);
}