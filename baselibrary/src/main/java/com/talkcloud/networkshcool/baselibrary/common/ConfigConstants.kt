package com.talkcloud.networkshcool.baselibrary.common

/**
 * Date:2021/5/10
 * Time:18:41
 * author:joker
 * 全局常量(不可变的)配置说明
 */
object ConfigConstants {

    /** 错误日志名称 **/
    const val ERROR_JOURNAL = "ANetworkshcoolError"

    /** 应用启动，权限判断返回码 **/
    const val PERMISSIONS_INIT_REQUEST_CODE = 1000

    //权限授权
    const val PERMISSIONS_GRANTED = 1001

    //权限拒绝
    const val PERMISSIONS_DENIED = 1002

    //权限方案
    const val PACKAGE_URL_SCHEME = "package:"

    //权限参数
    const val EXTRA_PERMISSIONS = "me.chunyu.clwang.permission.extra_permission"

    /** 存储相关常量 **/
    //Byte与Byte的倍数
    const val BYTE = 1

    //KB与Byte的倍数
    const val KB = 1024

    //MB与Byte的倍数
    const val MB = 1024 * KB

    //GB与Byte的倍数
    const val GB = 1024 * MB

    /** okhttp配置 **/
    //okhttp 写超时
    const val OKHTTP_WRITE_TIME_OUT = 15L

    //okhttp 连接超时
    const val OKHTTP_CONNECT_TIME_OUT = 15L

    //okhttp 读取超时
    const val OKHTTP_READ_TIME_OUT = 15L


    /** TAG名称 **/
    const val TAG_ALL = "NetWorkShcool"


    /** 页面标识 **/
    const val ACTIVITY_SPECIES = "activity_species"

    //登录
    const val LOGINACTIVITY = "LoginActivity"

    //忘记密码
    const val FORGETPWDACTIVITY = "ForgetPwdActivity"

    //更换手机
    const val CHANGEMOBILEACTIVITY = "ChangeMobileActivity"

    //用户名和密码登录
    const val PWDLOGINACTIVITY = "PwdLoginActivity"

    //设置新密码
    const val SETNEWPASSWORDACTIVITY = "SetNewPasswordActivity"

    //验证码输入页面
    const val VCODEINPUTACTIVITY = "VCodeInputActivity"

    //我的页面
    const val MYFRAGMENT = "MyFragment"

    //检查身份
    const val CHECKIDENTITYPAGE = "checkIdentityPage"

    /** 用户身份 **/
    //老师
    const val IDENTITY_TEACHER = "7"

    //学生
    const val IDENTITY_STUDENT = "8"

    /** 用户登录方式**/
    //手机号登录
    const val LOGINMETHOD_MOBILE = -99990

    //账号登录
    const val LOGINMETHOD_ACCOUNT = -99989

    //密码登录
    const val LOGINMETHOD_PWD = -99986


    /** 广播action字段 **/
    //关闭广播
    const val NOTIFACATIO_CLOSE = "notifacatio_close"

    /** 更新文件下载的APK 保存目录 **/
    const val APK_DIR = "networkshcoolApk"

    /** UI设计标准 **/
    //pad的设计图宽
    const val PAD_WIDTH = 1024

    //pad的设计图高
    const val PAD_HEIGHT = 768

    //phone的设计图宽
    const val PHONE_WIDTH = 375

    //phone的设计图高
    const val PHONE_HEIGHT = 812

    /** 录制声音保存目录 **/
    const val AUDIO_DIR = "networkshcoolAudio"

    //录制状态---未录制
    const val AUDIO_NORECORDED = -99999

    //录制状态---录制中
    const val AUDIO_RECORDING = -99998

    //音频录制默认最大时间
    const val RECORD_MAX_DURATION = 10 * 60 * 1000

    /** 音频播放器 **/
    //音频播放
    const val AUDIO_PLAY = -99997

    //音频暂停
    const val AUDIO_PAUSE = -99996

    //音频停止
    const val AUDIO_STOP = -99995

    /** PermissionsActivity 权限判断后，执行方法 **/
    //升级APP
    const val PERMISSIONS_GRANTED_UPDATEAPP = -99994

    //录音
    const val PERMISSIONS_GRANTED_STARTAUDIORECORD = -99993

    //启动页需要申请的权限
    const val PERMISSIONS_GRANTED_STARTUPACTIVITY = -99992


    /** AudioRecordDialog 模式 **/
    //评论模式
    const val DIALOG_COMMENT = -99992

    //录音模式
    const val DAILOG_AUDIORECORD = -99991

    /** 企业网盘数据状态 **/
    //正常
    const val NETWORKDISKBEANS_NORMAL = -99990

    //搜索
    const val NETWORKDISKBEANS_SEARCH = -99989

    /** 企业网盘已经选择文件类型数据 **/
    //图片
    const val NETWORKDISK_PICS_SELECTED = "networkdisk_pics_selected"

    //视频
    const val NETWORKDISK_VIDEOS_SELECTED = "networkdisk_videos_selected"

    //音频
    const val NETWORKDISK_AUDIOS_SELECTED = "networkdisk_audios_selected"

    //文件
    const val NETWORKDISK_FILES_SELECTED = "networkdisk_files_selected"

    /** 用户协议 && 隐私政策 **/
    //用户协议
    const val USERAGREEMENT = -99988

    //隐私政策
    const val USERPRIVACYPOLICY = -99987

    /** 课节课件预览 **/
    const val LESSONPREVIEW = -99985

    /** 短信图文验证 **/
    const val SMS_GRAPHIC_VERIFICATION = -99984

}