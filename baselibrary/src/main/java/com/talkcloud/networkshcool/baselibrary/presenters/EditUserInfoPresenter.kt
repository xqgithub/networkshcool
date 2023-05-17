package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.EditUserInfoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class EditUserInfoPresenter(mContext: Context, mView: EditUserInfoView) :
    BasePresenter<EditUserInfoView>(mContext, mView) {

    // 意见提交 TODO 先用回调 ，后期优化 rxjava/携程
    fun saveUserInfo(imgList: MutableList<String>, name: String) {
        if (imgList.isNotEmpty()) {
            uploadImg(imgList) { list: List<UploadEntity> ->

//                val pathList = mutableListOf<String>()
//                val urlList = mutableListOf<String>()

                list.forEach {
                    //                    pathList.add(it.path)
                    //                    urlList.add(it.url)

                    NSLog.d("goyw", " url : " + it.url)

                    saveUserInfo(it.path, name)
                    // 保存远程图像到本地
                    AppPrefsUtil.saveRemoteHeaderUrl(it.url)

                }
            }
        } else {
            saveUserInfo("", name)
        }
    }

    // 提交意见
    private fun saveUserInfo(avatar: String, name: String) {

        // 显示loading
        mView.showLoading()

        val mMap: MutableMap<String, String> = HashMap()
        if (avatar.isNotEmpty()) {
            mMap["avatar"] = avatar
        }
        mMap["username"] = name

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_user,
//            ApiService.user
//        ).apiService

        apiService.saveUserInfo(mMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<String>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<String>>) {
//                    Log.d("goyw", "t: $t");
                    mView.hideSuccessLoading()
                    mView.handlerSuccess()
                }

                override fun onFailure(message: String, error_code: Int) {
//                    mView.hideLoading()
                    mView.showFailed()
                }

            })
    }
}