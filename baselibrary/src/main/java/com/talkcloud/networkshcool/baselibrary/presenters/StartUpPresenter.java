package com.talkcloud.networkshcool.baselibrary.presenters;

import android.Manifest;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.activities.LoginPlusActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.MainMenuActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.ServiceAgreementDialog;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.StartUpView;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Date:2021/5/11
 * Time:11:54
 * author:joker
 * 启动页的Presenter
 */
public class StartUpPresenter implements HandlerUtils.OnReceiveMessageListener, ServiceAgreementDialog.ServiceAgreementDialogListener {

    private Activity mActivity;

    private StartUpView startUpView;

    private PermissionsActivity.PermissionsListener permissionsListener;

    // 时间类
    private Timer timer;
    //计时时间 默认是3
    private int timecount = 1;
    //运行计时器标识
    private final int whatcode_runningtimer = 1;

    private Handler handler;


    public StartUpPresenter(StartUpView startUpView, Activity mActivity, PermissionsActivity.PermissionsListener permissionsListener) {
        this.startUpView = startUpView;
        this.mActivity = mActivity;
        this.permissionsListener = permissionsListener;
        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), mActivity, this);
    }

    /**
     * 启动计时器
     */
    public void startTimerTask() {
        if (!StringUtils.isBlank(task)) {
            timer = new Timer();
            timer.schedule(task, 0, 1000);// 启动计时器
        }
    }


    /**
     * 倒计时的内部类
     */
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, whatcode_runningtimer, 100);
        }
    };

    /**
     * handler 回调方法
     *
     * @param msg
     */
    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case whatcode_runningtimer:
                timecount--;
                if (timecount < 0) {
                    timer.cancel();

                    boolean isAgree = MySPUtilsUser.getInstance().getUserPrivacyAgreementStatus();
                    if (isAgree) {
                        //判断是否有权限需要申请
                        PermissionsChecker mPermissionsChecker = new PermissionsChecker(mActivity);
                        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION};
                        if (mPermissionsChecker.lacksPermissions(permissions)) {//去申请权限
                            PublicPracticalMethodFromJAVA.getInstance().startPermissionsActivity(mActivity, permissions, permissionsListener, ConfigConstants.PERMISSIONS_GRANTED_STARTUPACTIVITY);
                        } else {//权限已经有了
                            String token = MySPUtilsUser.getInstance().getUserToken();
                            if (!StringUtils.isBlank(token)) {
                                //跳转到首页
                                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, MainMenuActivity.class, -1, null, true,
                                        R.anim.activity_xhold, R.anim.activity_xhold);
                            } else {
                                //跳转到登录页
                                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, LoginPlusActivity.class, -1, null, true,
                                        R.anim.activity_xhold, R.anim.activity_xhold);
                            }
                        }
                    } else {
                        //显示协议弹框
                        showServiceAgreementDialog();
                    }
                }
                break;
        }
    }

    /**
     * 启动页设置语言,让全局APP都可以变成指定的语言，在华为P30手机上面测试选择语言后 再次进入需要请求一下，不然不会切换成已选语言
     */
    public void setLanguage() {
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        if (StringUtils.isBlank(locale_language) && StringUtils.isBlank(locale_country)) {
            //获取当前系统国家的语言
            Map<String, String> map = PublicPracticalMethodFromJAVA.getInstance().getCurrentLanguageAndCountry();
            String mLanguage = map.get(SPConstants.LOCALE_LANGUAGE);
            String mArea = map.get(SPConstants.LOCALE_COUNTRY);

            //获取本地app的国家语言
            List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(mActivity);
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).get(SPConstants.LOCALE_LANGUAGE).equals(mLanguage) && datas.get(i).get(SPConstants.LOCALE_COUNTRY).equals(mArea)) {
                    Locale newLocale = new Locale(mLanguage, mArea);
                    MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, true);
                    return;
                }
            }
            Locale newLocale = new Locale(datas.get(0).get(SPConstants.LOCALE_LANGUAGE), datas.get(0).get(SPConstants.LOCALE_COUNTRY));
            MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, true);
        } else {
            Locale newLocale = new Locale(locale_language, locale_country);
            MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, true);
        }
    }

    /**
     * 显示协议弹框
     */
    public void showServiceAgreementDialog() {
        int dialogWidth = 0;
        if (ScreenTools.getInstance().isPad(mActivity)) {
            dialogWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_390x);
        } else {
            dialogWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_340x);
        }
        ServiceAgreementDialog serviceAgreementDialog = new ServiceAgreementDialog(mActivity);
        serviceAgreementDialog.setDialogWidth(dialogWidth);
        serviceAgreementDialog.setServiceAgreementDialogListener(this);
        if (!serviceAgreementDialog.isShowing()) {
            serviceAgreementDialog.show();
        }
    }


    public void onDestroy() {
        if (!StringUtils.isBlank(handler)) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 同意协议回调
     */
    @Override
    public void agreeServiceAgreementAndPrivacyPolicy() {
        //更新 用户协议和隐私政策 标识
        MySPUtilsUser.getInstance().saveUserPrivacyAgreementStatus(true);
//        VariableConfig.userPrivacyAgreementStatus = true;

        //初始化相关SDK
//        TKBaseApplication.myApplication.initKV();
        TKBaseApplication.myApplication.initCoreLib();
        TKBaseApplication.myApplication.initBugly();
        TKBaseApplication.myApplication.initJpush();
        TKBaseApplication.myApplication.initShare();


        //判断是否有权限需要申请
        PermissionsChecker mPermissionsChecker = new PermissionsChecker(mActivity);
        String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (mPermissionsChecker.lacksPermissions(permissions)) {//去申请权限
            PublicPracticalMethodFromJAVA.getInstance().startPermissionsActivity(mActivity, permissions, permissionsListener, ConfigConstants.PERMISSIONS_GRANTED_STARTUPACTIVITY);
        } else {//权限已经有了
            String token = MySPUtilsUser.getInstance().getUserToken();
            if (!StringUtils.isBlank(token)) {
                //跳转到首页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, MainMenuActivity.class, -1, null, true,
                        R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                //跳转到登录页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, LoginPlusActivity.class, -1, null, true,
                        R.anim.activity_xhold, R.anim.activity_xhold);
            }
        }
    }

    /**
     * 设置启动页的图片
     */
    public void setLogo(ImageView iv_logo, ConstraintLayout cl_start) {
        int drawDrawableID = TKExtManage.getInstance().getStartLogo(mActivity);
        //获取图片的信息
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream is = mActivity.getResources().openRawResource(drawDrawableID);
//        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
//        int imageHeight = options.outHeight;
//        int imageWidth = options.outWidth;
//
//        int w = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_160x);
//        int h = w * imageHeight / imageWidth;
//
//        ConstraintUtil constraintUtil = new ConstraintUtil(cl_start);
//        ConstraintUtil.ConstraintBegin begin = constraintUtil.begin();
//        begin.setHeight(R.id.iv_logo, w);
//        begin.setWidth(R.id.iv_logo, h);
//        begin.commit();

        iv_logo.setImageDrawable(ContextCompat.getDrawable(mActivity, drawDrawableID));

    }


}
