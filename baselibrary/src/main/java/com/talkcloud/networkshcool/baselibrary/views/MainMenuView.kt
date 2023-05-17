package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity

interface MainMenuView : IBaseView {
    // 显示用户信息
    fun showUserInfo(userInfo: UserInfoEntity?)

    //系统更新请求回调
    fun sysversionCallback(isSuccess: Boolean, sysVersionEntity: SysVersionEntity?, msg: String?)

    //未读通知
    fun noticenew(isSuccess: Boolean, data: Any?, msg: String?)

}