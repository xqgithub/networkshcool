package com.talkcloud.networkshcool.baselibrary.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import com.tencent.mmkv.MMKV;

import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;

/**
 * 处理tagalias相关的逻辑
 */
public class TagAliasOperatorHelper {
    private static final String TAG = "JIGUANG-TagAliasHelper";

    private Context context;

    /**
     * 增加
     */
    public static final int ACTION_ADD = 1;
    /**
     * 覆盖
     */
    public static final int ACTION_SET = 2;
    /**
     * 删除部分
     */
    public static final int ACTION_DELETE = 3;
    /**
     * 删除所有
     */
    public static final int ACTION_CLEAN = 4;
    /**
     * 查询
     */
    public static final int ACTION_GET = 5;

    public static final int ACTION_CHECK = 6;

    public static final int DELAY_SEND_ACTION = 1;

    public static final int DELAY_SET_MOBILE_NUMBER_ACTION = 2;

    public static int sequence = 1;

    private static TagAliasOperatorHelper mInstance;

    private TagAliasOperatorHelper() {
    }

    public static TagAliasOperatorHelper getInstance() {
        if (mInstance == null) {
            synchronized (TagAliasOperatorHelper.class) {
                if (mInstance == null) {
                    mInstance = new TagAliasOperatorHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }


    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onTagOperatorResult, sequence:" + sequence + ",tags:" + jPushMessage.getTags());
        Log.i(TAG, "tags size:" + jPushMessage.getTags().size());
        init(context);

        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify tag Success,sequence:" + sequence);
            //  ToastHelper.showOk(context,"modify success");
        } else {
            String logs = "Failed to modify tags";
            if (jPushMessage.getErrorCode() == 6018) {
                //tag数量超过限制,需要先清除一部分再add
                logs += ", tags is exceed limit need to clean";
            }
            logs += ", errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
            //     ToastHelper.showOther(context,logs);
        }
    }

    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onCheckTagOperatorResult, sequence:" + sequence + ",checktag:" + jPushMessage.getCheckTag());
        init(context);
        if (jPushMessage.getErrorCode() == 0) {
            String logs = "modify tag " + jPushMessage.getCheckTag() + " bind state success,state:" + jPushMessage.getTagCheckStateResult();
            Log.i(TAG, logs);
            //    ToastHelper.showOk(context,"modify success");
        } else {
            String logs = "Failed to modify tags, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
            //      ToastHelper.showOther(context,logs);
        }
    }

    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onAliasOperatorResult, sequence:" + sequence + ",alias:" + jPushMessage.getAlias());
        init(context);

        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - modify alias Success,sequence:" + sequence);
            //      ToastHelper.showOk(context,"modify success");
        } else {
            String logs = "Failed to modify alias, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
            //  ToastHelper.showOther(context,logs);
            //  MMKV.defaultMMKV().putString(ALIAS_DATA, "");
            TagAliasBean tagAliasBean = new TagAliasBean();
            tagAliasBean.isAliasAction = true;
            tagAliasBean.alias = MMKV.defaultMMKV().getString(ALIAS_DATA, "");
            tagAliasBean.action = ACTION_SET;
            RetryActionIfNeeded(jPushMessage.getErrorCode(), tagAliasBean);
        }
    }

    //设置手机号码回调
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        int sequence = jPushMessage.getSequence();
        Log.i(TAG, "action - onMobileNumberOperatorResult, sequence:" + sequence + ",mobileNumber:" + jPushMessage.getMobileNumber());
        init(context);
        if (jPushMessage.getErrorCode() == 0) {
            Log.i(TAG, "action - set mobile number Success,sequence:" + sequence);
            //   ToastHelper.showOk(context,"modify success");
        } else {
            String logs = "Failed to set mobile number, errorCode:" + jPushMessage.getErrorCode();
            Log.e(TAG, logs);
            //     ToastHelper.showOther(context,logs);
            MMKV.defaultMMKV().putString(MN_DATA, "");

        }
    }

    public static final String ALIAS_DATA = "alias_data";
    public static final String MN_DATA = "mn_data";

    private SparseArray<Object> setActionCache = new SparseArray<Object>();

    public Object get(int sequence) {
        return setActionCache.get(sequence);
    }

    public Object remove(int sequence) {
        return setActionCache.get(sequence);
    }

    public void put(int sequence, Object tagAliasBean) {
        setActionCache.put(sequence, tagAliasBean);
    }

    @SuppressLint("HandlerLeak")
    private Handler delaySendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DELAY_SEND_ACTION:
                    if (msg.obj != null && msg.obj instanceof TagAliasBean) {
                        Logger.d(TAG, "on delay time");
                        sequence++;
                        TagAliasBean tagAliasBean = (TagAliasBean) msg.obj;
                        setActionCache.put(sequence, tagAliasBean);
                        if (context != null) {
                            handleAction(context, sequence, tagAliasBean);
                            Logger.d(TAG, "on delay time ");
                        } else {
                            Logger.e(TAG, "#unexcepted - context was null");
                        }
                    } else {
                        Logger.w(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
                case DELAY_SET_MOBILE_NUMBER_ACTION:
                    if (msg.obj != null && msg.obj instanceof String) {
                        Logger.i(TAG, "retry set mobile number");
                        sequence++;
                        String mobileNumber = (String) msg.obj;
                        setActionCache.put(sequence, mobileNumber);
                        if (context != null) {
                            handleAction(context, sequence, mobileNumber);
                        } else {
                            Logger.e(TAG, "#unexcepted - context was null");
                        }
                    } else {
                        Logger.w(TAG, "#unexcepted - msg obj was incorrect");
                    }
                    break;
            }
        }
    };

    public void handleAction(Context context, int sequence, String mobileNumber) {
        put(sequence, mobileNumber);
        Logger.d(TAG, "sequence:" + sequence + ",mobileNumber:" + mobileNumber);
        JPushInterface.setMobileNumber(context, sequence, mobileNumber);
    }

    /**
     * 处理设置tag
     */
    public void handleAction(Context context, int sequence, TagAliasBean tagAliasBean) {
        init(context);
        Logger.d(TAG, "handleAction" + tagAliasBean.toString());
        if (tagAliasBean == null) {
            Logger.w(TAG, "tagAliasBean was null");
            return;
        }
        put(sequence, tagAliasBean);
        if (tagAliasBean.isAliasAction) {
            switch (tagAliasBean.action) {
                case ACTION_GET:
                    JPushInterface.getAlias(context, sequence);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteAlias(context, sequence);
                    break;
                case ACTION_SET:
                    JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
                    break;
                default:
                    Logger.w(TAG, "unsupport alias action type");
                    return;
            }
        } else {
            switch (tagAliasBean.action) {
                case ACTION_ADD:
                    JPushInterface.addTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_SET:
                    JPushInterface.setTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_DELETE:
                    JPushInterface.deleteTags(context, sequence, tagAliasBean.tags);
                    break;
                case ACTION_CHECK:
                    //一次只能check一个tag
                    String tag = (String) tagAliasBean.tags.toArray()[0];
                    JPushInterface.checkTagBindState(context, sequence, tag);
                    break;
                case ACTION_GET:
                    JPushInterface.getAllTags(context, sequence);
                    break;
                case ACTION_CLEAN:
                    JPushInterface.cleanTags(context, sequence);
                    break;
                default:
                    Logger.w(TAG, "unsupport tag action type");
                    return;
            }
        }
    }

    private boolean RetryActionIfNeeded(int errorCode, TagAliasBean tagAliasBean) {
        /*if (!ExampleUtil.isConnected(context)) {
            Logger.w(TAG, "no network");
            return false;
        }*/
        //返回的错误码为6002 超时,6014 服务器繁忙,都建议延迟重试
        if (errorCode == 6002 || errorCode == 6014 || errorCode == 6022) {
            Logger.d(TAG, "need retry");
            if (tagAliasBean != null) {
                Message message = new Message();
                message.what = DELAY_SEND_ACTION;
                message.obj = tagAliasBean;
                //   delaySendHandler.sendMessageDelayed(message, 0);
                String logs = getRetryStr(tagAliasBean.isAliasAction, tagAliasBean.action, errorCode);
           //     ExampleUtil.showToast(logs, context);


                Timer retrySet = null;
                retrySet = new Timer();
                retrySet.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (sequence < 10) //最多重试10次
                            JPushInterface.setAlias(context, sequence++, tagAliasBean.alias);
                    }
                }, 10000); //10s钟内重试
                return true;
            }
        }
        return false;
    }

    private boolean RetrySetMObileNumberActionIfNeeded(int errorCode, String mobileNumber) {
        if (!ExampleUtil.isConnected(context)) {
            Logger.w(TAG, "no network");
            return false;
        }
        //返回的错误码为6002 超时,6024 服务器内部错误,建议稍后重试
        if (errorCode == 6002 || errorCode == 6024) {
            Logger.d(TAG, "need retry");
            Message message = new Message();
            message.what = DELAY_SET_MOBILE_NUMBER_ACTION;
            message.obj = mobileNumber;
            delaySendHandler.sendMessageDelayed(message, 1000 * 60);
            String str = "Failed to set mobile number due to %s. Try again after 60s.";
            str = String.format(Locale.ENGLISH, str, (errorCode == 6002 ? "timeout" : "server internal error”"));
            ExampleUtil.showToast(str, context);
            return true;
        }
        return false;

    }

    private String getRetryStr(boolean isAliasAction, int actionType, int errorCode) {
        String str = "Failed to %s %s due to %s. Try again after 60s.";
        str = String.format(Locale.ENGLISH, str, getActionStr(actionType), (isAliasAction ? "alias" : " tags"), (errorCode == 6002 ? "timeout" : "server too busy"));
        return str;
    }

    private String getActionStr(int actionType) {
        switch (actionType) {
            case ACTION_ADD:
                return "add";
            case ACTION_SET:
                return "set";
            case ACTION_DELETE:
                return "delete";
            case ACTION_GET:
                return "get";
            case ACTION_CLEAN:
                return "clean";
            case ACTION_CHECK:
                return "check";
        }
        return "unkonw operation";
    }


    public static class TagAliasBean {
        int action;
        Set<String> tags;
        String alias;
        boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction +
                    '}';
        }
    }

}
