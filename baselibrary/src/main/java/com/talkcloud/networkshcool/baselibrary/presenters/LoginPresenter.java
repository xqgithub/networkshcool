package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.Captcha;
import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.GPSUtils;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.LoginView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/11
 * Time:18:19
 * author:joker
 * 登录页面的Presenter
 */
public class LoginPresenter {

    private Activity mActivity;
    private LoginView loginView;


    public LoginPresenter(LoginView loginView, Activity mActivity) {
        this.loginView = loginView;
        this.mActivity = mActivity;
    }

    /**
     * 短信验证码接口请求
     */
    public void sms(String mobile, String locale, String ticket, String randstr) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    loginView.smsCallback(false, mActivity.getString(R.string.phone_regex_error));
                    return;
                }
            }

            String version = VariableConfig.V1;

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("locale", locale);
            map_bodys.put("mobile", mobile);

            if (!StringUtils.isBlank(ticket) && !StringUtils.isBlank(randstr)) {
                Captcha captcha = new Captcha();
                captcha.setTicket(ticket);
                captcha.setRandstr(randstr);
                map_bodys.put("captcha", captcha);
                version = VariableConfig.V2;
            }

            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, version);
            apiService.sms(map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                            loginView.smsCallback(true, mobile);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            loginView.smsCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.sms).getApiService();
//            apiService.sms(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                            loginView.smsCallback(true, mobile);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            loginView.smsCallback(false, message);
//                        }
//                    });

        }
    }

    /**
     * 获取国家区域号数据
     */
    public void getCountryCodeDatas(String localename) {
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.countrycode()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<List<CountryAreaEntity>>>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<List<CountryAreaEntity>>> apiResponseResponse) {
                        List<CountryAreaEntity> countryAreaEntityList = apiResponseResponse.body().getData();
                        getCountryCode(localename, countryAreaEntityList);
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        loginView.countryNameAndCodeCallback(VariableConfig.default_localename, VariableConfig.default_localecode, VariableConfig.default_locale);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.countrycode).getApiService();
//        apiService.countrycode()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse<List<CountryAreaEntity>>>>(mActivity, false, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse<List<CountryAreaEntity>>> apiResponseResponse) {
//                        List<CountryAreaEntity> countryAreaEntityList = apiResponseResponse.body().getData();
//                        getCountryCode(localename, countryAreaEntityList);
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        loginView.countryNameAndCodeCallback(VariableConfig.default_localename, VariableConfig.default_localecode, VariableConfig.default_locale);
//                    }
//                });
    }

    /**
     * 获取国家的名称和code
     */
    public void getCountryCode(String localename, List<CountryAreaEntity> countryAreaEntityList) {
        if (StringUtils.isBlank(localename)) {
            //获取用户当前的位置
            if (GPSUtils.getInstance(mActivity).isLocationProviderEnabled()) {
                Location location = GPSUtils.getInstance(mActivity).getLocation();
                if (!StringUtils.isBlank(location)) {
//                    String name = GPSUtils.getInstance(mActivity).getAddressCountryName(location.getLatitude(), location.getLongitude());
                    String countryCode = GPSUtils.getInstance(mActivity).getAddressCountryCode(location.getLatitude(), location.getLongitude());
                    String name = "";
                    String code = "";
                    String locale = "";

                    if (!StringUtils.isBlank(countryCode)) {
                        for (int i = 0; i < countryAreaEntityList.size(); i++) {
                            if (countryCode.equals(countryAreaEntityList.get(i).getLocale())) {
                                name = countryAreaEntityList.get(i).getCountry();
                                code = countryAreaEntityList.get(i).getCode();
                                locale = countryAreaEntityList.get(i).getLocale();
                            }
                        }
                        loginView.countryNameAndCodeCallback(name, code, locale);
                        return;
                    }
                }
            }
            loginView.countryNameAndCodeCallback(VariableConfig.default_localename, VariableConfig.default_localecode, VariableConfig.default_locale);
        } else {
//            String code = GPSUtils.getInstance(mActivity).getCountryCode(localename);
            String code = "";
            String locale = "";
            for (int i = 0; i < countryAreaEntityList.size(); i++) {
                if (localename.equals(countryAreaEntityList.get(i).getCountry())) {
                    code = countryAreaEntityList.get(i).getCode();
                    locale = countryAreaEntityList.get(i).getLocale();
                }
            }
            loginView.countryNameAndCodeCallback(localename, code, locale);
        }
    }


    /**
     * 设置tv_title的位置
     */
    public void setTitlePosition(Context mContext, ConstraintLayout layout, int fatherid, int id) {
        int topMargin = 0;
        int startMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x);
        int endMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x);
        if (ScreenTools.getInstance().isPad(mContext)) {
            topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_91x);
        } else {
            topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_84x);
        }


        ConstraintSet constraintSet = new ConstraintSet();
        //从根布局中克隆约束参数
        constraintSet.clone(layout);
        //清空控件原有的约束
        constraintSet.clear(id);

        //设置宽
        constraintSet.constrainWidth(id, 0);
        //设置高
        constraintSet.constrainHeight(id, ViewGroup.LayoutParams.WRAP_CONTENT);

        //与父级的顶部对齐
        constraintSet.connect(
                id, ConstraintSet.TOP, fatherid, ConstraintSet.TOP,
                topMargin);
        //与父级的左边对齐
        constraintSet.connect(
                id, ConstraintSet.START, fatherid, ConstraintSet.START,
                startMargin);
        //与父级的右边对齐
        constraintSet.connect(
                id, ConstraintSet.END, fatherid, ConstraintSet.END,
                endMargin);
        constraintSet.applyTo(layout);
    }

    /**
     * 设置cl_close的位置
     */
    public void setClosePosition(Context mContext, ConstraintLayout layout, int fatherid, int id) {
        int topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_14x);
        int startMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10x);

        ConstraintSet constraintSet = new ConstraintSet();
        //从根布局中克隆约束参数
        constraintSet.clone(layout);
        //清空控件原有的约束
        constraintSet.clear(id);

        //设置宽
        constraintSet.constrainWidth(id, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置高
        constraintSet.constrainHeight(id, ViewGroup.LayoutParams.WRAP_CONTENT);

        //与父级的顶部对齐
        constraintSet.connect(
                id, ConstraintSet.TOP, fatherid, ConstraintSet.TOP,
                topMargin);
        //与父级的左边对齐
        constraintSet.connect(
                id, ConstraintSet.START, fatherid, ConstraintSet.START,
                startMargin);
        constraintSet.applyTo(layout);
    }

    /**
     * 退出程序
     */
    private long mExitTime;

    public void exitAPP() {
        if (VariableConfig.login_process == VariableConfig.login_process_normal) {
            // 双击退出程序
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showShortToastFromRes(R.string.toast_double_exit, ToastUtils.top);
                mExitTime = System.currentTimeMillis();
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(mActivity, R.anim.activity_xhold);
                System.exit(0);
            }
        } else {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(mActivity, R.anim.activity_right_out);
        }
    }


    /**
     * 退出页面
     */
    public void exitActivity() {
        if (VariableConfig.login_process == VariableConfig.login_process_normal) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(mActivity, R.anim.activity_xhold);
        } else {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(mActivity, R.anim.activity_right_out);
        }
    }


    /**
     * 用户隐私协议状态设置
     */
    public void setUserPrivacyAgreementBG(TextView textView, ImageView imageView) {

        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

        int startindex_useragreement = 0;
        int endindex_useragreement = 0;
        int startindex_userprivacypolicy = 0;
        int endindex_userprivacypolicy = 0;

        //文字设置
        String content1 = mActivity.getResources().getString(R.string.login_userprivacyagreement);
        String content2 = mActivity.getResources().getString(R.string.login_userprivacyagreement2);
        String content = content1 + content2;
        SpannableString msp = new SpannableString(content);


        //点击用户协议跳转
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", ConfigConstants.USERAGREEMENT);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//去掉下划线
            }
        };


        //点击跳转到隐私协议
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", ConfigConstants.USERPRIVACYPOLICY);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//去掉下划线
            }
        };


        //判断当前语言
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
            //获取本地app的国家语言
            List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(mActivity);
            if (locale_language.equals(datas.get(0).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(0).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("\"User Agreement\"");
                endindex_useragreement = startindex_useragreement + 16;

                startindex_userprivacypolicy = endindex_useragreement + 5;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16;

            } else if (locale_language.equals(datas.get(1).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(1).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("《用户协议》");
                endindex_useragreement = startindex_useragreement + 6;

                startindex_userprivacypolicy = endindex_useragreement + 1;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6;

            } else if (locale_language.equals(datas.get(2).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(2).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("《用户协议》");
                endindex_useragreement = startindex_useragreement + 6;

                startindex_userprivacypolicy = endindex_useragreement + 1;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6;
            } else {
                startindex_useragreement = content.indexOf("\"User Agreement\"");
                endindex_useragreement = startindex_useragreement + 16;

                startindex_userprivacypolicy = endindex_useragreement + 5;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16;
            }
        }

        msp.setSpan(clickableSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#1D6AFF"));
        msp.setSpan(colorSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        msp.setSpan(clickableSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#1D6AFF"));
        msp.setSpan(colorSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(msp);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.networkshcool_1d6aff)), content1.length(), (content1 + content2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        //图标设置
        if (VariableConfig.userPrivacyAgreementStatus) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, imageView, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
            imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_selected));
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, imageView, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), "#00000000");
            imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_unselected));
        }
    }

    /**
     * 设置用户隐私协议是否已经阅读
     */
    public void setUserPrivacyAgreementStatus(ImageView imageView) {
//        boolean userprivacyagreementstatus = MySPUtilsUser.getInstance().getUserPrivacyAgreementStatus();
//        if (userprivacyagreementstatus) {
//            MySPUtilsUser.getInstance().saveUserPrivacyAgreementStatus(false);
//        } else {
//            MySPUtilsUser.getInstance().saveUserPrivacyAgreementStatus(true);
//        }

        //图标设置
        if (VariableConfig.userPrivacyAgreementStatus) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, imageView, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), "#00000000");
            imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_unselected));
            VariableConfig.userPrivacyAgreementStatus = false;
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, imageView, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
            imageView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_selected));
            VariableConfig.userPrivacyAgreementStatus = true;
        }
    }
}
