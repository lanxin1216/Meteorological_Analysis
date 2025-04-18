package com.example.meteorologicalAnalysis.service;

import org.springframework.web.multipart.MultipartFile;

public interface WeatherHiveService {

    /**
     * 上传文件到 HDFS，并将数据导入到 Hive 表
     *
     * @param file 上传的 CSV 文件
     * @param year 对应 Hive 分区
     * @return 上传的 HDFS 路径
     */
    String uploadAndImport(MultipartFile file, int year);
}
