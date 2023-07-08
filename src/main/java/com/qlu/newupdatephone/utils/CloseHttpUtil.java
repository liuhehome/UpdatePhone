package com.qlu.newupdatephone.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by a on 2019/10/14.
 */
public class CloseHttpUtil {
    public static void close(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        if (null != response) {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                System.err.println("释放连接出错");
                e.printStackTrace();
            }
        }
    }

    public static void setTimeOut(HttpGet httpGet) {
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(10000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(10000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(10000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();

        // 将上面的配置信息 运用到这个Get请求里
        httpGet.setConfig(requestConfig);
    }
}
