package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkListEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.ChoiceDialog;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkListView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class HomeworkListPresenter {

    private Activity mActivity;
    private HomeworkListView homeworkListView;
    //    private String version = "v1";
    private String version = VariableConfig.V1;
    private String user_identity;


    public HomeworkListPresenter(HomeworkListView loginView, Activity mActivity) {
        this.homeworkListView = loginView;
        this.mActivity = mActivity;
        user_identity = MySPUtilsUser.getInstance().getUserIdentity();
        if (user_identity.equals(ConfigConstants.IDENTITY_STUDENT)) { //学生端 作业列表 接口版本号是v2
//            version = "v2";
            version = VariableConfig.V2;
        }
    }

    /**
     * 我的作业列表接口请求
     */
    public void getHomeworkList(String state, int page, int rows) {
        if (!StringUtils.isBlank(state)) {
            String url;

            Observable<Response<ApiResponse<HomeworkListEntity>>> observable = null;

            Map<String, Object> map_options = new HashMap<>();
            map_options.put("page", page);
            map_options.put("rows", rows);

            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, version);

            if (user_identity.equals(ConfigConstants.IDENTITY_STUDENT)) {
//                url = ApiService.homeworkList;
                map_options.put("submits", state);
                observable = apiService.getHomeworkList(map_options);

            } else {
//                url = ApiService.homeworkListTeacher;
                map_options.put("remarks", state);
                observable = apiService.getHomeworkListTeacher(map_options);
            }


            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<HomeworkListEntity>>>(mActivity, false, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<HomeworkListEntity>> apiResponse) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkListCallback(true, apiResponse.body());
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkListCallback(false, message);
                        }
                    });
//            ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_teachers_students, url, version).getApiService();
//            apiService.getHomeworkList(map_options)
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseSubscriber<Response<ApiResponse<HomeworkListEntity>>>(mActivity, false, false) {
//                        @Override
//
//                        public void onSuccess(Response<ApiResponse<HomeworkListEntity>> apiResponse) {
//                            if (homeworkListView != null)
//                                homeworkListView.homeworkListCallback(true, apiResponse.body());
//                        }
//
//                        @Override
//                        public void onFailure(String message, int error_code) {
//                            if (homeworkListView != null)
//                                homeworkListView.homeworkListCallback(false, message);
//                        }
//                    });

        }
    }

    /**
     * 删除草稿
     */
    public void deleteHomework(String hId, int index) {
        if (!StringUtils.isBlank(hId)) {

            ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
            apiService.deleteHomework(hId)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<Response<ApiResponse<String>>>(mActivity, false, false) {
                        @Override

                        public void onSuccess(Response<ApiResponse<String>> apiResponse) {
                            if (homeworkListView != null)
                                homeworkListView.homeworkDeleteCallback(true, apiResponse.body(), index);
                        }

                        @Override
                        public void onFailure(String message, int error_code) {
                            LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                            if (homeworkListView != null)
                                homeworkListView.homeworkDeleteCallback(false, message, index);
                        }
                    });

        }
    }

    /**
     * 删除提示弹框
     */
    public void showDeleteDialog(HomeworkInfoEntity info, int index) {
        ChoiceDialog choiceDialog = new ChoiceDialog(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_375x));
        String title = mActivity.getResources().getString(R.string.logout_title);
        String content = String.format(mActivity.getResources().getString(R.string.homeworklist_delete_work), info.getTitle());
        String no = mActivity.getResources().getString(R.string.logout_no);
        String yes = mActivity.getResources().getString(R.string.logout_yes);
        choiceDialog.setTextInformation(title, content, no, yes);
        choiceDialog.setDialogBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
        choiceDialog.setCancelBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
        choiceDialog.setConfirmBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);

        choiceDialog.confirmButton(v -> {
            deleteHomework(info.getHomework_id(), index);
            choiceDialog.dismissDialog();
        });

        choiceDialog.notSure(v -> choiceDialog.dismissDialog());

        choiceDialog.closeWindow(v -> choiceDialog.dismissDialog());

        if (!choiceDialog.isShowing()) {
            choiceDialog.show();
        }
    }
}
