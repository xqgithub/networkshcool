package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.SetNewPasswordPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.SetNewPasswordView;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;

import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/17
 * Time:14:19
 * author:joker
 * 1.设置新密码页面
 * 2.修改密码页面
 */
public class SetNewPasswordActivity extends BaseActivity implements SetNewPasswordView, View.OnClickListener, CustomAdapt {

    private SetNewPasswordPresenter presenter;

    private ConstraintLayout cl_close;
    private ConstraintLayout cl_pwd;
    private ConstraintLayout cl_pwd_confirm;
    private ConstraintLayout cl_global;
    private EditTextCustomize et_pwd;
    private EditTextCustomize et_pwd_confirm;
    private TextView tv_confirm;
    private TextView tv_title;
    private TextView tv_title2;
    private ImageView iv_left_bg;
    private ImageView iv_close;
    private ImageView iv_pwd_status;
    private ImageView iv_confirmpwd_status;


    private String locale = VariableConfig.default_locale;
    private String localecode = VariableConfig.default_localecode;
    private String localename = VariableConfig.default_localename;
    private String mobile = "";
    private String verificationcode = "";
    private String new_pwd = "";
    private String new_pwd_confirm = "";

    //密码状态是否显示
    private boolean pwdIsVisible = false;
    //确认密码状态是否显示
    private boolean confirmPwdIsVisible = false;


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
        return R.layout.activity_setnewpassword;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {
        cl_close = findViewById(R.id.cl_close);
        cl_pwd = findViewById(R.id.cl_pwd);
        cl_pwd_confirm = findViewById(R.id.cl_pwd_confirm);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_confirm = findViewById(R.id.et_pwd_confirm);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_title = findViewById(R.id.tv_title);
        tv_title2 = findViewById(R.id.tv_title2);
        cl_global = findViewById(R.id.cl_global);
        iv_left_bg = findViewById(R.id.iv_left_bg);
        iv_close = findViewById(R.id.iv_close);
        iv_pwd_status = findViewById(R.id.iv_pwd_status);
        iv_confirmpwd_status = findViewById(R.id.iv_confirmpwd_status);
    }

    @Override
    protected void initData() {
        presenter = new SetNewPasswordPresenter(this, this);

        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            mobile = bundle.getString("mobile");
            locale = bundle.getString("locale");
            localecode = bundle.getString("localecode");
            verificationcode = bundle.getString("verificationcode");
        }


        //设置输入框的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
        //设置输入框的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_pwd_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
        //设置tv_confirm 背景颜色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);

        if (VariableConfig.login_process == VariableConfig.login_process_changepassword) {
            tv_title.setText(getString(R.string.setnewpassword_title_ps));

            tv_title2.setVisibility(View.GONE);

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

        //隐藏密码
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        et_pwd_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    @Override
    protected void initListener() {
        cl_close.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        iv_pwd_status.setOnClickListener(this);
        iv_confirmpwd_status.setOnClickListener(this);


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
                if (StringUtils.isBlank(s) || StringUtils.isBlank(et_pwd_confirm.getText().toString().trim())) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(SetNewPasswordActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(SetNewPasswordActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
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


        //确认密码输入框监听
        et_pwd_confirm.setOnEditTextListener(new EditTextCustomize.onEditTextListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isBlank(s) || StringUtils.isBlank(et_pwd.getText().toString().trim())) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(SetNewPasswordActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(SetNewPasswordActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_pwd.setEditTextBG(cl_pwd_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {
                    et_pwd.setEditTextBG(cl_pwd_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }


            }
        });
    }


    @Override
    public void forgotPwdcallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(msg);
            MySPUtilsUser.getInstance().saveUserMobile(mobile);
            MySPUtilsUser.getInstance().saveUserPwd(new_pwd);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_PWD);

            //验证码接口请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
            presenter.changeloginidentity(msg, ConfigConstants.IDENTITY_STUDENT);
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
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
            MySPUtilsUser.getInstance().saveUserPwd(new_pwd);
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
//                PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, MainMenuActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, MainMenuActivity.class, -1, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
            } else {//用户有多重身份，需要进入选择身份页面
                Bundle bundle = new Bundle();
                bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.SETNEWPASSWORDACTIVITY);
                bundle.putString("current_identity", userIdentityEntity.getCurrent_identity());
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(SetNewPasswordActivity.this, ChooseIdentityActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void pwdupdateCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            //更新密码
            MySPUtilsUser.getInstance().saveUserPwd(new_pwd);

            //token的值 设置为空
            MySPUtilsUser.getInstance().saveUserToken("");
            //更换密码后，需要重新走登录流程
            VariableConfig.login_process = VariableConfig.login_process_normal;
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(SetNewPasswordActivity.this, LoginPlusActivity.class,
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cl_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
        } else if (id == R.id.tv_confirm) {
            if (VariableConfig.login_process == VariableConfig.login_process_changepassword) {
                String token = MySPUtilsUser.getInstance().getUserToken();
                new_pwd = et_pwd.getText().toString().trim();
                new_pwd_confirm = et_pwd_confirm.getText().toString().trim();
                presenter.pwdupdate(token, verificationcode, new_pwd, new_pwd_confirm);
            } else {
                new_pwd = et_pwd.getText().toString().trim();
                new_pwd_confirm = et_pwd_confirm.getText().toString().trim();
                presenter.forgotPwd(locale, mobile, verificationcode, new_pwd, new_pwd_confirm);
//                presenter.forgotPwd(locale, mobile, "8888", new_pwd, new_pwd_confirm);
            }
        } else if (id == R.id.iv_pwd_status) {
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
        } else if (id == R.id.iv_confirmpwd_status) {
            if (confirmPwdIsVisible) {
                //隐藏密码
                et_pwd_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iv_confirmpwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_invisible));
                confirmPwdIsVisible = false;
            } else {
                //显示密码
                et_pwd_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                iv_confirmpwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_visible));
                confirmPwdIsVisible = true;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
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
                KeyBoardUtil.getInstance().hideKeyBoard(this, et_pwd);
//                KeyBoardUtil.getInstance().hideKeyBoard(this, et_pwd_confirm);
            } else {
                KeyBoardUtil.getInstance().showKeyBoard(this, et_pwd);
//                KeyBoardUtil.getInstance().showKeyBoard(this, et_pwd_confirm);
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
