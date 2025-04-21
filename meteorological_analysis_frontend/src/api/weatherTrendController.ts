// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getMonthlyTemperatureTrend GET /api/weather/trend/monthly-temperature */
export async function getMonthlyTemperatureTrendUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMonthlyTemperatureTrendUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListTemperatureTrendDTO_>(
    "/api/weather/trend/monthly-temperature",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getMonthlyWindSpeed GET /api/weather/trend/monthly-wind */
export async function getMonthlyWindSpeedUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMonthlyWindSpeedUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListMonthAvgWindDTO_>(
    "/api/weather/trend/monthly-wind",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getSeasonalTemperature GET /api/weather/trend/seasonal-temperature */
export async function getSeasonalTemperatureUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getSeasonalTemperatureUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSeasonTemperatureDTO_>(
    "/api/weather/trend/seasonal-temperature",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getYearlyPressureTrend GET /api/weather/trend/yearly-pressure */
export async function getYearlyPressureTrendUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getYearlyPressureTrendUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherTrendDTO_>(
    "/api/weather/trend/yearly-pressure",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
