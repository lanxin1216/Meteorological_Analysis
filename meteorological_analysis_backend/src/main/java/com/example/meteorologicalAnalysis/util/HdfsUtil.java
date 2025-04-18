package com.example.meteorologicalAnalysis.util;

import com.example.meteorologicalAnalysis.common.BusinessException;
import com.example.meteorologicalAnalysis.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class HdfsUtil {
    @Value("${hadoop.hdfs.uri}")
    private String hdfsUri;

    @Value("${hadoop.user.name}")
    private String hdfsUser;

    private Configuration conf;

    // 初始化Hadoop配置
    @PostConstruct
    public void init() {
        conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);
        System.setProperty("HADOOP_USER_NAME", hdfsUser);
    }

    /**
     * 测试HDFS连接
     */
    public boolean testConnection() {
        try (FileSystem fs = FileSystem.get(conf)) {
            return fs.exists(new Path("/"));
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 上传文件到HDFS（仅允许CSV）
     */
    public String uploadFile(InputStream inputStream, String fileName) throws IOException {
        if (!fileName.toLowerCase().endsWith(".csv")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式错误，仅支持CSV文件");
        }

        String hdfsPath = "/weather/raw/" + fileName;
        try (FileSystem fs = FileSystem.get(conf)) {
            Path destPath = new Path(hdfsPath);
            if (fs.exists(destPath)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件已存在");
            }

            // 使用 FSDataOutputStream 直接写入 HDFS
            try (FSDataOutputStream outputStream = fs.create(destPath)) {
                byte[] buffer = new byte[1024];
                int length;
                // 使用循环读取 inputStream 中的数据并写入到 HDFS
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                log.error("文件上传失败", e);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
            }
            return hdfsPath;
        }
    }

}