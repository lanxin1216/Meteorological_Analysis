package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.dao.CorrelationAnalysisDao;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.pojo.dto.correlation.CorrelationDTO;
import com.example.meteorologicalAnalysis.pojo.dto.correlation.TempDewPointDiffDTO;
import com.example.meteorologicalAnalysis.service.CorrelationAnalysisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CorrelationAnalysisServiceImpl implements CorrelationAnalysisService {

    @Resource
    private CorrelationAnalysisDao correlationAnalysisDao;
    @Resource
    private HiveDao hiveDao;

    @Override
    public List<CorrelationDTO> getTempPressureCorrelation(Integer year) {
        // 检查年份分区是否存在
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "指定年份数据不存在");
        }

        List<Map<String, Object>> rawData = correlationAnalysisDao.findTempPressureCorrelation(year);

        return rawData.stream()
                .map(row -> new CorrelationDTO(
                        year,
                        ((Number) row.get("month")).intValue(),
                        ((Number) row.get("correlation_value")).doubleValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<TempDewPointDiffDTO> getTempDewPointDistribution(Integer year) {
        // 检查年份分区是否存在
        if (!hiveDao.checkYearPartitionExists(year)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "指定年份数据不存在");
        }

        List<Map<String, Object>> rawData = correlationAnalysisDao.findTempDewPointDistribution(year);

        return rawData.stream()
                .map(row -> new TempDewPointDiffDTO(
                        ((Number) row.get("temp_diff_range")).intValue(),
                        ((Number) row.get("record_count")).intValue()
                ))
                .collect(Collectors.toList());
    }
}