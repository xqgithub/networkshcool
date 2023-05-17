package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;

/**
 * Date:2021/5/18
 * Time:20:25
 * author:joker
 * 个人设置页面 View
 */
public interface PersonalSettingsView {

    void logoutCallback(boolean isSuccess, String msg);

    void sysversionCallback(boolean isSuccess, SysVersionEntity sysVersionEntity, String msg);

    void eyeProtectionCallback(boolean isSure);
}
