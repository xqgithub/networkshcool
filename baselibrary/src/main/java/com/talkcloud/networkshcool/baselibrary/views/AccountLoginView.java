package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;

/**
 * Date:2021/6/22
 * Time:15:49
 * author:joker
 * 用户账号登录的View
 */
public interface AccountLoginView {
    
    void accountLoginCallback(boolean isSuccess, LoginEntity info, String msg);

    //切换身份请求
    void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg);
}
