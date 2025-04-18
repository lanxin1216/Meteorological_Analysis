package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import com.example.meteorologicalAnalysis.service.HiveService;
import com.example.meteorologicalAnalysis.service.WeatherHiveService;
import com.example.meteorologicalAnalysis.util.HdfsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {

    @Autowired
    private WeatherHiveService weatherHiveService;

    /**
     * 上传CSV文件到HDFS并导入到Hive表
     */
    @PostMapping("/upload-and-import")
    public BaseResponse<?> uploadAndImport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("year") int year
    ) {
        String hdfsPath = weatherHiveService.uploadAndImport(file, year);
        return ResultUtils.success(hdfsPath, "上传并导入成功");
    }

}