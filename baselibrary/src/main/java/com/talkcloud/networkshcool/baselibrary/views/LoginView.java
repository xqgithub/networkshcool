package com.talkcloud.networkshcool.baselibrary.views;

/**
 * Date:2021/5/11
 * Time:16:29
 * author:joker
 * 登录
 */
public interface LoginView {
    void smsCallback(boolean isSuccess, String msg);

    void countryNameAndCodeCallback(String localename, String localecode, String locale);

}
