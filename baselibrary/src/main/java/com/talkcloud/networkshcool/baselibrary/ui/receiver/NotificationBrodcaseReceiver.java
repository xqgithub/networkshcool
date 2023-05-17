package com.talkcloud.networkshcool.baselibrary.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Date:2021/5/25
 * Time:11:00
 * author:joker
 */
public class NotificationBrodcaseReceiver extends BroadcastReceiver {

    public static ICallBack miCallBack;

    public static void setiCallBack(ICallBack iCallBack) {
        miCallBack = iCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!StringUtils.isBlank(action)) {
            if (ConfigConstants.NOTIFACATIO_CLOSE.equals(action)) {
                miCallBack.callBack(intent.getIntExtra("id", 0));
            }
        }
    }


    /**
     * 回调接口
     */
    public interface ICallBack {
        void callBack(int id);
    }
}
