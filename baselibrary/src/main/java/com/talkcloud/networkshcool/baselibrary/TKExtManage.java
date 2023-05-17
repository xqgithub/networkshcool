package com.talkcloud.networkshcool.baselibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Author  guoyw
 * Date    2021/7/29
 * Desc    第三方定制管理
 */
public class TKExtManage {

    private static final String APP_LOGO = "APP_LOGO";
    private static final String APP_NAME = "APP_NAME";
    private static final String START_LOGO = "START_LOGO";
    private static final String APP_SCHEME = "APP_SCHEME";

    private static volatile TKExtManage mInstance;


    //企业ID
    private String companyId = "";
    //企业域名
    private String companyDomain = "";

    private String homeExtUrl = "";
    private boolean isShowHomeExtBtn = false;

    // 用户协议 或者 隐私协议  关键字
    private String userAgreementUrl = "";

    private int launcherRes;

    private TKExtManage() {
        if (mInstance != null) {
            throw new RuntimeException("instance is exist!");
        }
    }

    public static TKExtManage getInstance() {
        if (mInstance == null) {
            synchronized (TKExtManage.class) {
                if (mInstance == null) {
                    mInstance = new TKExtManage();
                }
            }
        }
        return mInstance;
    }


    // 第三方首页 管理
    public String getHomeExtUrl() {
        return homeExtUrl;
    }

    public void setHomeExtUrl(String homeExtUrl) {
        this.homeExtUrl = homeExtUrl;
    }

    // 第三方首页按钮是否 展示
    public boolean isShowHomeExtBtn() {
        return isShowHomeExtBtn;
    }

    public void setShowHomeExtBtn(boolean showHomeExtBtn) {
        isShowHomeExtBtn = showHomeExtBtn;
    }


    // 设置companyId
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserAgreementUrl() {
        return userAgreementUrl;
    }

    public void setUserAgreementUrl(String userAgreementUrl) {
        this.userAgreementUrl = userAgreementUrl;
    }

    public String getCompanyDomain() {
        return companyDomain;
    }

    public void setCompanyDomain(String companyDomain) {
        this.companyDomain = companyDomain;
    }

    // 获取图标icon
    public int getAppLogoRes(Context context) {
        int appLogo = getAppRes(context, APP_LOGO);
        if (appLogo == 0) {
            appLogo = R.mipmap.ic_launcher;
        }
        return appLogo;
    }

    // 获取App Name 资源
    public int getAppNameRes(Context context) {
        int appName = getAppRes(context, APP_NAME);
        if (appName == 0) {
            appName = R.string.app_name;
        }
        return appName;
    }

    /**
     * 获取启动页图标
     *
     * @param context
     * @return
     */
    public int getStartLogo(Context context) {
        int startlogo = getAppRes(context, START_LOGO);
        if (startlogo == 0) {
            startlogo = R.mipmap.start_logo;
        }
        return startlogo;
    }


    /**
     * 获取 Scheme 数据
     *
     * @param context
     * @return
     */
    public String getAppScheme(Context context) {
        String scheme = "";
        Bundle bundle_metadata = getAppMetaData(context);
        if (!StringUtils.isBlank(bundle_metadata)) {
            scheme = bundle_metadata.getString(APP_SCHEME);
        }
        return scheme;
    }


    // 获取app 到资源
    public int getAppRes(Context context, String key) {

        Bundle metaData = null;
        int appRes = 0;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appRes = metaData.getInt(key);
//                if (appRes == 0) {
//                    appRes = R.string.app_name;
//                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appRes;
    }

    /**
     * 获取AppMetaData数据
     *
     * @param context
     * @return
     */
    public Bundle getAppMetaData(Context context) {
        Bundle metaData = null;
        int appRes = 0;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaData;
    }

}
