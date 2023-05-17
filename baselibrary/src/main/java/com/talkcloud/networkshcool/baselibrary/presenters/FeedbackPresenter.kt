package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity
import com.talkcloud.networkshcool.baselibrary.views.FeedbackView
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class FeedbackPresenter(mContext: Context, mView: FeedbackView) : BasePresenter<FeedbackView>(mContext, mView) {

    // 意见提交 TODO 先用回调 ，后期优化 rxjava/携程
    fun requestSuggestion(content: String, label: String, imgList: MutableList<String>) {
        if (imgList.isNotEmpty()) {
            uploadImg(imgList) { list: List<UploadEntity> ->
                val pathList = mutableListOf<String>()
                list.forEach {
                    pathList.add(it.path)
                }
                requestContentSuggestion(content, label, pathList)
            }
        } else {
            requestContentSuggestion(content, label, imgList)
        }
    }

    // 提交意见
    private fun requestContentSuggestion(content: String, label: String, list: MutableList<String>) {

        mView.showLoading()

        val mMap: MutableMap<String, Any> = HashMap()

        mMap["content"] = content
        mMap["label"] = label
        if (list.isNotEmpty()) {
            mMap["files"] = list
        }

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_user,
//            ApiService.suggestion
//        ).apiService

        apiService.requestSuggestion(mMap)
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<String>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<String>>) {
//                    TKLog.d("goyw", "t: ${t.body().toString()}");
                    mView.hideSuccessLoadingShowTips()
                    mView.handlerSuccess()
                }

                override fun onFailure(message: String, error_code: Int) {
                    mView.showFailed()
                }
            })
    }


    // 上传图片
//    private fun uploadImg(imgList: MutableList<String>, success: (list: MutableList<String>) -> Unit) {
//
//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_user,
//            ApiService.upload
//        ).apiService
//
//        val partList: MutableList<MultipartBody.Part> = mutableListOf()
//
//        imgList.forEach {
//            val file = File(it);
//            val requestBody: RequestBody =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file)
//            val part = MultipartBody.Part.createFormData("files[]", file.name, requestBody)
//
//            partList.add(part)
//        }
//
//        apiService.uploadFiles(partList, PublicPracticalMethodFromJAVA.getInstance().optionsparams)
//            .subscribeOn(Schedulers.io())
//            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
//            .subscribe(object :
//                BaseSubscriber<ApiResponse<List<UploadEntity>>>(mContext, false, false) {
//
//                override fun onSuccess(response: ApiResponse<List<UploadEntity>>) {
//                    val dataList = response.data
//                    val pathList = mutableListOf<String>()
//                    dataList.forEach {
//                        pathList.add(it.path)
//                    }
//
//                    success(pathList)
//                }
//
//                override fun onFailure(message: String, error_code: Int) {
////                    Log.d("goyw", "t: $message");
//                }
//            })
//    }
}