package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.IBaseView
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
open class BasePresenter<T : IBaseView>(protected var mContext: Context, protected var mView: T) {

    // 上传图片
    fun uploadImg(imgList: MutableList<String>, success: (list: List<UploadEntity>) -> Unit) {

        NSLog.d("jobMap BasePresenter uploadImg : $imgList")

        mView.showLoading()

        if (imgList.isEmpty()) {
            // 返回空集合
            success(listOf())
            return
        }

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
//        val apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.upload).apiService

        val partList: MutableList<MultipartBody.Part> = mutableListOf()

        imgList.forEach {
            val file = File(it);
            val requestBody: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val part = MultipartBody.Part.createFormData("files[]", file.name, requestBody)

            partList.add(part)
        }

        apiService.uploadFiles(partList)
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(object :
                BaseSubscriber<Response<ApiResponse<List<UploadEntity>>>>(mContext, false, false) {

                override fun onSuccess(response: Response<ApiResponse<List<UploadEntity>>>) {
                    val dataList = response.body()?.data

//                    val pathList = mutableListOf<String>()
//                    val urlList = mutableListOf<String>()
//                    dataList?.forEach {
//                        pathList.add(it.path)
//                        Log.d("goyw", " url : "  + it.url)
//                        urlList.add(it.url)
//                    }

                    NSLog.d(" 上传成功  : $dataList")

                    if (success != null) {
                        success(dataList!!)
                    } else {
                        mView.hideLoading()
                    }

                }

                override fun onFailure(message: String, error_code: Int) {
                    mView.showFailed()
//                    mView.hideLoading()
                }
            })
    }
}