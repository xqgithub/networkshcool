package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.views.CourseMainView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * author:wsf
 * createTime:2021/5/17
 * description: 课程主页
 */
public class CourseMainPresenter {

    private Activity mActivity;
    private CourseMainView courseDetailView;


    public CourseMainPresenter(CourseMainView courseMainView, Activity mActivity) {
        this.courseDetailView = courseMainView;
        this.mActivity = mActivity;
    }

    /**
     * 课程最近日期
     */
    public void getCourseDate() {
        if (!VariableConfig.checkIdentityFlag) {
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.getCourseDateList()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<List<String>>>>(mActivity, false, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<List<String>>> apiResponse) {
                            if (courseDetailView != null) {
                                courseDetailView.courseDateCallback(true, apiResponse.body().getData());
                            }

                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (courseDetailView != null) {
                                courseDetailView.courseDateCallback(false, error_code);
                            }
                        }
                    });
        }
    }
}
