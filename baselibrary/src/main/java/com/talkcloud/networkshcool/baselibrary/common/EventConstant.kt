package com.talkcloud.networkshcool.baselibrary.common


/**
 * Author  guoyw
 * Date    2021/5/20
 * Desc    Event 相关常量
 */
class EventConstant {

    companion object {
        // 我的信息 资料
//        const val KEY_USER_INFO = "key_user_info"

        // 切换身份刷新消息
        const val CHANGE_IDENTITY_REFRESH = "refreshCourseFrg"

        // 无身份刷新消息
        const val NO_IDENTITY_REFRESH = "no_identity_refresh"

        // 刷新最近课程日期
        const val RECENTCOURSE_DATE_REFRESH = "refreshCourseDate"

        // 获取小红花数据
        const val MY_FLOWS_REFRESH = "my_flows_refresh"

        // 修改用户信息
        const val EVENT_EDIT_USER_INFO = "event_edit_user_info"

        // 作业发布成功
        const val EVENT_PUBLISH_HOMEWORK_SUCCESS = "event_publish_homework_success"

        //短信验证码倒计时
        const val EVENT_VERIFICATIONCODE_COUNTDOWN = "event_verificationcode_countdown"

        //国家区域信息
        const val EVENT_COUNTRYAREA = "event_countryarea"

        //企业网盘被选择数据
        const val EVENT_NETWORKDISKBEANSSELECTED = "event_networkdiskbeansselected"

        //企业网盘确定按钮
        const val EVENT_NETWORKDISKCONFIRM = "event_networkdiskconfirm"

        //刷新作业详情数据
        const val EVENT_REFRESHHOMEWORKDETAIL_DATA = "event_refreshhomeworkdetail_data"

        //获取未读通知的数据
        const val EVENT_UNREAD_NOTIFICATION = "event_unread_notification"

        //未读通知数据首页UI更新
        const val EVENT_UNREAD_NOTIFICATION_MAIN_UI_UPDATE = "event_unread_notification_main_ui_update"

        //作业详情Activity 专递数据给 Fragment
        const val EVENT_HOMEWORKDETAIL_PASS_VALUE = "event_homeworkdetail_pass_value"
    }
}