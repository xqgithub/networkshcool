package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import cn.jpush.android.api.JPushInterface
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.TKExtManage
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.common.EventConstant
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser
import com.talkcloud.networkshcool.baselibrary.presenters.LoginPlusPresenter
import com.talkcloud.networkshcool.baselibrary.utils.*
import com.talkcloud.networkshcool.baselibrary.utils.ConstraintUtil.ConstraintBegin
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils.HandlerHolder
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils.OnReceiveMessageListener
import com.talkcloud.networkshcool.baselibrary.views.LoginPlusView
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView
import com.talkcloud.networkshcool.baselibrary.weiget.RxViewUtils
import kotlinx.android.synthetic.main.activity_loginplus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.properties.Delegates


/**
 * Date:2021/9/8
 * Time:14:34
 * author:joker
 * 登录页面升级
 */
class LoginPlusActivity : BaseActivity(), RxViewUtils.Action1<View>, LoginPlusView, GraphicVerificationView.GraphicVerificationListener, OnReceiveMessageListener {

    private lateinit var mPresenter: LoginPlusPresenter

    private var locale = VariableConfig.default_locale
    private var localecode = VariableConfig.default_localecode
    private var localename = VariableConfig.default_localename

    //登录方式
    private var loginMethod by Delegates.notNull<Int>()

    //输入框获取的电话号码
    private lateinit var mobile: String

    //密码
    private lateinit var pwd: String

    //短信验证码倒计时
    private var sms_countdown = 0L

    //密码状态是否显示
    private var pwdIsVisible = false

    private var sequence = 1

    //账号输入框临时数据
    private var account_temp: String = ""

    //手机号输入框临时数据
    private var phone_temp: String = ""


    private lateinit var handler: Handler

    //运行计时器标识
    private val WHATCODE_REMOVEVIEW = -1111111


    override fun onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this)
            if (VariableConfig.login_process == VariableConfig.login_process_normal) {
                //隐藏状态栏
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                return
            }
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this)
        }
        //状态栏状态设置
        PublicPracticalMethodFromJAVA.getInstance()
            .transparentStatusBar(
                this,
                false, true,
                -1
            )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_loginplus
    }

    override fun initUiView() {
        //设置切换选项卡的背景
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
            this, cl_tabswitch,
            resources.getDimensionPixelSize(R.dimen.dimen_12x).toFloat(), -1, "", "#EFEEF1"
        )

        switchLoginMethodUi(MySPUtilsUser.getInstance().userLoginMethod)

        if (ScreenTools.getInstance().isPad(this)) iv_left_bg.visibility = View.VISIBLE else iv_left_bg.visibility = View.GONE

    }

    override fun initData() {
        mPresenter = LoginPlusPresenter(this, this)
        //初始化handler
        handler = HandlerHolder(Looper.myLooper(), this, this)

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }

        tv_title.apply {
            val title = String.format(resources.getString(R.string.login_title), resources.getString(TKExtManage.getInstance().getAppNameRes(this@LoginPlusActivity)))
            text = title
        }

        mPresenter.setUserPrivacyAgreementBG(tv_userprivacyagreement, iv_userprivacyagreement)

        mPresenter.getCountryCodeDatas()

    }

    override fun initListener() {
        RxViewUtils.getInstance().setOnClickListeners(
            this, 500,
            tv_phone_login, tv_account_login, iv_userprivacyagreement,
            cl_phonecountrycode, tv_confirm, tv_useverificationcode,
            tv_forgetpwd, tv_usepwd, iv_pwd_status
        )

        //手机号输入框监听事件
        et_phone.setOnEditTextListener(object : EditTextCustomize.onEditTextListener {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                var color_bg = ""
                when (loginMethod) {
                    ConfigConstants.LOGINMETHOD_MOBILE -> {
                        color_bg = if (StringUtils.isBlank(s)) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this@LoginPlusActivity, tv_confirm, resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", color_bg)
                        phone_temp = s.toString().trim()
                    }
                    ConfigConstants.LOGINMETHOD_PWD -> {
                        color_bg = if (StringUtils.isBlank(s) || StringUtils.isBlank(et_pwd.text.toString().trim())) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                        phone_temp = s.toString().trim()
                    }
                    ConfigConstants.LOGINMETHOD_ACCOUNT -> {
                        color_bg = if (StringUtils.isBlank(s) || StringUtils.isBlank(et_pwd.text.toString().trim())) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                        account_temp = s.toString().trim()
                    }
                }

                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this@LoginPlusActivity, tv_confirm, resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", color_bg)
            }

            override fun onFocusChange(v: View, hasFocus: Boolean) {
                var strokeColor = if (hasFocus) {//获取焦点
                    if (!et_phone.isEnabled) return
                    VariableConfig.color_inputbox_focus_bg
                } else {//失去焦点
                    VariableConfig.color_inputbox_unfocus_bg
                }
                et_phone.setEditTextBG(cl_phonenum, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
            }
        })

        //密码框监听事件
        et_pwd.setOnEditTextListener(object : EditTextCustomize.onEditTextListener {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                var color_bg = ""
                when (loginMethod) {
                    ConfigConstants.LOGINMETHOD_PWD -> {
                        color_bg = if (StringUtils.isBlank(s) || StringUtils.isBlank(et_phone.text.toString().trim())) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                    }
                    ConfigConstants.LOGINMETHOD_ACCOUNT -> {
                        color_bg = if (StringUtils.isBlank(s) || StringUtils.isBlank(et_phone.text.toString().trim())) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                    }
                }
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this@LoginPlusActivity, tv_confirm, resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", color_bg)
            }

            override fun onFocusChange(v: View, hasFocus: Boolean) {
                var strokeColor = if (hasFocus) {//获取焦点
                    if (!et_pwd.isEnabled) return
                    VariableConfig.color_inputbox_focus_bg
                } else {//失去焦点
                    VariableConfig.color_inputbox_unfocus_bg
                }
                et_pwd.setEditTextBG(cl_pwd, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
            }
        })
    }

    /**
     * 点击事件回调
     */
    override fun onRxViewClick(v: View) {
        when (v) {
            tv_phone_login, tv_useverificationcode -> {
                switchLoginMethodUi(ConfigConstants.LOGINMETHOD_MOBILE)
            }
            tv_account_login -> switchLoginMethodUi(ConfigConstants.LOGINMETHOD_ACCOUNT)
            tv_usepwd -> switchLoginMethodUi(ConfigConstants.LOGINMETHOD_PWD)
            iv_userprivacyagreement -> {
                iv_userprivacyagreement.apply {
                    var bgcolor = ""
                    var drawableID = -1
                    if (VariableConfig.userPrivacyAgreementStatus) {
                        VariableConfig.userPrivacyAgreementStatus = false
                        bgcolor = "#00000000"
                        drawableID = R.mipmap.login_userprivacyagreement_unselected
                    } else {
                        VariableConfig.userPrivacyAgreementStatus = true
                        bgcolor = VariableConfig.color_button_selected
                        drawableID = R.mipmap.login_userprivacyagreement_selected
                    }
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this@LoginPlusActivity, this, this@LoginPlusActivity.resources.getDimensionPixelSize(R.dimen.dimen_120x), bgcolor)
                    setImageDrawable(ContextCompat.getDrawable(this@LoginPlusActivity, drawableID))
                }
            }
            cl_phonecountrycode -> {
                with(Bundle()) {
                    putString("localename", localename)
                    putString("localecode", localecode)
                    var enterAnimID: Int
                    var exitAnimID: Int
                    if (ScreenTools.getInstance().isPad(this@LoginPlusActivity)) {
                        enterAnimID = R.anim.activity_xhold
                        exitAnimID = R.anim.activity_xhold
                    } else {
                        enterAnimID = R.anim.activity_right_in
                        exitAnimID = R.anim.activity_xhold
                    }
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(
                        this@LoginPlusActivity, CountryAreaActivity::class.java,
                        -1, this, false, enterAnimID, exitAnimID
                    )
                }
            }
            tv_confirm -> {
                if (!VariableConfig.userPrivacyAgreementStatus) {
                    ToastUtils.showShortToastFromRes(R.string.login_userprivacyagreement3, ToastUtils.top)
                    return
                }

                if (loginMethod == ConfigConstants.LOGINMETHOD_MOBILE) {
                    mobile = et_phone.text.toString().trim()

                    if (VariableConfig.verificationcode_countdown_flag) {
//                        mPresenter.sms(mobile, locale)
                        graphicverification_view.let {
                            it.setGraphicVerificationListener(this)
                            it.loadUrl()
                            it.visibility = View.VISIBLE
                        }
                    } else {
                        if (MySPUtilsUser.getInstance().mobileTemp == mobile) {
                            with(Bundle()) {
                                putString("mobile", mobile)
                                putString("mode", "1")
                                putString("locale", locale)
                                putString("localecode", localecode)
                                putString("localename", localename)
                                putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.LOGINACTIVITY)

                                var enterAnimID: Int
                                var exitAnimID: Int
                                if (ScreenTools.getInstance().isPad(this@LoginPlusActivity)) {
                                    enterAnimID = R.anim.activity_xhold
                                    exitAnimID = R.anim.activity_xhold
                                } else {
                                    enterAnimID = R.anim.activity_right_in
                                    exitAnimID = R.anim.activity_xhold
                                }
                                PublicPracticalMethodFromJAVA.getInstance().intentToJump(
                                    this@LoginPlusActivity, VCodeInputActivity::class.java,
                                    -1, this, false, enterAnimID, exitAnimID
                                )
                            }
                        } else {
                            val content = String.format(resources.getString(R.string.Verification_code_sended), sms_countdown.toString())
                            ToastUtils.showShortToastFromText(content, ToastUtils.top)
                        }
                    }
                } else if (loginMethod == ConfigConstants.LOGINMETHOD_PWD) {
                    mobile = et_phone.text.toString().trim()
                    pwd = et_pwd.text.toString().trim()
                    mPresenter.passwordLogin(locale, mobile, pwd, iv_loading, tv_confirm_content_show)
                } else if (loginMethod == ConfigConstants.LOGINMETHOD_ACCOUNT) {
                    mobile = et_phone.text.toString().trim()
                    pwd = et_pwd.text.toString().trim()
                    mPresenter.accountLogin(locale, mobile, pwd, iv_loading, tv_confirm_content_show)
                }
            }
            iv_pwd_status -> {
                if (pwdIsVisible) {
                    //隐藏密码
                    pwdIsVisible = false
                    et_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
                    iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_invisible))
                } else {
                    //显示密码
                    pwdIsVisible = true
                    et_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.pwdlogin_visible))
                }
            }
            tv_forgetpwd -> {
                mobile = et_phone.text.toString().trim()
                with(Bundle()) {
                    putString("mobile", mobile)
                    putString("locale", locale)
                    putString("localename", localename)
                    putString("localecode", localecode)

                    var enterAnimID: Int
                    var exitAnimID: Int
                    if (ScreenTools.getInstance().isPad(this@LoginPlusActivity)) {
                        enterAnimID = R.anim.activity_xhold
                        exitAnimID = R.anim.activity_xhold
                    } else {
                        enterAnimID = R.anim.activity_right_in
                        exitAnimID = R.anim.activity_xhold
                    }
                    //跳转到忘记密码页面
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(this@LoginPlusActivity, ForgetPwdActivity::class.java, -1, this, false, enterAnimID, exitAnimID)
                }
            }
        }
    }

    /**
     * 切换登录方式界面
     */
    override fun switchLoginMethodUi(loginMethod: Int) {
        this.loginMethod = loginMethod
        when (loginMethod) {
            //手机号登录
            ConfigConstants.LOGINMETHOD_MOBILE -> {
                tv_phonecountrycode.visibility = View.VISIBLE
                iv_arrowdown.visibility = View.VISIBLE
                tv_usepwd.visibility = View.VISIBLE
                tv_pwd.visibility = View.GONE
                cl_pwd.visibility = View.GONE
                tv_useverificationcode.visibility = View.GONE
                tv_forgetpwd.visibility = View.GONE

                tv_phone_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_8x).toFloat(), -1, "", "#FFFFFF"
                    )
                    setTextColor(Color.parseColor("#1e1f20"))
                }

                tv_account_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        -1f, -1, "", VariableConfig.color_transparent_bg
                    )
                    setTextColor(Color.parseColor("#991e1f20"))
                }
                
                et_phone.apply {
                    var strokeColor = if (hasFocus()) VariableConfig.color_inputbox_focus_bg else VariableConfig.color_inputbox_unfocus_bg
                    setEditTextBG(cl_phonenum, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
                    hint = resources.getString(R.string.login_et_phone_hint)
                    keyListener = DigitsKeyListener.getInstance("1234567890")
                    inputType = InputType.TYPE_CLASS_NUMBER
                    filters = arrayOf<InputFilter>(object : LengthFilter(11) {})

                    if (StringUtils.isBlank(phone_temp)) setText(MySPUtilsUser.getInstance().userMobile) else setText(phone_temp)
                }

                iv_phone.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.login_phone))

                tv_phonename.text = resources.getString(R.string.login_phonename)



                tv_confirm.apply {
                    val bgcolor = if (StringUtils.isBlank(et_phone.text.toString().trim())) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", bgcolor
                    )
                    tv_confirm_content_show.text = resources.getString(R.string.login_confirm)
                    setTextColor(Color.parseColor("#ffffffff"))
                }

            }
            //账号登录
            ConfigConstants.LOGINMETHOD_ACCOUNT -> {
                tv_phonecountrycode.visibility = View.GONE
                iv_arrowdown.visibility = View.GONE
                tv_usepwd.visibility = View.GONE
                tv_pwd.visibility = View.VISIBLE
                cl_pwd.visibility = View.VISIBLE
                tv_useverificationcode.visibility = View.GONE
                tv_forgetpwd.visibility = View.GONE


                tv_account_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_8x).toFloat(), -1, "", "#FFFFFF"
                    )
                    setTextColor(Color.parseColor("#1e1f20"))
                }

                tv_phone_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        -1f, -1, "", VariableConfig.color_transparent_bg
                    )
                    setTextColor(Color.parseColor("#991e1f20"))
                }

                et_phone.apply {
                    var strokeColor = if (hasFocus()) VariableConfig.color_inputbox_focus_bg else VariableConfig.color_inputbox_unfocus_bg
                    setEditTextBG(cl_phonenum, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
                    hint = resources.getString(R.string.accountlogin_et_account_hint)
                    keyListener = null
                    maxLines = 10
                    inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
                    setHorizontallyScrolling(false)
                    filters = arrayOf<InputFilter>(object : LengthFilter(200) {})

                    if (StringUtils.isBlank(account_temp)) setText(MySPUtilsUser.getInstance().userAccount) else setText(account_temp)
                }

                iv_phone.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.accountlogin_account))

                tv_confirm.apply {
                    val bgcolor = if (StringUtils.isBlank(et_phone.text.toString().trim()) ||
                        StringUtils.isBlank(et_pwd.text.toString().trim())
                    ) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", bgcolor
                    )
                    tv_confirm_content_show.text = resources.getString(R.string.pwdlogin_confirm)
                    setTextColor(Color.parseColor("#ffffffff"))
                }


                tv_phonename.text = resources.getString(R.string.accountlogin_name)


                et_pwd.apply {
                    var strokeColor = if (hasFocus()) VariableConfig.color_inputbox_focus_bg else VariableConfig.color_inputbox_unfocus_bg
                    setEditTextBG(cl_pwd, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
                    //隐藏密码
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    setText("")
                    iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this@LoginPlusActivity, R.mipmap.pwdlogin_invisible))
                }


            }
            //密码登录
            ConfigConstants.LOGINMETHOD_PWD -> {
                tv_usepwd.visibility = View.GONE
                tv_pwd.visibility = View.VISIBLE
                cl_pwd.visibility = View.VISIBLE
                tv_useverificationcode.visibility = View.VISIBLE
                tv_forgetpwd.visibility = View.VISIBLE


                tv_phone_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_8x).toFloat(), -1, "", "#FFFFFF"
                    )
                    setTextColor(Color.parseColor("#1e1f20"))
                }

                tv_account_login.apply {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        -1f, -1, "", VariableConfig.color_transparent_bg
                    )
                    setTextColor(Color.parseColor("#991e1f20"))
                }

                et_phone.apply {
                    var strokeColor = if (hasFocus()) VariableConfig.color_inputbox_focus_bg else VariableConfig.color_inputbox_unfocus_bg
                    setEditTextBG(cl_phonenum, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
                    hint = resources.getString(R.string.login_et_phone_hint)
                    keyListener = DigitsKeyListener.getInstance("1234567890")
                    inputType = InputType.TYPE_CLASS_NUMBER
                    filters = arrayOf<InputFilter>(object : LengthFilter(11) {})

                    if (StringUtils.isBlank(phone_temp)) setText(MySPUtilsUser.getInstance().userMobile) else setText(phone_temp)
                }

                et_pwd.apply {
                    var strokeColor = if (hasFocus()) VariableConfig.color_inputbox_focus_bg else VariableConfig.color_inputbox_unfocus_bg
                    setEditTextBG(cl_pwd, resources.getDimensionPixelSize(R.dimen.dimen_12x), 1, strokeColor, VariableConfig.color_transparent_bg)
                    //隐藏密码
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    setText("")
                    iv_pwd_status.setImageDrawable(ContextCompat.getDrawable(this@LoginPlusActivity, R.mipmap.pwdlogin_invisible))
                }


                tv_confirm.apply {
                    val bgcolor = if (StringUtils.isBlank(et_phone.text.toString().trim()) ||
                        StringUtils.isBlank(et_pwd.text.toString().trim())
                    ) VariableConfig.color_button_unselected else VariableConfig.color_button_selected
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        this@LoginPlusActivity, this,
                        resources.getDimensionPixelSize(R.dimen.dimen_16x).toFloat(), -1, "", bgcolor
                    )
                    tv_confirm_content_show.text = resources.getString(R.string.pwdlogin_confirm)
                    setTextColor(Color.parseColor("#ffffffff"))
                }

                tv_phonename.text = resources.getString(R.string.login_phonename)
            }
        }
    }

    /**
     * 获取当前国家和code回调
     */
    override fun countryNameAndCodeCallback(localename: String, localecode: String, locale: String) {
        this.localename = localename
        this.localecode = localecode
        this.locale = locale
        tv_phonecountrycode.text = "+$localecode"
    }

    /**
     * 发送短信回调
     */
    override fun smsCallback(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            //保存数据
            MySPUtilsUser.getInstance().saveMobileTemp(msg)
            with(Bundle()) {
                putString("mobile", msg)
                putString("mode", "1")
                putString("locale", locale)
                putString("localecode", localecode)
                putString("localename", localename)
                putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.LOGINACTIVITY)

                var enterAnimID: Int
                var exitAnimID: Int
                if (ScreenTools.getInstance().isPad(this@LoginPlusActivity)) {
                    enterAnimID = R.anim.activity_xhold
                    exitAnimID = R.anim.activity_xhold
                } else {
                    enterAnimID = R.anim.activity_right_in
                    exitAnimID = R.anim.activity_xhold
                }
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(
                    this@LoginPlusActivity, VCodeInputActivity::class.java,
                    -1, this, false, enterAnimID, exitAnimID
                )
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
        }
    }

    /**
     *  密码登录回调
     */
    override fun passwordLoginCallback(isSuccess: Boolean, info: LoginEntity?, msg: String?) {
        if (isSuccess) {
            with(MySPUtilsUser.getInstance()) {
                saveUserToken(info!!.token)
                saveUserMobile(mobile)
                saveUserPwd(pwd)
                saveUserLoginMethod(ConfigConstants.LOGINMETHOD_PWD)
            }
            //登录请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
            mPresenter.changeloginidentity(info!!.token, ConfigConstants.IDENTITY_STUDENT)

            //绑定别名
            JPushInterface.setAlias(this@LoginPlusActivity, sequence++, info!!.user_account_id)
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
            mPresenter.loginAnimation(iv_loading, tv_confirm_content_show, false)
        }
    }

    /**
     * 账号登录回调
     */
    override fun accountLoginCallback(isSuccess: Boolean, info: LoginEntity?, msg: String?) {
        if (isSuccess) {
            with(MySPUtilsUser.getInstance()) {
                saveUserToken(info!!.token)
                saveUserAccount(mobile)
                saveUserPwd(pwd)
                saveUserLoginMethod(ConfigConstants.LOGINMETHOD_ACCOUNT)
            }
            //登录请求成功后，需要调用切换身份接口来判断 是否需要进入切换身份界面
            mPresenter.changeloginidentity(info!!.token, ConfigConstants.IDENTITY_STUDENT)

            //绑定别名
            JPushInterface.setAlias(this@LoginPlusActivity, sequence++, info!!.user_account_id)
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
            mPresenter.loginAnimation(iv_loading, tv_confirm_content_show, false)
        }
    }

    /**
     * 切换身份回调
     */
    override fun changeloginidentityCallback(isSuccess: Boolean, userIdentityEntity: UserIdentityEntity?, msg: String?) {
        mPresenter.loginAnimation(iv_loading, tv_confirm_content_show, false)

        if (isSuccess) {
            //保存数据
            with(MySPUtilsUser.getInstance()) {
                saveUserToken(userIdentityEntity!!.token)
                saveUserLocale(locale)
                saveUserLocaleCode(localecode)
                saveUserLocaleName(localename)
                if (loginMethod == ConfigConstants.LOGINMETHOD_PWD) saveUserMobile(mobile) else saveUserAccount(mobile)
                saveUserLoginMethod(if (loginMethod == ConfigConstants.LOGINMETHOD_PWD) ConfigConstants.LOGINMETHOD_PWD else ConfigConstants.LOGINMETHOD_ACCOUNT)
                if (StringUtils.isBlank(userIdentityEntity.current_identity)) {
                    saveUserIdentity("")
                    AppPrefsUtil.saveUserIdentity("")
                } else {
                    saveUserIdentity(userIdentityEntity.current_identity)
                    AppPrefsUtil.saveUserIdentity(userIdentityEntity.current_identity)
                }
            }

            val identitys = userIdentityEntity!!.identitys
            if (identitys.size == 1 || identitys.size == 0) {//当前用户只有一个身份,或者用户没有身份，直接进入首页
                //跳转到首页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, MainMenuActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold)
            } else {
                with(Bundle()) {
                    putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.PWDLOGINACTIVITY)
                    putString("current_identity", userIdentityEntity.current_identity)
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(this@LoginPlusActivity, ChooseIdentityActivity::class.java, -1, this, false, R.anim.activity_right_in, R.anim.activity_xhold)
                }
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
        }
    }


    /**
     * priority 数值越大，优先级越高,默认优先级为0
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    fun EventBusCallbackMessage(messageEvent: MessageEvent) {
        if (EventConstant.EVENT_COUNTRYAREA == messageEvent.message_type) {
            with(messageEvent.message as Bundle) {
                this.let {
                    locale = it.getString("locale")
                    localename = it.getString("localename")
                    localecode = it.getString("localecode")
                    countryNameAndCodeCallback(localename, localecode, locale)
                }
            }
        }

        if (EventConstant.EVENT_VERIFICATIONCODE_COUNTDOWN === messageEvent.message_type) {
            sms_countdown = if ((messageEvent.message as Long) == -1L) 0L else messageEvent.message as Long
        }
    }

    /**
     * 手指监听
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()
            var views: Array<View?>
            if (loginMethod == ConfigConstants.LOGINMETHOD_MOBILE) {
                views = arrayOfNulls<View>(1)
                views[0] = et_phone
                if (!PublicPracticalMethodFromJAVA.getInstance().isTouchPointInView(views, x, y)) KeyBoardUtil.getInstance().hideKeyBoard(this, et_phone) else KeyBoardUtil.getInstance().showKeyBoard(this, et_phone)
            } else if (loginMethod == ConfigConstants.LOGINMETHOD_PWD || loginMethod == ConfigConstants.LOGINMETHOD_ACCOUNT) {
                views = arrayOfNulls<View>(1)
                views[0] = et_phone
//                views[1] = et_pwd
                if (!PublicPracticalMethodFromJAVA.getInstance().isTouchPointInView(views, x, y)) KeyBoardUtil.getInstance().hideKeyBoard(this, et_phone) else KeyBoardUtil.getInstance().showKeyBoard(this, et_phone)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    /**
     * 物理返回键 监听
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mPresenter.exitAPP()
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 验证码 图文验证 回调
     */

    override fun jsReturnResults(ret: Int, ticket: String, randstr: String) {
//        LogUtils.i(
//            ConfigConstants.TAG_ALL, "msg =-= $ret",
//            "ticket =-= $ticket",
//            "randstr =-= $randstr"
//        )
        PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, WHATCODE_REMOVEVIEW, 100)

        if (ret == 0) {
            mobile = et_phone.text.toString().trim()
            mPresenter.sms(mobile, locale, ticket, randstr)
        }
    }

    /**
     * handler 回调
     */
    override fun handlerMessage(msg: Message) {
        when (msg.what) {
            WHATCODE_REMOVEVIEW -> {
                graphicverification_view.visibility = View.GONE
            }
        }
    }
}