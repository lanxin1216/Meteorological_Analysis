package com.example.meteorologicalAnalysis.controller;

import com.example.meteorologicalAnalysis.common.BaseResponse;
import com.example.meteorologicalAnalysis.common.ResultUtils;
import com.example.meteorologicalAnalysis.dao.HiveDao;
import com.example.meteorologicalAnalysis.util.HdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private HdfsUtil hdfsUtil;

    @Autowired
    private HiveDao hiveTestService;

    /**
     * 测试HDFS连接
     */
    @GetMapping("/hdfs")
    public BaseResponse<?> testConnection() {
        boolean isConnected = hdfsUtil.testConnection();
        return ResultUtils.success("HDFS连接状态：" + (isConnected ? "成功" : "失败"));
    }

    /**
     * 测试Hive连接
     */
    @GetMapping("/hive")
    public BaseResponse<String> testHiveConnection() {
        hiveTestService.testHiveConnection();
        return ResultUtils.success("HDFS连接连接测试完成");
    }
}
