package com.example.meteorologicalAnalysis.constant;

import com.example.meteorologicalAnalysis.common.BusinessException;
import lombok.Getter;

@Getter
public enum WeatherDataType {
    TEMPERATURE("temperature", "气温"),
    DEW_POINT("dew_point", "露点气温"),
    PRESSURE("pressure", "气压"),
    WIND_DIRECTION("wind_direction", "风向"),
    WIND_SPEED("wind_speed", "风速");

    private final String column;
    private final String label;

    WeatherDataType(String column, String label) {
        this.column = column;
        this.label = label;
    }

    public static WeatherDataType fromColumn(String column) {
        for (WeatherDataType type : values()) {
            if (type.column.equalsIgnoreCase(column)) {
                return type;
            }
        }
        throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的气象数据类型: " + column);
    }
}
