package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.CourseListEntity;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.CourseListView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class CourseListPresenter {

    private Activity mActivity;
    private CourseListView courseListView;


    public CourseListPresenter(CourseListView loginView, Activity mActivity) {
        this.courseListView = loginView;
        this.mActivity = mActivity;
    }

    /**
     * 课程列表接口请求
     */
    public void getCourseList(String state, int page, int rows) {
        if (!StringUtils.isBlank(state)) {
            Map<String, Object> map_options = new HashMap<>();
            map_options.put("state", state);
            map_options.put("page", page);
            map_options.put("rows", rows);
            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.getCourseList(map_options)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<CourseListEntity>>>(mActivity, false, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<CourseListEntity>> apiResponse) {
                            if (courseListView != null)
                                courseListView.courseListCallback(true, apiResponse.body());
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (courseListView != null)
                                courseListView.courseListCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, ApiService.courseList).getApiService();
//            apiService.getCourseList(map_options)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<CourseListEntity>>>(mActivity, false, false) {
//                        @Override
//
//                        public void onSuccess(Response<ApiResponse<CourseListEntity>> apiResponse) {
//                            if (courseListView != null)
//                                courseListView.courseListCallback(true, apiResponse.body());
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            if (courseListView != null)
//                                courseListView.courseListCallback(false, message);
//                        }
//                    });

        }
    }
}
