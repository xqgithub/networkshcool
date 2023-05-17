package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.Captcha;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.ChangeMobileView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/18
 * Time:9:28
 * author:joker
 * 更换手机号页面 Presenter
 */
public class ChangeMobilePresenter {

    private Activity mActivity;
    private ChangeMobileView changeMobileView;

    public ChangeMobilePresenter(Activity activity, ChangeMobileView changeMobileView) {
        this.mActivity = activity;
        this.changeMobileView = changeMobileView;
    }


    /**
     * 短信验证码接口请求
     */
    public void sms(String mobile, String locale, String ticket, String randstr) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    changeMobileView.smsCallback(false, mActivity.getString(R.string.phone_regex_error));
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
                            changeMobileView.smsCallback(true, mobile);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            changeMobileView.smsCallback(false, message);
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
//                            changeMobileView.smsCallback(true, mobile);
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
////                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            changeMobileView.smsCallback(false, message);
//                        }
//                    });


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
     * 验证手机号是否存在
     */
    public void checkMobile(String mobile, String locale) {
        if (!StringUtils.isBlank(mobile)) {
            if (locale.equals(VariableConfig.default_locale)) {//手机号在中国区域
                String telRegex = "^\\d{11}$";
                if (!mobile.matches(telRegex)) {
                    changeMobileView.checkMobileCallback(false, false, mActivity.getString(R.string.phone_regex_error));
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

                                changeMobileView.checkMobileCallback(true, exists, msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            changeMobileView.checkMobileCallback(false, false, message);
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
//                                changeMobileView.checkMobileCallback(true, exists, msg);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            changeMobileView.checkMobileCallback(false, false, message);
//                        }
//                    });
        }
    }


}
