-- 创建库
create database meteorologist;
-- 使用库
USE meteorologist;
-- 创建表
CREATE EXTERNAL TABLE weather_analysis
(
    data_time      TIMESTAMP,
    year           INT,
    month          INT,
    day            INT,
    hour           INT,
    temperature    DOUBLE,
    dew_point      DOUBLE,
    pressure       DOUBLE,
    wind_direction DOUBLE,
    wind_speed     DOUBLE
)
    PARTITIONED BY (p_year INT) -- 单独的分区字段
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY ','
    LOCATION '/weather/hive/weather_analysis'
    TBLPROPERTIES (
        'skip.header.line.count' = '1',
        'serialization.null.format' = ''
        );

SELECT hour, temperature
FROM weather_analysis
WHERE p_year = 2021
  AND year = 2021
  AND month = 1
  AND day = 1;

--查询某月按天的气象平均值（指定字段）
SELECT sub.day, ROUND(AVG(sub.temperature), 2) AS avg_value
FROM (SELECT day, temperature
      FROM weather_analysis
      WHERE p_year = 2021
        AND month = 1) sub
GROUP BY sub.day;
--查询某年按月的气象平均值（指定字段）
SELECT sub.month, ROUND(AVG(sub.temperature), 2) AS avg_value
FROM (SELECT month, temperature
      FROM weather_analysis
      WHERE p_year = 2021) sub
GROUP BY sub.month;
