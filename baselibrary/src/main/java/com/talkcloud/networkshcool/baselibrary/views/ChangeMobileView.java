package com.talkcloud.networkshcool.baselibrary.views;

/**
 * Date:2021/5/18
 * Time:9:27
 * author:joker
 * 更换手机号页面 view
 */
public interface ChangeMobileView {
    void smsCallback(boolean isSuccess, String msg);

    void checkMobileCallback(boolean isSuccess, boolean exists, String msg);
}
