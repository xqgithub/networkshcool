package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.CourseDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.CourseDetailView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class CourseDetailPresenter {

    private Activity mActivity;
    private CourseDetailView courseDetailView;


    public CourseDetailPresenter(CourseDetailView loginView, Activity mActivity) {
        this.courseDetailView = loginView;
        this.mActivity = mActivity;
    }

    /**
     * 课程列表接口请求
     */
    public void getCourseDetail(String cId) {
        if (!StringUtils.isBlank(cId)) {
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V2);
            apiService.getCourseDetail(cId)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<CourseDetailInfoEntity>>>(mActivity, true, true) {
                        @Override

                        public void onSuccess(Response<ApiResponse<CourseDetailInfoEntity>> apiResponse) {
                            if (courseDetailView != null) {
                                courseDetailView.courseDetailCallback(true, apiResponse.body().getData());
                            }

                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (courseDetailView != null) {
                                courseDetailView.courseDetailCallback(false, message);
                            }
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.courseDetail + "/" + cId, VariableConfig.V2).getApiService();
//            apiService.getCourseDetail()
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<CourseDetailInfoEntity>>>(mActivity, true, true) {
//                        @Override
//
//                        public void onSuccess(Response<ApiResponse<CourseDetailInfoEntity>> apiResponse) {
//                            if (courseDetailView != null) {
//                                courseDetailView.courseDetailCallback(true, apiResponse.body().getData());
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            if (courseDetailView != null) {
//                                courseDetailView.courseDetailCallback(false, message);
//                            }
//                        }
//                    });

        }
    }
}
