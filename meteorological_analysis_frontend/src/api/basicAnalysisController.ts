// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getByDate GET /api/basicAnalysis/date */
export async function getByDateUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByDateUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>("/api/basicAnalysis/date", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getByDay GET /api/basicAnalysis/day */
export async function getByDayUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByDayUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>("/api/basicAnalysis/day", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getByMonth GET /api/basicAnalysis/month */
export async function getByMonthUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getByMonthUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListWeatherPoint_>(
    "/api/basicAnalysis/month",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
