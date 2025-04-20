package com.example.meteorologicalAnalysis.dao.impl;

import com.example.meteorologicalAnalysis.dao.TrendAnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TrendAnalysisDaoImpl implements TrendAnalysisDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findMonthlyAvgTemperature(int year) {
        String sql = "SELECT month, ROUND(AVG(temperature), 2) as avgTemp " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? " +
                     "GROUP BY month";

        return jdbcTemplate.queryForList(sql, year);
    }

    @Override
    public List<Map<String, Object>> findYearlyPressureTrend(int startYear, int endYear) {
        String sql = "SELECT p_year as year, ROUND(AVG(pressure), 2) as avgValue " +
                     "FROM weather_analysis " +
                     "WHERE p_year BETWEEN ? AND ? " +
                     "GROUP BY p_year";

        return jdbcTemplate.queryForList(sql, startYear, endYear);
    }

    @Override
    public List<Map<String, Object>> findMonthlyAvgWindSpeed(int year) {
        String sql = "SELECT month, ROUND(AVG(wind_speed), 2) as avgSpeed " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? " +
                     "GROUP BY month";

        return jdbcTemplate.queryForList(sql, year);
    }

    @Override
    public List<Map<String, Object>> findSeasonalTemperature(int year) {
        String sql = "SELECT " +
                     "    CASE " +
                     "        WHEN month IN (3,4,5) THEN '春季' " +
                     "        WHEN month IN (6,7,8) THEN '夏季' " +
                     "        WHEN month IN (9,10,11) THEN '秋季' " +
                     "        ELSE '冬季' " +
                     "    END AS season, " +
                     "    ROUND(AVG(temperature), 2) AS avg_temp, " +
                     "    p_year AS year " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? " +
                     "GROUP BY p_year, " +
                     "    CASE " +
                     "        WHEN month IN (3,4,5) THEN '春季' " +
                     "        WHEN month IN (6,7,8) THEN '夏季' " +
                     "        WHEN month IN (9,10,11) THEN '秋季' " +
                     "        ELSE '冬季' " +
                     "    END " +
                     "ORDER BY season";

        return jdbcTemplate.queryForList(sql, year);
    }
}