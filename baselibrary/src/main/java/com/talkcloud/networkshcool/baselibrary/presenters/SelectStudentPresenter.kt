package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.StudentAllEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.HomeworkPublishView
import com.talkcloud.networkshcool.baselibrary.views.SelectStudentView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/9/27
 * Desc
 */
class SelectStudentPresenter(mContext: Context, mView: SelectStudentView) : BasePresenter<SelectStudentView>(mContext, mView) {

    // 获取学生列表
    fun getStudentList(lessonId: String, pageNum: Int, rows: Int, name: String = "") {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        val map = mutableMapOf<String, Any>()
        map["lesson_id"] = lessonId
        map["page"] = pageNum
        map["rows"] = rows
        if(name.isNotEmpty()) {
            map["name"] = name
        }

        apiService.getStudentList(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<StudentAllEntity>>>(mContext, pageNum == 1, false) {

                override fun onSuccess(t: Response<ApiResponse<StudentAllEntity>>) {
//                    NSLog.d("goyw", "t student: ${t.body().toString()}");
                    val studentEntity: StudentAllEntity? = t.body()?.data
                    studentEntity?.let { mView.handlerStudentInfo(it, name) }
                }

                override fun onFailure(message: String, error_code: Int) {

                    mView.handlerStudentInfoFailed(message)
                }
            })
    }


}