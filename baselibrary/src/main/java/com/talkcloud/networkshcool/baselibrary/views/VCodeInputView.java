package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;

/**
 * Date:2021/5/11
 * Time:16:29
 * author:joker
 * 输入验证码
 */
public interface VCodeInputView {
    //验证码请求登录
    void verificationCodeLoginCallback(boolean isSuccess, LoginEntity info, String msg);

    void VerificationCodeCheckCallback(boolean isSuccess, String token);

    //切换身份请求
    void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg);

    void smsCallback(boolean isSuccess, String msg);

    void mobileCallback(boolean isSuccess, String msg);


    void nextStepCallback();
}
