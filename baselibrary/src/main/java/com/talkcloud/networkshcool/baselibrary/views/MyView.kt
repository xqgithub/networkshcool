package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.UserExtInfoEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity

interface MyView : IBaseView {

    // 显示用户信息
//    fun showUserInfo(userInfo: UserInfoEntity?)

    // 显示用户额外信息
    fun showUserExtInfo(userExtInfo: UserExtInfoEntity?)
}