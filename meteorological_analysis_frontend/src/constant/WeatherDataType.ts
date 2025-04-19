export enum WeatherDataType {
    TEMPERATURE = 'temperature',
    DEW_POINT = 'dew_point',
    PRESSURE = 'pressure',
    WIND_DIRECTION = 'wind_direction',
    WIND_SPEED = 'wind_speed',
}

export const WeatherDataTypeLabel: Record<WeatherDataType, string> = {
    [WeatherDataType.TEMPERATURE]: '气温',
    [WeatherDataType.DEW_POINT]: '露点气温',
    [WeatherDataType.PRESSURE]: '气压',
    [WeatherDataType.WIND_DIRECTION]: '风向',
    [WeatherDataType.WIND_SPEED]: '风速',
};

export function fromColumn(column: string): WeatherDataType {
    const type = Object.values(WeatherDataType).find(
        (type) => type.toLowerCase() === column.toLowerCase()
    );
    if (!type) {
        throw new Error(`不支持的气象数据类型: ${column}`);
    }
    return type;
}
