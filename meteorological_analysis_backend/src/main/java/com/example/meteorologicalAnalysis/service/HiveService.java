package com.example.meteorologicalAnalysis.service;

/**
 * hive 操作服务
 */
public interface HiveService {

    // 测试连接
    void testHiveConnection();

    /**
     * 设置动态分区配置
     */
    void setDynamicPartitionConfig();

    /**
     * 数据导入表
     */
    void loadDataIntoWeatherAnalysis(String hdfsPath, int year);
}

