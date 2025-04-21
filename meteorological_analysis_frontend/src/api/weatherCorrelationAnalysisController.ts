// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getTempDewPointDistribution GET /api/weather/correlation/temp-dewpoint */
export async function getTempDewPointDistributionUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTempDewPointDistributionUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListTempDewPointDiffDTO_>(
    "/api/weather/correlation/temp-dewpoint",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** getTempPressureCorrelation GET /api/weather/correlation/temp-pressure */
export async function getTempPressureCorrelationUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTempPressureCorrelationUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListCorrelationDTO_>(
    "/api/weather/correlation/temp-pressure",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}
