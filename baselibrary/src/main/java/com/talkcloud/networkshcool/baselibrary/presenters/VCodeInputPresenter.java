package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.Captcha;
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.VCodeInputView;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/11
 * Time:18:19
 * author:joker
 * 输入验证码页面的Presenter
 */
public class VCodeInputPresenter {

    private Activity mActivity;
    private VCodeInputView vCodeInputView;


    public VCodeInputPresenter(VCodeInputView vCodeInputView, Activity mActivity) {
        this.vCodeInputView = vCodeInputView;
        this.mActivity = mActivity;
    }

    /**
     * 验证码登录接口请求
     */
    public void VerificationCodeLogin(String locale, String mobile, String pwd_or_code) {
        if (!StringUtils.isBlank(pwd_or_code)) {
//            Map<String, Object> map_headers = new HashMap<>();
//            map_headers.put("Content-Type", "application/json");
//            map_headers.put("version", "v1");

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("locale", locale);
            map_bodys.put("mobile", mobile);//15695623594,18507103016
            map_bodys.put("mode", "1");
            map_bodys.put("pwd_or_code", pwd_or_code);//8888
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
            apiService.login(TKExtManage.getInstance().getCompanyId(), map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
                            String msg = apiResponseResponse.body().getMsg();
                            String token = apiResponseResponse.body().getData().getToken();
                            if (apiResponseResponse.body().getData().getUser_account_id() != null)
                                MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA,
                                        apiResponseResponse.body().getData().getUser_account_id());
                            if (!StringUtils.isBlank(token)) {
                                vCodeInputView.verificationCodeLoginCallback(true, apiResponseResponse.body().getData(), msg);
                            } else {
                                vCodeInputView.verificationCodeLoginCallback(false, apiResponseResponse.body().getData(), msg);
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            vCodeInputView.verificationCodeLoginCallback(false, null, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.login).getApiService();
//            apiService.login(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
//                            String msg = apiResponseResponse.body().getMsg();
//                            String token = apiResponseResponse.body().getData().getToken();
//                            if (apiResponseResponse.body().getData().getUser_account_id() != null)
//                                MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA,
//                                        apiResponseResponse.body().getData().getUser_account_id());
//                            if (!StringUtils.isBlank(token)) {
//                                vCodeInputView.verificationCodeLoginCallback(true, apiResponseResponse.body().getData(), msg);
//                            } else {
//                                vCodeInputView.verificationCodeLoginCallback(false, apiResponseResponse.body().getData(), msg);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            vCodeInputView.verificationCodeLoginCallback(false, null, message);
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
                        vCodeInputView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        vCodeInputView.changeloginidentityCallback(false, null, message);
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
//                        vCodeInputView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        vCodeInputView.changeloginidentityCallback(false, null, message);
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
                    vCodeInputView.smsCallback(false, mActivity.getString(R.string.phone_regex_error));
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
                            vCodeInputView.smsCallback(true, mobile);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            vCodeInputView.smsCallback(false, message);
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
//                            vCodeInputView.smsCallback(true, mobile);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            vCodeInputView.smsCallback(false, message);
//                        }
//                    });

        }
    }


    /**
     * 修改手机
     */
    public void mobile(String token, String mode, String pwd_or_code, String new_locale, String new_mobile, String new_smscode) {
        String old_mobile = MySPUtilsUser.getInstance().getUserMobile();
        if (old_mobile.equals(new_mobile)) {
            vCodeInputView.mobileCallback(false, mActivity.getString(R.string.phone_is_same));
            return;
        }

//        Map<String, Object> map_headers = new HashMap<>();
//        map_headers.put("Content-Type", "application/json");
//        map_headers.put("Authorization", PublicPracticalMethodFromJAVA.getInstance().getToken(token));
//        map_headers.put("version", "v1");

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("mode", mode);
        map_bodys.put("pwd_or_code", pwd_or_code);
        map_bodys.put("new_locale", new_locale);
        map_bodys.put("new_mobile", new_mobile);
        map_bodys.put("new_smscode", new_smscode);

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.mobile(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        vCodeInputView.mobileCallback(true, apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        vCodeInputView.mobileCallback(false, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.mobile).getApiService();
//        apiService.mobile(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                        vCodeInputView.mobileCallback(true, apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        vCodeInputView.mobileCallback(false, message);
//                    }
//                });
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
     * 验证码位数输入满的时候自动下一步
     */
    public void automaticNextStep() {
        vCodeInputView.nextStepCallback();
    }


    /**
     * 验证码下划线颜色设置
     */
    public void setCodeUnderlineBgColor(int codesSize, View[] underlineviews, String color) {
        for (int i = 0; i < codesSize; i++) {
            underlineviews[i].setBackgroundColor(Color.parseColor(color));
        }
    }


    /**
     * 验证码登录  校验验证码
     */
    public void VerificationCodeCheck(String locale, String mobile, String pwd_or_code) {
        if (!StringUtils.isBlank(pwd_or_code)) {
//            Map<String, Object> map_headers = new HashMap<>();
//            map_headers.put("Content-Type", "application/json");
//            map_headers.put("version", "v1");

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("locale", locale);
            map_bodys.put("mobile", mobile);//15695623594,18507103016
            map_bodys.put("mode", "1");
            map_bodys.put("pwd_or_code", pwd_or_code);//8888
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
            apiService.login(TKExtManage.getInstance().getCompanyId(), map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
                            String msg = apiResponseResponse.body().getMsg();
                            String token = apiResponseResponse.body().getData().getToken();
                            if (!StringUtils.isBlank(token)) {
                                vCodeInputView.VerificationCodeCheckCallback(true, token);
                            } else {
                                vCodeInputView.VerificationCodeCheckCallback(false, msg);
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            vCodeInputView.VerificationCodeCheckCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.login).getApiService();
//            apiService.login(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
//                            String msg = apiResponseResponse.body().getMsg();
//                            String token = apiResponseResponse.body().getData().getToken();
//                            if (!StringUtils.isBlank(token)) {
//                                vCodeInputView.VerificationCodeCheckCallback(true, token);
//                            } else {
//                                vCodeInputView.VerificationCodeCheckCallback(false, msg);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            vCodeInputView.VerificationCodeCheckCallback(false, message);
//                        }
//                    });
        }
    }


    /**
     * 校验账号
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
                            vCodeInputView.VerificationCodeCheckCallback(true, msg);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            vCodeInputView.VerificationCodeCheckCallback(false, message);
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
//                            vCodeInputView.VerificationCodeCheckCallback(true, msg);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            vCodeInputView.VerificationCodeCheckCallback(false, message);
//                        }
//                    });
        }
    }


}
