package com.example.meteorologicalAnalysis.dao.impl;

import com.example.meteorologicalAnalysis.dao.AnalysisExtremeWeatherDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AnalysisExtremeWeatherDaoImpl implements AnalysisExtremeWeatherDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 根据年份获取极端温度
    @Override
    public List<Map<String, Object>> getExtremeTemperatures(int year) {
        String sql =
                "WITH temp_stats AS ( " +
                "  SELECT " +
                "    MAX(temperature) AS max_temp, " +
                "    MIN(temperature) AS min_temp " +
                "  FROM weather_analysis " +
                "  WHERE p_year = ? " +
                "), " +
                "max_temp_records AS ( " +
                "  SELECT " +
                "    w.data_time, " +
                "    w.month, " +
                "    w.day, " +
                "    w.hour, " +
                "    w.temperature " +
                "  FROM weather_analysis w " +
                "  JOIN temp_stats ts ON w.temperature = ts.max_temp " +
                "  WHERE w.p_year = ? " +
                "  ORDER BY w.data_time " +
                "  LIMIT 1 " +
                "), " +
                "min_temp_records AS ( " +
                "  SELECT " +
                "    w.data_time, " +
                "    w.month, " +
                "    w.day, " +
                "    w.hour, " +
                "    w.temperature " +
                "  FROM weather_analysis w " +
                "  JOIN temp_stats ts ON w.temperature = ts.min_temp " +
                "  WHERE w.p_year = ? " +
                "  ORDER BY w.data_time " +
                "  LIMIT 1 " +
                ") " +
                "SELECT " +
                "  'max' AS type, " +
                "  temperature AS value, " +
                "  CONCAT(month, '-', day, ' ', printf('%02d:00', hour)) AS time " +
                "FROM max_temp_records " +
                "UNION ALL " +
                "SELECT " +
                "  'min' AS type, " +
                "  temperature AS value, " +
                "  CONCAT(month, '-', day, ' ', printf('%02d:00', hour)) AS time " +
                "FROM min_temp_records";

        return jdbcTemplate.queryForList(sql, year, year, year);
    }


    // 根据年份和风速阈值查找强风日
    @Override
    public List<Map<String, Object>> findStrongWindDaysByYear(double windSpeedThreshold) {
        String sql =
                "SELECT " +
                "  p_year AS year, " +
                "  COUNT(DISTINCT CONCAT(year, '-', month, '-', day)) AS days " +
                "FROM weather_analysis " +
                "WHERE wind_speed >= ? " +
                "GROUP BY p_year";

        return jdbcTemplate.queryForList(sql, windSpeedThreshold);
    }
}
