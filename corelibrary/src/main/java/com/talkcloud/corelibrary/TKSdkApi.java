package com.talkcloud.corelibrary;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.classroomsdk.utils.TKLog;
import com.eduhdsdk.room.RoomClient;
import com.eduhdsdk.tools.CrashHandler;
import com.eduhdsdk.tools.FunctionSetManage;

import java.util.HashMap;
import java.util.Map;

/**
 * Author  guoyw
 * Date    2021/5/21
 * Desc
 */
public class TKSdkApi {

    // 配置参数
    public static void configParam(Context context, int app_name, int logo) {
        //设置教室内通知样式的app名称
        FunctionSetManage.getInstance().setAppName(app_name);
        //设置教室内通知样式logo
        FunctionSetManage.getInstance().setAppLogo(logo);
        //设置教室crash时日志收集的标识
        FunctionSetManage.getInstance().setCarshEnterprise("network_school");

        FunctionSetManage.getInstance().setLoaddingGifImgSrc(R.drawable.tk_loadingpad);

        //设置进入教室时是否跳过设备检测页面(选择添加)
//        FunctionSetManage.getInstance().setIsSkipDeviceTesting(context, false);
        //设置关闭崩溃日志收集的弹窗
//        FunctionSetManage.getInstance().setIsSkipCrashHandleDialog(true);
        //设置关闭功能引导(选择添加)
//        FunctionSetManage.getInstance().setIsGuide(getApplicationContext(), true);
        //设置进教室的音量百分比(选择添加)
//        FunctionSetManage.getInstance().setMaxVolume(0.5f);
        //日志异常捕获-拓课云
        new CrashHandler((Activity) context);
    }



    // 进入教室
    public static void joinRoom(Activity activity, TKJoinRoomModel model) {
        //param：Map格式，内含进入房间所需的基本参数，例如：
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("serial", model.getRoomId());
        params.put("password", model.getPwd());
        params.put("nickname", model.getNickName());
        params.put("userrole", model.getUserRole());
        params.put("userid", model.getUserId());

        // global global.talk-cloud.net
        if("demo".equals(BuildConfig.host)) {
            params.put("host", "demo.talk-cloud.net");
        } else if("testing".equals(BuildConfig.host)) {
            params.put("host", "testing.talk-cloud.net");
        } else if("release".equals(BuildConfig.host)){
            params.put("host", "global.talk-cloud.net");
        }

//        params.put("port", 443);
        params.put("clientType", "2");

        TKLog.d("goyw", "joinRoom : " + params.toString());

        RoomClient.getInstance().joinRoom(activity, params);
    }


    // 回放
    public static void joinPlayMp4Back(Activity activity, String url) {
        RoomClient.getInstance().joinPlayMp4Back(activity, url);
    }

    // 常规件回放
    public static void joinPlayBackNormalRoom(Activity activity, TKJoinBackRoomModel model) {
        Map<String, Object> params = new HashMap<String, Object>();

        if("demo".equals(BuildConfig.host)) {
            params.put("host", "demo.talk-cloud.net");
        } else if("testing".equals(BuildConfig.host)) {
            params.put("host", "testing.talk-cloud.net");
        } else if("release".equals(BuildConfig.host)){
            params.put("host", "global.talk-cloud.net");
        }

//        params.put("domain", "www");//企业id

        params.put("path",model.getUrl());
        params.put("serial", model.getRoomId());//教室号
        params.put("recordtitle", model.getTitle());//根据后台返回的数据（回放标题）

        TKLog.d("goyw", "joinPlayBackNormalRoom : " + params.toString());

        RoomClient.getInstance().joinPlayBackRoom(activity, params);
    }


    public static void joinPlayBackRoom(Activity activity, TKJoinBackRoomModel model) {
        // 有mp4就播放mp4
        if(!TextUtils.isEmpty(model.getMp4url())) {
            joinPlayMp4Back(activity, model.getMp4url());
        } else {
            joinPlayBackNormalRoom(activity, model);
        }
    }












    // 进入教室
//    public static void testJoinRoom(Activity activity) {
//        //param：Map格式，内含进入房间所需的基本参数，例如：
//
//        HashMap<String, Object> params = new HashMap<String, Object>();
//
//        params.put("serial", "232620497");
//        params.put("password", "4");
//        params.put("nickname", "卡卡西");
//        params.put("userrole", 2);
//        params.put("userid", "123");
//
//        // global global.talk-cloud.net
//        params.put("host", "demo.talk-cloud.net");
//        params.put("port", 443);
//        params.put("clientType", "2");
//
//        RoomClient.getInstance().joinRoom(activity, params);
//    }
//
//
//    // 常规回放 测试
//    public static void testJoinPlayBackRoom(Activity activity) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("host", "demo.talk-cloud.net");//域名
//        params.put("domain", "www");//企业id
//        params.put("path","demo.talk-cloud.net：8081/8c1c04d1-af5f-4ae5-9f50-846f11955920-1660304892/");
//        params.put("serial", "1660304892");//教室号
//        params.put("recordtitle", "1622195800917");//根据后台返回的数据（回放标题）
//        RoomClient.getInstance().joinPlayBackRoom(activity, params);
//    }

}
