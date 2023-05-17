package com.talkcloud.networkshcool.baselibrary.presenters

import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.TKExtManage
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.common.SPConstants
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.Captcha
import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView
import com.talkcloud.networkshcool.baselibrary.utils.GPSUtils
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.LoginPlusView
import com.tencent.mmkv.MMKV
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_loginplus.*
import retrofit2.Response

/**
 * Date:2021/9/8
 * Time:16:24
 * author:joker
 */
class LoginPlusPresenter(private var mActivity: Activity, private var loginPlusView: LoginPlusView) {


    /**
     * 短信验证码接口请求
     */
    fun sms(mobile: String, locale: String, ticket: String = "", randstr: String = "") {
//        if (!StringUtils.isBlank(mobile)) {
//            if (locale == VariableConfig.default_locale) {//手机号在中国区域
//                val telRegex = Regex("^\\d{11}$")
//                if (!telRegex.containsMatchIn(input = mobile)) {
//                    loginPlusView.smsCallback(false, mActivity.getString(R.string.phone_regex_error))
//                    return
//                }
//            }
//            val map_bodys = mutableMapOf<String, Any>()
//            map_bodys["locale"] = locale
//            map_bodys["mobile"] = mobile
//            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
//            apiService.sms(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : BaseSubscriber<Response<ApiResponse<Any>>>(mActivity, true, false) {
//                    override fun onSuccess(apiresponse: Response<ApiResponse<Any>>?) {
//                        try {
//                            apiresponse?.let { _apiresponse ->
//                                _apiresponse.body()?.let {
//                                    var resultcode = it.result
//                                    var msg = it.msg
//                                    if (resultcode == 0) {
//                                        loginPlusView.smsCallback(true, mobile)
//                                        return
//                                    }
//                                }
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace()
//                        }
//                        loginPlusView.smsCallback(false, mActivity.resources.getString(R.string.data_wrong))
//                    }
//
//                    override fun onFailure(message: String, error_code: Int) {
//                        loginPlusView.smsCallback(false, message)
//                    }
//                })
//        }


        if (!StringUtils.isBlank(mobile)) {
            if (locale == VariableConfig.default_locale) {//手机号在中国区域
                val telRegex = Regex("^\\d{11}$")
                if (!telRegex.containsMatchIn(input = mobile)) {
                    loginPlusView.smsCallback(false, mActivity.getString(R.string.phone_regex_error))
                    return
                }
            }

            var version: String = VariableConfig.V1

            val map_bodys = mutableMapOf<String, Any>()
            map_bodys["locale"] = locale
            map_bodys["mobile"] = mobile
            if (!StringUtils.isBlank(ticket) && !StringUtils.isBlank(randstr)) {
                with(Captcha()) {
                    this.ticket = ticket
                    this.randstr = randstr
                    map_bodys["captcha"] = this
                    version = VariableConfig.V2
                }
            }

            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, version)
            apiService.smsV2(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<Response<ApiResponse<Any>>>(mActivity, true, false) {
                    override fun onSuccess(apiresponse: Response<ApiResponse<Any>>?) {
                        try {
                            apiresponse?.let { _apiresponse ->
                                _apiresponse.body()?.let {
                                    var resultcode = it.result
                                    var msg = it.msg
                                    if (resultcode == 0) {
                                        loginPlusView.smsCallback(true, mobile)
                                        return
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        loginPlusView.smsCallback(false, mActivity.resources.getString(R.string.data_wrong))
                    }

                    override fun onFailure(message: String, error_code: Int) {
                        loginPlusView.smsCallback(false, message)
                    }
                })
        }
    }

    /**
     * 获取国家区域号数据
     */
    fun getCountryCodeDatas(localename: String = "") {
        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
        apiService.countrycode()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<List<CountryAreaEntity>>>>(mActivity, false, false) {
                override fun onSuccess(apiresponse: Response<ApiResponse<List<CountryAreaEntity>>>?) {
                    try {
                        apiresponse?.let { _apiresponse ->
                            _apiresponse.body()?.let {
                                var resultcode = it.result
                                var msg = it.msg
                                if (resultcode == 0) {
                                    var datas = it.data
                                    getCountryCode(localename, datas)
                                    return
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    loginPlusView.countryNameAndCodeCallback(VariableConfig.default_localename, VariableConfig.default_localecode, VariableConfig.default_locale)
                }

                override fun onFailure(message: String, error_code: Int) {
                    loginPlusView.countryNameAndCodeCallback(VariableConfig.default_localename, VariableConfig.default_localecode, VariableConfig.default_locale)
                }
            })
    }

    /**
     * 通过定位比对后，获取国家的名称和code
     */
    fun getCountryCode(localename: String, countryAreaEntityList: List<CountryAreaEntity>) {
        var name = VariableConfig.default_localename
        var code = VariableConfig.default_localecode
        var locale = VariableConfig.default_locale
        if (StringUtils.isBlank(localename)) {

            //获取用户当前的位置
            if (GPSUtils.getInstance(mActivity).isLocationProviderEnabled) {
                val location = GPSUtils.getInstance(mActivity).location
                if (!StringUtils.isBlank(location)) {
                    val countryCode = GPSUtils.getInstance(mActivity).getAddressCountryCode(location.latitude, location.longitude)

                    if (!StringUtils.isBlank(countryCode)) {
                        for (entity in countryAreaEntityList) {
                            if (countryCode == entity.locale) {
                                name = entity.country
                                code = entity.code
                                locale = entity.locale
                            }
                        }
                        loginPlusView.countryNameAndCodeCallback(name, code, locale)
                        return
                    }
                }
            }
            loginPlusView.countryNameAndCodeCallback(name, code, locale)
        } else {
            for (entity in countryAreaEntityList) {
                code = entity.code
                locale = entity.locale
            }
            loginPlusView.countryNameAndCodeCallback(localename, code, locale)
        }
    }


    /**
     * 密码登录
     */
    fun passwordLogin(locale: String, mobile: String, pwd_or_code: String, iv_loading: ImageView, tv_confirm_content_show: TextView) {
        if (!StringUtils.isBlank(mobile) && !StringUtils.isBlank(pwd_or_code)) {
            if (locale == VariableConfig.default_locale) {//手机号在中国区域
                val telRegex = Regex("^\\d{11}$")
                if (!telRegex.containsMatchIn(input = mobile)) {
                    loginPlusView.passwordLoginCallback(false, null, mActivity.getString(R.string.phone_regex_error))
                    return
                }
            }
            loginAnimation(iv_loading, tv_confirm_content_show, true)

            val map_bodys = mutableMapOf<String, Any>()
            map_bodys["locale"] = locale
            map_bodys["mobile"] = mobile
            map_bodys["mode"] = "2"
            map_bodys["pwd_or_code"] = pwd_or_code

            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
            apiService.login(TKExtManage.getInstance().companyId, map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, false, false) {
                    override fun onSuccess(apiresponse: Response<ApiResponse<LoginEntity>>?) {
                        try {
                            apiresponse?.let { _apiresponse ->
                                _apiresponse.body()?.let {
                                    var resultcode = it.result
                                    var msg = it.msg
                                    if (resultcode == 0) {
                                        var token = it.data.token
                                        var account_id = it.data.user_account_id
                                        if (!StringUtils.isBlank(token) && !StringUtils.isBlank(account_id)) {
                                            MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA, account_id)
                                            loginPlusView.passwordLoginCallback(true, it.data, msg)
                                            return
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        loginPlusView.passwordLoginCallback(false, null, mActivity.resources.getString(R.string.data_wrong))
                    }

                    override fun onFailure(message: String, error_code: Int) {
                        loginPlusView.passwordLoginCallback(false, null, message)
                    }
                })
        }
    }

    /**
     * 账号登录
     */
    fun accountLogin(locale: String, mobile: String, pwd_or_code: String, iv_loading: ImageView, tv_confirm_content_show: TextView) {
        if (!StringUtils.isBlank(mobile) && !StringUtils.isBlank(pwd_or_code)) {
            loginAnimation(iv_loading, tv_confirm_content_show, true)

            val map_bodys = mutableMapOf<String, Any>()
            map_bodys["mobile"] = mobile
            map_bodys["mode"] = "3"
            map_bodys["pwd_or_code"] = pwd_or_code

            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
            apiService.login(TKExtManage.getInstance().companyId, map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, false, false) {
                    override fun onSuccess(apiresponse: Response<ApiResponse<LoginEntity>>?) {
                        try {
                            apiresponse?.let { _apiresponse ->
                                _apiresponse.body()?.let {
                                    var resultcode = it.result
                                    var msg = it.msg
                                    if (resultcode == 0) {
                                        var token = it.data.token
                                        var account_id = it.data.user_account_id
                                        if (!StringUtils.isBlank(token) && !StringUtils.isBlank(account_id)) {
                                            MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA, account_id)
                                            loginPlusView.accountLoginCallback(true, it.data, msg)
                                            return
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        loginPlusView.accountLoginCallback(false, null, mActivity.resources.getString(R.string.data_wrong))
                    }

                    override fun onFailure(message: String?, error_code: Int) {
                        loginPlusView.accountLoginCallback(false, null, message)
                    }
                })
        }
    }


    /**
     * 登录中动画
     */
    private var animator: ObjectAnimator? = null
    fun loginAnimation(imageView: ImageView, textView: TextView, isStartUp: Boolean) {
        if (isStartUp) {
            if (StringUtils.isBlank(animator)) {
                animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f)
                animator?.duration = 1000
                animator?.repeatCount = ObjectAnimator.INFINITE
                animator?.interpolator = LinearInterpolator()
            }
            animator!!.start()
            imageView.visibility = View.VISIBLE
            textView.text = mActivity.resources.getString(R.string.pwdlogin_confirming)
        } else {
            if (!StringUtils.isBlank(animator)) {
                animator!!.cancel()
                imageView.visibility = View.GONE
                textView.text = mActivity.resources.getString(R.string.pwdlogin_confirm)
            }
        }
    }

    /**
     * 切换身份
     */
    fun changeloginidentity(token: String, identity: String) {
        val map_bodys = mutableMapOf<String, Any>()
        map_bodys["identity"] = identity
        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)
        apiService.changeLoginIdentity(map_bodys)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, false, false) {
                override fun onSuccess(apiresponse: Response<ApiResponse<UserIdentityEntity>>?) {
                    try {
                        apiresponse?.let { _apiresponse ->
                            _apiresponse.body()?.let {
                                var resultcode = it.result
                                var msg = it.msg
                                if (resultcode == 0) {
                                    loginPlusView.changeloginidentityCallback(true, it.data, msg)
                                    return
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    loginPlusView.changeloginidentityCallback(false, null, mActivity.resources.getString(R.string.data_wrong))
                }

                override fun onFailure(message: String?, error_code: Int) {
                    loginPlusView.changeloginidentityCallback(false, null, message)
                }
            })
    }


    /**
     * 用户隐私协议状态设置
     */
    fun setUserPrivacyAgreementBG(textView: TextView, imageView: ImageView) {
        var startindex_useragreement = 0
        var endindex_useragreement = 0
        var startindex_userprivacypolicy = 0
        var endindex_userprivacypolicy = 0

        //文字设置
        val content1 = mActivity.resources.getString(R.string.login_userprivacyagreement)
        val content2 = mActivity.resources.getString(R.string.login_userprivacyagreement2)
        val content = content1 + content2
        val msp = SpannableString(content)


        //点击用户协议跳转
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val bundle = Bundle()
                bundle.putInt("type", ConfigConstants.USERAGREEMENT)
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView::class.java, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false //去掉下划线
            }
        }

        //点击跳转到隐私协议
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val bundle = Bundle()
                bundle.putInt("type", ConfigConstants.USERPRIVACYPOLICY)
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView::class.java, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false //去掉下划线
            }
        }

        //判断当前语言
        val locale_language = MySPUtilsLanguage.getInstance().localeLanguage
        val locale_country = MySPUtilsLanguage.getInstance().localeCountry
        if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
            //获取本地app的国家语言
            val datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(mActivity)
            if (locale_language == datas[0][SPConstants.LOCALE_LANGUAGE] && locale_country == datas[0][SPConstants.LOCALE_COUNTRY]) {
                startindex_useragreement = content.indexOf("\"User Agreement\"")
                endindex_useragreement = startindex_useragreement + 16

                startindex_userprivacypolicy = endindex_useragreement + 5
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16
            } else if (locale_language == datas[1][SPConstants.LOCALE_LANGUAGE] && locale_country == datas[1][SPConstants.LOCALE_COUNTRY]) {
                startindex_useragreement = content.indexOf("《用户协议》")
                endindex_useragreement = startindex_useragreement + 6

                startindex_userprivacypolicy = endindex_useragreement + 1
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6
            } else if (locale_language == datas[2][SPConstants.LOCALE_LANGUAGE] && locale_country == datas[2][SPConstants.LOCALE_COUNTRY]) {
                startindex_useragreement = content.indexOf("《用户协议》")
                endindex_useragreement = startindex_useragreement + 6

                startindex_userprivacypolicy = endindex_useragreement + 1
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6
            } else {
                startindex_useragreement = content.indexOf("\"User Agreement\"")
                endindex_useragreement = startindex_useragreement + 16

                startindex_userprivacypolicy = endindex_useragreement + 5
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16
            }
        }

        msp.setSpan(clickableSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#1D6AFF"))
        msp.setSpan(colorSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        msp.setSpan(clickableSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        val colorSpan2 = ForegroundColorSpan(Color.parseColor("#1D6AFF"))
        msp.setSpan(colorSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        textView.text = msp
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.parseColor("#00000000")

        //圆圈状态设置
        imageView.apply {
            var bgcolor = ""
            var drawableID = -1
            if (VariableConfig.userPrivacyAgreementStatus) {
                bgcolor = VariableConfig.color_button_selected
                drawableID = R.mipmap.login_userprivacyagreement_selected
            } else {
                bgcolor = "#00000000"
                drawableID = R.mipmap.login_userprivacyagreement_unselected
            }

            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, this, mActivity.resources.getDimensionPixelSize(R.dimen.dimen_120x), bgcolor)
            setImageDrawable(ContextCompat.getDrawable(mActivity, drawableID))
        }
    }

    /**
     * 退出程序
     */
    private var mExitTime: Long = 0
    fun exitAPP() {
        // 双击退出程序
        if (System.currentTimeMillis() - mExitTime > 2000) {
            ToastUtils.showShortToastFromRes(R.string.toast_double_exit, ToastUtils.top)
            mExitTime = System.currentTimeMillis()
        } else {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(mActivity, R.anim.activity_xhold)
            System.exit(0)
        }
    }
}