package com.example.meteorologicalAnalysis.dao;

import java.util.List;
import java.util.Map;

/**
 * 基础分析dao层
 */
public interface BasicAnalysisDao {

    /**
     * 查询某天按小时的气象值（指定字段）
     */
    List<Map<String, Object>> queryByHour(int year, int month, int day, String column);

    /**
     * 查询某月按天的气象平均值（指定字段）
     */
    List<Map<String, Object>> queryByDay(int year, int month, String column);

    /**
     * 查询某年按月的气象平均值（指定字段）
     */
    List<Map<String, Object>> queryByMonth(int year, String column);
}
