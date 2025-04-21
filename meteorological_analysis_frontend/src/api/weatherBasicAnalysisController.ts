// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getByDay GET /api/weather/basicAnalysis/ByDay */
export async function getByDayUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByDayUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>(
    "/api/weather/basicAnalysis/ByDay",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getByMonth GET /api/weather/basicAnalysis/ByMonth */
export async function getByMonthUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByMonthUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>(
    "/api/weather/basicAnalysis/ByMonth",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getByYear GET /api/weather/basicAnalysis/ByYear */
export async function getByYearUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByYearUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>(
    "/api/weather/basicAnalysis/ByYear",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getTop500ByYear GET /api/weather/basicAnalysis/getAllData */
export async function getTop500ByYearUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTop500ByYearUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherDataVO_>(
    "/api/weather/basicAnalysis/getAllData",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
