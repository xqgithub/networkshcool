package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.talkcloud.networkshcool.baselibrary.help.TimeCountdown;
import com.talkcloud.networkshcool.baselibrary.presenters.VCodeInputPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.FastDoubleClickUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.VCodeInputView;
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView;
import com.wynsbin.vciv.VerificationCodeInputView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/12
 * Time:9:14
 * author:joker
 * 验证码输入页面
 */
public class VCodeInputActivity extends BaseActivity implements VCodeInputView, View.OnClickListener, CustomAdapt, GraphicVerificationView.GraphicVerificationListener, HandlerUtils.OnReceiveMessageListener {

    private VCodeInputPresenter presenter;
    private TextView tv_confirm;
    private TextView tv_title2;
    private TextView tv_resend;
    private ImageView iv_left_bg;
    private ImageView iv_close;
    private ConstraintLayout cl_close;
    private ConstraintLayout cl_global;
    private VerificationCodeInputView et_vciv;

    private String mode = "";
    private String mobile = "";
    private String locale = "";
    private String localecode = "";
    private String localename = "";
    private String verificationcode = "";
    private String activity_species = "";

    private static int sequence = 1;


    //倒计时类
    private TimeCountdown timeCountdown;

    //输入验证码的个数
    private int mCodesSize = 0;
    //验证码的view
    private View[] mUnderLineViews;


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
        return R.layout.activity_vcodeinput;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化UI控件
     */
    @Override
    protected void initUiView() {
        tv_confirm = findViewById(R.id.tv_confirm);
        cl_close = findViewById(R.id.cl_close);
        et_vciv = findViewById(R.id.et_vciv);
        tv_title2 = findViewById(R.id.tv_title2);
        tv_resend = findViewById(R.id.tv_resend);
        iv_left_bg = findViewById(R.id.iv_left_bg);
        cl_global = findViewById(R.id.cl_global);
        iv_close = findViewById(R.id.iv_close);
        graphicverification_view = findViewById(R.id.graphicverification_view);
        tv_resend.setClickable(false);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        presenter = new VCodeInputPresenter(this, this);


        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);

        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            mode = bundle.getString("mode");
            mobile = bundle.getString("mobile");
            locale = bundle.getString("locale");
            localecode = bundle.getString("localecode");
            localename = bundle.getString("localename");
            activity_species = bundle.getString(ConfigConstants.ACTIVITY_SPECIES);
            tv_title2.setVisibility(View.VISIBLE);
            tv_title2.setText(getString(R.string.vcodeinput_title2) + "+" + localecode + " " + mobile);
        } else {
            tv_title2.setVisibility(View.INVISIBLE);
        }

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (VariableConfig.verificationcode_countdown_flag) {
            timeCountdown = new TimeCountdown(60000, 1000);
            timeCountdown.start();
        }

        //设置tv_confirm 背景颜色  不能点击
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);

        if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                VariableConfig.login_process == VariableConfig.login_process_changepassword) {
            tv_title2.setText(getString(R.string.vcodeinput_title2) + "+" + localecode + " " + mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length()));


            iv_left_bg.setVisibility(View.GONE);


            presenter.setTitlePosition(this, cl_global, R.id.cl_global, R.id.tv_title);
            presenter.setClosePosition(this, cl_global, R.id.cl_global, R.id.cl_close);
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

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        tv_confirm.setOnClickListener(this);
        cl_close.setOnClickListener(this);
        tv_resend.setOnClickListener(this);

        //验证码输入框监听
        et_vciv.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
            @Override
            public void onComplete(String code) {
                verificationcode = code;
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(VCodeInputActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                presenter.automaticNextStep();
            }

            @Override
            public void onInput() {
                verificationcode = "";
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(VCodeInputActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
            }

            @Override
            public void onInputPlus(int codesSize, View[] underlineviews) {
                mCodesSize = codesSize;
                mUnderLineViews = underlineviews;
                presenter.setCodeUnderlineBgColor(mCodesSize, mUnderLineViews, VariableConfig.color_button_selected);
            }
        });
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_confirm) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.tv_confirm)) {
                presenter.automaticNextStep();
            }
        } else if (id == R.id.cl_close) {
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            }
        } else if (id == R.id.tv_resend) {//清空原来验证码
//            et_vciv.clearCode();
//            //发送验证码
//            presenter.sms(mobile, locale);
            graphicverification_view.setGraphicVerificationListener(this);
            graphicverification_view.loadUrl("file:///android_asset/web/graphicverification.html");
            graphicverification_view.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void verificationCodeLoginCallback(boolean isSuccess, LoginEntity info, String msg) {
        String token = "";
        if (info != null && info.getToken() != null) {
            token = info.getToken();
        }
        if (isSuccess) {

            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(token);
            MySPUtilsUser.getInstance().saveUserMobile(mobile);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_MOBILE);

            //验证码接口请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
            presenter.changeloginidentity(token, ConfigConstants.IDENTITY_STUDENT);

            if (info != null && info.getUser_account_id() != null) {  //绑定别名
                JPushInterface.setAlias(VCodeInputActivity.this, sequence++, info.getUser_account_id());
            }

        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
            presenter.setCodeUnderlineBgColor(mCodesSize, mUnderLineViews, "#ff2855");
        }
    }

    @Override
    public void VerificationCodeCheckCallback(boolean isSuccess, String token) {
        if (isSuccess) {
            if (activity_species.equals(ConfigConstants.LOGINACTIVITY) && VariableConfig.login_process == VariableConfig.login_process_changemobile) {
                //保存第一次验证码数据
                MySPUtilsUser.getInstance().saveUserVerificationCodeFirst(verificationcode);
                //计时器停止
                if (!StringUtils.isBlank(timeCountdown)) {
                    timeCountdown.cancel();
                    VariableConfig.verificationcode_countdown_flag = true;
                }
                //跳转到更新手机页面
                Bundle bundle = new Bundle();
                bundle.putString("mode", "1");
                bundle.putString("locale", locale);
                bundle.putString("localename", localename);
                bundle.putString("localecode", localecode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, ChangeMobileActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, ChangeMobileActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            } else if (activity_species.equals(ConfigConstants.LOGINACTIVITY) && VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("locale", locale);
                bundle.putString("localecode", localecode);
                bundle.putString("localename", localename);
                bundle.putString("verificationcode", verificationcode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            } else if (activity_species.equals(ConfigConstants.FORGETPWDACTIVITY)) {
                //跳转到设置新密码页面
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("locale", locale);
                bundle.putString("localecode", localecode);
                bundle.putString("localename", localename);
                bundle.putString("verificationcode", verificationcode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        } else {
            ToastUtils.showShortToastFromText(token, ToastUtils.top);
        }
    }

    @Override
    public void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg) {
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(userIdentityEntity.getToken());
            MySPUtilsUser.getInstance().saveUserLocale(locale);
            MySPUtilsUser.getInstance().saveUserLocaleCode(localecode);
            MySPUtilsUser.getInstance().saveUserLocaleName(localename);
            MySPUtilsUser.getInstance().saveUserMobile(mobile);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_MOBILE);

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
                bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.VCODEINPUTACTIVITY);
                bundle.putString("current_identity", userIdentityEntity.getCurrent_identity());
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, ChooseIdentityActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void smsCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            if (StringUtils.isBlank(timeCountdown)) {
                timeCountdown = new TimeCountdown(60000, 1000);
            }
            timeCountdown.start();
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void mobileCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //更新手机号
            MySPUtilsUser.getInstance().saveUserMobile(mobile);
            //token的值 设置为空
            MySPUtilsUser.getInstance().saveUserToken("");
            //更换手机号成功，需要重新走登录流程
            VariableConfig.login_process = VariableConfig.login_process_normal;
            if (ScreenTools.getInstance().isPad(this)) {
//                PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, LoginActivity.class,
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, LoginPlusActivity.class,
                        -1, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, LoginPlusActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else {
            presenter.setCodeUnderlineBgColor(mCodesSize, mUnderLineViews, "#ff2855");
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void nextStepCallback() {
        if (!StringUtils.isBlank(verificationcode) && verificationcode.length() == 6) {
            if (activity_species.equals(ConfigConstants.LOGINACTIVITY)) {

                if (VariableConfig.login_process == VariableConfig.login_process_changemobile) {
                    mode = "1";
                    presenter.VerificationCodeCheck2(mode, verificationcode);
                } else if (VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                    mode = "1";
                    presenter.VerificationCodeCheck2(mode, verificationcode);
                } else {
                    presenter.VerificationCodeLogin(locale, mobile, verificationcode);
                }
            } else if (activity_species.equals(ConfigConstants.FORGETPWDACTIVITY)) {
                presenter.VerificationCodeCheck(locale, mobile, verificationcode);
            } else if (activity_species.equals(ConfigConstants.CHANGEMOBILEACTIVITY)) {
                String token = MySPUtilsUser.getInstance().getUserToken();
                String pwd = MySPUtilsUser.getInstance().getUserPwd();
                String user_verificationcode_first = MySPUtilsUser.getInstance().getUserVerificationCodeFirst();
                if ("2".equals(mode)) {
                    presenter.mobile(token, mode, pwd, locale, mobile, verificationcode);
                } else if ("1".equals(mode)) {
                    presenter.mobile(token, mode, user_verificationcode_first, locale, mobile, verificationcode);
                }
            } else if (activity_species.equals(ConfigConstants.PWDLOGINACTIVITY)) {
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("locale", locale);
                bundle.putString("localecode", localecode);
                bundle.putString("localename", localename);
                bundle.putString("verificationcode", verificationcode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(VCodeInputActivity.this, SetNewPasswordActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void EventBusTimeMessage(MessageEvent messageEvent) {
        if (EventConstant.EVENT_VERIFICATIONCODE_COUNTDOWN == messageEvent.getMessage_type()) {
//            LogUtils.i(ConfigConstants.TAG_ALL, "messageEvent =-= " + messageEvent.getMessage());
            if ((Long) messageEvent.getMessage() != -1) {
                tv_resend.setVisibility(View.VISIBLE);
                tv_resend.setClickable(false);
//                String content = messageEvent.getMessage() + getString(R.string.vcodeinput_resend);
                String content = String.format(getResources().getString(R.string.vcodeinput_resend), String.valueOf(messageEvent.getMessage()));
                tv_resend.setText(content);
            } else {
                tv_resend.setVisibility(View.VISIBLE);
                tv_resend.setClickable(true);
                String content1 = getResources().getString(R.string.vcodeinput_resend2);
                String content2 = getResources().getString(R.string.vcodeinput_resend3);
                SpannableString msp = new SpannableString(content1 + content2);
//                //设置字体前景色
                msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.networkshcool_1d6aff)), content1.length(), (content1 + content2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_resend.setText(msp);
            }
        }
    }


    /**
     * 验证码 图文验证 回调
     */
    @Override
    public void jsReturnResults(int ret, String ticket, String randstr) {
        PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, WHATCODE_REMOVEVIEW, 100);
        if (ret == 0) {
            et_vciv.clearCode();
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
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            }
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
                KeyBoardUtil.getInstance().hideKeyBoard(this, et_vciv);
            } else {
                KeyBoardUtil.getInstance().showKeyBoard(this, et_vciv);
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
