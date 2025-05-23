package com.example.meteorologicalAnalysis.dao.impl;

import com.example.meteorologicalAnalysis.dao.BasicAnalysisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BasicAnalysisDaoImpl implements BasicAnalysisDao {

    // 注入JdbcTemplate
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findTop500ByYear(int year) {
        String sql = "SELECT " +
                     "data_time, year," +
                     "month, day, hour, " +
                     "temperature, dew_point, pressure, " +
                     "wind_direction, wind_speed " +
                     "FROM weather_analysis " +
                     "WHERE p_year = ? LIMIT 500";
        return jdbcTemplate.queryForList(sql, year);
    }

    @Override
    public List<Map<String, Object>> queryByHour(int year, int month, int day, String column) {
        // 构建SQL查询
        String sql = "SELECT hour, " + column + " FROM weather_analysis " +
                     "WHERE p_year = ? AND month = ? AND day = ? ";

        // 使用JdbcTemplate执行查询
        return jdbcTemplate.query(sql, new Object[]{year, month, day}, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("hour", rs.getInt("hour"));
            result.put(column, rs.getObject(column));
            return result;
        });
    }

    @Override
    public List<Map<String, Object>> queryByDay(int year, int month, String column) {
        // 构建SQL查询（计算每天的平均值）
//        String sql = "SELECT day, ROUND(AVG(" + column + "), 2) AS avg_value " +
//                     "FROM weather_analysis " +
//                     "WHERE p_year = ? AND month = ? " +
//                     "GROUP BY day ";
        String sql =
                "SELECT sub.day, ROUND(AVG(sub." + column + "), 2) AS avg_value " +
                "FROM ( " +
                "    SELECT day, " + column +
                "    FROM weather_analysis " +
                "    WHERE p_year = ? AND month = ? " +
                ") sub " +
                "GROUP BY sub.day";

        // 使用JdbcTemplate执行查询
        return jdbcTemplate.query(sql, new Object[]{year, month}, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("day", rs.getInt("day"));
            result.put("avg_" + column, rs.getDouble("avg_value"));
            return result;
        });
    }

    @Override
    public List<Map<String, Object>> queryByMonth(int year, String column) {
        // 构建SQL查询（计算每月的平均值）
//        String sql = "SELECT month, ROUND(AVG(" + column + "), 2) AS avg_value " +
//                     "FROM weather_analysis " +
//                     "WHERE p_year = ? " +
//                     "GROUP BY month ";
        // 优化
        String sql =
                "SELECT sub.month, ROUND(AVG(sub." + column + "), 2) AS avg_value " +
                "FROM ( " +
                "    SELECT month, " + column +
                "    FROM weather_analysis " +
                "    WHERE p_year = ? " +
                ") sub " +
                "GROUP BY sub.month";

        // 使用JdbcTemplate执行查询
        return jdbcTemplate.query(sql, new Object[]{year}, (rs, rowNum) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("month", rs.getInt("month"));
            result.put("avg_" + column, rs.getDouble("avg_value"));
            return result;
        });
    }
}
