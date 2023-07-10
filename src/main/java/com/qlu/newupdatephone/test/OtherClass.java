package com.qlu.newupdatephone.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.qlu.newupdatephone.entity.SidUser;

/**
 *     {@code @RequestMapping("/test")}
 *     public void testMethod(HttpServletRequest request) {
 *         OtherClass otherClass = new OtherClass();
 *         HttpSession session = request.getSession();
 *         SidUser sidUser = (SidUser) session.getAttribute("SidUser");
 *         System.out.println("SidUser from HttpSession: " + sidUser);
 *         otherClass.testSidUserFields(request);
 *     }
 */

public class OtherClass {
    public void testSidUserFields(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SidUser sidUser = (SidUser) session.getAttribute("SidUser");

        if (sidUser == null) {
            System.out.println("SidUser is null");
        }else {

            String xm = sidUser.getXM();
            String xh = sidUser.getXH();
            String tel = sidUser.getTEL();

            // 打印或日志记录字段值
            System.out.println("XM: " + xm);
            System.out.println("XH: " + xh);
            System.out.println("TEL: " + tel);
        }
    }
}
