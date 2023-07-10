package com.qlu.newupdatephone.controller;
import com.alibaba.fastjson.JSONObject;
import com.qlu.newupdatephone.entity.SidUser;
import com.qlu.newupdatephone.test.OtherClass;
import com.qlu.newupdatephone.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
public class SIDOAuthCodeController {

    //保存刷新令牌
    @Value("${cas-server.refreshToken}")
    private String refreshToken = null;
    public String access_token = null;

    @GetMapping("/loginByCode")
    @ResponseBody
    public void accessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //统一身份中心地址
        String serverUrl = "https://sso.qlu.edu.cn";
        //身份中心分发的clientId
        String client_id = "QYWXSJH";
        //回调地址，登录成功后会将code传递给该地址
        String redirect_uri = "https://web3.qlu.edu.cn:3000";

        StringBuilder url = new StringBuilder();

        url.append(serverUrl).append("/oauth2.0/authorize");
        url.append("?response_type=code&client_id=");
        url.append(client_id);
        url.append("&redirect_uri=");
        url.append(getEncodeUrl(redirect_uri));

        System.out.println(url);

        response.sendRedirect(String.valueOf(url));
    }

    private String getEncodeUrl(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
    }


    @RequestMapping("/")
    public void receive(HttpServletRequest request, @RequestParam("code") String code) {

        //向CAS服务端请求地址
        String url = "https://sso.qlu.edu.cn/oauth2.0/accessToken";
        Map<String, Object> params = new LinkedHashMap<>();
        //客户端模式
        params.put("grant_type", "authorization_code");
        //应用账号
        params.put("client_id", "QYWXSJH");
        //应用密钥
        params.put("client_secret", "RHZZtSE-Og6sk5nFOJG0Hv-S1m83_0my08fPJRQPtI1gt9y7ER6WBfJA7adZKqse");
        //应用回调地址
        params.put("redirect_uri", "https://web3.qlu.edu.cn:3000");
        //code
        params.put("code", code);

        //发送GET请求
        String s = HttpClientUtils.getInstance().doPost(url, null, params);
        //解析json
        JSONObject jsonObject = JSONObject.parseObject(s);
        //获取token
        String accessToken = jsonObject.getString("access_token");
        //获取刷新令牌，如果令牌失效，则通过刷新令牌获取
        String refreshToken = jsonObject.getString("refresh_token");
        //令牌失效时间
        String expiresIn = jsonObject.getString("expires_in");
        System.out.println("access_token:" + accessToken);
        System.out.println("refresh_token:" + refreshToken);
        System.out.println("expires_in:" + expiresIn);
        //获取session
        HttpSession session = request.getSession();
        //将accessToken保存到session中
        session.setAttribute("accessToken", accessToken);
        //为session设置失效时间
        session.setMaxInactiveInterval(10);
        //将refreshToken保存起来
        this.refreshToken = refreshToken;
        //将access_token保存起来
        this.access_token = accessToken;

    }

    @RequestMapping("/usermsg")
    public void receive(HttpServletRequest request, HttpServletResponse response) {

        //使用access_token获取用户信息
        String url = "https://sso.qlu.edu.cn/oauth2.0/profile";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("access_token",access_token);
        String s = HttpClientUtils.getInstance().doPost(url, null, params);
        JSONObject jsonObject = JSONObject.parseObject(s);

        System.out.println(s);
        System.out.println(jsonObject);

        // 创建sidUser对象并保存用户信息
        SidUser sidUser = new SidUser();

        sidUser.setXM(jsonObject.getJSONObject("attributes").getString("XM"));
        sidUser.setXH(jsonObject.getJSONObject("attributes").getString("XH"));
        sidUser.setTEL(jsonObject.getJSONObject("attributes").getString("TEL"));

        // 将userInfo保存到session中或其他地方，以便在其他类中使用
        HttpSession session = request.getSession();
        session.setAttribute("SidUser", sidUser);

        System.out.println(sidUser.getXM());
        System.out.println(sidUser.getXH());
        System.out.println(sidUser.getTEL());
        System.out.println("=============================");

    }
}