package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkTeacherDetailEntity
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.HomeworkDetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/6/22
 * Desc
 */
class HomeworkDetailPresenter(mContext: Context, mView: HomeworkDetailView) : BasePresenter<HomeworkDetailView>(mContext, mView) {

    // 获取作业详情 老师端
    fun getHomeworkTeacherDetails(homeworkId: String) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.getHomeworkTeacherDetails(homeworkId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<HomeworkTeacherDetailEntity>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<HomeworkTeacherDetailEntity>>) {
                    NSLog.d("goyw", "t: ${response.body()}")

                    var entity: HomeworkTeacherDetailEntity? = response.body()?.data

                    mView.showHomeworkInfo(entity)
                }

                override fun onFailure(message: String, error_code: Int) {
//                    mView.hideLoading()
                }
            })
    }
}