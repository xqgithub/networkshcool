package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;

/**
 * Date:2021/5/13
 * Time:14:39
 * author:joker
 * 密码登录 view
 */
public interface PwdLoginView {
    void passwordLoginCallback(boolean isSuccess, LoginEntity info, String msg);

    void passwordLoginCallbackPS(boolean isSuccess, String token, String msg);

    //切换身份请求
    void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg);

    void smsCallback(boolean isSuccess, String msg);
}
