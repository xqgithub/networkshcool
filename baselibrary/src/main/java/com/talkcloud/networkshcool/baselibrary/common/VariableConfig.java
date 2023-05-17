package com.talkcloud.networkshcool.baselibrary.common;

import com.talkcloud.networkshcool.baselibrary.BuildConfig;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;

/**
 * Date:2021/5/10
 * Time:19:39
 * author:joker
 * 全局变量
 */
public class VariableConfig {

    //用户系统全局url地址
    public static String base_url_user = BuildConfig.gatewayServer + "gateway/";
    //师生系统全局url地址
    public static String base_url_teachers_students = BuildConfig.homeServer + "home/";

    //版本更新地址
//    public static String versionUpdate_url = BuildConfig.versionupdateServer;
//    //版本更新的version  封包日期+APP版本号
//    public static String versionUpdate_version = BuildConfig.packetdate + PublicPracticalMethodFromJAVA.getInstance().appVersionNameConversion();

    public static String API_DEMO_VERSION_UPDATE_URL = "https://demo.talk-cloud.net/ClientAPI/getupdateinfo/";
    //版本更新地址-release环境
    public static String API_RELEASE_VERSION_UPDATE_URL = "https://global.talk-cloud.net/ClientAPI/getupdateinfo/";

    public static String DEMO_VERSION_UPDATE_PACKET_DATE = "20211021";
    //版本更新封包日期—release环境 正式需改
    public static String RELEASE_VERSION_UPDATE_PACKET_DATE = "20211021";


    // 版本更新地址
    public static String getVersionUpdateUrl() {

        if ("release".equals(BuildConfig.host)) {
            return API_RELEASE_VERSION_UPDATE_URL;
        } else {
            return API_DEMO_VERSION_UPDATE_URL;
        }
    }

    // 版本更新的version  封包日期+000
    public static String getVersionUpdateVersion() {
        if ("release".equals(BuildConfig.host)) {
//            return RELEASE_VERSION_UPDATE_PACKET_DATE + PublicPracticalMethodFromJAVA.getInstance().appVersionNameConversion();
            return RELEASE_VERSION_UPDATE_PACKET_DATE + "000";
        } else {
//            return DEMO_VERSION_UPDATE_PACKET_DATE + PublicPracticalMethodFromJAVA.getInstance().appVersionNameConversion();
            return DEMO_VERSION_UPDATE_PACKET_DATE + "000";
        }
    }

    //透明色
    public static String color_transparent_bg = "#00000000";
    //按钮、图标选中颜色
    public static String color_button_selected = "#1d6aff";
    //按钮、图标未选中颜色
    public static String color_button_unselected = "#801d6aff";
    //文字选中颜色
    public static String color_text_selected = "#ffffffff";
    //文字未选中颜色
    public static String color_text_unselected = "#ffffffff";
    //选择框老师的背景色
    public static String color_teacher_bg = "#a9e0fc";
    //选择框学生的背景色
    public static String color_student_bg = "#9be1ab";
    //输入框有焦点背景色
    public static String color_inputbox_focus_bg = "#1d6aff";
    //输入框有无焦点背景色
    public static String color_inputbox_unfocus_bg = "#338f92a1";


    //验证码倒计时其否启动
    public static boolean verificationcode_countdown_flag = true;

    /**
     * 登录流程标识：9999.从启动页进入登录页   9998.从个人设置更换手机进入登录页   9997.从个人设置修改密码进入登录页面
     **/
    public static int login_process = -1;
    public static int login_process_normal = 9999;
    public static int login_process_changemobile = 9998;
    public static int login_process_changepassword = 9997;


    /**
     * 默认国家信息配置
     **/
    //默认国家代号
    public static String default_locale = TKBaseApplication.myApplication.getApplicationContext().getString(R.string.default_locale);
    //默认国家区号
    public static String default_localecode = TKBaseApplication.myApplication.getApplicationContext().getString(R.string.default_localecode);
    //默认国家名称
    public static String default_localename = TKBaseApplication.myApplication.getApplicationContext().getString(R.string.default_localename);


    /**
     * 用户隐私协议状态
     **/
    public static boolean userPrivacyAgreementStatus = false;

    /**
     * 护眼模式是否已经开启过的标识
     */
    public static boolean eyeProtectionisOpen = false;

    /**
     * 用户拒绝CheckPermissionsActivity页面权限申请
     */
    public static boolean IsRefuseAccessRequest = false;

    /**
     * 作业点评是否保存为快捷评语状态
     **/
//    public static boolean HomeWorkCommentShortcutStatus = false;

    /**
     * 检测身份标识 true 正在检查身份  false 检查身份完毕
     * ps：目前只考虑从无身份变成有身份的情况，后期调整，可能需要修改
     */
    public static boolean checkIdentityFlag = false;


    public static final String V1 = "v1";
    public static final String V2 = "v2";


    public static enum UrlPathHelp {

        //用户端地址
        USER(base_url_user),
        //师生端地址
        TEACHERS_STUDENTS(base_url_teachers_students);

        private String url;

        UrlPathHelp(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }


}
