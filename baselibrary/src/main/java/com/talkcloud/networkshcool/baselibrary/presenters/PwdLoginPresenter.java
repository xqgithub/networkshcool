package com.talkcloud.networkshcool.baselibrary.presenters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.Captcha;
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.PwdLoginView;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/13
 * Time:14:38
 * author:joker
 * 密码登录 Presenter
 */
public class PwdLoginPresenter {

    private Activity mActivity;
    private PwdLoginView pwdLoginView;


    public PwdLoginPresenter(Activity activity, PwdLoginView pwdLoginView) {
        this.mActivity = activity;
        this.pwdLoginView = pwdLoginView;
    }


    /**
     * 密码登录
     */
    public void passwordLogin(String locale, String mobile, String pwd_or_code, ImageView iv_loading, TextView tv_confirm) {
        if (!StringUtils.isBlank(mobile) && !StringUtils.isBlank(pwd_or_code)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    pwdLoginView.passwordLoginCallback(false, null, mActivity.getString(R.string.phone_regex_error));
                    return;
                }
            }


            loginAnimation(iv_loading, tv_confirm, true);

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("locale", locale);
            map_bodys.put("mobile", mobile);//15695623594,18507103016
            map_bodys.put("mode", "2");
            map_bodys.put("pwd_or_code", pwd_or_code);

            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
            apiService.login(TKExtManage.getInstance().getCompanyId(), map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, false, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
                            String msg = apiResponseResponse.body().getMsg();
                            String token = apiResponseResponse.body().getData().getToken();
                            if (!StringUtils.isBlank(token)) {
                                if (apiResponseResponse.body().getData().getUser_account_id() != null)
                                    MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA,
                                            apiResponseResponse.body().getData().getUser_account_id());
                                pwdLoginView.passwordLoginCallback(true, apiResponseResponse.body().getData(), msg);

                            } else {
                                pwdLoginView.passwordLoginCallback(false, null, msg);
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            pwdLoginView.passwordLoginCallback(false, null, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.login).getApiService();
//            apiService.login(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, false, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
//                            String msg = apiResponseResponse.body().getMsg();
//                            String token = apiResponseResponse.body().getData().getToken();
//                            if (!StringUtils.isBlank(token)) {
//                                if (apiResponseResponse.body().getData().getUser_account_id() != null)
//                                    MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA,
//                                            apiResponseResponse.body().getData().getUser_account_id());
//                                pwdLoginView.passwordLoginCallback(true, apiResponseResponse.body().getData(), msg);
//
//                            } else {
//                                pwdLoginView.passwordLoginCallback(false, null, msg);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            pwdLoginView.passwordLoginCallback(false, null, message);
//                        }
//                    });
        }
    }


    /**
     * 检验账号
     */
    public void VerificationCodeCheck2(String mode, String pwd_or_code) {
        if (!StringUtils.isBlank(pwd_or_code)) {
//            Map<String, Object> map_headers = new HashMap<>();
//            map_headers.put("Content-Type", "application/json");
//            map_headers.put("version", "v1");

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("mode", mode);
            map_bodys.put("pwd_or_code", pwd_or_code);//8888
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
            apiService.verify(map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                            String msg = apiResponseResponse.body().getMsg();
                            pwdLoginView.passwordLoginCallbackPS(true, "", msg);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            pwdLoginView.passwordLoginCallbackPS(false, "", message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.verify).getApiService();
//            apiService.verify(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                            String msg = apiResponseResponse.body().getMsg();
//                            pwdLoginView.passwordLoginCallbackPS(true, "", msg);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            pwdLoginView.passwordLoginCallbackPS(false, "", message);
//                        }
//                    });
        }
    }


    /**
     * 切换身份
     */
    public void changeloginidentity(String token, String identity) {

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("identity", identity);

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.changeLoginIdentity(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<UserIdentityEntity>> apiResponseResponse) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "userIdentityEntityApiResponse =-= " + userIdentityEntityApiResponse.getResult());
                        pwdLoginView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        pwdLoginView.changeloginidentityCallback(false, null, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.changeLoginIdentity).getApiService();
//        apiService.changeLoginIdentity(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, false, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse<UserIdentityEntity>> apiResponseResponse) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "userIdentityEntityApiResponse =-= " + userIdentityEntityApiResponse.getResult());
//                        pwdLoginView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        pwdLoginView.changeloginidentityCallback(false, null, message);
//                    }
//                });
    }


    /**
     * 短信验证码接口请求
     */
    public void sms(String mobile, String locale, String ticket, String randstr) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    pwdLoginView.smsCallback(false, mActivity.getString(R.string.phone_regex_error));
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
                            pwdLoginView.smsCallback(true, mobile);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            pwdLoginView.smsCallback(false, message);
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
//                            pwdLoginView.smsCallback(true, mobile);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            pwdLoginView.smsCallback(false, message);
//                        }
//                    });

        }
    }

    /**
     * 登录中动画
     */
    private ObjectAnimator animator;

    public void loginAnimation(ImageView imageView, TextView textView, boolean isStartUp) {
        if (isStartUp) {
            if (StringUtils.isBlank(animator)) {
                animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
                animator.setDuration(1000);
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.setInterpolator(new LinearInterpolator());
            }
            animator.start();
            imageView.setVisibility(View.VISIBLE);
            textView.setText(mActivity.getResources().getString(R.string.pwdlogin_confirming));
        } else {
            if (!StringUtils.isBlank(animator)) {
                animator.cancel();
                imageView.setVisibility(View.GONE);
                textView.setText(mActivity.getResources().getString(R.string.pwdlogin_confirm));
            }
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
     * 用户隐私协议状态设置
     */
    public void setUserPrivacyAgreementBG(TextView textView, ImageView imageView) {

        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);

//        //文字设置
//        String content1 = mActivity.getResources().getString(R.string.login_userprivacyagreement);
//        String content2 = mActivity.getResources().getString(R.string.login_userprivacyagreement2);
//        SpannableString msp = new SpannableString(content1 + content2);
////                //设置字体前景色
//        msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity, R.color.networkshcool_1d6aff)), content1.length(), (content1 + content2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(msp);


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
