package com.example.meteorologicalAnalysis.service.impl;

import com.example.meteorologicalAnalysis.service.HiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * hive 操作服务
 */
@Service
public class HiveServiceImpl implements HiveService {

    @Autowired
    private JdbcTemplate hiveJdbcTemplate;

    @Override
    public void testHiveConnection() {
        String sql = "SHOW TABLES";
        hiveJdbcTemplate.queryForList(sql).forEach(row -> System.out.println(row));
    }

    @Override
    public void setDynamicPartitionConfig() {
        hiveJdbcTemplate.execute("SET hive.exec.dynamic.partition = true");
        hiveJdbcTemplate.execute("SET hive.exec.dynamic.partition.mode = nonstrict");
    }

    @Override
    public void loadDataIntoWeatherAnalysis(String hdfsPath, int year) {
        String hiveSql = String.format(
                "LOAD DATA INPATH '%s' INTO TABLE weather_analysis PARTITION (year=%d)",
                hdfsPath, year
        );
        hiveJdbcTemplate.execute(hiveSql);
    }
}