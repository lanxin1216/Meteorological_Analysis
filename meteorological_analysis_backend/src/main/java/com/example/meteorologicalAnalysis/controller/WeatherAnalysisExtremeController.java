package com.example.meteorologicalAnalysis.controller;


import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.pojo.dto.extreme.ExtremeTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.extreme.StrongWindDaysDTO;
import com.example.meteorologicalAnalysis.service.AnalysisExtremeWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 极端分析
 */
@RestController
@RequestMapping("/api/weather/extreme")
public class WeatherAnalysisExtremeController {

    @Autowired
    private AnalysisExtremeWeatherService analysisExtremeWeatherService;

    /**
     * 获取年度极端温度数据
     * @param year 年份
     * @return 极端温度数据
     */
    @GetMapping("/temperaturesByYear")
    public BaseResponse<ExtremeTemperatureDTO> getExtremeTemperatures(@RequestParam int year) {
        ExtremeTemperatureDTO result = analysisExtremeWeatherService.getExtremeTemperatures(year);
        return ResultUtils.success(result);
    }

    /**
     * 获取强风天气统计
     * 风速阈值(默认10m/s)
     *
     * @return 每年风速超过阈值的天数统计
     */
    @GetMapping("/strong-wind-days")
    public BaseResponse<List<StrongWindDaysDTO>> getStrongWindDays(@RequestParam(defaultValue = "10.0") double threshold) {
        return ResultUtils.success(analysisExtremeWeatherService.getStrongWindDaysStatistics(threshold));
    }
}
