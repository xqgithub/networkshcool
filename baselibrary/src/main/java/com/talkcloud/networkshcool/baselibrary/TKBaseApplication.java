package com.talkcloud.networkshcool.baselibrary;

import android.content.Context;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.eduhdsdk.tools.ScreenScale;
import com.eduhdsdk.tools.Tools;
import com.facebook.stetho.Stetho;
import com.llew.huawei.verifier.LoadedApkHuaWei;
import com.talkcloud.networkshcool.baselibrary.help.MyActivityLifecycleCallbacks;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.help.TKShareHelper;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.utils.AppUtils;
import com.talkcloud.networkshcool.baselibrary.utils.CrashHandler;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkplus.functiondomain.TKPluginsConstants;
import com.talkplus.functiondomain.TKPluginsManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import cn.jpush.android.api.JPushInterface;

/**
 * Date:2021/5/10
 * Time:18:33
 * author:joker
 * 自定义Application
 */
public class TKBaseApplication extends MultiDexApplication {

    public static TKBaseApplication myApplication;

    //Activity的计数
//    private int mActivityCount = 0;

    public MyActivityLifecycleCallbacks lifecycleCallbacks = new MyActivityLifecycleCallbacks();

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initConfig();
        //这句话是为了防止 webview页面打开的时候，造成语言混乱
        //new WebView(this).destroy();

        initKV();
        TKShareHelper.INSTANCE.preInit(this);
        boolean isAgree = MySPUtilsUser.getInstance().getUserPrivacyAgreementStatus();
        if (isAgree) {
            initCoreLib();
            initBugly();
            initJpush();
            initShare();
        }

    }

    // 初始化分享相关功能
    public void initShare() {
        TKShareHelper.INSTANCE.init(this);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //1.初始化AutoSize
        ScreenTools.getInstance().initAutoSize(this, this);

        //2.日志类加载初始化
        new LogUtils.Builder()
                .setLogSwitch(AppUtils.isAppDebug(this))//设置log总开关，默认开
                //.setGlobalTag("CMJ")// 设置log全局标签，默认为空
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(false)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose
        //3.加载全部异常捕获
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        //4.初始化Stetho出正式包的时候，建议屏蔽掉
        if (AppUtils.isAppDebug(this)) {
            Stetho.initializeWithDefaults(this);
        }
        //5.注册Activity生命周期监听回调，此部分一定加上，因为有些版本不加的话多语言切换不回来
        initActivityLifecycle();

    }

    @Override
    protected void attachBaseContext(Context base) {
        //系统语言等设置发生改变时会调用此方法，需要要重置app语言
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(base));
//        super.attachBaseContext(base);
    }


    /**
     * 初始化ActivityLifecycle
     */
    private void initActivityLifecycle() {
        registerActivityLifecycleCallbacks(lifecycleCallbacks);
    }


    public void initBugly() {
        // a6bb433246（公司的）  5b29dde48c（自己的） //正式需改
        String buglyKey = "5b29dde48c";
        if ("release".equals(BuildConfig.host)) {
            buglyKey = "a6bb433246";
        } else {
            buglyKey = "5b29dde48c";
        }
        CrashReport.initCrashReport(getApplicationContext(), buglyKey, false);
    }

    // 初始sdk相关
    public void initCoreLib() {
        ScreenScale.init(this);//适配方案
        //没有appid 暂时不需要umeng功能
        TKPluginsManager.getInstance().setContext(this).setPluginShareType(TKPluginsConstants.PLUGIN_SHARE_TYPE_NONE).setPluginStatisticsType(TKPluginsConstants.PLUGIN_STATISTICS_TYPE_NONE).init();
//        TKPluginsManager.getInstance().setContext(this).setShareData("appId"/微信,"appSecret"/微信).setStatisticsData("appId","channel").init();//配置umeng 具体详见2.5
        // 解决通过反射解决在HuaWei手机出现Register too many Broadcast Receivers的crash
        if (!Tools.phoneIsSony(this)) {
            LoadedApkHuaWei.hookHuaWeiVerifier(this);
        }
    }

    public void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        //开启crashlog日志上报
        JPushInterface.initCrashHandler(this);

        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
        String lastAlis = MMKV.defaultMMKV().getString(TagAliasOperatorHelper.ALIAS_DATA, "");
        Log.d("PushMessageReceiver", "   registrationID:" + registrationID + "lastAlis" + lastAlis);

    }

    public void initKV() {
        MMKV.initialize(this);
    }

}
