package com.talkcloud.networkshcool.baselibrary.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eduhdsdk.interfaces.JoinmeetingCallBack;
import com.eduhdsdk.interfaces.MeetingNotify;
import com.eduhdsdk.room.RoomClient;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.presenters.JoinRoomPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.JoinRoomView;
import com.talkcloud.room.TKRoomManager;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Author  guoyw
 * Date    2021/5/25
 * Desc    进入教室base 后期优化继承BaseMvpActivity
 */
public abstract class BaseJoinRoomFragment extends BaseFragment implements JoinRoomView, JoinmeetingCallBack, MeetingNotify {

    // 进入教室相关
    protected JoinRoomPresenter joinRoomPresenter;

    protected static boolean isJoiningRoom;

    // 记录教室id
    protected static String mSerialId;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d("goyw", "BaseJoinRoomFragment");

        RoomClient.getInstance().regiestInterface(this, this);

        joinRoomPresenter = new JoinRoomPresenter(mActivity, this);

    }

    @Override
    public void onStop() {
        super.onStop();
        isJoiningRoom = false;
    }

    @Override
    public void callBack(int nRet) {

        isJoiningRoom = false;

        if (isAdded()) { //如果被添加到activity中(长时间息屏，可能会导致activity销毁)
            Log.d("goyw", "callBack : " + nRet);

            String result = "";
            if (nRet == 0) {
//                isJoiningRoom = false;
            } else if (nRet == 100) {
                // 踢人重置  登陆按钮
//            if (re_loading != null) {
//                re_loading.setVisibility(View.GONE);
//
//            }

            } else if (nRet == 101) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_5005);
            } else if (nRet == 4008 || nRet == 4012) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4008);
            } else if (nRet == 4110) {
//            startactivityInputPwdActivity();
            } else if (nRet == 4007) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4007);
            } else if (nRet == 3001) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_3001);
            } else if (nRet == 3002) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_3002);
            } else if (nRet == 3003) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_3003);
            } else if (nRet == 3004) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_3004);
            } else if (nRet == 4109) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4109);
            } else if (nRet == 4103) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4103);
            } else if (nRet == 4112) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4112);
            } else if (nRet == 4020) {//添加错误码需要在roomsession里面添加下
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4020);
            } else if (nRet == 4113) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_4113);
            } else if (nRet == 1502) {
                result = getString(com.talkcloud.corelibrary.R.string.checkmeeting_error_1502);
            } else if (nRet == 4116) {
                result = getString(com.talkcloud.corelibrary.R.string.error_4116);
            } else {
                if (nRet == 3 || nRet == 11 || nRet == 801 || nRet == 802) {
                    result = getString(com.talkcloud.corelibrary.R.string.WaitingForNetwork);
                } else if (nRet == -1) {
                    result = getString(com.talkcloud.corelibrary.R.string.getroommessageerror);
                } else {
                    result = getString(com.talkcloud.corelibrary.R.string.please_contact_customer, nRet);
                }
            }

            if (!TextUtils.isEmpty(result)) {
//                Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
                ToastUtils.showShortTop(result);
            }

        }
    }

    @Override
    public void onKickOut(int i, String s) {

    }

    @Override
    public void onWarning(int i) {

    }

    @Override
    public void onClassBegin() {

    }

    @Override
    public void onClassDismiss() {

    }

    @Override
    public void onLeftRoomComplete() {
//        NSLog.d("fragment 离开教室");
        //发送事件
//        MessageEvent event = new MessageEvent();
//        event.setMessage_type(EventConstant.MY_FLOWS_REFRESH);
//        EventBus.getDefault().post(event);
        // TODO 刷新我的页面的小红花
//        EventBus.getDefault().post(new MessageEvent(EventConstant.MY_FLOWS_REFRESH));

        //  测试要退出刷新教室状态
//        NSLog.d("mSerialId : " + mSerialId);

        Map<String, String> map = new HashMap<>();
        map.put("lessonId", mSerialId);
        EventBus.getDefault().post(new MessageEvent(EventConstant.EVENT_PUBLISH_HOMEWORK_SUCCESS, map));

    }

    @Override
    public void onEnterRoomFailed(int i, String s) {

    }

    @Override
    public void joinRoomComplete() {

    }

    @Override
    public void onCameraDidOpenError() {

    }

    //权限回调  requestCode  4
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals("android.permission.CAMERA")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasVideo = true;
                    TKRoomManager.getInstance().getMySelf().setHasVideo(true);
                }
            } else if (permissions[i].equals("android.permission.RECORD_AUDIO")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasAudio = true;
                    TKRoomManager.getInstance().getMySelf().setHasAudio(true);
                }
            }
        }
        RoomClient.getInstance().checkPermissionsFinshJoinRoom(mActivity);
    }

//    @Override
//    public void onOpenEyeProtection(boolean b) {
//
//    }

    @Override
    public void onOpenEyeProtection(boolean isOpen) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void hideSuccessLoading() {

    }

    @Override
    public void showFailed() {

    }


}
