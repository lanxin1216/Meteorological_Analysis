package com.example.meteorologicalAnalysis.dao;

/**
 * hive 基础操作服务
 */
public interface HiveDao {

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

    /**
     * 检查分区是否存在
     */
    boolean checkYearPartitionExists(int year);
}

