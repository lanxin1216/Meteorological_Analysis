package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.constant.WeatherDataType;
import com.example.meteorologicalAnalysis.dao.BasicAnalysisDao;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherPoint;
import com.example.meteorologicalAnalysis.service.BasicAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BasicAnalysisServiceImpl implements BasicAnalysisService {

    @Autowired
    private BasicAnalysisDao basicAnalysisDao;

    @Override
    public List<WeatherPoint> getWeatherPointByDate(int year, int month, int day, WeatherDataType type) {
        String column = type.getColumn();
        List<Map<String, Object>> rawList = basicAnalysisDao.queryByHour(year, month, day, column);
        // 封装
        List<WeatherPoint> resultList = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            resultList.add(new WeatherPoint(
                    String.format("%02d时", map.get("hour")),  // 格式化为"00时"样式
                    safeGetDouble(map.get(column))
            ));
        }
        return resultList;
    }

    @Override
    public List<WeatherPoint> getWeatherPointByDay(int year, int month, WeatherDataType type) {
        String column = type.getColumn();
        List<Map<String, Object>> rawList = basicAnalysisDao.queryByDay(year, month, column);

        List<WeatherPoint> resultList = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            resultList.add(new WeatherPoint(
                    String.format("%d日", map.get("day")),  // 格式化为"1日"样式
                    safeGetDouble(map.get("avg_" + column))  // 注意字段名变化
            ));
        }
        return resultList;
    }

    @Override
    public List<WeatherPoint> getWeatherPointByMonth(int year, WeatherDataType type) {
        String column = type.getColumn();
        List<Map<String, Object>> rawList = basicAnalysisDao.queryByMonth(year, column);
        List<WeatherPoint> resultList = new ArrayList<>();
        for (Map<String, Object> map : rawList) {
            resultList.add(new WeatherPoint(
                    String.format("%d月", map.get("month")),  // 格式化为"1月"样式
                    safeGetDouble(map.get("avg_" + column))  // 注意字段名变化
            ));
        }
        return resultList;
    }

    /**
     * 安全转换数值类型
     */
    private Double safeGetDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}

















