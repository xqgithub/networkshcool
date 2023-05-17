package com.talkcloud.networkshcool.baselibrary.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.talkcloud.networkshcool.baselibrary.presenters.BasePresenter
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil
import com.talkcloud.networkshcool.baselibrary.utils.FileUtil
import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.GlideEngine
import com.talkcloud.networkshcool.baselibrary.views.IBaseView
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectImgDialog
import java.io.File


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc    优化直接使用BaseMvpActivity
 */
open abstract class BaseTakePhotoActivity2<T : BasePresenter<*>> : BaseMvpActivity<T>(), IBaseView {

    //    private lateinit var mTakePhoto: TakePhoto
    private lateinit var mTempFile: File

    private var selectImgDialog: TKSelectImgDialog? = null

//    private var mTakePhotoType = MyConstant.PARAMS_TAKE_PHOTO_NORMAL;

    private lateinit var uiStyle: PictureSelectorUIStyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        //1.判断是pad还是phone，设置横屏或者竖屏
//        if (ScreenTools.getInstance().isPad(this)) {
//            //设置横屏
//            ScreenTools.getInstance().setLandscape(this)
//        } else {
//            ScreenTools.getInstance().setPortrait(this)
//        }
//
//        PublicPracticalMethodFromJAVA.getInstance()
//            .transparentStatusBar(
//                this,
//                true, true,
//                android.R.color.white
//            );

        uiStyle = PictureSelectorUIStyle.ofSelectTotalStyle()
        uiStyle.isCompleteReplaceNum = true

    }


    // 一般选择 一次最多选择的图片张数
    protected fun showSelectImgAlertView(num: Int) {

        if (selectImgDialog == null) {
            selectImgDialog = TKSelectImgDialog(this@BaseTakePhotoActivity2)
        }

        selectImgDialog?.setSelectImgListener({
            // 相册
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(num)
                .setPictureUIStyle(uiStyle)
                .isMaxSelectEnabledMask(true)
                .forResult(PictureConfig.CHOOSE_REQUEST)
        }, {
            // 拍照
            PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(num)
                .setPictureUIStyle(uiStyle)
                .isMaxSelectEnabledMask(true)
                .forResult(PictureConfig.REQUEST_CAMERA);

        });
        selectImgDialog?.show()
    }

    // 头像选择
    protected fun showSelectHeadImgAlertView() {

        if (selectImgDialog == null) {
            selectImgDialog = TKSelectImgDialog(this@BaseTakePhotoActivity2)
        }

        selectImgDialog?.setSelectImgListener({
            //相册
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isEnableCrop(true)// 是否裁剪
                .selectionMode(PictureConfig.SINGLE)
                .setPictureUIStyle(uiStyle)
                .withAspectRatio(1, 1)
                .forResult(PictureConfig.CHOOSE_REQUEST)


        }, {
            // 拍照
            PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isEnableCrop(true)// 是否裁剪
                .selectionMode(PictureConfig.SINGLE)
                .setPictureUIStyle(uiStyle)
                .withAspectRatio(1, 1)
                .forResult(PictureConfig.REQUEST_CAMERA)
        });
        selectImgDialog?.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                // 相册
                PictureConfig.CHOOSE_REQUEST -> {
                    val result = PictureSelector.obtainMultipleResult(data)
                    takeSuccess(result)

//                    NSLog.d("result :CHOOSE_REQUEST  " + result[0].cutPath)
                }

                // 拍照
                PictureConfig.REQUEST_CAMERA -> {
                    val result = PictureSelector.obtainMultipleResult(data)
                    takeSuccess(result)

//                    NSLog.d("result :REQUEST_CAMERA  " + result[0].cutPath)
                }
            }
        }
    }

    abstract fun takeSuccess(result: List<LocalMedia>)

    /*
        新建临时文件
     */
    private fun createTempFile() {
//        val tempFileName = "${DateUtil.curTime()}.png"
//        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
//            this.mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
//            return
//        }
        val tempFileName = "${DateUtil.curTime()}.png"

        val imgPath = FileUtil.getImageDir(this)

        if (imgPath != null && FileUtil.isExistFile(imgPath)) {
//            FileUtil.createDirs(imgPath)
            this.mTempFile = File(imgPath, tempFileName)
        } else {
            this.mTempFile = File(filesDir, tempFileName)
        }
//        this.mTempFile = File(filesDir, tempFileName)
    }


    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }


    override fun hideSuccessLoading() {
        mLoadingDialog.hideSuccessLoading()
    }

    override fun showFailed() {
        mLoadingDialog.showFailed()
    }


    /**
     * 切换语言使用
     */
    override fun attachBaseContext(newBase: Context?) {
//        super.attachBaseContext(newBase);
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase))
    }

}