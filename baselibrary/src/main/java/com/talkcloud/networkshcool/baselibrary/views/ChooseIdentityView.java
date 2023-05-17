package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;

/**
 * Date:2021/5/14
 * Time:11:16
 * author:joker
 * 用户选择身份 view
 */
public interface ChooseIdentityView {
    //切换身份请求
    void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg);
}
