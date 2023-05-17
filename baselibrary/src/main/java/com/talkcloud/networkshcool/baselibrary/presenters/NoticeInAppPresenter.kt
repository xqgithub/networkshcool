package com.talkcloud.networkshcool.baselibrary.presenters

import android.app.Activity
import com.talkcloud.corelibrary.TKJoinRoomModel
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.JoinRoomEntity
import com.talkcloud.networkshcool.baselibrary.entity.NoticeInAppEntity
import com.talkcloud.networkshcool.baselibrary.utils.JsonResolutionUtils
import com.talkcloud.networkshcool.baselibrary.views.JoinRoomView
import com.talkcloud.networkshcool.baselibrary.views.NoticeInAppView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.Response
import java.util.HashMap

/**
 * Date:2021/9/16
 * Time:16:14
 * author:joker
 */
class NoticeInAppPresenter constructor(private var mActivity: Activity, private var noticeInaAppView: NoticeInAppView) {

    /**
     * 通知列表
     */
    fun noticeList(page: Int, rows: Int) {
        val mMap: MutableMap<String, Any> = HashMap()
        mMap["page"] = page
        mMap["rows"] = rows
        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)
        apiService.notice(mMap)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<NoticeInAppEntity>>>(mActivity, true, false) {

                override fun onSuccess(apiresponse: Response<ApiResponse<NoticeInAppEntity>>?) {
                    try {
                        apiresponse?.let { _apiresponse ->
                            _apiresponse.body()?.let {
                                var resultcode = it.result
                                var msg = it.msg
                                if (resultcode == 0) {
                                    var data = it.data
                                    noticeInaAppView.noticeListCallback(true, data, msg)
                                    return
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    noticeInaAppView.noticeListCallback(false, null, mActivity.resources.getString(R.string.data_wrong))
                }

                override fun onFailure(message: String, error_code: Int) {
                    noticeInaAppView.noticeListCallback(false, null, message)
                }
            })
    }

    /**
     * 读取通知
     */
    fun readNotice(datas: IntArray, position: Int) {
        val mMap: MutableMap<String, Any> = HashMap()
        mMap["id"] = datas
        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)
        apiService.readnotice(mMap)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<Any>>>(mActivity, true, false) {
                override fun onSuccess(apiresponse: Response<ApiResponse<Any>>?) {
                    try {
                        apiresponse?.let { _apiresponse ->
                            _apiresponse.body()?.let {
                                var resultcode = it.result
                                var msg = it.msg
                                if (resultcode == 0) {
                                    var data = it.data
                                    noticeInaAppView.readNoticeCallback(true, position, msg)
                                    return
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    noticeInaAppView.readNoticeCallback(false, null, mActivity.resources.getString(R.string.data_wrong))
                }

                override fun onFailure(message: String, error_code: Int) {
                    noticeInaAppView.readNoticeCallback(false, null, message)
                }
            })
    }
}