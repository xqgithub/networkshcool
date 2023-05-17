package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.Captcha;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.ForgetPwdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/17
 * Time:11:23
 * author:joker
 * 忘记密码页面Presenter
 */
public class ForgetPwdPresenter {

    private Activity mActivity;
    private ForgetPwdView forgetPwdView;

    public ForgetPwdPresenter(Activity activity, ForgetPwdView forgetPwdView) {
        this.mActivity = activity;
        this.forgetPwdView = forgetPwdView;
    }


    /**
     * 短信验证码接口请求
     */
    public void sms(String mobile, String locale, String ticket, String randstr) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    forgetPwdView.smsCallback(false, mActivity.getString(R.string.phone_regex_error));
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
                            forgetPwdView.smsCallback(true, mobile);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            forgetPwdView.smsCallback(false, message);
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
//                            forgetPwdView.smsCallback(true, mobile);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            forgetPwdView.smsCallback(false, message);
//                        }
//                    });

        }
    }


    /**
     * 验证手机号是否存在
     */
    public void checkMobile(String mobile, String locale) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    forgetPwdView.checkMobileCallback(false, false, mActivity.getString(R.string.phone_regex_error));
                    return;
                }
            }

            Map<String, Object> map_bodys = new HashMap<>();
            map_bodys.put("locale", locale);
            map_bodys.put("mobile", mobile);
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
            apiService.checkMobile(map_bodys)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                        @Override
                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                            String msg = apiResponseResponse.body().getMsg();
                            String data = apiResponseResponse.body().getData().toString();
                            try {
                                JSONObject jsonObject_data = new JSONObject(data);
                                boolean exists = jsonObject_data.optBoolean("exists", false);

                                forgetPwdView.checkMobileCallback(true, exists, msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            forgetPwdView.checkMobileCallback(false, false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.checkMobile).getApiService();
//            apiService.checkMobile(map_bodys)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                        @Override
//                        public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                            String msg = apiResponseResponse.body().getMsg();
//                            String data = apiResponseResponse.body().getData().toString();
//                            try {
//                                JSONObject jsonObject_data = new JSONObject(data);
//                                boolean exists = jsonObject_data.optBoolean("exists", false);
//
//                                forgetPwdView.checkMobileCallback(true, exists, msg);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            forgetPwdView.checkMobileCallback(false, false, message);
//                        }
//                    });

        }
    }


}
