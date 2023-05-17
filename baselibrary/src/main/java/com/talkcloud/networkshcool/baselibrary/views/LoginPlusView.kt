package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity

/**
 * Date:2021/9/8
 * Time:15:36
 * author:joker
 * 登录升级接口
 */
interface LoginPlusView {

    //切换登录方式UI
    fun switchLoginMethodUi(loginMethod: Int)

    //国家名字和code 获取回调
    fun countryNameAndCodeCallback(localename: String, localecode: String, locale: String)

    //短信发送回调
    fun smsCallback(isSuccess: Boolean, msg: String)

    fun passwordLoginCallback(isSuccess: Boolean, info: LoginEntity?, msg: String?)

    //切换身份请求
    fun changeloginidentityCallback(isSuccess: Boolean, userIdentityEntity: UserIdentityEntity?, msg: String?)

    fun accountLoginCallback(isSuccess: Boolean, info: LoginEntity?, msg: String?)
}