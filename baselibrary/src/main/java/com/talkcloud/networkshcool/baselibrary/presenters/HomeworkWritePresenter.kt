package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import android.widget.Toast
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.HomeworkWriteView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/6/22
 * Desc
 */
class HomeworkWritePresenter(mContext: Context, mView: HomeworkWriteView) : BasePresenter<HomeworkWriteView>(mContext, mView) {


    fun submitHomework(homeworkId: String, studentId: String, jobMap: MutableMap<String, Any>, mediaList: MutableList<TKHomeworkResourceEntity>) {

        val durationList: MutableList<Long> = mutableListOf()
        // 本地的资源url
        val localMediaList: MutableList<String> = mediaList.filter {
            !(it.url.startsWith("http") || it.url.startsWith("https"))
        }.map {
            durationList.add(it.duration.toLong())
            it.url
        }.toMutableList()

        val remoteMediaList = mediaList.filter {
            it.url.startsWith("http") || it.url.startsWith("https")
        }.toMutableList()


        uploadImg(localMediaList) { list: List<UploadEntity> ->
            val resourceList = mutableListOf<MutableMap<String, Any>>()

            // 服务端详情返回的回显数据 草稿会用到
            remoteMediaList.forEach {

                val remoteMap = mutableMapOf<String, Any>()

                remoteMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                remoteMap["source"] = it.source
                remoteMap["duration"] = it.duration

                resourceList.add(remoteMap)
            }

            list.forEachIndexed { index, it ->

                val resourceMap = mutableMapOf<String, Any>()

                resourceMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                resourceMap["source"] = "1"
                resourceMap["duration"] = durationList[index]
                resourceList.add(resourceMap)
            }

            jobMap["submit_files"] = resourceList

            NSLog.d("jobMap : $jobMap")

            submitHomework(homeworkId, studentId, jobMap)
        }
    }

    // 提交作业
    private fun submitHomework(homeworkId: String, studentId: String, map: MutableMap<String, Any>) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V2)

        apiService.submitHomework(homeworkId, studentId, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<String>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<String>>) {
                    NSLog.d("goyw", "t: ${t.body()}");
                    mView.hideSuccessLoading()

                    mView.submitHomeworkSuccess(homeworkId, map["is_draft"] as String?)
                }

                override fun onFailure(message: String, error_code: Int) {
                    NSLog.d("goyw", " $message  ,,  $error_code");
//                    mView.hideLoading()
                }
            })
    }


    // 获取作业详情 学生端
    fun getHomeworkStudentDetails(homeworkId: String, studentId: String) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V2)

        apiService.getHomeworkStudentDetails(homeworkId, studentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<HomeworkStudentDetailEntity>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<HomeworkStudentDetailEntity>>) {
                    NSLog.d("goyw", "t: ${response.body()?.data.toString()}")

                    val entity: HomeworkStudentDetailEntity? = response.body()?.data

                    mView.showHomeworkInfo(entity)
                }

                override fun onFailure(message: String, error_code: Int) {
//                    mView.hideLoading()
                }
            })
    }



    // 撤回作业 学生端
    fun rollbackHomeworkStudentDetails(homeworkId: String, studentId: String) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.rollbackHomeworkStudentDetails(homeworkId, studentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<*>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<*>>) {
                    mView.rollbackHomeworkSuccess()
                }

                override fun onFailure(message: String, error_code: Int) {
//                    mContext.toast(message)
                    ToastUtils.showShortTop(message)
//                    mView.hideLoading()
                }
            })
    }


}