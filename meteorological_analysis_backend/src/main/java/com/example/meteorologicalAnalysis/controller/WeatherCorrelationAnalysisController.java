package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.pojo.dto.correlation.CorrelationDTO;
import com.example.meteorologicalAnalysis.pojo.dto.correlation.TempDewPointDiffDTO;
import com.example.meteorologicalAnalysis.service.CorrelationAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 相关性分析
 */
@RestController
@RequestMapping("/api/weather/correlation")
public class WeatherCorrelationAnalysisController {

    @Resource
    private CorrelationAnalysisService correlationAnalysisService;

    /**
     * 获取指定年份温度与气压的月度相关系数
     *
     * @param year 指定年份
     * @return 相关系数列表
     */
    @GetMapping("/temp-pressure")
    public BaseResponse<List<CorrelationDTO>> getTempPressureCorrelation(@RequestParam Integer year) {
        return ResultUtils.success(correlationAnalysisService.getTempPressureCorrelation(year));
    }

    /**
     * 获取指定年份露点温度与温度差值的分布
     *
     * @param year 指定年份
     * @return 差值分布列表
     */
    @GetMapping("/temp-dewpoint")
    public BaseResponse<List<TempDewPointDiffDTO>> getTempDewPointDistribution(@RequestParam Integer year) {
        return ResultUtils.success(correlationAnalysisService.getTempDewPointDistribution(year));
    }
}