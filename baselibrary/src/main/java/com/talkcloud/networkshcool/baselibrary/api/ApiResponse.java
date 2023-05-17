package com.talkcloud.networkshcool.baselibrary.api;

/**
 * Date:2021/5/10
 * Time:19:12
 * author:joker
 * 解析基类
 */
public class ApiResponse<T> {
    /**
     * 用于描述数据是否请求成功
     */
    private int result;

    /**
     * 返回的数据
     */
    private T data;


    /**
     * 如果请求失败，会显示此字段将包含失败原因
     */
    private String msg;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "result=" + result +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
