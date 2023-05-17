package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity
import com.talkcloud.networkshcool.baselibrary.help.TKShareHelper
import com.talkcloud.networkshcool.baselibrary.ui.activities.BrowserActivity
import com.talkcloud.networkshcool.baselibrary.utils.*
import com.talkcloud.sharelibrary.TKShareConstants2
import com.talkcloud.sharelibrary.model.TKShareLinkModel
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.dialog_share.*


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class TKShareDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.BottomDialog
) : Dialog(mContext, themeResId) {

    private var mCtx: Context = mContext

    private var downloadUrl: String? = null
    private var downloadName: String? = null

    private var downloadEntity: DownloadEntity? = null

    private lateinit var rxPermissions: RxPermissions


    init {
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        setContentView(R.layout.dialog_share)
        setCanceledOnTouchOutside(false)
        val window = window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }


    private fun initData() {
        rxPermissions = RxPermissions(mCtx as FragmentActivity)
    }

    private fun initListener() {

        mCancelBtn.setOnClickListener {
            dismiss()
        }


        // 打印
        mPrinterIv.setOnClickListener {

            (mCtx as BrowserActivity).doPrint()


            // 先判断权限
//            rxPermissions
//                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe { granted ->
//                    if (granted) {
//
//                       val filPath = SDCardUtils.getExternalFilesDir(mCtx, BaseConstant.SOURCES_PATH).absolutePath + File.separator + downloadName
//
//
//                        if (!File(filPath).exists()) {
//                            TKDownloadFileUtil.getInstance().readyDownloadFile(mCtx as Activity, downloadName, mCtx.getString(R.string.share_downloading), downloadUrl, filPath)
//                        } else {
//                            TKPrintUtil.doPdfPrint(mCtx, filPath)
//                        }
//
////                        dismiss()
//
//                    } else {
//                        mCtx.toast(mCtx.getString(R.string.share_save_desc_text))
//                    }
//                }

        }


        // 微信分享
        mWXIv.setOnClickListener {

            if (!TKShareHelper.isInstallApp(mCtx, TKShareConstants2.TYPE_WX)) {
                val platformType = mCtx.getString(R.string.share_wx_title)
//                Toast.makeText(mCtx, mCtx.getString(R.string.share_install_desc, platformType), Toast.LENGTH_LONG).show()
                ToastUtils.showShortTop(mCtx.getString(R.string.share_install_desc))
                return@setOnClickListener
            }

            shareDocLink(TKShareConstants2.TYPE_WX)

            dismiss()
        }

        // 钉钉分享
        mDDIv.setOnClickListener {
            if (!TKShareHelper.isInstallApp(mCtx, TKShareConstants2.TYPE_DD)) {
                val platformType = mCtx.getString(R.string.share_dd_title)
//                Toast.makeText(mCtx, mCtx.getString(R.string.share_install_desc, platformType), Toast.LENGTH_LONG).show()
                ToastUtils.showShortTop(mCtx.getString(R.string.share_install_desc, platformType))
                return@setOnClickListener
            }

            shareDocLink(TKShareConstants2.TYPE_DD)

            dismiss()
        }
    }


    fun setDownloadInfo(entity: DownloadEntity): TKShareDialog {
        this.downloadEntity = entity
        this.downloadUrl = entity.downloadPath
        this.downloadName = entity.fileName

//        val filPath = SDCardUtils.getExternalFilesDir(mCtx, BaseConstant.SOURCES_PATH).absolutePath + File.separator + downloadName
//        NSLog.d(" filePath :" + filPath)

        return this
    }


//    fun setDownloadName(downloadName: String): TKShareDialog {
//        this.downloadName = downloadName
//        return this
//    }
//
//    fun setDownloadUrl(downloadUrl: String): TKShareDialog {
//        this.downloadUrl = downloadUrl
//        return this
//    }
//    fun setShareType(type: Int): TKShareDialog {
//        this.mShareType = type
//
//        return this
//    }


    // 分享文档
    private fun shareDocLink(type: Int) {

        val model = TKShareLinkModel()
//        model.icon = TKExtManage.getInstance().getAppLogoRes(mCtx)
        model.title = AppPrefsUtil.getUserName() + mCtx.getString(R.string.share_text) + downloadName + mCtx.getString(R.string.share_enclosure_text)
        model.desc = mCtx.getString(R.string.share_desc_text)

        model.url = downloadUrl

        TKShareHelper.shareLink(mCtx, type, model)
    }


    companion object {
        const val TYPE_LINK = 0
        const val TYPE_IMG = 1
    }


}