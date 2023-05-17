package com.talkcloud.networkshcool.baselibrary.presenters

import android.Manifest
import android.app.Activity
import android.content.Context
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.TKExtManage
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber
import com.talkcloud.networkshcool.baselibrary.api.CallManager
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity
import com.talkcloud.networkshcool.baselibrary.ui.dialog.SysVersionUpdateDialog
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils
import com.talkcloud.networkshcool.baselibrary.utils.UpdateAppUtils
import com.talkcloud.networkshcool.baselibrary.views.MainMenuView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import okhttp3.Callback
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.util.*


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class MainMenuPresenter(mContext: Context, mView: MainMenuView, private var permissionsListener: PermissionsActivity.PermissionsListener) : BasePresenter<MainMenuView>(mContext, mView) {

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
//                BaseSubscriber<Response<ApiResponse<UserInfoEntity?>>>(mContext, false, false) {
//                override fun onSuccess(response: Response<ApiResponse<UserInfoEntity?>>) {
////                    Log.d("goyw", "response userinfo: ${response.body()?.data}")
//                    response.body()?.data?.let {
//                        mView.showUserInfo(it)
//                    }
//                }
//
//                override fun onFailure(message: String, error_code: Int) {
////                    Log.d("goyw", "t: $message");
//                }
//            })
//    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        if (!VariableConfig.checkIdentityFlag) {
            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1)
//            val apiService = RetrofitServiceManager.getInstance(
//                VariableConfig.base_url_user,
//                ApiService.user
//            ).apiService

            apiService.userInfo
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :
                    BaseSubscriber<Response<ApiResponse<UserInfoEntity?>>>(mContext, false, false) {
                    override fun onSuccess(response: Response<ApiResponse<UserInfoEntity?>>) {
//                    Log.d("goyw", "response userinfo: ${response.body()?.data}")
                        response.body()?.data?.let {
                            mView.showUserInfo(it)
                        }
                    }

                    override fun onFailure(message: String, error_code: Int) {
//                    Log.d("goyw", "t: $message");
                    }
                })
        }
    }


    /**
     * 获取系统版本接口
     */
    fun sysversion() {
        val map_bodys: MutableMap<String, String> = HashMap()
        map_bodys["companydomain"] = TKExtManage.getInstance().companyDomain //企业域名 非定制默认www
        map_bodys["source"] = "4" //企业来源 4：门课
        /**
         * 0：Mac、
         * 1：PC、
         * 3：iOS iPad、
         * 5：iOS iPhone、
         * 6：TV盒子、
         * 7：新学问PC、
         * 8：新学问TV、
         * 9：Android、
         * 10：会议Android、
         * 11：会议iOS
         */
        map_bodys["type"] = "9"
        map_bodys["version"] = VariableConfig.getVersionUpdateVersion() //版本号 拓课云APP端通行用法为：封包日期+版本号，如：20210621100
        val call: Call = CallManager.getInstance(VariableConfig.getVersionUpdateUrl()).getPostRequest(map_bodys)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
//                mView.sysversionCallback(false, null, e.message)
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                try {
                    response.body()?.let {
                        val jsonString = StringUtils.byte2String(response.body()!!.bytes())
                        JSONObject(jsonString).let {
                            val result = it.optInt("result", -1)
                            if (result == 0) {
                                val sysVersionEntity = SysVersionEntity()
                                sysVersionEntity.version = it.optString("version", "")
                                sysVersionEntity.filename = it.optString("filename", "")
                                sysVersionEntity.filetype = it.optString("filetype", "")
                                sysVersionEntity.isupdate = it.optString("isupdate", "")
                                sysVersionEntity.updateflag = it.optString("updateflag", "")
                                sysVersionEntity.url = it.optString("url", "")
                                sysVersionEntity.apptype = it.optString("apptype", "")
                                sysVersionEntity.updateaddr = it.optString("updateaddr", "")
                                sysVersionEntity.setupaddr = it.optString("setupaddr", "")
                                sysVersionEntity.versionnum = it.optString("versionnum", "")
                                mView.sysversionCallback(true, sysVersionEntity, "")
                            } else {
                                mView.sysversionCallback(true, null, "")
                            }
                            return
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                mView.sysversionCallback(false, null, mContext.resources.getString(R.string.data_wrong))
            }
        })
    }


    /**
     * 升级更新APP
     */
    fun updateAPP(sysVersionEntity: SysVersionEntity) {
        sysVersionEntity?.let {
            if ("1" == it.updateflag || PublicPracticalMethodFromJAVA.getInstance().isShowSysVersionUpdateDialog) {

                val sysVersionUpdateDialog = SysVersionUpdateDialog(mContext)
                sysVersionUpdateDialog.setInitDatas(sysVersionEntity)
                //立即更新按钮
                sysVersionUpdateDialog.confirmButton {
                    //权限判断
                    val mPermissionsChecker = PermissionsChecker(mContext)
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (mPermissionsChecker.lacksPermissions(permissions)) {
                        //去申请权限
                        PublicPracticalMethodFromJAVA.getInstance().startPermissionsActivity(mContext as Activity?, permissions, permissionsListener, ConfigConstants.PERMISSIONS_GRANTED_UPDATEAPP)
                    } else {
                        //初始化下载
                        UpdateAppUtils.getInstance().initUtils(mContext as Activity?, sysVersionEntity)
                        //准备开始下载apk
                        UpdateAppUtils.getInstance().readyDownloadApk(mContext.resources.getString(TKExtManage.getInstance().getAppNameRes(mContext)), mContext.resources.getString(R.string.download_progress) + "0%")
                        sysVersionUpdateDialog.dismissDialog()
                    }
                }

                if (!sysVersionUpdateDialog.isShowing) {
                    sysVersionUpdateDialog.show()
                }
            }
        }
    }

    /**
     * 未读通知
     */
    fun noticeNew() {
        if (!VariableConfig.checkIdentityFlag) {
            val apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1)
            apiService.noticenew()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseSubscriber<Response<ApiResponse<Any>>>(mContext, false, false) {
                    override fun onSuccess(apiresponse: Response<ApiResponse<Any>>?) {
                        try {
                            apiresponse?.let { _apiresponse ->
                                _apiresponse.body()?.let {
                                    var resultcode = it.result
                                    var msg = it.msg
                                    if (resultcode == 0) {
                                        var data = it.data
                                        val jsonObject_data: JSONObject = JSONObject(data.toString())
                                        val count = jsonObject_data.optInt("count", 0)
                                        val homework_unreads = jsonObject_data.optInt("homework_unreads", 0)
                                        with(mutableMapOf<String, Int>()) {
                                            this["count"] = count
                                            this["homework_unreads"] = homework_unreads
                                            mView.noticenew(true, this, msg)
                                        }
                                        return
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        mView.noticenew(false, null, mContext.resources.getString(R.string.data_wrong))
                    }

                    override fun onFailure(message: String?, error_code: Int) {
                        mView.noticenew(false, null, message)
                    }
                })
        }
    }
}