package com.talkcloud.networkshcool.baselibrary.help;

import android.os.CountDownTimer;

import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Date:2021/5/12
 * Time:18:07
 * author:joker
 * 时间倒计时器
 */
public class TimeCountdown extends CountDownTimer {

    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */

    private MessageEvent messageEvent;

    public TimeCountdown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        messageEvent = new MessageEvent();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        VariableConfig.verificationcode_countdown_flag = false;
        long time = millisUntilFinished / 1000L;
        messageEvent.setMessage_type(EventConstant.EVENT_VERIFICATIONCODE_COUNTDOWN);
        messageEvent.setMessage(time);
        EventBus.getDefault().post(messageEvent);
//        LogUtils.i(ConfigConstants.TAG_ALL, "time =-= " + time);
    }

    @Override
    public void onFinish() {
        VariableConfig.verificationcode_countdown_flag = true;
        MySPUtilsUser.getInstance().saveMobileTemp("");
        long time = -1L;
        messageEvent.setMessage_type(EventConstant.EVENT_VERIFICATIONCODE_COUNTDOWN);
        messageEvent.setMessage(time);
        EventBus.getDefault().post(messageEvent);
        LogUtils.i(ConfigConstants.TAG_ALL, "onFinish =-= ");
    }
}
