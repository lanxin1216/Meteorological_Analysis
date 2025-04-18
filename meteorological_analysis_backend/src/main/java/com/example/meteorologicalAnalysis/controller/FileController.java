package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.service.WeatherHiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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