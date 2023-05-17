package com.talkcloud.networkshcool.baselibrary.jpush;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkListActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkWriteActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.MainMenuActivity;

import org.json.JSONObject;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);
        Intent intent = new Intent("com.jiguang.demo.message");
        intent.putExtra("msg", customMessage.message);
        context.sendBroadcast(intent);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        Log.d(TAG, "[onNotifyMessageOpened] " + message);
        try {
            if (message != null && message.notificationExtras != null) {
                JSONObject jsonObject_data = new JSONObject(message.notificationExtras);
                if (jsonObject_data != null && jsonObject_data.get("type") != null) {
                    if (jsonObject_data.get("type").equals("homework.remind")) { //作业提醒 跳转作业详情
                        String homework_id = "";
                        String students_id = "";
                        if (jsonObject_data.get("students_id") != null) {
                            students_id = jsonObject_data.get("students_id") + "";
                        }
                        if (jsonObject_data.get("homework_id") != null) {
                            homework_id = jsonObject_data.get("homework_id") + "";
                        }

                        Intent notifyIntent = new Intent();
                        Intent mainIntent = new Intent();
                        notifyIntent.setClass(context, HomeworkWriteActivity.class);
                        notifyIntent.putExtra(BaseConstant.KEY_PARAM1, homework_id);
                        notifyIntent.putExtra(BaseConstant.KEY_PARAM2, students_id);

                        mainIntent.setClass(context, MainMenuActivity.class);
                        //需要注意这里的顺序
                        Intent[] intents = new Intent[]{mainIntent, notifyIntent};
                        PendingIntent pendingIntent = PendingIntent.
                                getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent.send();
                    } else if (jsonObject_data.get("type").equals("homework.assign")) { // 作业布置通知 跳转到作业列表

                        Intent notifyIntent = new Intent();
                        Intent mainIntent = new Intent();
                        notifyIntent.setClass(context, HomeworkListActivity.class);
                        mainIntent.setClass(context, MainMenuActivity.class);
                        //需要注意这里的顺序
                        Intent[] intents = new Intent[]{mainIntent, notifyIntent};
                        PendingIntent pendingIntent = PendingIntent.
                                getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent.send();
                    } else if (jsonObject_data.get("type").equals("homework.remark")) { //作业点评
                        String homework_id = "";
                        String students_id = "";
                        if (jsonObject_data.get("students_id") != null) {
                            students_id = jsonObject_data.get("students_id") + "";
                        }
                        if (jsonObject_data.get("homework_id") != null) {
                            homework_id = jsonObject_data.get("homework_id") + "";
                        }

                        Intent notifyIntent = new Intent();
                        Intent mainIntent = new Intent();
                        notifyIntent.setClass(context, HomeworkWriteActivity.class);
                        notifyIntent.putExtra(BaseConstant.KEY_PARAM1, homework_id);
                        notifyIntent.putExtra(BaseConstant.KEY_PARAM2, students_id);

                        mainIntent.setClass(context, MainMenuActivity.class);
                        //需要注意这里的顺序
                        Intent[] intents = new Intent[]{mainIntent, notifyIntent};
                        PendingIntent pendingIntent = PendingIntent.
                                getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);
                        pendingIntent.send();
                    } else if (jsonObject_data.get("type").equals("course.go_to_class")) { //上课通知
                        Intent i = new Intent(context, MainMenuActivity.class);  //跳转到主页
                        Bundle bundle = new Bundle();
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }
                }
            }

        } catch (Throwable throwable) {
            Log.d(TAG, "[throwable] " + throwable.toString());
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.d(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.d(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.d(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.d(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.d(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        Log.d(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.d(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
        Intent intent = new Intent("com.jiguang.demo.register");
        context.sendBroadcast(intent);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Log.e(TAG, "[onTagOperatorResult] " + jPushMessage);
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);
        Log.e(TAG, "[onNotificationSettingsCheck] isOn:" + isOn + ",source:" + source);
    }

}
