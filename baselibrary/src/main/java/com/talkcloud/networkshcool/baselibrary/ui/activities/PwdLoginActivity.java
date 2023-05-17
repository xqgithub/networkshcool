package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.PwdLoginPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.FastDoubleClickUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.PwdLoginView;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/13
 * Time:14:34
 * author:joker
 * 1.密码登录页面
 * 2.验证身份密码登录
 */
public class PwdLoginActivity extends BaseActivity implements PwdLoginView, View.OnClickListener, CustomAdapt, GraphicVerificationView.GraphicVerificationListener, HandlerUtils.OnReceiveMessageListener {


    private PwdLoginPresenter presenter;
    private TextView tv_confirm;
    private TextView tv_useverificationcode;
    private TextView tv_phonecountrycode;
    private TextView tv_forgetpwd;
    private TextView tv_title;
    private TextView tv_title2;
    private TextView tv_useaccount;
    private EditTextCustomize et_phone;
    private EditTextCustomize et_pwd;
    private ConstraintLayout cl_close;
    private ConstraintLayout cl_phonecountrycode;
    private ConstraintLayout cl_phonenum;
    private ConstraintLayout cl_global;
    private ConstraintLayout cl_slide;
    private ConstraintLayout cl_pwd;
    private ImageView iv_pwd_status;
    private ImageView iv_left_bg;
    private ImageView iv_close;
    private ImageView iv_loading;
    private TextView tv_userprivacyagreement;
    private ImageView iv_userprivacyagreement;
    private View dividingline_vertical;

    private String locale = VariableConfig.default_locale;
    private String localecode = VariableConfig.default_localecode;
    private String localename = VariableConfig.default_localename;
    private String mobile = "";
    private String pwd = "";
    private String mode = "";

    //短信验证码倒计时
    private long sms_countdown = 0L;

    //密码状态是否显示
    private boolean pwdIsVisible = false;

    private static int sequence = 1;


    private GraphicVerificationView graphicverification_view;

    private Handler handler;
    //运行计时器标识
    private final int WHATCODE_REMOVEVIEW = -1111111;


    @Override
    protected void onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
            if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                //隐藏状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                return;
            }
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }
        //状态栏状态设置
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        false, true,
                        -1
                );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pwdlogin;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_useverificationcode = findViewById(R.id.tv_useverificationcode);
        cl_close = findViewById(R.id.cl_close);
        et_phone = findViewById(R.id.et_phone);
        tv_phonecountrycode = findViewById(R.id.tv_phonecountrycode);
        cl_phonecountrycode = findViewById(R.id.cl_phonecountrycode);
        et_pwd = findViewById(R.id.et_pwd);
        iv_pwd_status = findViewById(R.id.iv_pwd_status);
        tv_forgetpwd = findViewById(R.id.tv_forgetpwd);
        cl_phonenum = findViewById(R.id.cl_phonenum);
        cl_pwd = findViewById(R.id.cl_pwd);
        tv_title = findViewById(R.id.tv_title);
        tv_title2 = findViewById(R.id.tv_title2);
        iv_left_bg = findViewById(R.id.iv_left_bg);
        cl_global = findViewById(R.id.cl_global);
        iv_close = findViewById(R.id.iv_close);
        iv_loading = findViewById(R.id.iv_loading);
        tv_userprivacyagreement = findViewById(R.id.tv_userprivacyagreement);
        iv_userprivacyagreement = findViewById(R.id.iv_userprivacyagreement);
        cl_slide = findViewById(R.id.cl_slide);
        tv_useaccount = findViewById(R.id.tv_useaccount);
        dividingline_vertical = findViewById(R.id.dividingline_vertical);
        graphicverification_view = findViewById(R.id.graphicverification_view);
    }

    @Override
    protected void initData() {
        presenter = new PwdLoginPresenter(this, this);


        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //设置tv_confirm 背景颜色
        if (StringUtils.isBlank(et_phone.getText().toString().trim()) || StringUtils.isBlank(et_pwd.getText().toString().trim())) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
        }


        //设置手机号输入框的背景色
        et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
        //设置密码输入框的背景色
        et_pwd.setEditTextBG(cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);

        //获取bundle中的值
        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            mobile = bundle.getString("mobile");
            mode = bundle.getString("mode");
            locale = bundle.getString("locale");
            localename = bundle.getString("localename");
            localecode = bundle.getString("localecode");
        }
        et_phone.setText(mobile);
//        et_phone.setText("19907149797");
//        et_pwd.setText("12345678a");
        tv_phonecountrycode.setText("+" + localecode);


        //隐藏密码
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());


        if (ScreenTools.getInstance().isPad(this)) {
            iv_left_bg.setVisibility(View.VISIBLE);
        } else {
            iv_left_bg.setVisibility(View.GONE);
        }

        //判断是否是修改手机号或修改密码流程
        if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                VariableConfig.login_process == VariableConfig.login_process_changepassword) {
            tv_title.setText(getString(R.string.login_title_ps));
            tv_title2.setText(getString(R.string.pwdlogin_title2_ps));

            mobile = MySPUtilsUser.getInstance().getUserMobile();
            et_phone.setText(mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length()));
            et_phone.setTextColor(ContextCompat.getColor(this, R.color.networkshcool_a3a3ae));
            et_phone.setEnabled(false);

            tv_phonecountrycode.setTextColor(ContextCompat.getColor(this, R.color.networkshcool_a3a3ae));

            cl_phonecountrycode.setEnabled(false);

            tv_useverificationcode.setText(getString(R.string.pwdlogin_useverificationcode_ps));

            tv_forgetpwd.setVisibility(View.INVISIBLE);

            tv_useaccount.setVisibility(View.GONE);
            dividingline_vertical.setVisibility(View.GONE);


            tv_confirm.setText(getString(R.string.login_confirm));

            iv_left_bg.setVisibility(View.GONE);
            presenter.setTitlePosition(this, cl_slide, R.id.cl_slide, R.id.tv_title);
            presenter.setClosePosition(this, cl_slide, R.id.cl_slide, R.id.cl_close);
            iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
        } else {
            if (ScreenTools.getInstance().isPad(this)) {
                //设置返回按钮的背景
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, cl_close, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
            } else {
                iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
            }
        }
    }

    @Override
    protected void initListener() {
        tv_confirm.setOnClickListener(this);
        tv_useverificationcode.setOnClickListener(this);
        cl_close.setOnClickListener(this);
        cl_phonecountrycode.setOnClickListener(this);
        iv_pwd_status.setOnClickListener(this);
        tv_forgetpwd.setOnClickListener(this);
        iv_userprivacyagreement.setOnClickListener(this);
        tv_userprivacyagreement.setOnClickListener(this);
        tv_useaccount.setOnClickListener(this);

        //手机号输入框监听
        et_phone.setOnEditTextListener(new EditTextCustomize.onEditTextListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isBlank(s) || StringUtils.isBlank(et_pwd.getText().toString().trim())) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(PwdLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(PwdLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获取焦点
                    if (!et_phone.isEnabled()) {
                        return;
                    }
                    et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {//失去焦点
                    et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }
            }
        });


        //密码输入框监听
        et_pwd.setOnEditTextListener(new EditTextCustomize.onEditTextListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isBlank(s) || StringUtils.isBlank(et_phone.getText().toString().trim())) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(PwdLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(PwdLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_pwd.setEditTextBG(cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {
                    et_pwd.setEditTextBG(cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }
            }
        });
    }

    /**
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_useverificationcode) {
            mobile = et_phone.getText().toString().trim();
            if (!StringUtils.isBlank(mobile)) {
                Bundle bundle_useverificationcode = new Bundle();
                bundle_useverificationcode.putString("mobile", mobile);
                if (ScreenTools.getInstance().isPad(this)) {
                    if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                        PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, LoginActivity.class,
                                -1, bundle_useverificationcode, true, R.anim.activity_xhold, R.anim.activity_xhold);
                        return;
                    }
                }
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, LoginActivity.class,
                        -1, bundle_useverificationcode, true, R.anim.activity_right_in, R.anim.activity_right_out);
            } else {
                if (ScreenTools.getInstance().isPad(this)) {
                    if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                        PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, LoginActivity.class,
                                -1, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
                        return;
                    }
                }
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, LoginActivity.class,
                        -1, null, true, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else if (id == R.id.tv_useaccount) {
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, AccountLoginActivity.class,
                        -1, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, AccountLoginActivity.class,
                        -1, null, true, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else if (id == R.id.cl_close) {
            if (ScreenTools.getInstance().isPad(this)) {
                if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                    PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
                    return;
                }
            }
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        } else if (id == R.id.cl_phonecountrycode) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.cl_phonecountrycode)) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("localename", localename);
                bundle2.putString("localecode", localecode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, CountryAreaActivity.class, -1, bundle2, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, CountryAreaActivity.class, -1, bundle2, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        } else if (id == R.id.iv_pwd_status) {//显示或隐藏密码内容
            if (pwdIsVisible) {
                //隐藏密码
                et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_invisible));
                pwdIsVisible = false;
            } else {
                //显示密码
                et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_visible));
                pwdIsVisible = true;
            }
        } else if (id == R.id.tv_forgetpwd) {
            mobile = et_phone.getText().toString().trim();
            Bundle bundle = new Bundle();
            bundle.putString("mobile", mobile);
            bundle.putString("locale", locale);
            bundle.putString("localename", localename);
            bundle.putString("localecode", localecode);

            if (ScreenTools.getInstance().isPad(this)) {
                //跳转到忘记密码页面
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, ForgetPwdActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                //跳转到忘记密码页面
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, ForgetPwdActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else if (id == R.id.tv_confirm) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.tv_confirm)) {
                if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                        VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                    mobile = MySPUtilsUser.getInstance().getUserMobile();
                    pwd = et_pwd.getText().toString().trim();
                    presenter.VerificationCodeCheck2(mode, pwd);
                } else {
                    if (!VariableConfig.userPrivacyAgreementStatus) {
                        ToastUtils.showShortToastFromRes(R.string.login_userprivacyagreement3, ToastUtils.top);
                        return;
                    }
                    mobile = et_phone.getText().toString().trim();
                    pwd = et_pwd.getText().toString().trim();
                    presenter.passwordLogin(locale, mobile, pwd, iv_loading, tv_confirm);
                }
            }
        } else if (id == R.id.iv_userprivacyagreement) {
            presenter.setUserPrivacyAgreementStatus(iv_userprivacyagreement);
        } else if (id == R.id.tv_userprivacyagreement) {//跳转到隐私协议页面
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, UserAgreementWebView.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void EventBusCountryAreaMessage(MessageEvent messageEvent) {
        if (VariableConfig.login_process != VariableConfig.login_process_changemobile &&
                VariableConfig.login_process != VariableConfig.login_process_changepassword) {
            if (EventConstant.EVENT_COUNTRYAREA.equals(messageEvent.getMessage_type())) {
                Bundle bundle = (Bundle) messageEvent.getMessage();
                if (!StringUtils.isBlank(bundle)) {
                    locale = bundle.getString("locale");
                    localename = bundle.getString("localename");
                    localecode = bundle.getString("localecode");
                    tv_phonecountrycode.setText("+" + localecode);
                }
            }
        }

        if (EventConstant.EVENT_VERIFICATIONCODE_COUNTDOWN == messageEvent.getMessage_type()) {
            if ((Long) messageEvent.getMessage() != -1) {
                sms_countdown = (long) messageEvent.getMessage();
            } else {
                sms_countdown = 0L;
            }
        }

    }

    @Override
    public void passwordLoginCallback(boolean isSuccess, LoginEntity info, String msg) {
        if (isSuccess) {
            //保存数据
            if (info != null && info.getToken() != null) {
                String token = info.getToken();
                MySPUtilsUser.getInstance().saveUserToken(token);
                MySPUtilsUser.getInstance().saveUserMobile(mobile);
                MySPUtilsUser.getInstance().saveUserPwd(pwd);
                MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_PWD);
                //登录请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
                presenter.changeloginidentity(token, ConfigConstants.IDENTITY_STUDENT);

                if (info.getUser_account_id() != null) {  //绑定别名
                    JPushInterface.setAlias(PwdLoginActivity.this, sequence++, info.getUser_account_id());
                }
            }

        } else {

            presenter.loginAnimation(iv_loading, tv_confirm, false);


            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
            //设置输入框的背景色
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                    "#ff2855", VariableConfig.color_transparent_bg);
        }
    }

    @Override
    public void passwordLoginCallbackPS(boolean isSuccess, String token, String msg) {
        if (isSuccess) {
            //保存数据
//            MySPUtilsUser.getInstance().saveUserToken(token);
//            MySPUtilsUser.getInstance().saveUserMobile(mobile);
//            MySPUtilsUser.getInstance().saveUserPwd(pwd);
            if (VariableConfig.login_process == VariableConfig.login_process_changemobile) {
                //跳转到更换手机号页面
                Bundle bundle = new Bundle();
                bundle.putString("mode", "2");
                bundle.putString("locale", locale);
                bundle.putString("localename", localename);
                bundle.putString("localecode", localecode);

                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, ChangeMobileActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, ChangeMobileActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            } else if (VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                //发送短信
                if (VariableConfig.verificationcode_countdown_flag) {
//                    presenter.sms(mobile, locale);
                    graphicverification_view.setGraphicVerificationListener(this);
                    graphicverification_view.loadUrl("file:///android_asset/web/graphicverification.html");
                    graphicverification_view.setVisibility(View.VISIBLE);
                } else {

                    if (MySPUtilsUser.getInstance().getMobileTemp().equals(mobile)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile", mobile);
                        bundle.putString("locale", locale);
                        bundle.putString("localecode", localecode);
                        bundle.putString("localename", localename);
                        bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.PWDLOGINACTIVITY);
                        if (ScreenTools.getInstance().isPad(this)) {
                            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                        } else {
                            PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                        }
                    } else {
                        String content = String.format(getResources().getString(R.string.Verification_code_sended), sms_countdown + "");
                        ToastUtils.showShortToastFromText(content, ToastUtils.top);
                    }
                }
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg) {
        presenter.loginAnimation(iv_loading, tv_confirm, false);
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(userIdentityEntity.getToken());
            MySPUtilsUser.getInstance().saveUserLocale(locale);
            MySPUtilsUser.getInstance().saveUserLocaleCode(localecode);
            MySPUtilsUser.getInstance().saveUserLocaleName(localename);
            MySPUtilsUser.getInstance().saveUserMobile(mobile);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_PWD);
            if (StringUtils.isBlank(userIdentityEntity.getCurrent_identity())) {
                MySPUtilsUser.getInstance().saveUserIdentity("");
                AppPrefsUtil.saveUserIdentity("");
            } else {
                MySPUtilsUser.getInstance().saveUserIdentity(userIdentityEntity.getCurrent_identity());
                AppPrefsUtil.saveUserIdentity(userIdentityEntity.getCurrent_identity());
            }


            List<Integer> identitys = userIdentityEntity.getIdentitys();
            if (identitys.size() == 1 || identitys.size() == 0) {//当前用户只有一个身份,或者用户没有身份，直接进入首页
                //跳转到首页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, MainMenuActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {//用户有多重身份，需要进入选择身份页面
                Bundle bundle = new Bundle();
                bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.PWDLOGINACTIVITY);
                bundle.putString("current_identity", userIdentityEntity.getCurrent_identity());
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, ChooseIdentityActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void smsCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveMobileTemp(msg);


            Bundle bundle = new Bundle();
            bundle.putString("mobile", msg);
            bundle.putString("locale", locale);
            bundle.putString("localecode", localecode);
            bundle.putString("localename", localename);
            bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.PWDLOGINACTIVITY);
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(PwdLoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }


    /**
     * 验证码 图文验证 回调
     */
    @Override
    public void jsReturnResults(int ret, String ticket, String randstr) {
        PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, WHATCODE_REMOVEVIEW, 100);
        if (ret == 0) {
            //发送验证码
            presenter.sms(mobile, locale, ticket, randstr);
        }
    }


    /**
     * handler 回调
     */
    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case WHATCODE_REMOVEVIEW:
                graphicverification_view.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (VariableConfig.login_process == VariableConfig.login_process_normal) {
            presenter.setUserPrivacyAgreementBG(tv_userprivacyagreement, iv_userprivacyagreement);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (ScreenTools.getInstance().isPad(this)) {
                if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                    PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
                    return false;
                }
            }
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            boolean isHide = KeyBoardUtil.getInstance().isShouldHideKeyboard(v, ev);
//            LogUtils.i(ConfigConstants.TAG_ALL, "isHide =-=" + isHide);

            if (isHide) {
                KeyBoardUtil.getInstance().hideKeyBoard(this, et_phone);
//                KeyBoardUtil.getInstance().hideKeyBoard(this, et_pwd);
            } else {
                KeyBoardUtil.getInstance().showKeyBoard(this, et_phone);
//                KeyBoardUtil.getInstance().showKeyBoard(this, et_pwd);
            }
        }
        return super.dispatchTouchEvent(ev);
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

}
