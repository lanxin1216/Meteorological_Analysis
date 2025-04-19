package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.constant.WeatherDataType;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherDataVO;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherPoint;
import com.example.meteorologicalAnalysis.service.BasicAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/basicAnalysis")
public class BasicAnalysisController {

    @Autowired
    private BasicAnalysisService basicAnalysisService;

    /**
     * 获取某年的500条
     */
    @GetMapping("/getAllData")
    public BaseResponse<List<WeatherDataVO>> getTop500ByYear(@RequestParam int year) {
        List<WeatherDataVO> weatherAnalysisList = basicAnalysisService.getTop500ByYear(year);
        return ResultUtils.success(weatherAnalysisList);
    }

    /**
     * 获取某天的每小时数据
     */
    @GetMapping("/ByDay")
    public BaseResponse<List<WeatherPoint>> getByDay(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam String type
    ) {
        WeatherDataType dataType = WeatherDataType.fromColumn(type);

        List<WeatherPoint> weatherPointByDate = basicAnalysisService.getWeatherPointByDate(year, month, day, dataType);
        return ResultUtils.success(weatherPointByDate);
    }

    /**
     * 获取某月每天的平均数据
     */
    @GetMapping("/ByMonth")
    public BaseResponse<List<WeatherPoint>> getByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam String type
    ) {
        WeatherDataType dataType = WeatherDataType.fromColumn(type);
        List<WeatherPoint> weatherPointByDay = basicAnalysisService.getWeatherPointByDay(year, month, dataType);
        return ResultUtils.success(weatherPointByDay);
    }

    /**
     * 获取某年每月的平均数据
     */
    @GetMapping("/ByYear")
    public BaseResponse<List<WeatherPoint>> getByYear(
            @RequestParam int year,
            @RequestParam String type
    ) {
        WeatherDataType dataType = WeatherDataType.fromColumn(type);
        List<WeatherPoint> weatherPointByMonth = basicAnalysisService.getWeatherPointByMonth(year, dataType);
        return ResultUtils.success(weatherPointByMonth);
    }
}
