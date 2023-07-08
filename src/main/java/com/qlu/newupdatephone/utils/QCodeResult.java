package com.qlu.newupdatephone.utils;


import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhuxh
 * @date 2019/6/18
 */
public class QCodeResult<T> implements Serializable {

    private static final long serialVersionUID = -4569216979022946969L;
    private T data;//返回数据

    private boolean isSuccess = false;//成功标识

    private String resultMsg = "执行失败!";//结果信息

    public QCodeResult(){

    }

    public static <T> QCodeResult<T> fail(){
        return new QCodeResult<>();
    }

    public static <T> QCodeResult<T> fail(String msg){
        QCodeResult<T> result = fail();
        result.setResultMsg(msg);
        return result;
    }

    public static <T> QCodeResult<T> success(String msg){
        QCodeResult<T> result = success();
        result.setResultMsg(msg);
        return result;
    }


    public static <T> QCodeResult<T> success(){
        QCodeResult<T> result = new QCodeResult<>();
        result.setResultMsg("执行成功！");
        result.setIsSuccess(true);
        return result;
    }

    public static <T> QCodeResult<T> success(T t){
        QCodeResult<T> result = success();
        result.setData(t);
        return result;
    }

    public static <T> QCodeResult<T> success(QCodeResult<T> result){
        if(result == null){
            return success();
        }
        result.setResultMsg("执行成功！");
        result.setIsSuccess(true);
        return result;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }



    /**
     * 删除数据
     * @param keys
     * @return
     */
    public QCodeResult removeData(String... keys) {
        if(this.data==null || !(this.data instanceof Map)){
            return this;
        }
        Map<String,Object> map = (Map<String, Object>) this.data;
        for (String key : keys) {
            map.remove(key);
        }
        return this;
    }

    /**
     * 清空返回数据
     * @return
     */
    public QCodeResult clearData() {
        this.data = null;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
