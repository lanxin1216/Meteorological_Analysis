// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getStrongWindDays GET /api/weather/extreme/strong-wind-days */
export async function getStrongWindDaysUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getStrongWindDaysUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListStrongWindDaysDTO_>(
    "/api/weather/extreme/strong-wind-days",
    {
      method: "GET",
      params: {
        // threshold has a default value: 10
        threshold: "10",
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getExtremeTemperatures GET /api/weather/extreme/temperaturesByYear */
export async function getExtremeTemperaturesUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getExtremeTemperaturesUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseExtremeTemperatureDTO_>(
    "/api/weather/extreme/temperaturesByYear",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
