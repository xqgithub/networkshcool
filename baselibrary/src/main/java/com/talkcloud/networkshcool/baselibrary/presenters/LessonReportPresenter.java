package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonReportEntity;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.LessonReportView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class LessonReportPresenter {

    private Activity mActivity;
    private LessonReportView lessonReportView;


    public LessonReportPresenter(LessonReportView loginView, Activity mActivity) {
        this.lessonReportView = loginView;
        this.mActivity = mActivity;
    }

    /**
     * 课程列表接口请求
     */
    public void getLessonReport(String lessonId) {
        if (!StringUtils.isBlank(lessonId)) {

            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.getLessonReport(lessonId)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<LessonReportEntity>>>(mActivity, true, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<LessonReportEntity>> apiResponse) {
                            if (lessonReportView != null)
                                lessonReportView.lessonReportCallback(true, apiResponse.body().getData());
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (lessonReportView != null)
                                lessonReportView.lessonReportCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.lessonReport + "/" + lessonId + "/report").getApiService();
//            apiService.getLessonReport()
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<LessonReportEntity>>>(mActivity, false, false) {
//                        @Override
//
//                        public void onSuccess(Response<ApiResponse<LessonReportEntity>> apiResponse) {
//                            if (lessonReportView != null)
//                                lessonReportView.lessonReportCallback(true, apiResponse.body().getData());
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            if (lessonReportView != null)
//                                lessonReportView.lessonReportCallback(false, message);
//                        }
//                    });

        }
    }
}
