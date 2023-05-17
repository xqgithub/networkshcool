package com.talkcloud.networkshcool.baselibrary.views;

/**
 * Date:2021/5/17
 * Time:11:23
 * author:joker
 * 忘记密码页面
 */
public interface ForgetPwdView {
    void smsCallback(boolean isSuccess, String msg);

    void checkMobileCallback(boolean isSuccess, boolean exists, String msg);
}
