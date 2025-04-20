package com.example.meteorologicalAnalysis.dao.impl;

import com.example.meteorologicalAnalysis.dao.CorrelationAnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CorrelationAnalysisDaoImpl implements CorrelationAnalysisDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findTempPressureCorrelation(Integer year) {
        String sql = "SELECT " +
                     "    p_year as year, " +
                     "    month, " +
                     "    ROUND(CORR(temperature, pressure), 4) AS correlation_value " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? " +
                     "GROUP BY p_year, month ";

        return jdbcTemplate.queryForList(sql, year);
    }

    @Override
    public List<Map<String, Object>> findTempDewPointDistribution(Integer year) {
        String sql = "SELECT " +
                     "    FLOOR((temperature - dew_point)/5)*5 AS temp_diff_range, " +
                     "    COUNT(*) AS record_count " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? " +
                     "GROUP BY FLOOR((temperature - dew_point)/5)*5 ";

        return jdbcTemplate.queryForList(sql, year);
    }
}