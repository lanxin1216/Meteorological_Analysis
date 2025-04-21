package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.pojo.dto.trend.MonthAvgWindDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.SeasonTemperatureDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.TemperatureTrendDTO;
import com.example.meteorologicalAnalysis.pojo.dto.trend.WeatherTrendDTO;
import com.example.meteorologicalAnalysis.service.TrendAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 趋势分析
 */
@RestController
@RequestMapping("/api/weather/trend")
public class WeatherTrendController {

    @Resource
    private TrendAnalysisService trendAnalysisService;

    /**
     * 获取月度平均温度趋势
     *
     * @param year 查询年份
     * @return 每月平均温度
     */
    @GetMapping("/monthly-temperature")
    public BaseResponse<List<TemperatureTrendDTO>> getMonthlyTemperatureTrend(
            @RequestParam int year) {
        return ResultUtils.success(trendAnalysisService.getMonthlyTemperatureTrend(year));
    }

    /**
     * 获取年度气压变化趋势
     *
     * @param startYear 起始年份
     * @param endYear   结束年份
     * @return 每年平均气压
     */
    @GetMapping("/yearly-pressure")
    public BaseResponse<List<WeatherTrendDTO>> getYearlyPressureTrend(
            @RequestParam int startYear,
            @RequestParam int endYear) {
        return ResultUtils.success(trendAnalysisService.getYearlyPressureTrend(startYear, endYear));
    }

    /**
     * 获取月度平均风速
     *
     * @param year 查询年份
     * @return 每月平均风速
     */
    @GetMapping("/monthly-wind")
    public BaseResponse<List<MonthAvgWindDTO>> getMonthlyWindSpeed(
            @RequestParam int year) {
        return ResultUtils.success(trendAnalysisService.getMonthlyAvgWindSpeed(year));
    }

    /**
     * 获取季节温度对比数据
     *
     * @param year 查询年份(默认当前年份)
     * @return 季节温度对比数据
     */
    @GetMapping("/seasonal-temperature")
    public BaseResponse<List<SeasonTemperatureDTO>> getSeasonalTemperature(
            @RequestParam int year) {
        return ResultUtils.success(trendAnalysisService.getSeasonalTemperature(year));
    }
}