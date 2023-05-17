package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.AccountLoginPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.FastDoubleClickUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.AccountLoginView;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Date:2021/6/25
 * Time:14:08
 * author:joker
 * 用户账号登录
 */
public class AccountLoginActivity extends BaseActivity implements AccountLoginView, View.OnClickListener {

    private AccountLoginPresenter presenter;


    private ConstraintLayout cl_close;
    private ConstraintLayout cl_phonenum;
    private ConstraintLayout cl_pwd;
    private TextView tv_confirm;
    private EditTextCustomize et_phone;
    private EditTextCustomize et_pwd;
    private TextView tv_useverificationcode;
    private ImageView iv_pwd_status;
    private TextView tv_userprivacyagreement;
    private ImageView iv_userprivacyagreement;
    private TextView tv_usepwd;
    private ImageView iv_loading;
    private ImageView iv_close;


    //密码状态是否显示
    private boolean pwdIsVisible = false;

    private String locale = VariableConfig.default_locale;
    private String localecode = VariableConfig.default_localecode;
    private String localename = VariableConfig.default_localename;
    private String mobile = "";
    private String pwd = "";
    private String mode = "";
    private static int sequence = 1;


    @Override
    protected void onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //状态栏状态设置
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            false, true,
                            -1
                    );
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_accountlogin;
    }

    @Override
    protected void initUiView() {
        cl_close = findViewById(R.id.cl_close);
        tv_confirm = findViewById(R.id.tv_confirm);
        cl_phonenum = findViewById(R.id.cl_phonenum);
        cl_pwd = findViewById(R.id.cl_pwd);
        et_phone = findViewById(R.id.et_phone);
        et_pwd = findViewById(R.id.et_pwd);
        tv_useverificationcode = findViewById(R.id.tv_useverificationcode);
        iv_pwd_status = findViewById(R.id.iv_pwd_status);
        tv_userprivacyagreement = findViewById(R.id.tv_userprivacyagreement);
        iv_userprivacyagreement = findViewById(R.id.iv_userprivacyagreement);
        tv_usepwd = findViewById(R.id.tv_usepwd);
        iv_loading = findViewById(R.id.iv_loading);
        iv_close = findViewById(R.id.iv_close);
    }

    @Override
    protected void initData() {
        presenter = new AccountLoginPresenter(this, this);

        //当账号不为空的时候，显示账号
        String account = MySPUtilsUser.getInstance().getUserAccount();
        if (!StringUtils.isBlank(account)) {
            et_phone.setText(account);
        }


        //设置登录按钮背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
        //设置输入框的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
        //设置输入框的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);

        //隐藏密码
        et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            mobile = bundle.getString("mobile");
            mode = bundle.getString("mode");
            locale = bundle.getString("locale");
            localename = bundle.getString("localename");
            localecode = bundle.getString("localecode");
        }


        if (ScreenTools.getInstance().isPad(this)) {
            //设置返回按钮的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, cl_close, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
        } else {
            iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
        }

    }

    @Override
    protected void initListener() {
        tv_useverificationcode.setOnClickListener(this);
        cl_close.setOnClickListener(this);
        iv_pwd_status.setOnClickListener(this);
        iv_userprivacyagreement.setOnClickListener(this);
        tv_userprivacyagreement.setOnClickListener(this);
        tv_usepwd.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);


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
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, cl_phonenum, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }
            }
        });


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
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_unselected);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);
                }
            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_focus_bg, VariableConfig.color_transparent_bg);
                } else {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(AccountLoginActivity.this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                            VariableConfig.color_inputbox_unfocus_bg, VariableConfig.color_transparent_bg);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_useverificationcode || id == R.id.cl_close) {
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
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
        } else if (id == R.id.iv_userprivacyagreement) {
            presenter.setUserPrivacyAgreementStatus(iv_userprivacyagreement);
        } else if (id == R.id.tv_userprivacyagreement) {//跳转到隐私协议页面
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, UserAgreementWebView.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.tv_usepwd) {//跳转到密码登录
            if (!StringUtils.isBlank(mobile)) {
                Bundle bundle = new Bundle();
                bundle.putString("mobile", mobile);
                bundle.putString("mode", "2");
                bundle.putString("locale", locale);
                bundle.putString("localename", localename);
                bundle.putString("localecode", localecode);
                if (ScreenTools.getInstance().isPad(this)) {
                    //跳转到密码登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(AccountLoginActivity.this, PwdLoginActivity.class, -1, bundle, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    //跳转到密码登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(AccountLoginActivity.this, PwdLoginActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            } else {
                if (ScreenTools.getInstance().isPad(this)) {
                    //跳转到密码登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(AccountLoginActivity.this, PwdLoginActivity.class, -1, null, false, R.anim.activity_xhold, R.anim.activity_xhold);
                } else {
                    //跳转到密码登录页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(AccountLoginActivity.this, PwdLoginActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_right_out);
                }
            }
        } else if (id == R.id.tv_confirm) {
            if (!FastDoubleClickUtils.isFastDoubleClick(R.id.tv_confirm)) {
                if (!VariableConfig.userPrivacyAgreementStatus) {
                    ToastUtils.showShortToastFromRes(R.string.login_userprivacyagreement3, ToastUtils.top);
                    return;
                }
                //账号
                mobile = et_phone.getText().toString().trim();
                //密码
                pwd = et_pwd.getText().toString().trim();
                presenter.accountLogin(locale, mobile, pwd, iv_loading, tv_confirm);
            }
        }

    }

    /**
     * 账号登录回调
     *
     * @param isSuccess
     * @param info
     * @param msg
     */
    @Override
    public void accountLoginCallback(boolean isSuccess, LoginEntity info, String msg) {
        String token = "";
        if (info != null && info.getToken() != null) {
            token = info.getToken();
        }
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(token);
            MySPUtilsUser.getInstance().saveUserAccount(mobile);
            MySPUtilsUser.getInstance().saveUserPwd(pwd);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_ACCOUNT);

            if (info != null && info.getUser_account_id() != null) {  //绑定别名
                JPushInterface.setAlias(AccountLoginActivity.this, sequence++, info.getUser_account_id());
            }

            //登录请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
            presenter.changeloginidentity(token, ConfigConstants.IDENTITY_STUDENT);
        } else {
            presenter.loginAnimation(iv_loading, tv_confirm, false);
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
            //设置输入框的背景色
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_pwd, getResources().getDimensionPixelSize(R.dimen.dimen_12x), 1,
                    "#ff2855", VariableConfig.color_transparent_bg);
        }
    }

    /**
     * 切换身份回调
     *
     * @param isSuccess
     * @param userIdentityEntity
     * @param msg
     */
    @Override
    public void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg) {
        presenter.loginAnimation(iv_loading, tv_confirm, false);
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveUserToken(userIdentityEntity.getToken());
            MySPUtilsUser.getInstance().saveUserLocale(locale);
            MySPUtilsUser.getInstance().saveUserLocaleCode(localecode);
            MySPUtilsUser.getInstance().saveUserLocaleName(localename);
            MySPUtilsUser.getInstance().saveUserAccount(mobile);
            MySPUtilsUser.getInstance().saveUserLoginMethod(ConfigConstants.LOGINMETHOD_ACCOUNT);
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
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(AccountLoginActivity.this, ChooseIdentityActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
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
}
