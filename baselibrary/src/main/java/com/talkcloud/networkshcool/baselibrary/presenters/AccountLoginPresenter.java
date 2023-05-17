package com.talkcloud.networkshcool.baselibrary.presenters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.AccountLoginView;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/6/22
 * Time:15:50
 * author:joker
 * 用户账号登录 Presenter
 */
public class AccountLoginPresenter {

    private Activity mActivity;
    private AccountLoginView accountLoginView;

    public AccountLoginPresenter(Activity activity, AccountLoginView accountLoginView) {
        this.mActivity = activity;
        this.accountLoginView = accountLoginView;
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


    /**
     * 账号登录
     *
     * @param locale
     * @param mobile
     * @param pwd_or_code
     * @param iv_loading
     * @param tv_confirm
     */
    public void accountLogin(String locale, String mobile, String pwd_or_code, ImageView iv_loading, TextView tv_confirm) {
        if (!StringUtils.isBlank(mobile) && !StringUtils.isBlank(pwd_or_code)) {
            loginAnimation(iv_loading, tv_confirm, true);

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("mobile", mobile);//账号
            map_bodys.put("mode", "3");
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
                            if (apiResponseResponse.body().getData().getUser_account_id() != null)
                                MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA,
                                        apiResponseResponse.body().getData().getUser_account_id());
                            if (!StringUtils.isBlank(token)) {
                                accountLoginView.accountLoginCallback(true, apiResponseResponse.body().getData(), msg);
                            } else {
                                accountLoginView.accountLoginCallback(false, apiResponseResponse.body().getData(), msg);
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            accountLoginView.accountLoginCallback(false, null, message);
                        }
                    });


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
                        accountLoginView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        accountLoginView.changeloginidentityCallback(false, null, message);
                    }
                });


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


}
