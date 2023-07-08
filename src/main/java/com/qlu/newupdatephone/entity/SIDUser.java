package com.qlu.newupdatephone.entity;
/**
// 获取session中的userInfo对象
HttpSession session = request.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");

// 使用userInfo对象中的用户信息
        String username = userInfo.getUsername();
        String email = userInfo.getEmail();
// 其他需要使用的用户信息字段
 */
public class SIDUser {
        private String XM;

        private String GH;

        private String SJH;

    public String getXM() {
        return XM;
    }

    public void setXM(String XM) {
        this.XM = XM;
    }

    public String getGH() {
        return GH;
    }

    public void setGH(String GH) {
        this.GH = GH;
    }

    public String getSJH() {
        return SJH;
    }

    public void setSJH(String SJH) {
        this.SJH = SJH;
    }

    public SIDUser(String XM, String GH, String SJH) {
        this.XM = XM;
        this.GH = GH;
        this.SJH = SJH;
    }

    public SIDUser() {
    }
// 构造方法、getter和setter方法

    }
