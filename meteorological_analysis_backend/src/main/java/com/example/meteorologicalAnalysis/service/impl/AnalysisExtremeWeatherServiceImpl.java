package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.dao.AnalysisExtremeWeatherDao;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.pojo.dto.extreme.ExtremeTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.extreme.StrongWindDaysDTO;
import com.example.meteorologicalAnalysis.service.AnalysisExtremeWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class AnalysisExtremeWeatherServiceImpl implements AnalysisExtremeWeatherService {

    @Autowired
    private AnalysisExtremeWeatherDao analysisExtremeWeatherDao;

    @Autowired
    private HiveDao hiveDao;

    @Override
    public ExtremeTemperatureDTO getExtremeTemperatures(int year) {
        // 先检查分区
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "查询年份数据不存在");
        }

        List<Map<String, Object>> results = analysisExtremeWeatherDao.getExtremeTemperatures(year);

        ExtremeTemperatureDTO vo = new ExtremeTemperatureDTO();
        vo.setYear(year);

        results.forEach(item -> {
            String type = (String) item.get("type");
            double value = ((Number) item.get("value")).doubleValue();
            String time = (String) item.get("time");

            if ("max".equals(type)) {
                vo.setMaxTemperature(value);
                vo.setMaxTemperatureTime(time);
            } else if ("min".equals(type)) {
                vo.setMinTemperature(value);
                vo.setMinTemperatureTime(time);
            }
        });

        return vo;
    }

    @Override
    public List<StrongWindDaysDTO> getStrongWindDaysStatistics() {
        double windSpeedThreshold = 10.0; // 风速阈值

        // 获取原始数据
        List<Map<String, Object>> rawData = analysisExtremeWeatherDao.findStrongWindDaysByYear(windSpeedThreshold);

        // 数据封装为DTO
        return rawData.stream()
                .map(row -> new StrongWindDaysDTO(
                        ((Number) row.get("year")).intValue(),
                        ((Number) row.get("days")).intValue()
                ))
                .collect(Collectors.toList());
    }

}
