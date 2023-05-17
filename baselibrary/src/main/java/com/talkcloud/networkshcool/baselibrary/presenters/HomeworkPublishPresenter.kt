package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.*
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.HomeworkPublishView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/6/18
 * Desc
 */
class HomeworkPublishPresenter(mContext: Context, mView: HomeworkPublishView) : BasePresenter<HomeworkPublishView>(mContext, mView) {

    fun publishHomework(lessonId: String, jobMap: MutableMap<String, Any>, mediaList: MutableList<TKHomeworkResourceEntity>) {

        val durationList: MutableList<Long> = mutableListOf()
        val netDataList: MutableList<TKHomeworkResourceEntity> = mutableListOf()
        // 本地的资源url
        val localMediaList: MutableList<String> = mediaList.filter {
            if ("2" == it.source) {
                // 网盘
                netDataList.add(it)
            }
            "1" == it.source
        }.filter {
            // 本地
            !(it.url.startsWith("http") || it.url.startsWith("https"))
        }.map {
            durationList.add(it.duration.toLong())
            it.url
        }.toMutableList()

//        NSLog.d("netdata : " + netDataList)

        uploadImg(localMediaList) { list: List<UploadEntity> ->
            val resourceList = mutableListOf<MutableMap<String, Any>>()
            list.forEachIndexed { index, it ->

                val resourceMap = mutableMapOf<String, Any>()

                resourceMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                resourceMap["source"] = "1"
                resourceMap["duration"] = durationList[index]
                resourceList.add(resourceMap)
            }

            netDataList.forEach {
                val resourceMap = mutableMapOf<String, Any>()

                resourceMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                resourceMap["source"] = "2"

                resourceList.add(resourceMap)
            }

            jobMap["resources"] = resourceList

            NSLog.d("jobMap : $jobMap")

            publishHomework(lessonId, jobMap)
        }
    }

    // 提交作业
    private fun publishHomework(lessonId: String, map: MutableMap<String, Any>) {

        // 显示loading
        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.publishHomework(lessonId, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<TKHomeworkDetailEntity>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<TKHomeworkDetailEntity>>) {
                    NSLog.d("goyw", "t: ${t.body()}");

                    mView.hideSuccessLoading()
                    // TKHomeworkDetailEntity 只有id有值
                    val homeworkId = t.body()?.data?.id

                    mView.publishHomeworkSuccess(homeworkId, map["is_draft"] as String?)
                }

                override fun onFailure(message: String, error_code: Int) {
                    mView.showFailed()
                }
            })
    }


    // 提交编辑作业
    fun putEditHomework(homeworkId: String, lessonId: String, jobMap: MutableMap<String, Any>, mediaList: MutableList<TKHomeworkResourceEntity>) {

        NSLog.d("mediaList :$mediaList")

        val durationList: MutableList<Long> = mutableListOf()

        val netDataList: MutableList<TKHomeworkResourceEntity> = mutableListOf()
        val remoteMediaList: MutableList<TKHomeworkResourceEntity> = mutableListOf()
        // 本地的资源url
        val localMediaList: MutableList<String> = mediaList.filter {
            if ("2" == it.source) {
                // 网盘
                netDataList.add(it)
            }
            "1" == it.source
        }.filter {
            if(it.url.startsWith("http") || it.url.startsWith("https")) {
                remoteMediaList.add(it)
            }
            !(it.url.startsWith("http") || it.url.startsWith("https"))
        }.map {
            durationList.add(it.duration.toLong())
            it.url
        }.toMutableList()

//        val remoteMediaList = mediaList.filter {
//            it.source == "1" && (it.url.startsWith("http") || it.url.startsWith("https"))
//        }.toMutableList()

//        NSLog.d("remoteMediaList + $remoteMediaList")

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

            //上传返回的数据
            list.forEachIndexed { index, it ->

                val resourceMap = mutableMapOf<String, Any>()

                resourceMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                resourceMap["source"] = "1"
                resourceMap["duration"] = durationList[index]
                resourceList.add(resourceMap)
            }

            // TODO 网盘数据
            netDataList.forEach {
                val resourceMap = mutableMapOf<String, Any>()

                resourceMap["id"] = it.id
                // 枚举备注: 1本地上传 2企业网盘
                resourceMap["source"] = "2"

                resourceList.add(resourceMap)
            }

            jobMap["resources"] = resourceList

            NSLog.d("jobMap update : $jobMap")

            putEditHomework(homeworkId, lessonId, jobMap)
        }

    }

    // 提交作业
    private fun putEditHomework(homeworkId: String, lessonId: String, jobMap: MutableMap<String, Any>) {

        // 显示loading
        mView.showLoading()

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.putEditHomework(homeworkId, lessonId, jobMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<String>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<String>>) {
                    NSLog.d("goyw", "t: ${t.body()}");
                    mView.hideSuccessLoading()

                    mView.publishHomeworkSuccess(homeworkId, jobMap["is_draft"] as String?)
                }

                override fun onFailure(message: String, error_code: Int) {
                    NSLog.d("goyw", " $message  ,,  $error_code");
                    mView.showFailed()
                }
            })
    }


    // 获取学生列表 TODO 暂时写在这里
    fun getLessonStudentList(lessonId: String) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.getLessonStudentList(lessonId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseSubscriber<Response<ApiResponse<List<StudentData>>>>(mContext, false, false) {

                override fun onSuccess(t: Response<ApiResponse<List<StudentData>>>) {
                    NSLog.d("goyw", "t student: ${t.body().toString()}")
                    t.body()?.let { mView.handleLessonStudentInfo(it.data) }
                }

                override fun onFailure(message: String, error_code: Int) {
                }
            })
    }

}