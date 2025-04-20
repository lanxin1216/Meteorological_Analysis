package com.example.meteorologicalAnalysis.service;

import com.example.meteorologicalAnalysis.constant.WeatherDataType;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherDataVO;
import com.example.meteorologicalAnalysis.pojo.vo.WeatherPoint;

import java.util.List;

/**
 * 基本分析
 */
public interface BasicAnalysisService {

    /**
     * 获取某年的前500条数据
     */
     List<WeatherDataVO> getTop500ByYear(int year);

    /**
     * 获取指定日期的时段气象变化
     */
    List<WeatherPoint> getWeatherPointByDate(int year, int month, int day, WeatherDataType type);

    /**
     * 获取指定月份每天的气象变化
     */
    List<WeatherPoint> getWeatherPointByDay(int year, int month, WeatherDataType type);

    /**
     * 获取每月的平均气象变化
     */
    List<WeatherPoint> getWeatherPointByMonth(int year, WeatherDataType type);
}
