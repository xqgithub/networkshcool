package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.eduhdsdk.viewutils.EyeProtectionUtil;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.presenters.PersonalSettingsPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.AppUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.PersonalSettingsView;
import com.talkcloud.networkshcool.baselibrary.weiget.SlideButton;
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKUnregisterDialog;
import com.tencent.mmkv.MMKV;

import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/17
 * Time:16:42
 * author:joker
 * 个人设置页面
 */
public class PersonalSettingsActivity extends BaseActivity implements PersonalSettingsView, HandlerUtils.OnReceiveMessageListener
        , View.OnClickListener, SlideButton.SlideButtonOnCheckedListener, PermissionsActivity.PermissionsListener, CustomAdapt {


    private PersonalSettingsPresenter presenter;

    private ImageView iv_close;
    private ConstraintLayout cl_changemobile;
    private ConstraintLayout cl_changepassword;
    private ConstraintLayout cl_language;
    private TextView tv_mobilenum;
    private TextView tv_languagetype;
    private ConstraintLayout cl_useragreement;
    private ConstraintLayout cl_userprivacypolicy;
    private ConstraintLayout cl_dropout;
    private ConstraintLayout cl_version;
    private ConstraintLayout cl_settings;
    private ConstraintLayout cl_unregister;
    private TextView tv_version;
    private TextView tv_versionpromp;
    private TextView tv_statusbar_top;

    private String mobile = "";
    private String account = "";

    private SlideButton slidebutton;

    //是否有版本需要更新
    private boolean is_update = false;
    private SysVersionEntity sysVersionEntity;


    //申请WRITE_SETTINGS权限码
    private static final int REQUEST_CODE_WRITE_SETTINGS = 7777;

    private static int sequence = 1;

    private Handler handler;
    private final int HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS = -10000;
    private final int HANDLERWHAT_SYSVERSIONCALLBACKFAILURE = -10001;

    // 注销dialog
    private TKUnregisterDialog unregisterDialog;

    @Override
    protected void onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }

        //这里的状态栏设置
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        true, true,
                        R.color.appwhite
                );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personalsettings;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {
        iv_close = findViewById(R.id.iv_close);
        cl_changemobile = findViewById(R.id.cl_changemobile);
        tv_mobilenum = findViewById(R.id.tv_mobilenum);
        cl_changepassword = findViewById(R.id.cl_changepassword);
        cl_language = findViewById(R.id.cl_language);
        tv_languagetype = findViewById(R.id.tv_languagetype);
        slidebutton = findViewById(R.id.slidebutton);
        cl_useragreement = findViewById(R.id.cl_useragreement);
        cl_userprivacypolicy = findViewById(R.id.cl_userprivacypolicy);
        cl_dropout = findViewById(R.id.cl_dropout);
        tv_version = findViewById(R.id.tv_version);
        cl_version = findViewById(R.id.cl_version);
        tv_versionpromp = findViewById(R.id.tv_versionpromp);
        tv_statusbar_top = findViewById(R.id.tv_statusbar_top);
        cl_settings = findViewById(R.id.cl_settings);

        cl_unregister = findViewById(R.id.cl_unregister);
    }

    @Override
    protected void initData() {
        presenter = new PersonalSettingsPresenter(this, this, this);

        mobile = MySPUtilsUser.getInstance().getUserMobile();
        if (!StringUtils.isBlank(mobile)) {
            tv_mobilenum.setText(mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length()));
        }

        int userloginmethod = MySPUtilsUser.getInstance().getUserLoginMethod();
        if (userloginmethod == ConfigConstants.LOGINMETHOD_ACCOUNT) {//隐藏 更换手机和修改密码UI
            cl_settings.setVisibility(View.GONE);
        }

        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(this);
        for (int i = 0; i < datas.size(); i++) {
            if (locale_language.equals(datas.get(i).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(i).get(SPConstants.LOCALE_COUNTRY))) {
                tv_languagetype.setText(datas.get(i).get("name"));
            }
        }


        //设置退出的样式
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_dropout, getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1,
                "", "#ffffffff");
        //滑动按设置
        slidebutton.setSmallCircleModel(Color.parseColor("#00000000"), Color.parseColor(VariableConfig.color_button_selected), Color.parseColor("#F7F8F9"), Color.parseColor("#ffffffff"), Color.parseColor("#ffffffff"));

        //设置护眼模式
        Boolean eyeprotection = MySPUtilsUser.getInstance().getUserEyeProtectionStatus();
        slidebutton.setChecked(eyeprotection);


        tv_version.setText(getString(R.string.personalsettings_version) + AppUtils.getAppVersionName(this));

        //请求系统版本接口，查看是否有应用可以更新
        presenter.sysversion();

        presenter.setBarHeight(tv_statusbar_top);

        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);

        if(AppPrefsUtil.getUnregisterFlag()) {
            cl_unregister.setVisibility(View.VISIBLE);
        } else {
            cl_unregister.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
        cl_changemobile.setOnClickListener(this);
        cl_changepassword.setOnClickListener(this);
        cl_language.setOnClickListener(this);
        slidebutton.setOnCheckedListener(this);
        cl_useragreement.setOnClickListener(this);
        cl_userprivacypolicy.setOnClickListener(this);
        cl_dropout.setOnClickListener(this);
        cl_version.setOnClickListener(this);
        cl_unregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        } else if (id == R.id.cl_changemobile) {
            VariableConfig.login_process = VariableConfig.login_process_changemobile;
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, LoginActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.cl_changepassword) {
            VariableConfig.login_process = VariableConfig.login_process_changepassword;
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, LoginActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.cl_language) {
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, LanguageSwitchActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.cl_useragreement) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", ConfigConstants.USERAGREEMENT);
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.cl_userprivacypolicy) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", ConfigConstants.USERPRIVACYPOLICY);
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.cl_dropout) {
            presenter.showLogOutDialog();
        } else if (id == R.id.cl_version) {
            if (is_update) {
                presenter.updateAPP(sysVersionEntity);
            }
//                presenter.showAudioRecordDialog();
//                presenter.showNetworkDiskDialog();
        } else if (id == R.id.cl_unregister) {

            if (unregisterDialog == null) {
                unregisterDialog = new TKUnregisterDialog(this);
            }
            unregisterDialog.show();
        }
    }

    @Override
    public void onCheckedChangeListener(boolean isChecked) {
//        presenter.requestWriteSettings(REQUEST_CODE_WRITE_SETTINGS, isChecked);
        presenter.eyeProtectionOperating(isChecked);
    }

    @Override
    public void logoutCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //清除极光推送别名
            JPushInterface.deleteAlias(getApplicationContext(), sequence);
            MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA, "");

            //token的值 设置为空
            MySPUtilsUser.getInstance().saveUserToken("");
            //用户的身份置空
            MySPUtilsUser.getInstance().saveUserIdentity("");
            AppPrefsUtil.saveUserIdentity("");

            //需要重新走登录流程
            VariableConfig.login_process = VariableConfig.login_process_normal;
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PersonalSettingsActivity.this, LoginPlusActivity.class,
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void sysversionCallback(boolean isSuccess, SysVersionEntity sysVersionEntity, String msg) {
        if (isSuccess) {
            if (!StringUtils.isBlank(sysVersionEntity)) {
                this.sysVersionEntity = sysVersionEntity;
                PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS, 1);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, HANDLERWHAT_SYSVERSIONCALLBACKFAILURE, 1);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    /**
     * 护眼模式弹框 操作是否确定
     *
     * @param isSure
     */
    @Override
    public void eyeProtectionCallback(boolean isSure) {
        if (isSure) {
            if (Build.VERSION.SDK_INT >= 26) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                intent.setData(Uri.fromParts("package", getPackageName(), (String) null));
                startActivityForResult(intent, EyeProtectionUtil.requestCodes);
            } else if (Build.VERSION.SDK_INT >= 23) {
//                PermissionUtil.gotoPermission(this);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, EyeProtectionUtil.requestCodes);
            }
        } else {
            slidebutton.setChecked(false);
//            EyeProtectionUtil.openSuspensionWindow(this, false);
            MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (Settings.System.canWrite(this)) {
////                    LogUtils.i(ConfigConstants.TAG_ALL, "权限获取了 =-=");
//                    slidebutton.setChecked(true);
//                    //保存数据
//                    MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(true);
//                    PublicPracticalMethodFromJAVA.getInstance().setScrennManualMode(this, true);
//                } else {
////                    LogUtils.i(ConfigConstants.TAG_ALL, "权限拒绝了 =-=");
//                    slidebutton.setChecked(false);
//                    slidebutton.closeSlideAnimation();
//                }
//            }
//        }
        switch (requestCode) {
            case EyeProtectionUtil.requestCodes:
                if (Settings.canDrawOverlays(this)) {
                    slidebutton.setChecked(true);
//                    EyeProtectionUtil.openSuspensionWindow(this, true);
                    MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(true);
                } else {
                    slidebutton.setChecked(false);
//                    EyeProtectionUtil.openSuspensionWindow(this, false);
                    MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(false);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        if (!StringUtils.isBlank(handler)) {
            handler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 物理返回键 监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
        return false;
    }

    @Override
    public boolean isBaseOnWidth() {
        boolean isBaseOnWidth;
        if (ScreenTools.getInstance().isPad(this)) {
            isBaseOnWidth = false;
        } else {
            isBaseOnWidth = true;
        }
        return isBaseOnWidth;
    }

    @Override
    public float getSizeInDp() {
        float sizeInDp;
        if (ScreenTools.getInstance().isPad(this)) {
            sizeInDp = ConfigConstants.PAD_HEIGHT;
        } else {
            sizeInDp = ConfigConstants.PHONE_WIDTH;
        }
        return sizeInDp;
    }


    @Override
    public void allPermissionsGranted(int mark) {
        if (mark == ConfigConstants.PERMISSIONS_GRANTED_UPDATEAPP) {
//            if (is_update) {
//                presenter.updateAPP(sysVersionEntity);
//            }
        }
    }

    /**
     * 权限拒绝 回调
     */
    @Override
    public void permissionsDenied(int mark) {
    }


    /**
     * handler 回调
     *
     * @param msg
     */
    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS:
                tv_versionpromp.setText(R.string.personalsettings_versionpromp_canbeupdated);
                is_update = true;
                break;
            case HANDLERWHAT_SYSVERSIONCALLBACKFAILURE:
                tv_versionpromp.setText(R.string.personalsettings_versionpromp);
                is_update = false;
                break;
        }
    }
}
