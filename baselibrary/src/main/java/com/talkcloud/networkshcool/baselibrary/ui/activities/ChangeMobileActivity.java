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
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.ChangeMobilePresenter;
import com.talkcloud.networkshcool.baselibrary.utils.FastDoubleClickUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.ChangeMobileView;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/17
 * Time:20:45
 * author:joker
 * 更换手机号页面
 */
public class ChangeMobileActivity extends BaseActivity implements ChangeMobileView, View.OnClickListener, CustomAdapt, GraphicVerificationView.GraphicVerificationListener, HandlerUtils.OnReceiveMessageListener {


    private ChangeMobilePresenter presenter;

    private ConstraintLayout cl_phonenum;
    private ConstraintLayout cl_phonecountrycode;
    private ConstraintLayout cl_close;
    private ConstraintLayout cl_global;
    private EditTextCustomize et_phone;
    private TextView tv_confirm;
    private TextView tv_phonecountrycode;
    private ImageView iv_left_bg;
    private ImageView iv_close;


    private String mobile = "";
    private String mode = "";
    private String locale = VariableConfig.default_locale;
    private String localecode = VariableConfig.default_localecode;
    private String localename = VariableConfig.default_localename;

    //短信验证码倒计时
    private long sms_countdown = 0L;


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
        return R.layout.activity_changemobile;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {
        cl_phonenum = findViewById(R.id.cl_phonenum);
        et_phone = findViewById(R.id.et_phone);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_phonecountrycode = findViewById(R.id.tv_phonecountrycode);
        cl_phonecountrycode = findViewById(R.id.cl_phonecountrycode);
        cl_close = findViewById(R.id.cl_close);
        iv_left_bg = findViewById(R.id.iv_left_bg);
        cl_global = findViewById(R.id.cl_global);
        iv_close = findViewById(R.id.iv_close);
        graphicverification_view = findViewById(R.id.graphicverification_view);
    }

    @Override
    protected void initData() {
        presenter = new ChangeMobilePresenter(this, this);

        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            mode = bundle.getString("mode");
            locale = bundle.getString("locale");
            localename = bundle.getString("localename");
            localecode = bundle.getString("localecode");
        }
        tv_phonecountrycode.setText("+" + localecode);


        //设置tv_confirm 背景颜色
        if (StringUtils.isBlank(et_phone.getText().toString().trim())) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
        }
        //设置输入框的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
        //设置返回按钮的背景
//        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, cl_close, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);


        iv_left_bg.setVisibility(View.GONE);
        presenter.setTitlePosition(this, cl_global, R.id.cl_global, R.id.tv_title);
        presenter.setClosePosition(this, cl_global, R.id.cl_global, R.id.cl_close);
        iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
    }

    @Override
    protected void initListener() {
        cl_phonecountrycode.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        cl_close.setOnClickListener(this);


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
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(ChangeMobileActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(ChangeMobileActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获取焦点
                    et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {//失去焦点
                    et_phone.setEditTextBG(cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_phonecountrycode) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.cl_phonecountrycode)) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("localename", localename);
                bundle2.putString("localecode", localecode);
                if (ScreenTools.getInstance().isPad(this)) {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(ChangeMobileActivity.this, CountryAreaActivity.class, -1, bundle2, false,
                            R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(ChangeMobileActivity.this, CountryAreaActivity.class, -1, bundle2, false,
                            R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        } else if (id == R.id.tv_confirm) {
            mobile = et_phone.getText().toString().trim();
//                if (VariableConfig.verificationcode_countdown_flag) {
//                    presenter.checkMobile(mobile, locale);
//                } else {
//                    if (MySPUtilsUser.getInstance().getMobileTemp().equals(mobile)) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("mode", mode);
//                        bundle.putString("mobile", mobile);
//                        bundle.putString("locale", locale);
//                        bundle.putString("localecode", localecode);
//                        bundle.putString("localename", localename);
//                        bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.CHANGEMOBILEACTIVITY);
//                        PublicPracticalMethodFromJAVA.getInstance().intentToJump(ChangeMobileActivity.this, VCodeInputActivity.class, -1, bundle, false);
//                    } else {
//                        String content = String.format(getResources().getString(R.string.Verification_code_sended), sms_countdown + "");
//                        ToastUtils.showShortToastFromText(content, ToastUtils.top);
//                    }
//                }
            presenter.checkMobile(mobile, locale);
        } else if (id == R.id.cl_close) {
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void EventBusCountryAreaMessage(MessageEvent messageEvent) {
        if (EventConstant.EVENT_COUNTRYAREA.equals(messageEvent.getMessage_type())) {
            Bundle bundle = (Bundle) messageEvent.getMessage();
            if (!StringUtils.isBlank(bundle)) {
                locale = bundle.getString("locale");
                localename = bundle.getString("localename");
                localecode = bundle.getString("localecode");
                tv_phonecountrycode.setText("+" + localecode);
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
            bundle.putString("mode", mode);
            bundle.putString("mobile", msg);
            bundle.putString("locale", locale);
            bundle.putString("localecode", localecode);
            bundle.putString("localename", localename);
            bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.CHANGEMOBILEACTIVITY);
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(ChangeMobileActivity.this, VCodeInputActivity.class, -1, bundle, false,
                        R.anim.activity_xhold, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(ChangeMobileActivity.this, VCodeInputActivity.class, -1, bundle, false,
                        R.anim.activity_right_in, R.anim.activity_right_out);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void checkMobileCallback(boolean isSuccess, boolean exists, String msg) {
        if (isSuccess) {
            if (exists) {
                ToastUtils.showShortToastFromText(getResources().getString(R.string.phone_does_exist), ToastUtils.top);
            } else {
//                presenter.sms(mobile, locale);

                graphicverification_view.setGraphicVerificationListener(this);
                graphicverification_view.loadUrl("file:///android_asset/web/graphicverification.html");
                graphicverification_view.setVisibility(View.VISIBLE);
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
    protected void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
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
