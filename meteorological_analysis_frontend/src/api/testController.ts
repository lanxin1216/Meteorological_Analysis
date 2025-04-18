// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** testConnection GET /api/test/hdfs */
export async function testConnectionUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseObject_>("/api/test/hdfs", {
    method: "GET",
    ...(options || {}),
  });
}

/** testHiveConnection GET /api/test/hive */
export async function testHiveConnectionUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponseString_>("/api/test/hive", {
    method: "GET",
    ...(options || {}),
  });
}
