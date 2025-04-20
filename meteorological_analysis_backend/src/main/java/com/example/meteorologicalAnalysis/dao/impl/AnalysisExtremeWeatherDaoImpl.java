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

    @Override
    public List<Map<String, Object>> getExtremeTemperatures(int year) {
        String sql = "WITH temp_stats AS (" +
                     "  SELECT " +
                     "    MAX(temperature) AS max_temp, " +
                     "    MIN(temperature) AS min_temp " +
                     "  FROM weather_analysis " +
                     "  WHERE p_year = ? " +
                     "), " +
                     "max_temp_records AS (" +
                     "  SELECT " +
                     "    data_time, " +
                     "    month, " +
                     "    day, " +
                     "    hour, " +
                     "    temperature " +
                     "  FROM weather_analysis " +
                     "  WHERE p_year = ? AND temperature = (SELECT max_temp FROM temp_stats) " +
                     "  ORDER BY data_time " +
                     "  LIMIT 1 " +
                     "), " +
                     "min_temp_records AS (" +
                     "  SELECT " +
                     "    data_time, " +
                     "    month, " +
                     "    day, " +
                     "    hour, " +
                     "    temperature " +
                     "  FROM weather_analysis " +
                     "  WHERE p_year = ? AND temperature = (SELECT min_temp FROM temp_stats) " +
                     "  ORDER BY data_time " +
                     "  LIMIT 1 " +
                     ") " +
                     "SELECT " +
                     "  'max' AS type, " +
                     "  temperature AS value, " +
                     "  CONCAT(month, '-', day, ' ', hour, ':00') AS time " +
                     "FROM max_temp_records " +
                     "UNION ALL " +
                     "SELECT " +
                     "  'min' AS type, " +
                     "  temperature AS value, " +
                     "  CONCAT(month, '-', day, ' ', hour, ':00') AS time " +
                     "FROM min_temp_records";

        return jdbcTemplate.queryForList(sql, year, year, year);
    }

    @Override
    public List<Map<String, Object>> findStrongWindDaysByYear(double windSpeedThreshold) {
        String sql = "SELECT p_year as year, COUNT(DISTINCT day) as days " +
                     "FROM weather_analysis " +
                     "WHERE wind_speed >= ? " +
                     "GROUP BY p_year";

        return jdbcTemplate.queryForList(sql, windSpeedThreshold);
    }
}
