package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.views.ChooseIdentityView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/14
 * Time:11:17
 * author:joker
 * 用户选择身份 Presenter
 */
public class ChooseIdentityPresenter {

    private Activity mActivity;
    private ChooseIdentityView chooseIdentityView;

    public ChooseIdentityPresenter(Activity activity, ChooseIdentityView chooseIdentityView) {
        this.mActivity = activity;
        this.chooseIdentityView = chooseIdentityView;
    }


    /**
     * 切换身份
     */
    public void changeloginidentity(String token, String identity) {

//        Map<String, Object> map_headers = new HashMap<>();
//        map_headers.put("Content-Type", "application/json");
//        map_headers.put("Authorization", PublicPracticalMethodFromJAVA.getInstance().getToken(token));
//        map_headers.put("version", "v1");


        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("identity", identity);

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.changeLoginIdentity(map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<UserIdentityEntity>> apiResponseResponse) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "userIdentityEntityApiResponse =-= " + userIdentityEntityApiResponse.getResult());
                        chooseIdentityView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        chooseIdentityView.changeloginidentityCallback(false, null, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.changeLoginIdentity).getApiService();
//        apiService.changeLoginIdentity(map_bodys)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse<UserIdentityEntity>>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse<UserIdentityEntity>> apiResponseResponse) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "userIdentityEntityApiResponse =-= " + userIdentityEntityApiResponse.getResult());
//                        chooseIdentityView.changeloginidentityCallback(true, apiResponseResponse.body().getData(), apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
////                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                        chooseIdentityView.changeloginidentityCallback(false, null, message);
//                    }
//                });
    }


    /**
     * 设置老师和学生 UI 背景
     */
    public void setTeacherAndStudentBG(TextView textView, String textViewColor, ConstraintLayout constraintLayout, String constraintLayoutColor) {
        textView.setTextColor(Color.parseColor(textViewColor));
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, constraintLayout, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x),
                -1, "", constraintLayoutColor);
    }


}
