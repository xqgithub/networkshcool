package com.talkcloud.networkshcool.baselibrary.common

/**
 * Date:2021/5/24
 * Time:9:22
 * author:joker
 * SP文件
 */
object SPConstants {

    /** Sp配置文件---user.xml start **/
    //sp文件名称
    const val USER_NAME = "user"

    //用户token
    const val TOKEN = "token"

    //用户手机号
    const val USER_MOBILE = "user_mobile"

    //用户账号
    const val USER_ACCOUNT = "user_account"

    //用户登录方式
    const val USER_LOGINMETHOD = "user_loginmethod"

    //用户密码
    const val USER_PWD = "user_pwd"

    //用户账号国家英文代号
    const val USER_LOCALE = "user_locale"

    //用户账号国家区号
    const val USER_LOCALECODE = "user_localecode"

    //用户账号国家名称
    const val USER_LOCALENAME = "user_localename"

    //用户账号登录身份
    const val USER_IDENTITY = "user_identity"


    //用户验证身份第一次验证码
    const val USER_VERIFICATIONCODE_FIRST = "user_verificationcode_first"


    //用户是否开启护眼模式
    const val USER_EYEPROTECTION = "user_eyeprotection"

    //验证发送读秒期间手机号暂时保存
    const val MOBILE_TEMP = "mobile_temp"

    //用户是否点击了同意 用户协议和隐私政策
    const val USERPRIVACYAGREEMENT_STATUS = "userprivacyagreement_status"


    //用户拒绝CheckPermissionsActivity页面权限申请
    const val IsRefuseAccessRequest = "isrefuseaccessrequest"

    //用户升级框弹出时间
    const val USER_SYSVERSIONUPDATE_SHOWTIME = "user_sysversionupdate_showtime"

    //用户的userid
    const val USER_ID = "user_id"

    //用户的nickname
    const val USER_NICKNAME = "user_nickname"


    /** Sp配置文件---user.xml end **/


    /** Sp配置文件---language.xml start **/
    //SP文件名
    const val LANGUAGE_NAME = "language"

    //当前语言
    const val LOCALE_LANGUAGE = "locale_language"

    //当前国家
    const val LOCALE_COUNTRY = "locale_country"

    /** Sp配置文件---language.xml end **/


    /** Sp配置文件---networkdiskhistoryrecord.xml start **/
    //SP文件名
    const val HISTORYRECORD_NAME = "historyrecord"

    /** Sp配置文件---networkdiskhistoryrecord.xml end **/


}