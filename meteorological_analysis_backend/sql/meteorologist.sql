-- 创建库
create database meteorologist;
-- 使用库
USE meteorologist;
-- 创建表
CREATE EXTERNAL TABLE weather_analysis
(
    data_time      TIMESTAMP,
    month          INT,
    day            INT,
    hour           INT,
    temperature    DOUBLE,
    dew_point      DOUBLE,
    pressure       DOUBLE,
    wind_direction DOUBLE,
    wind_speed     DOUBLE
)
    PARTITIONED BY (year INT)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY ','
    STORED AS TEXTFILE
    LOCATION '/weather/hive/weather_analysis'
    TBLPROPERTIES (
        'skip.header.line.count' = '1',
        'serialization.null.format' = ''
        );