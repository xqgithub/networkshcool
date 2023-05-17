package com.talkcloud.networkshcool.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;


/**
 * desc  : 吐司相关工具类
 */
public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static Toast sToast;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static boolean isJumpWhenMore;
    public static final int center = 1;
    public static final int bottom = 2;
    public static final int top = 3;


    /**
     * 吐司初始化
     *
     * @param isJumpWhenMore 当连续弹出吐司时，是要弹出新吐司还是只修改文本内容
     *                       <p>{@code true}: 弹出新吐司<br>{@code false}: 只修改文本内容</p>
     *                       <p>如果为{@code false}的话可用来做显示任意时长的吐司</p>
     */
    public static void init(boolean isJumpWhenMore) {
        ToastUtils.isJumpWhenMore = isJumpWhenMore;
    }

    /**
     * 安全地显示短时吐司
     *
     * @param text 文本
     */
    public static void showShortToastFromText(final CharSequence text, final int locationflag) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_SHORT, locationflag);
            }
        });
    }


    /**
     * 显示在中间 短
     */
    public static void showShortCenter(final CharSequence text) {
        showShortToastFromText(text, ToastUtils.top);
    }

    /**
     * 显示在中间 短
     */
    public static void showShortTop(final CharSequence text) {
        showShortToastFromText(text, ToastUtils.top);
    }


    /**
     * 显示短时吐司,资源文件获取
     */
    public static void showShortToastFromRes(final @StringRes int resId, int locationflag) {
        showShortToastFromText(TKBaseApplication.myApplication.getResources().getString(resId), locationflag);
    }

    /**
     * 重载 默认底部显示
     *
     * @param resId
     */
    public static void showShortToastFromRes(final @StringRes int resId) {
        showShortToastFromText(TKBaseApplication.myApplication.getResources().getString(resId), ToastUtils.top);
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongToastFromText(final CharSequence text, final int locationflag) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, Toast.LENGTH_LONG, locationflag);
            }
        });
    }

    /**
     * 显示长时吐司
     */
    public static void showLongToastFromRes(final @StringRes int resId, int locationflag) {
        showLongToastFromText(TKBaseApplication.myApplication.getResources().getString(resId), locationflag);
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static TextView textview;
    private static int lastlocation = 0;

    private static void showToast(CharSequence text, int duration, int locationflag) {
        try {
            cancelToast();
            if (locationflag == top) {
                if (sToast == null) {
                    sToast = Toast.makeText(TKBaseApplication.myApplication.getApplicationContext(), text, duration);
                } else {
                    sToast.setText(text);
                }
                sToast.setDuration(duration);
                sToast.setGravity(Gravity.TOP, 0, ConvertUtils.dp2px(100f));
                sToast.show();
            } else if (locationflag == center) {
                if (sToast == null) {
                    sToast = Toast.makeText(TKBaseApplication.myApplication.getApplicationContext(), text, duration);
                } else {
                    sToast.setText(text);
                }
                sToast.setDuration(duration);
                sToast.setGravity(Gravity.CENTER, 0, 0);
                sToast.show();
            } else if (locationflag == bottom) {
                if (sToast == null) {
                    //            sToast = Toast.makeText(Utils.getContext(), text, duration);
                    View view = Toast.makeText(TKBaseApplication.myApplication.getApplicationContext(), "", duration).getView();
                    sToast = new Toast(TKBaseApplication.myApplication.getApplicationContext());
                    sToast.setView(view);
                    sToast.setText(text);
                    sToast.setDuration(duration);
                } else {
                    sToast.setText(text);
                    sToast.setDuration(duration);
                }
                sToast.show();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        } finally {
            lastlocation = locationflag;
        }
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}