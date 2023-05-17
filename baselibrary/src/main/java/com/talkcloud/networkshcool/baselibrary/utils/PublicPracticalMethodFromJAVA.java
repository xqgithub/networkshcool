package com.talkcloud.networkshcool.baselibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.activities.ChooseIdentityActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/11
 * Time:8:44
 * author:joker
 * 公共实用类
 */
public class PublicPracticalMethodFromJAVA {

    private volatile static PublicPracticalMethodFromJAVA mInstance;

    /**
     * 单例模式
     */
    public static PublicPracticalMethodFromJAVA getInstance() {
        if (mInstance == null) {
            synchronized (PublicPracticalMethodFromJAVA.class) {
                if (mInstance == null) {
                    mInstance = new PublicPracticalMethodFromJAVA();
                }
            }
        }
        return mInstance;
    }


    /**
     * 00001
     * 1.解决app点击桌面图标每次重新启动
     * 2.当是web页面跳转我们app的时候，如果本应用已经在后台，那么直接唤起；如果没有则重新启动
     *
     * @param activity
     */
    public void restartApp(Activity activity) {
        if (!activity.isTaskRoot()) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                String scheme = intent.getScheme();
                if (!StringUtils.isBlank(scheme) && scheme.equals(TKExtManage.getInstance().getAppScheme(activity))) {
                    activity.finish();
                }
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                        Intent.ACTION_MAIN.equals(action)) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 应用信息界面
     *
     * @param activity
     */
    public void toApplicationInfo(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     *
     * @param activity
     */
    public void toSystemConfig(Activity activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到权限设置
     *
     * @param activity
     */
    public void toPermissionSetting(Activity activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            toSystemConfig(activity);
        } else {
            try {
                toApplicationInfo(activity);
            } catch (Exception e) {
                e.printStackTrace();
                toSystemConfig(activity);
            }
        }
    }

    /**
     * 获得手机屏幕信息
     */
    public void getPhoneScreenInfo(Context context) {
        ScreenTools.getInstance().getDisplayAreaInformation(context);
        ScreenTools.getInstance().getEquipmentInformation(context);
    }


    /**
     * 状态栏透明方法
     * - SYSTEM_UI_FLAG_LOW_PROFILE   设置状态栏和导航栏中的图标变小，变模糊或者弱化其效果。这个标志一般用于游戏，电子书，视频，或者不需要去分散用户注意力的应用软件。
     * - SYSTEM_UI_FLAG_HIDE_NAVIGATION  隐藏导航栏，点击屏幕任意区域，导航栏将重新出现，并且不会自动消失
     * - SYSTEM_UI_FLAG_FULLSCREEN       隐藏状态栏，点击屏幕区域不会出现，需要从状态栏位置下拉才会出现
     * - SYSTEM_UI_FLAG_LAYOUT_STABLE    稳定布局，主要是在全屏和非全屏切换时，布局不要有大的变化。一般和View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN、View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION搭配使用。同时，android:fitsSystemWindows要设置为true
     * - SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  将布局内容拓展到导航栏的后面
     * - SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN     将布局内容拓展到状态的后面
     * - SYSTEM_UI_FLAG_IMMERSIVE    使状态栏和导航栏真正的进入沉浸模式,即全屏模式，如果没有设置这个标志，设置全屏时，我们点击屏幕的任意位置，就会恢复为正常模式
     * - SYSTEM_UI_FLAG_IMMERSIVE_STICKY  它的效果跟View.SYSTEM_UI_FLAG_IMMERSIVE一样。但是，它在全屏模式下，用户上下拉状态栏或者导航栏时，这些系统栏只是以半透明的状态显示出来，并且在一定时间后会自动消息
     *
     * @param useThemestatusBarColor 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     * @param useStatusBarColor      是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    public void transparentStatusBar(Activity activity,
                                     boolean useThemestatusBarColor,
                                     boolean useStatusBarColor,
                                     @ColorRes int color) {
//        //适配Android 4.4 +的方法
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = activity.getWindow();
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//        //适配 Android 5.0+ 的方法
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }

        Window window = activity.getWindow();
        //5.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

  /*          window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View decorView = window.getDecorView();*/
         /*   int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(options);*/

//            if (useThemestatusBarColor) {
//                window.setStatusBarColor(activity.getResources().getColor(color));
//            } else {
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }

            //获取护眼模式的状态
            Boolean eyeprotection = MySPUtilsUser.getInstance().getUserEyeProtectionStatus();
            if (eyeprotection) {
                window.setStatusBarColor(Color.parseColor("#33FF7A00"));
            } else {
                if (useThemestatusBarColor) {
                    window.setStatusBarColor(ContextCompat.getColor(activity, color));
                } else {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = window.getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    | localLayoutParams.flags);
        }

        //android6.0以后可以对状态栏文字颜色和图标进行修改
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (useStatusBarColor) {//暗色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {//浅色
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }


    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * handler运行
     */
    public void runHandlerFun(Handler handler, int whatcode, long delayMillis) {
        Message message = Message.obtain();
        message.what = whatcode;
        handler.sendMessageDelayed(message, delayMillis);
    }

    /**
     * 页面跳转
     *
     * @param mActivity   上下文
     * @param cls         目标的Activity
     * @param flag        跳转的方式
     * @param bundle      bundle值
     * @param isFinish    进入页面后，是否结束上一个页面
     * @param enterAnimID 进入动画ID
     * @param exitAnimID  退出动画ID
     */
    public void intentToJump(Activity mActivity, Class<?> cls, int flag, Bundle bundle, boolean isFinish, int enterAnimID, int exitAnimID) {
        Intent intent = new Intent(mActivity, cls);
        if (!StringUtils.isBlank(bundle)) {
            intent.putExtras(bundle);
        }
        if (flag != -1) {
            intent.setFlags(flag);
        }

        mActivity.startActivity(intent);

        if (isFinish) {
            mActivity.finish();
        }

        if (!StringUtils.isBlank(enterAnimID) || !StringUtils.isBlank(exitAnimID)) {
            if (isFinish) {
                mActivity.overridePendingTransition(enterAnimID, exitAnimID);
            } else {
                mActivity.overridePendingTransition(enterAnimID, R.anim.activity_xhold);
            }
        }
    }

    /**
     * 关闭栈内除了栈顶的所有的Activitys
     */
    public void closeActivitys() {
        List<Activity> activities = TKBaseApplication.myApplication.lifecycleCallbacks.getActivitys();
        if (!StringUtils.isBlank(activities) && activities.size() > 1) {
            for (int i = 1; i < activities.size(); i++) {
                Activity activity = activities.get(i);
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    /**
     * Acticity 页面关闭,可以传动动画文件
     *
     * @param mActivity
     * @param exitAnimID
     */
    public void activityFinish(Activity mActivity, int exitAnimID) {
        mActivity.finish();
        mActivity.overridePendingTransition(0, exitAnimID);
    }

    /**
     * 动态设置Shape  RECTANGLE
     */
    public void setDynamicShapeRECTANGLE(Context mContext, View view, float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.RECTANGLE);

        //设置shape的圆角度数
        if (!StringUtils.isBlank(CornerRadius) && CornerRadius != -1) {
            drawable.setCornerRadius(CornerRadius);
        }

        //设置shape的边的宽度和颜色
        if (!StringUtils.isBlank(strokewidth) && strokewidth != -1
                && !StringUtils.isBlank(strokeColor)) {
//            drawable.setStroke(strokewidth, ContextCompat.getColor(mContext, R.color.appblack));
            drawable.setStroke(strokewidth, Color.parseColor(strokeColor));
        }

        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
//            drawable.setColor(ContextCompat.getColor(mContext, bgcolor));
            drawable.setColor(Color.parseColor(bgcolor));
        }
        view.setBackground(drawable);
    }


    /**
     * 动态设置Shape  RECTANGLE        背景色或者渐变色
     *
     * @param view                    被设置的对象view
     * @param CornerRadiusLeftTop     左上角度数
     * @param CornerRadiusRightTop    右上角度数
     * @param CornerRadiusLeftBottom  左下角度数
     * @param CornerRadiusRightBottom 右下角度数
     * @param strokewidth             边线的宽度
     * @param strokeColor             边线的颜色
     * @param orientation             渐变方向
     * @param bgcolor                 渐变色
     */
    public void setDynamicShapeRECTANGLE2(View view,
                                          float CornerRadiusLeftTop, float CornerRadiusRightTop, float CornerRadiusLeftBottom, float CornerRadiusRightBottom,
                                          int strokewidth, String strokeColor, GradientDrawable.Orientation orientation, String... bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.RECTANGLE);

        //设置shape的圆角度数
        float[] radius = {CornerRadiusLeftTop, CornerRadiusLeftTop, CornerRadiusRightTop, CornerRadiusRightTop, CornerRadiusLeftBottom,
                CornerRadiusLeftBottom, CornerRadiusRightBottom, CornerRadiusRightBottom};
        drawable.setCornerRadii(radius);

        //设置shape的边的宽度和颜色
        if (!StringUtils.isBlank(strokewidth) && strokewidth != -1
                && !StringUtils.isBlank(strokeColor)) {
            drawable.setStroke(strokewidth, Color.parseColor(strokeColor));
        }

        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
            if (bgcolor.length > 1 && !StringUtils.isBlank(orientation)) {
                int[] colors = new int[bgcolor.length];
                for (int i = 0; i < bgcolor.length; i++) {
                    colors[i] = Color.parseColor(bgcolor[i]);
                }
                drawable.setOrientation(orientation);
                drawable.setColors(colors);
            } else {
                //设置背景色
                drawable.setColor(Color.parseColor(bgcolor[0]));
            }
        }
        view.setBackground(drawable);
    }


    /**
     * 动态设置Shape  OVAL
     */
    public void setDynamicShapeOVAL(Context mContext, View view, int width, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.OVAL);

        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
            drawable.setColor(Color.parseColor(bgcolor));
        }

        //设置圆的size大小
        if (!StringUtils.isBlank(width) && width != -1) {
            drawable.setSize(width, width);
        }
        view.setBackground(drawable);
    }

    /**
     * 动态设置Shape  OVAL
     */
    public void setDynamicShapeOVAL2(Context mContext, View view, int width, int strokewidth, String strokeColor, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.OVAL);


        if (!StringUtils.isBlank(strokewidth) && strokewidth != -1 &&
                !StringUtils.isBlank(strokeColor)) {
            drawable.setStroke(strokewidth, Color.parseColor(strokeColor));
        }
        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
            drawable.setColor(Color.parseColor(bgcolor));
        }

        //设置圆的size大小
        if (!StringUtils.isBlank(width) && width != -1) {
            drawable.setSize(width, width);
        }
        view.setBackground(drawable);
    }

    /**
     * 获取当前时区
     */
    public String getCurrentTimeZone() {
//        Calendar cal = Calendar.getInstance();
//        TimeZone tz1 = cal.getTimeZone();
//        LogUtils.i(ConfigConstants.TAG_ALL, "tz1 =-= " + tz);
        String default_timezone = "";
        TimeZone tz = TimeZone.getDefault();
//        default_timezone = tz.getID().replaceAll("\\/", "%2F");
        default_timezone = tz.getID();
//        LogUtils.i(ConfigConstants.TAG_ALL, "default_timezone =-= " + default_timezone);
        return default_timezone;
    }

    /**
     * 获取当前系统语言格式
     *
     * @return
     */
    public String getCurrentLanguage() {
        String lc = "";
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
            lc = locale_language + "-" + locale_country;
        } else {
            Locale locale = TKBaseApplication.myApplication.getApplicationContext().getResources().getConfiguration().locale;
            String language = locale.getLanguage().toLowerCase();
            String country = locale.getCountry().toLowerCase();
            lc = language + "-" + country;
        }
        return lc;
    }

    /**
     * 获取当前系统language和country
     *
     * @return
     */
    public Map<String, String> getCurrentLanguageAndCountry() {
        Map<String, String> map = new HashMap<>();
        Locale locale = TKBaseApplication.myApplication.getApplicationContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        map.put(SPConstants.LOCALE_LANGUAGE, language);
        map.put(SPConstants.LOCALE_COUNTRY, country);
        return map;
    }

    /**
     * 获取接口需要传的参数固定值
     */
    public Map<String, Object> getOptionsparams() {
        Map<String, Object> map_options = new HashMap<>();
        map_options.put("timezone", getCurrentTimeZone());
        map_options.put("lang", getCurrentLanguage());
        return map_options;
    }

    /**
     * 获取token的值
     */
    public String getToken(String token) {
        String result_token = "";
        if (token.startsWith("Bearer ")) {
            result_token = token;
        } else {
            result_token = "Bearer " + token;
        }
        return result_token;
    }

    /**
     * 设置屏幕的亮度模式
     * Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC：值为1，自动调节亮度
     * Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL：值为0，手动模式。
     */
    public void setScrennManualMode(Context mContext, boolean isautomatic) {
        try {
            ContentResolver contentResolver = mContext.getContentResolver();
            if (isautomatic) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            } else {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            LogUtils.i(ConfigConstants.TAG_ALL, "mode =-= " + mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取屏幕亮度
     * 屏幕最大亮度为255。
     * 屏幕最低亮度为0。
     * 屏幕亮度值范围必须位于：0～255。
     *
     * @return
     */
    public int getScreenBrightness(Context mContext) {
        ContentResolver contentResolver = mContext.getContentResolver();
        int defVal = 125;
        return Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal);
    }


    /**
     * 设置屏幕亮度
     */
    public void saveScreenBrightness(Context mContext, int value) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, value);
    }

    /**
     * APP应用版本号转换
     *
     * @return
     */
    public String appVersionNameConversion() {
        String versionname = AppUtils.getAppVersionName(TKBaseApplication.myApplication.getApplicationContext());
        versionname = versionname.replace(".", "");
        return versionname;
    }


    /**
     * 获取本应用语言数据
     */
    public List<Map<String, String>> getLanguageDatas(Context mContext) {
        List<Map<String, String>> list = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("name", mContext.getString(R.string.language_name_en));
        map.put("namedescription", mContext.getString(R.string.language_name_en));
        map.put(SPConstants.LOCALE_LANGUAGE, "en");
        map.put(SPConstants.LOCALE_COUNTRY, "US");
        list.add(map);

        Map<String, String> map2 = new HashMap<>();
        map2.put("name", mContext.getString(R.string.language_name_zh_cn));
        map2.put("namedescription", mContext.getString(R.string.language_name_zh_cn));
        map2.put(SPConstants.LOCALE_LANGUAGE, "zh");
        map2.put(SPConstants.LOCALE_COUNTRY, "CN");
        list.add(map2);

        Map<String, String> map3 = new HashMap<>();
        map3.put("name", mContext.getString(R.string.language_name_zh_tw));
        map3.put("namedescription", mContext.getString(R.string.language_name_zh_tw));
        map3.put(SPConstants.LOCALE_LANGUAGE, "zh");
        map3.put(SPConstants.LOCALE_COUNTRY, "TW");
        list.add(map3);

        return list;
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    public boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditTextCustomize)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    /**
     * 启动权限管理类
     */
    public void startPermissionsActivity(Activity mActivity, String[] permissions, PermissionsActivity.PermissionsListener permissionsListener, int mark) {
        int[] dailogcontent = new int[]{R.string.quit,
                R.string.settings,
                R.string.help,
                R.string.string_help_text};
        PermissionsActivity.startActivityForResult(mActivity, ConfigConstants.PERMISSIONS_INIT_REQUEST_CODE, dailogcontent, permissions, permissionsListener, mark);
    }


    /**
     * 清除录音文件
     */
    public void clearAudioFiles() {
        try {
            //清理目录下的录音文件
            if (FileUtils.isDir(SDCardUtils.getExternalFilesDir(TKBaseApplication.myApplication.getApplicationContext(), ConfigConstants.AUDIO_DIR).getAbsolutePath())) {
                FileUtils.deleteDir(SDCardUtils.getExternalFilesDir(TKBaseApplication.myApplication.getApplicationContext(), ConfigConstants.AUDIO_DIR).getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 关闭栈中的Activitys
     *
     * @param index 0 关闭所有的Activity
     */
    public void closeStackActivities(int index) {
//        LogUtils.i(ConfigConstants.TAG_ALL, "currentActivity =-= " + MyApplication.myApplication.lifecycleCallbacks.getActivityName(MyApplication.myApplication.lifecycleCallbacks.current().toString()));
        for (int i = index; i < TKBaseApplication.myApplication.lifecycleCallbacks.count(); i++) {
            List<Activity> activities = TKBaseApplication.myApplication.lifecycleCallbacks.getActivitys();
//            LogUtils.i(ConfigConstants.TAG_ALL, "activities =-= " + MyApplication.myApplication.lifecycleCallbacks.getActivityName(activities.get(i).toString()));
            activities.get(i).finish();
        }
    }


    /**
     * dialog 需要全屏的时候用，和clearFocusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     *
     * @param window
     */
    public void focusNotAle(Window window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    /**
     * dialog 需要全屏的时候用，focusNotAle() 成对出现
     * 在show 前调用  focusNotAle   show后调用clearFocusNotAle
     *
     * @param window
     */
    public void clearFocusNotAle(Window window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    /**
     * 隐藏虚拟栏 ，显示的时候再隐藏掉
     *
     * @param window
     */
    public void hideNavigationBar(final Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions =
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                //隐藏导航栏
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                window.getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }


    /**
     * 判断点击是否在指定的控件内
     *
     * @param views
     * @param x
     * @param y
     * @return
     */
    public boolean isTouchPointInView(View[] views, int x, int y) {
        boolean isInView = false;
        int[] location = new int[2];
        for (View view : views) {
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
//            LogUtils.i(ConfigConstants.TAG_ALL, "x =-= " + x, "y =-= " + y,
//                    "left =-= " + left,
//                    "right =-= " + right,
//                    "top =-= " + top,
//                    "bottom =-= " + bottom
//            );
            if (x >= left && x <= right
                    && y >= top && y <= bottom) {//点击在指定的控件内
                isInView = true;
                break;
            }
        }
//        LogUtils.i(ConfigConstants.TAG_ALL, "isInView =-= " + isInView);
        return isInView;
    }


    /**
     * 检查身份
     */
    public void checkIdentity(Activity mActivity) {
        VariableConfig.checkIdentityFlag = true;
        changeloginidentity(mActivity, ConfigConstants.IDENTITY_STUDENT);
    }


    /**
     * 切换身份接口
     */
    public void changeloginidentity(Activity mActivity, String identity) {
        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("identity", identity);
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.changeLoginIdentity(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<UserIdentityEntity>> apiResponseResponse) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "userIdentityEntityApiResponse =-= " + userIdentityEntityApiResponse.getResult());
                        UserIdentityEntity userIdentityEntity = apiResponseResponse.body().getData();
                        //刷新token
                        MySPUtilsUser.getInstance().saveUserToken(userIdentityEntity.getToken());

                        List<Integer> identitys = userIdentityEntity.getIdentitys();
                        if (identitys.size() == 0) {//无身份
                            MySPUtilsUser.getInstance().saveUserIdentity("");
                            AppPrefsUtil.saveUserIdentity("");

                            //发送无身份刷新事件
                            MessageEvent event = new MessageEvent();
                            event.setMessage_type(EventConstant.NO_IDENTITY_REFRESH);
                            EventBus.getDefault().post(event);
                        } else if (identitys.size() == 1) {//只有一个身份
                            //保存当前身份
                            MySPUtilsUser.getInstance().saveUserIdentity(userIdentityEntity.getCurrent_identity());
                            AppPrefsUtil.saveUserIdentity(userIdentityEntity.getCurrent_identity());

                            //发送刷新事件
                            MessageEvent event = new MessageEvent();
                            event.setMessage_type(EventConstant.CHANGE_IDENTITY_REFRESH);
                            EventBus.getDefault().post(event);
                        } else {//有两个身份，进入选择身份页面
                            //保存当前身份
                            MySPUtilsUser.getInstance().saveUserIdentity(userIdentityEntity.getCurrent_identity());
                            AppPrefsUtil.saveUserIdentity(userIdentityEntity.getCurrent_identity());

                            Bundle bundle = new Bundle();
                            bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.CHECKIDENTITYPAGE);
                            bundle.putString("current_identity", userIdentityEntity.getCurrent_identity());
                            PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, ChooseIdentityActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
                        }
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        ToastUtils.showShortToastFromText(message, ToastUtils.top);
                    }
                });
    }


    /**
     * 是否显示升级弹框
     *
     * @return
     */
    public boolean isShowSysVersionUpdateDialog() {
        //获取弹框时间，并且比对，大于一个小时才会弹出
        int sysversionUpdateDialog_showTime = MySPUtilsUser.getInstance().getUserSysversionUpdateShowTime();
        return (Integer.parseInt(DateUtil.getCurrentDate(0)) - sysversionUpdateDialog_showTime) >= 1;
    }


    /**
     * 计算时差 根据 long 返回时间点
     *
     * @param millisecond
     * @return string 11分55秒
     */
    public String parseMillisecone(long millisecond) {
        String time = "";
        try {
            long yushu_day = millisecond % (1000 * 60 * 60 * 24);
            long yushu_hour = (millisecond % (1000 * 60 * 60 * 24))
                    % (1000 * 60 * 60);
            long yushu_minute = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60);
            long yushu_second = millisecond % (1000 * 60 * 60 * 24)
                    % (1000 * 60 * 60) % (1000 * 60) % 1000;

//            if (yushu_day == 0) {
//                return (millisecond / (1000 * 60 * 60 * 24)) + "天";
//            } else {
//                if (yushu_hour == 0) {
//                    return (millisecond / (1000 * 60 * 60 * 24)) + "天"
//                            + (yushu_day / (1000 * 60 * 60)) + "时";
//                } else {
//                    if (yushu_minute == 0) {
//                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
//                                + (yushu_day / (1000 * 60 * 60)) + "时"
//                                + (yushu_hour / (1000 * 60)) + "分";
//                    } else {
//                        return (millisecond / (1000 * 60 * 60 * 24)) + "天"
//                                + (yushu_day / (1000 * 60 * 60)) + "时"
//                                + (yushu_hour / (1000 * 60)) + "分"
//                                + (yushu_minute / 1000) + "秒";
//                    }
//                }
//            }

            long result_day = millisecond / (1000 * 60 * 60 * 24);
            long result_hour = yushu_day / (1000 * 60 * 60);
            long result_minute = yushu_hour / (1000 * 60);
            long result_second = yushu_minute / 1000;
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


}
