package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.util.Log;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonFilesEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.LessonListView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class LessonListPresenter {

    private Activity mActivity;
    private LessonListView lessonListView;


    public LessonListPresenter(LessonListView loginView, Activity mActivity) {
        this.lessonListView = loginView;
        this.mActivity = mActivity;
    }

    /**
     * 课程列表接口请求
     */
    public void getLessonList(String dateTime) {
        Log.d("okhttp", "getLessonList" + dateTime);

        if (!VariableConfig.checkIdentityFlag) {
            if (!StringUtils.isBlank(dateTime)) {
                ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V2);
                apiService.getLessonList(dateTime)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<Response<ApiResponse<List<LessonInfoEntity>>>>(mActivity, false, true) {
                            @Override

                            public void onSuccess(Response<ApiResponse<List<LessonInfoEntity>>> apiResponse) {
                                if (lessonListView != null)
                                    lessonListView.lessonListCallback(true, apiResponse.body().getData());
                            }

                            @Override
                            public void onFailure(String message, int error_code) {
                                if (lessonListView != null)
                                    lessonListView.lessonListCallback(false, message);
                            }
                        });
            }
        }
    }

    /**
     * 获取教室课件
     */
    public void getLessonFiles(String serial) {
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.lessonfiles(serial)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<List<LessonFilesEntity>>>>(mActivity, true, true) {
                    @Override
                    public void onSuccess(Response<ApiResponse<List<LessonFilesEntity>>> apiResponseResponse) {
                        try {
                            if (!StringUtils.isBlank(apiResponseResponse.body())) {
                                String msg = apiResponseResponse.body().getMsg();
                                int result = apiResponseResponse.body().getResult();
                                if (result == 0) {
                                    lessonListView.lessonFilesCallback(true, apiResponseResponse.body().getData(), msg);
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        lessonListView.lessonFilesCallback(false, null, mActivity.getResources().getString(R.string.data_wrong));
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        lessonListView.lessonFilesCallback(false, null, message);
                    }
                });
    }
}
