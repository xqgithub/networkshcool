package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;

import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;
import com.talkcloud.networkshcool.baselibrary.views.CountryAreaView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Date:2021/5/13
 * Time:9:19
 * author:joker
 * 国家区域号的Presenter
 */
public class CountryAreaPresenter {

    private Activity mActivity;
    private CountryAreaView countryAreaView;


    public CountryAreaPresenter(Activity activity, CountryAreaView countryAreaView) {
        this.mActivity = activity;
        this.countryAreaView = countryAreaView;
    }


    /**
     * 获取国家区域号数据
     */
    public void getCountryCodeDatas() {
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.countrycode()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<List<CountryAreaEntity>>>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<List<CountryAreaEntity>>> apiResponseResponse) {
                        List<CountryAreaEntity> countryAreaEntityList = apiResponseResponse.body().getData();
                        countryAreaView.CountryCodeCallback(true, countryAreaEntityList, apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        countryAreaView.CountryCodeCallback(false, null, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.countrycode).getApiService();
//        apiService.countrycode()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse<List<CountryAreaEntity>>>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse<List<CountryAreaEntity>>> apiResponseResponse) {
//                        List<CountryAreaEntity> countryAreaEntityList = apiResponseResponse.body().getData();
//                        countryAreaView.CountryCodeCallback(true, countryAreaEntityList, apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        countryAreaView.CountryCodeCallback(false, null, message);
//                    }
//                });
    }


}
