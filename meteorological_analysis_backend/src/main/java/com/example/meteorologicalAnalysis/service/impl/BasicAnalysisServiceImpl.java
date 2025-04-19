package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.constant.WeatherDataType;
import com.example.meteorologicalAnalysis.dao.BasicAnalysisDao;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherDataVO;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherPoint;
import com.example.meteorologicalAnalysis.service.BasicAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BasicAnalysisServiceImpl implements BasicAnalysisService {

    @Autowired
    private BasicAnalysisDao basicAnalysisDao;

    @Autowired
    private HiveDao hiveDao;

    @Override
    public List<WeatherDataVO> getTop500ByYear(int year) {
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

        List<Map<String, Object>> rawData = basicAnalysisDao.findTop500ByYear(year);
        return convertToVO(rawData);
    }

    private List<WeatherDataVO> convertToVO(List<Map<String, Object>> rawData) {
        for (Map<String, Object> map : rawData) {
            System.out.println("Returned keys: " + map.keySet());
        }


        System.out.println("--------------------");
        System.out.println("Map:" + rawData.get(0));

        return rawData.stream().map(map -> new WeatherDataVO(
                ((Timestamp) map.get("data_time")).toLocalDateTime(),
                (Integer) map.get("year"),
                (Integer) map.get("month"),
                (Integer) map.get("day"),
                (Integer) map.get("hour"),
                (Double) map.get("temperature"),
                (Double) map.get("dew_point"),
                (Double) map.get("pressure"),
                (Double) map.get("wind_direction"),
                (Double) map.get("wind_speed")
        )).collect(Collectors.toList());
    }

    @Override
    public List<WeatherPoint> getWeatherPointByDate(int year, int month, int day, WeatherDataType type) {
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }
        // 查询数据
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
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

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
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询年份数据不存在");
        }

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

















