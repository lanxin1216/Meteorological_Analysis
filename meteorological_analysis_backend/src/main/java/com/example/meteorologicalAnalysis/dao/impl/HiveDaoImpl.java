package com.example.meteorologicalAnalysis.dao.impl;

import com.example.meteorologicalAnalysis.dao.HiveDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * hive 操作服务
 */
@Service
public class HiveDaoImpl implements HiveDao {

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
                "LOAD DATA INPATH '%s' INTO TABLE weather_analysis PARTITION (p_year=%d)",
                hdfsPath, year
        );
        hiveJdbcTemplate.execute(hiveSql);
        // 修复元数据:确保 Hive 能识别分区
        hiveJdbcTemplate.execute("MSCK REPAIR TABLE weather_analysis");
    }

    /**
     * 检查指定年份分区是否存在
     * @param year 要检查的年份
     * @return 存在返回true，不存在返回false
     */
    @Override
    public boolean checkYearPartitionExists(int year) {
        try {
            String sql = "SHOW PARTITIONS weather_analysis PARTITION(p_year=" + year + ")";
            List<String> partitions = hiveJdbcTemplate.queryForList(sql, String.class);
            return !partitions.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}