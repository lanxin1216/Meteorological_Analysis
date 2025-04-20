package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.dao.TrendAnalysisDao;
import com.example.meteorologicalAnalysis.pojo.dto.trend.MonthAvgWindDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.SeasonTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.TemperatureTrendDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.WeatherTrendDTO;
import com.example.meteorologicalAnalysis.service.TrendAnalysisService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TrendAnalysisServiceImpl implements TrendAnalysisService {
    @Resource
    private TrendAnalysisDao trendAnalysisDao;

    @Resource
    private HiveDao hiveDao;

    @Override
    public List<TemperatureTrendDTO> getMonthlyTemperatureTrend(int year) {
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

        // 获取原始数据
        List<Map<String, Object>> rawData = trendAnalysisDao.findMonthlyAvgTemperature(year);

        // 数据封装
        return rawData.stream()
                .map(row -> new TemperatureTrendDTO(
                        ((Number) row.get("month")).intValue(),
                        ((Number) row.get("avgTemp")).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<WeatherTrendDTO> getYearlyPressureTrend(int startYear, int endYear) {
        // 参数校验
        if (ObjectUtils.isEmpty(startYear) || ObjectUtils.isEmpty(endYear)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "起始年份和结束年份不能为空");
        }
        if (startYear > endYear) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "起始年份不能大于结束年份");
        }
        // 检查分区
        if (!hiveDao.checkYearPartitionExists(startYear)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "起始年份数据不存在");
        }
        if (!hiveDao.checkYearPartitionExists(endYear)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "结束年份数据不存在");
        }

        // 获取原始数据
        List<Map<String, Object>> rawData = trendAnalysisDao.findYearlyPressureTrend(startYear, endYear);

        // 数据封装
        return rawData.stream()
                .map(row -> new WeatherTrendDTO(
                        ((Number) row.get("year")).intValue(),
                        ((Number) row.get("avgValue")).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthAvgWindDTO> getMonthlyAvgWindSpeed(int year) {
        // 参数校验
        if (ObjectUtils.isEmpty(year)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "年份不能为空");
        }
        // 检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

        // 获取原始数据
        List<Map<String, Object>> rawData = trendAnalysisDao.findMonthlyAvgWindSpeed(year);

        // 数据封装
        return rawData.stream()
                .map(row -> new MonthAvgWindDTO(
                        ((Number) row.get("month")).intValue(),
                        ((Number) row.get("avgSpeed")).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<SeasonTemperatureDTO> getSeasonalTemperature(int year) {
        // 参数校验
        if (ObjectUtils.isEmpty(year)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "年份不能为空");
        }
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

        // 获取原始数据
        List<Map<String, Object>> rawData = trendAnalysisDao.findSeasonalTemperature(year);

        // 数据封装
        List<SeasonTemperatureDTO> result = rawData.stream()
                .map(row -> new SeasonTemperatureDTO(
                        (String) row.get("season"),
                        ((Number) row.get("avg_temp")).doubleValue(),
                        ((Number) row.get("year")).intValue()
                ))
                .collect(Collectors.toList());

        // 确保返回四个季节的数据（如果某季节无数据则补0）
        String[] seasons = {"春季", "夏季", "秋季", "冬季"};
        Map<String, SeasonTemperatureDTO> seasonMap = result.stream()
                .collect(Collectors.toMap(SeasonTemperatureDTO::getSeason, Function.identity()));

        return Arrays.stream(seasons)
                .map(season -> seasonMap.getOrDefault(season,
                        new SeasonTemperatureDTO(season, 0.0, year)))
                .collect(Collectors.toList());
    }
}