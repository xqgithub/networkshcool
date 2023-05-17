package com.talkcloud.networkshcool.baselibrary.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.talkcloud.networkshcool.baselibrary.R;

/**
 * Author  guoyw
 * Date    2021/7/29
 * Desc    meta util
 */
public class TKMetaUtil {

    // 获取app 到资源
    public static int getAppRes(Context context, String key) {

        Bundle metaData = null;
        int appRes = 0;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appRes = metaData.getInt(key);
                if (appRes == 0) {
                    appRes = R.string.app_name;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appRes;
    }

}
