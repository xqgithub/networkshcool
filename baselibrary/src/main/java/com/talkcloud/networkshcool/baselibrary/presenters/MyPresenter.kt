package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.UserExtInfoEntity
import com.talkcloud.networkshcool.baselibrary.views.MyView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class MyPresenter(mContext: Context, mView: MyView) : BasePresenter<MyView>(mContext, mView) {

//    fun getUserInfo2() {
//        val userInfoObservable = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_user,
//            ApiService.user
//        ).apiService.userInfo
//
//        val userExtInfoObservable = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_teachers_students,
//            ApiService.userConfig
//        ).apiService.userExtInfo
//
//    }

    // 获取用户信息
//    fun getUserInfo() {
//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_user,
//            ApiService.user
//        ).apiService
//
//        apiService.userInfo
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object :
//                BaseSubscriber<ApiResponse<UserInfoEntity?>>(mContext, false, false) {
//                override fun onSuccess(response: ApiResponse<UserInfoEntity?>) {
////                    Log.d("goyw", "response userinfo: ${response.data}")
//                    response.data?.let {
//                        mView.showUserInfo(it)
//                    }
//
//                }
//
//                override fun onFailure(message: String, error_code: Int) {
//                    Log.d("goyw", "t: $message");
//                }
//
//            })
//
//    }


    // 获取用户附加信息 奖杯 身份
    fun getUserExtInfo() {
        if (!VariableConfig.checkIdentityFlag) {
            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)
//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_teachers_students,
//            ApiService.userConfig
//        ).apiService

            apiService.userExtInfo
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :
                    BaseSubscriber<Response<ApiResponse<UserExtInfoEntity?>>>(mContext, false, false) {
                    override fun onSuccess(response: Response<ApiResponse<UserExtInfoEntity?>>) {
//                    TKLog.d("goyw", "response userExtinfo: ${response.body()?.data}")
                        response.body()?.data?.let {
                            mView.showUserExtInfo(it)
                        }
                    }

                    override fun onFailure(message: String, error_code: Int) {
//                    Log.d("goyw", "t: $message");
                    }
                })
        }
    }
}