package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkDetailListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class HomeworkDetailListPresenter {

    private Activity mActivity;
    private HomeworkDetailListView homeworkListView;
    private String version = "v1";


    public HomeworkDetailListPresenter(HomeworkDetailListView loginView, Activity mActivity) {
        this.homeworkListView = loginView;
        this.mActivity = mActivity;
        String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
        if (user_identity.equals(ConfigConstants.IDENTITY_STUDENT)) { //学生端 作业列表 接口版本号是v2
            version = "v2";
        }
    }

    /**
     * 作业详情列表接口请求
     */
    public void getHomeworkDetailList(String is_submit, String homewordId, int page, int rows) {
        if (!StringUtils.isBlank(is_submit)) {
            Map<String, Object> map_options = new HashMap<>();
            map_options.put("is_submit", is_submit);
            map_options.put("page", page);
            map_options.put("rows", rows);
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.getHomeworkDetailList(homewordId, map_options)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<List<HomeworkDetailInfoEntity>>>>(mActivity, false, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<List<HomeworkDetailInfoEntity>>> apiResponse) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkDetailListCallback(true, apiResponse.body());
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            if (homeworkListView != null)
                                homeworkListView.homeworkDetailListCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.homeworkList + "/" + homewordId + "/students").getApiService();
//            apiService.getHomeworkDetailList(map_options)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<List<HomeworkDetailInfoEntity>>>>(mActivity, false, false) {
//                        @Override
//
//                        public void onSuccess(Response<ApiResponse<List<HomeworkDetailInfoEntity>>> apiResponse) {
//                            if (homeworkListView != null)
//                                homeworkListView.homeworkDetailListCallback(true, apiResponse.body());
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
//                            if (homeworkListView != null)
//                                homeworkListView.homeworkDetailListCallback(false, message);
//                        }
//                    });

        }
    }

    /**
     * 作业批量提醒
     */
    public void homeworkNotify(List<String> studentIds, String homewordId) {
        if (!StringUtils.isBlank(homewordId) && studentIds != null) {
            Map<String, Object> map_options = new HashMap<>();
            map_options.put("students", studentIds);

            //ApiService apiService = RetrofitServiceManager2.getInstance(VariableConfig.base_url_teachers_students,  VariableConfig.V1).getApiService();
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.homeworkNotify(homewordId, map_options)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<String>>>(mActivity, true, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<String>> apiResponse) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkNotifyCallback(true, apiResponse.body());
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkNotifyCallback(false, message);
                        }
                    });

        }
    }
}
