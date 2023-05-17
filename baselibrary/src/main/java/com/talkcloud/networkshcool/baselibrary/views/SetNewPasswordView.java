package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;

/**
 * Date:2021/5/17
 * Time:14:23
 * author:joker
 * 设置新密码view
 */
public interface SetNewPasswordView {
    void forgotPwdcallback(boolean isSuccess, String msg);

    //切换身份请求
    void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg);

    void pwdupdateCallback(boolean isSuccess, String msg);

}
