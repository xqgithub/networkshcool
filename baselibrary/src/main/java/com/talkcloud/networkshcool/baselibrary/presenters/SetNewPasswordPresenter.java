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
import com.talkcloud.networkshcool.baselibrary.entity.LoginEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.SetNewPasswordView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/17
 * Time:14:23
 * author:joker
 * 设置新密码 Presenter
 */
public class SetNewPasswordPresenter {

    private Activity mActivity;
    private SetNewPasswordView setNewPasswordView;

    public SetNewPasswordPresenter(Activity activity, SetNewPasswordView setNewPasswordView) {
        this.mActivity = activity;
        this.setNewPasswordView = setNewPasswordView;
    }

    /**
     * 忘记密码请求
     */
    public void forgotPwd(String locale, String mobile, String smscode, String new_pwd, String new_pwd_confirm) {
        if (StringUtils.isBlank(new_pwd)) {
            setNewPasswordView.forgotPwdcallback(false, mActivity.getString(R.string.setnewpassword_pwd_error));
            return;
        }
        if (StringUtils.isBlank(new_pwd_confirm)) {
            setNewPasswordView.forgotPwdcallback(false, mActivity.getString(R.string.setnewpassword_pwd_confirm_error));
            return;
        }
        if (!new_pwd.equals(new_pwd_confirm)) {
            setNewPasswordView.forgotPwdcallback(false, mActivity.getString(R.string.setnewpassword_pwd_different_error));
            return;
        }

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("locale", locale);
        map_bodys.put("mobile", mobile);
        map_bodys.put("smscode", smscode);
        map_bodys.put("new_pwd", new_pwd);

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.forgotpwd(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
                        String msg = apiResponseResponse.body().getMsg();
                        String token = apiResponseResponse.body().getData().getToken();
                        setNewPasswordView.forgotPwdcallback(true, token);
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        setNewPasswordView.forgotPwdcallback(false, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.forgotpwd).getApiService();
//        apiService.forgotpwd(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse<LoginEntity>>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse<LoginEntity>> apiResponseResponse) {
//                        String msg = apiResponseResponse.body().getMsg();
//                        String token = apiResponseResponse.body().getData().getToken();
//                        setNewPasswordView.forgotPwdcallback(true, token);
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        setNewPasswordView.forgotPwdcallback(false, message);
//                    }
//                });
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
                        setNewPasswordView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        setNewPasswordView.changeloginidentityCallback(false, null, message);
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
//                        setNewPasswordView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        setNewPasswordView.changeloginidentityCallback(false, null, message);
//                    }
//                });
    }

    /**
     * 修改密码
     */
    public void pwdupdate(String token, String smscode, String new_pwd, String new_pwd_confirm) {
        if (StringUtils.isBlank(new_pwd)) {
            setNewPasswordView.pwdupdateCallback(false, mActivity.getString(R.string.setnewpassword_pwd_error));
            return;
        }
        if (StringUtils.isBlank(new_pwd_confirm)) {
            setNewPasswordView.pwdupdateCallback(false, mActivity.getString(R.string.setnewpassword_pwd_confirm_error));
            return;
        }
        if (!new_pwd.equals(new_pwd_confirm)) {
            setNewPasswordView.pwdupdateCallback(false, mActivity.getString(R.string.setnewpassword_pwd_different_error));
            return;
        }

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("smscode", smscode);
        map_bodys.put("new_pwd", new_pwd);

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.pwdupdate(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        setNewPasswordView.pwdupdateCallback(true, apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        setNewPasswordView.pwdupdateCallback(false, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.pwdupdate).getApiService();
//        apiService.pwdupdate(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                        setNewPasswordView.pwdupdateCallback(true, apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        setNewPasswordView.pwdupdateCallback(false, message);
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


}
