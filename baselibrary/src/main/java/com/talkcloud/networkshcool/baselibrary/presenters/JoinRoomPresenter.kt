package com.talkcloud.networkshcool.baselibrary.presenters

import android.content.Context
import com.talkcloud.corelibrary.TKJoinBackRoomModel
import com.talkcloud.corelibrary.TKJoinRoomModel
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.JoinPlaybackRoomEntity
import com.talkcloud.networkshcool.baselibrary.entity.JoinRoomEntity
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.views.JoinRoomView
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import retrofit2.Response


/**
 * Author  guoyw
 * Date    2021/6/1
 * Desc
 */
class JoinRoomPresenter(mContext: Context, mView: JoinRoomView) :
    BasePresenter<JoinRoomView>(mContext, mView) {


    private fun getJoinRoomUrl(id: String): String? {
        return "lesson/$id/room"
    }

    // 进入房间
    fun requestJoinRoom(serialId: String, model: TKJoinRoomModel) {

//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_teachers_students,
//            getJoinRoomUrl(serialId)
//        ).apiService

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.joinRoom(serialId)
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(object :
                BaseSubscriber<Response<ApiResponse<JoinRoomEntity>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<JoinRoomEntity>>) {
//                    Log.d("goyw", "response : joinRoom " + response.body()?.data)

                    model.serialId = serialId

                    val pwd = response.body()?.data?.pwd
                    val roomId = response.body()?.data?.room_id

                    model.roomId = roomId
                    model.pwd = pwd

                    val state = response.body()?.data?.state
                    model.state = state

                    mView.joinRoom(model)
                }

                override fun onFailure(message: String, error_code: Int) {
                }
            })
    }


    private fun getJoinPlaybackRoomUrl(id: String): String? {
        return "lesson/$id/record"
    }

    // 进入回放房间
    fun requestJoinPlaybackRoom(serialId: String) {

//        val apiService = RetrofitServiceManager.getInstance(
//            VariableConfig.base_url_teachers_students,
//            getJoinPlaybackRoomUrl(serialId)
//        ).apiService

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.joinPlaybackRoom(serialId)
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(object :
                BaseSubscriber<Response<ApiResponse<List<JoinPlaybackRoomEntity>>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<List<JoinPlaybackRoomEntity>>>) {
//                    Log.d("goyw", "response : joinplayroom " + response.body()?.data)

                    val joinPlaybackRoomEntityList = response.body()?.data

                    val modelList = mutableListOf<TKJoinBackRoomModel>()

                    joinPlaybackRoomEntityList?.forEach {

                        val model = TKJoinBackRoomModel()
                        model.roomId = it.room_id
                        model.name = it.name
                        model.title = it.title
                        model.url = it.url
                        model.mp4url = it.mp4url

                        modelList.add(model)
                    }


                    mView.joinPlaybackRoom(modelList)
                }

                override fun onFailure(message: String, error_code: Int) {
//                    mContext.toast(message)
                }
            })
    }





    // 进入房间 通过房间号 进入房间
    fun requestJoinRoom2(roomId: String, model: TKJoinRoomModel) {

        val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)

        apiService.joinRoom2(roomId)
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(object :
                BaseSubscriber<Response<ApiResponse<JoinRoomEntity>>>(mContext, true, false) {

                override fun onSuccess(response: Response<ApiResponse<JoinRoomEntity>>) {
                    NSLog.d( "response : joinRoom 22" + response.body()?.data)

                    val pwd = response.body()?.data?.pwd
                    val roomId = response.body()?.data?.room_id

                    model.roomId = roomId
                    model.pwd = pwd

                    val state = response.body()?.data?.state
                    model.state = state

                    mView.joinRoom(model)
                }

                override fun onFailure(message: String, error_code: Int) {


                }
            })
    }

}