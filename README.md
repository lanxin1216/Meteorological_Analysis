### 基于 Hadoop + Hive 的哈尔滨市气象数据分析平台

#### **项目概述**
本项目旨在构建一个基于Hadoop和Hive的气象数据分析平台，通过HDFS存储海量气象数据，利用Hive构建数据仓库并执行分析任务，后端采用Spring Boot提供数据接口，前端通过图表展示分析结果。核心目标是为气象研究和预报提供数据支持，包括趋势分析、地理分布、相关性分析等。

#### **系统架构设计**
- **数据存储层**：HDFS存储原始气象数据（CSV/JSON格式）。
- **数据处理层**：Hive数据仓库，通过Hive SQL执行批量分析。
- **后端服务层**：Spring Boot提供RESTful API，集成Hive JDBC执行查询。
- **前端展示层**：Vue.js + ECharts，展示折线图、热力图等可视化结果。

架构示意：用户上传数据至HDFS → Hive数仓处理 → Spring Boot API查询 → 前端图表展示

#### **开发&运行环境**

##### 开发环境

- java - JDK 1.8
- SpringBoot 2.7.5
- Hadoop 3.1.3
- Hive 3.1.3
- CentOS7 操作系统
- Vue3
- Ant Design Vue

##### 部署&运行环境

- Hadoop 集群：3节点（1 NameNode + 2 DataNode）。  
- HDFS：保存实际数据。
- Hive：元数据使用 MySQL ，Thrift Server 提供 JDBC 连接。  

#### 项目运行

- 启动 Hadoop

  ```bash
  start-all.sh
  ```

- 启动 Hive数据仓库

  ```bash
  bin/hive --service metastore &
  bin/hive --service hiveserver2 &
  bin/hive
  ```

- 后端运行
- 前端运行

> HDFS目录权限移除处理：
> 给项目需要的目录授权：
>
> ```bash
> hdfs dfs -chmod -R 777 /weather/hive/weather_analysis
> hdfs dfs -chmod -R 777 /weather/hive
> hdfs dfs -chmod 777 /weather/raw
> hdfs dfs -chmod 777 /weather
> ```

#### 数据来源

 数据来源：美国国家气候数据中心（NCDC）

- ftp://ftp.ncdc.noaa.gov/pub/data/noaa/isd-lite/
- 获取哈尔滨市监测站编号：https://www.ncei.noaa.gov/maps/daily/
- 哈尔滨市编号：509530

- 格式：CSV文件

#### **处理流程**
**数仓搭建**：  

- Hive表定义（按时间分区）：  
  ```sql
  CREATE EXTERNAL TABLE weather_data (
      station_id STRING,
      temperature DOUBLE,
      humidity DOUBLE,
      wind_speed DOUBLE,
      pressure DOUBLE,
      latitude DOUBLE,
      longitude DOUBLE
  )
  PARTITIONED BY (event_time STRING)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
  LOCATION '/weather/hive';
  ```

**数据上传** 

- Spring Boot提供文件上传接口，调用HDFS API将数据存入 `/weather/raw` 目录。  
- 数据清洗：过滤缺失值（如湿度为-9999的异常记录）。

**数据清洗**：

- 系统对数据进行清洗。

**数据分析**：通过Hive SQL执行查询，结果存入HDFS或MySQL供前端调用。

#### **部分Hive SQL示例**
1. **月平均温度计算**：  
   
   ```sql
   SELECT 
       SUBSTR(event_time, 1, 7) AS month,
       AVG(temperature) AS avg_temp
   FROM weather_data
   GROUP BY SUBSTR(event_time, 1, 7);
   ```
   
2. **地区湿度分布**：  
   ```sql
   SELECT 
       CONCAT(latitude, ',', longitude) AS location,
       AVG(humidity) AS avg_humidity
   FROM weather_data
   GROUP BY latitude, longitude;
   ```

3. **温度-湿度相关性分析**：  
   
   ```sql
   SELECT 
       CORR(temperature, humidity) AS correlation
   FROM weather_data;
   ```
   
4. **年度极端风速检测**：  
   
   ```sql
   SELECT 
       station_id,
       MAX(wind_speed) AS max_wind_speed
   FROM weather_data
   WHERE wind_speed > 20  -- 定义风速阈值
   GROUP BY station_id;
   ```

5. **年度极端温度分析**

```
-- 每年最高/最低温度及发生日期
SELECT 
    year,
    MAX(temperature) AS max_temp,
    MIN(temperature) AS min_temp,
    MAX(CASE WHEN temperature = max_temp THEN CONCAT(month,'-',day) END) AS max_temp_date,
    MIN(CASE WHEN temperature = min_temp THEN CONCAT(month,'-',day) END) AS min_temp_date
FROM (
    SELECT 
        year, month, day, temperature,
        MAX(temperature) OVER (PARTITION BY year) AS max_temp,
        MIN(temperature) OVER (PARTITION BY year) AS min_temp
    FROM weather_analysis
) t
GROUP BY year;
```

6. **每年风速超过阈值(如10m/s)的天数**

```
-- 每年风速超过阈值(如10m/s)的天数
SELECT 
    year,
    COUNT(DISTINCT CONCAT(month,'-',day)) AS high_wind_days
FROM weather_analysis
WHERE wind_speed > 10
GROUP BY year;
```

7. **温度长期趋势**

```
-- 近5年每月平均温度变化
SELECT 
    year,
    month,
    ROUND(AVG(temperature), 2) AS avg_temp,
    ROUND(AVG(temperature) OVER (PARTITION BY month ORDER BY year 
         ROWS BETWEEN 4 PRECEDING AND CURRENT ROW), 2) AS 5yr_avg_temp
FROM weather_analysis
WHERE year BETWEEN 2019 AND 2023
GROUP BY year, month
ORDER BY year, month;
```

8. **季节变化分析**

```
-- 四季平均温度对比(北半球)
SELECT 
    year,
    CASE 
        WHEN month IN (3,4,5) THEN '春季'
        WHEN month IN (6,7,8) THEN '夏季'
        WHEN month IN (9,10,11) THEN '秋季'
        ELSE '冬季'
    END AS season,
    ROUND(AVG(temperature), 2) AS avg_temp
FROM weather_analysis
GROUP BY year, 
    CASE 
        WHEN month IN (3,4,5) THEN '春季'
        WHEN month IN (6,7,8) THEN '夏季'
        WHEN month IN (9,10,11) THEN '秋季'
        ELSE '冬季'
    END
ORDER BY year, season;
```

9. **温度-气压相关性**

```
-- 每月温度与气压的相关系数
SELECT 
    year,
    month,
    ROUND(CORR(temperature, pressure), 4) AS temp_pressure_corr
FROM weather_analysis
GROUP BY year, month
ORDER BY year, month;
```

10. **露点温度与相对湿度关系**

```
-- 露点温度与温度差值的分布
SELECT 
    FLOOR((temperature - dew_point)/5)*5 AS temp_dewpoint_diff,
    COUNT(*) AS record_count
FROM weather_analysis
GROUP BY FLOOR((temperature - dew_point)/5)*5
ORDER BY temp_dewpoint_diff;
```