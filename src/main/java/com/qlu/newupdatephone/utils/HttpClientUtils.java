package com.qlu.newupdatephone.utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 分装一个http请求的工具类
 */
public class HttpClientUtils {

    private static HttpClientUtils instance;
    CloseHttpUtil closeHttpUtil = new CloseHttpUtil();

    private HttpClientUtils() {
    }
    public static synchronized HttpClientUtils getInstance() {
        if (instance == null) {
            instance = new HttpClientUtils();
        }
        return instance;
    }
    /**
     * <p>发送GET请求
     *
     * @param url       GET请求地址(带参数)
     * @param headerMap GET请求头参数容器
     * @return 与当前请求对应的响应内容字
     */

    public String doGet(String url, Map<String, Object> headerMap) {
        String content = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpGet getMethod = new HttpGet(url);
            //头部请求信息
            if (headerMap != null) {
                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    getMethod.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            //发送get请求
            CloseableHttpResponse httpResponse = httpClient.execute(getMethod);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    //读取内容
                    content = EntityUtils.toString(httpResponse.getEntity());
                } finally {
                    httpResponse.close();
                }
            } else {
                throw new RuntimeException(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                closeHttpClient(httpClient);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return content;
    }
    public String doGetRequest(String url, Map<String, String> headMap, Map<String, String> paramMap) {
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String entityStr = null;
        CloseableHttpResponse response = null;
        try {
            /*
             * 由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
             */
            URIBuilder uriBuilder = new URIBuilder(url);
            for (Entry<String, String> param : paramMap.entrySet()) {
                uriBuilder.addParameter(param.getKey(), param.getValue());
            }
            System.out.println(uriBuilder);
            // 根据带参数的URI对象构建GET请求对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            System.out.println(httpGet);
            /*
             * 添加请求头信息
             */
            if (null != headMap) {
                for (Entry<String, String> header : headMap.entrySet()) {
                    httpGet.addHeader(header.getKey(), header.getValue());
                }
            }
            System.out.println(httpGet);
            /*
            httpGet.addHeader("Content-Type", "application/VIID+JSON;charset=utf8");
            httpGet.addHeader("User-Identify","12345678905030000000");
            */
            CloseHttpUtil.setTimeOut(httpGet);
            // 执行请求
            response = httpClient.execute(httpGet);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            entityStr = EntityUtils.toString(entity, "UTF-8");
        } catch (ClientProtocolException e) {
            System.err.println("Http协议出现问题");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("解析错误");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.err.println("URI解析异常");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO异常");
            e.printStackTrace();
        } finally {
            // 释放连接
            CloseHttpUtil.close(response, httpClient);
        }
        return entityStr;
    }

    /**
     * <p>发送POST请求
     *
     * @param url          POST请求地址
     * @param headerMap    POST请求头参数容器
     * @param parameterMap POST请求参数容器
     * @return 与当前请求对应的响应内容字
     */
    public String doPost(String url, Map<String, Object> headerMap, Map<String, Object> parameterMap) {
        String content = null;
        CloseableHttpClient httpClient = getHttpClient();
        try {
            HttpPost postMethod = new HttpPost(url);
//            postMethod.setHeader("Content-Type", "application/json;charset=utf-8");
//            postMethod.setHeader("Accept", "application/json;charset=utf-8");

            //头部请求信息
            if (headerMap != null) {
                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    postMethod.addHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }

            if (parameterMap != null) {
                Iterator iterator = parameterMap.keySet().iterator();
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    nvps.add(new BasicNameValuePair(key, String.valueOf(parameterMap.get(key))));
                }
                postMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            }


            CloseableHttpResponse httpResponse = httpClient.execute(postMethod);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                try {
                    //读取内容
                    content = EntityUtils.toString(httpResponse.getEntity());
                } finally {
                    httpResponse.close();
                }
            } else {
                throw new RuntimeException(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                closeHttpClient(httpClient);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return content;
    }
    public CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }
    private void closeHttpClient(CloseableHttpClient client) throws IOException {
        if (client != null) {
            client.close();
        }
    }

    public static String httpPostWithJson(String url, String json) {
        String returnValue = JSONToUtils.toJSONString(QCodeResult.fail("接口异常！"));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }

}
