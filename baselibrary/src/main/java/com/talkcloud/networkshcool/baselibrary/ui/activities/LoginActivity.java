package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
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
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.LoginPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.FastDoubleClickUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.LoginView;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/11
 * Time:14:38
 * author:joker
 * 1.登录页面
 * 2.验证身份手机号发送验证码页面
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener, CustomAdapt, GraphicVerificationView.GraphicVerificationListener, HandlerUtils.OnReceiveMessageListener {

    private TextView tv_confirm;
    private TextView tv_usepwd;
    private TextView tv_phonecountrycode;
    private TextView tv_title;
    private TextView tv_title2;
    private TextView tv_userprivacyagreement;
    private TextView tv_useaccount;
    private ImageView iv_userprivacyagreement;
    private EditTextCustomize et_phone;
    private ImageView iv_left_bg;
    private ImageView iv_close;
    private View dividingline_vertical;
    private LoginPresenter presenter;
    private String locale = VariableConfig.default_locale;
    private String localecode = VariableConfig.default_localecode;
    private String localename = VariableConfig.default_localename;
    //输入框获取的电话号码
    private String mobile = "";
    //短信验证码倒计时
    private long sms_countdown = 0L;

    private ConstraintLayout cl_phonecountrycode;
    private ConstraintLayout cl_phonenum;
    private ConstraintLayout cl_close;
    private ConstraintLayout cl_global;


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
        return R.layout.activity_login;
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
        et_phone = findViewById(R.id.et_phone);
        cl_phonecountrycode = findViewById(R.id.cl_phonecountrycode);
        tv_usepwd = findViewById(R.id.tv_usepwd);
        tv_phonecountrycode = findViewById(R.id.tv_phonecountrycode);
        cl_phonenum = findViewById(R.id.cl_phonenum);
        tv_title = findViewById(R.id.tv_title);
        tv_title2 = findViewById(R.id.tv_title2);
        cl_close = findViewById(R.id.cl_close);
        iv_left_bg = findViewById(R.id.iv_left_bg);
        cl_global = findViewById(R.id.cl_global);
        iv_close = findViewById(R.id.iv_close);
        tv_userprivacyagreement = findViewById(R.id.tv_userprivacyagreement);
        iv_userprivacyagreement = findViewById(R.id.iv_userprivacyagreement);
        tv_useaccount = findViewById(R.id.tv_useaccount);
        dividingline_vertical = findViewById(R.id.dividingline_vertical);
        graphicverification_view = findViewById(R.id.graphicverification_view);

    }


    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        presenter = new LoginPresenter(this, this);

        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        if (ScreenTools.getInstance().isPad(this)) {
            iv_left_bg.setVisibility(View.VISIBLE);
        } else {
            iv_left_bg.setVisibility(View.GONE);
        }

        //设置输入框的背景色
        et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);

        //判断是否是修改手机号或修改密码流程
        if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                VariableConfig.login_process == VariableConfig.login_process_changepassword) {
            tv_title.setText(getString(R.string.login_title_ps));
            tv_title2.setText(getString(R.string.login_title2_ps));

            mobile = MySPUtilsUser.getInstance().getUserMobile();
            et_phone.setText(mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length()));
            et_phone.setTextColor(ContextCompat.getColor(this, R.color.networkshcool_a3a3ae));
            et_phone.setEnabled(false);

            localecode = MySPUtilsUser.getInstance().getUserLocaleCode();
            tv_phonecountrycode.setTextColor(ContextCompat.getColor(this, R.color.networkshcool_a3a3ae));
            tv_phonecountrycode.setText("+" + localecode);

            cl_phonecountrycode.setEnabled(false);

            tv_confirm.setText(getString(R.string.login_confirm_ps));
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);

            tv_usepwd.setText(getString(R.string.login_usepwd_ps));

            if (VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                tv_usepwd.setVisibility(View.GONE);
            }

            cl_close.setVisibility(View.VISIBLE);

            iv_left_bg.setVisibility(View.GONE);

            tv_useaccount.setVisibility(View.GONE);
            dividingline_vertical.setVisibility(View.GONE);


            presenter.setTitlePosition(this, cl_global, R.id.cl_global, R.id.tv_title);
            presenter.setClosePosition(this, cl_global, R.id.cl_global, R.id.cl_close);
            iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
        } else {
            //设置返回按钮的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, cl_close, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);

            Bundle bundle = getIntent().getExtras();
            if (!StringUtils.isBlank(bundle)) {
                mobile = bundle.getString("mobile");
                et_phone.setText(mobile);
            } else {
                mobile = MySPUtilsUser.getInstance().getUserMobile();
                if (!StringUtils.isBlank(mobile)) {
                    et_phone.setText(mobile);
                }
            }

            //获取国家区域的值
            presenter.getCountryCodeDatas("");


            String title = String.format(getResources().getString(R.string.login_title), getResources().getString(TKExtManage.getInstance().getAppNameRes(this)));
            tv_title.setText(title);
        }


        //设置tv_confirm 背景颜色
        if (StringUtils.isBlank(et_phone.getText().toString().trim())) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
        }
    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        tv_confirm.setOnClickListener(this);
        cl_phonecountrycode.setOnClickListener(this);
        tv_usepwd.setOnClickListener(this);
        cl_close.setOnClickListener(this);
        iv_userprivacyagreement.setOnClickListener(this);
        tv_userprivacyagreement.setOnClickListener(this);
        tv_useaccount.setOnClickListener(this);


        et_phone.setOnEditTextListener(new EditTextCustomize.onEditTextListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isBlank(s)) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(LoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(LoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
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
                if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                        VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                    mobile = MySPUtilsUser.getInstance().getUserMobile();
                } else {
                    if (!VariableConfig.userPrivacyAgreementStatus) {
                        ToastUtils.showShortToastFromRes(R.string.login_userprivacyagreement3, ToastUtils.top);
                        return;
                    }
                    mobile = et_phone.getText().toString().trim();
                }

                if (VariableConfig.verificationcode_countdown_flag) {
//                    presenter.sms(mobile, locale);

                    graphicverification_view.setGraphicVerificationListener(this);
                    graphicverification_view.loadUrl("file:///android_asset/web/graphicverification.html");
                    graphicverification_view.setVisibility(View.VISIBLE);

                } else {
                    if (MySPUtilsUser.getInstance().getMobileTemp().equals(mobile)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mobile", mobile);
                        bundle.putString("mode", "1");
                        bundle.putString("locale", locale);
                        bundle.putString("localecode", localecode);
                        bundle.putString("localename", localename);
                        bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.LOGINACTIVITY);
                        if (ScreenTools.getInstance().isPad(this)) {
                            PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                        } else {
                            PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                        }
                    } else {
                        String content = String.format(getResources().getString(R.string.Verification_code_sended), sms_countdown + "");
                        ToastUtils.showShortToastFromText(content, ToastUtils.top);
                    }
                }
            }

//                CustomProgressDialog cProgressDialog = CustomProgressDialog
//                        .createDialog(this, false);
//                cProgressDialog.setMessage("");
//                cProgressDialog.show();

//                Bundle bundle77777 = new Bundle();
//                bundle77777.putString("mobile", mobile);
//                bundle77777.putString("locale", locale);
//                bundle77777.putString("localecode", localecode);
//                bundle77777.putString("localename", localename);
//                bundle77777.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.LoginActivity);
//                PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, VCodeInputActivity.class, -1, bundle77777, false);
        } else if (id == R.id.cl_phonecountrycode) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.cl_phonecountrycode)) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("localename", localename);
                bundle2.putString("localecode", localecode);

                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, CountryAreaActivity.class, -1, bundle2, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, CountryAreaActivity.class, -1, bundle2, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        } else if (id == R.id.tv_usepwd) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.tv_usepwd)) {
                if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                        VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                    mobile = MySPUtilsUser.getInstance().getUserMobile();
                } else {
                    mobile = et_phone.getText().toString().trim();
                }
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("mode", "2");
                bundle.putString("locale", locale);
                bundle.putString("localename", localename);
                bundle.putString("localecode", localecode);

                if (ScreenTools.getInstance().isPad(this)) {
                    if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                        //跳转到密码登录页面
                        PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, PwdLoginActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                        return;
                    }
                }
                //跳转到密码登录页面
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, PwdLoginActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else if (id == R.id.cl_close) {
            presenter.exitActivity();
        } else if (id == R.id.iv_userprivacyagreement) {
            presenter.setUserPrivacyAgreementStatus(iv_userprivacyagreement);
        } else if (id == R.id.tv_userprivacyagreement) {//跳转到隐私协议页面
//            PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, UserAgreementWebView.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.tv_useaccount) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.tv_useaccount)) {
                if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                        VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                    mobile = MySPUtilsUser.getInstance().getUserMobile();
                } else {
                    mobile = et_phone.getText().toString().trim();
                }

                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("mode", "3");
                bundle.putString("locale", locale);
                bundle.putString("localename", localename);
                bundle.putString("localecode", localecode);

                if (ScreenTools.getInstance().isPad(this)) {
                    //跳转到账号登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, AccountLoginActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    //跳转到账号登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, AccountLoginActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        }
    }

    /**
     * priority 数值越大，优先级越高,默认优先级为0
     *
     * @param messageEvent
     */
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
                    countryNameAndCodeCallback(localename, localecode, locale);
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
    public void smsCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveMobileTemp(msg);

            Bundle bundle = new Bundle();
            bundle.putString("mobile", msg);
            bundle.putString("mode", "1");
            bundle.putString("locale", locale);
            bundle.putString("localecode", localecode);
            bundle.putString("localename", localename);
            bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.LOGINACTIVITY);
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(LoginActivity.this, VCodeInputActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void countryNameAndCodeCallback(String name, String code, String mlocale) {
        localename = name;
        localecode = code;
        locale = mlocale;
        tv_phonecountrycode.setText("+" + localecode);
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
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (VariableConfig.login_process == VariableConfig.login_process_normal) {
            presenter.setUserPrivacyAgreementBG(tv_userprivacyagreement, iv_userprivacyagreement);
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
            presenter.exitAPP();
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
            } else {
                KeyBoardUtil.getInstance().showKeyBoard(this, et_phone);
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
