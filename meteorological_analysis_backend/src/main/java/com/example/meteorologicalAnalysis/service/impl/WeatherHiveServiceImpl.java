package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.service.WeatherHiveService;
import com.example.meteorologicalAnalysis.util.HdfsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Service
@Slf4j
public class WeatherHiveServiceImpl implements WeatherHiveService {

    @Resource
    private HdfsUtil hdfsUtil;

    @Resource
    private HiveDao hiveService;

    @Override
    public String uploadAndImport(MultipartFile file, int year) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }

        try {
            // 构建目标 HDFS 路径
            String fileName = file.getOriginalFilename();

            // 上传文件到 HDFS
            String hdfsPath = hdfsUtil.uploadFile(file.getInputStream(), fileName);

            // 将数据导入到hive数据库
            hiveService.setDynamicPartitionConfig();
            hiveService.loadDataIntoWeatherAnalysis(hdfsPath, year);

            return hdfsPath;

        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
        } catch (Exception e) {
            log.error("Hive 导入失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "导入 Hive 失败");
        }
    }
}
