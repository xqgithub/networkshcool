//package com.talkcloud.networkshcool.base
//
//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import com.jph.takephoto.app.TakePhoto
//import com.jph.takephoto.app.TakePhotoActivity
//import com.jph.takephoto.model.CropOptions
//import com.jph.takephoto.model.TResult
//import com.talkcloud.networkshcool.baselibrary.utils.DateUtil
//import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil
//import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
//import com.talkcloud.networkshcool.common.MyConstant
//import com.talkcloud.networkshcool.presenters.BasePresenter
//import com.talkcloud.networkshcool.utils.FileUtil
//import com.talkcloud.networkshcool.views.IBaseView
//import com.talkcloud.networkshcool.weiget.dialog.TKLoadingDialog
//import com.talkcloud.networkshcool.weiget.dialog.TKSelectImgDialog
//import java.io.File
//import kotlin.math.min
//
//
///**
// * Author  guoyw
// * Date    2021/5/17
// * Desc    暂时先继承TakePhotoActivity ,后面优化直接使用BaseMvpActivity
// */
//
//@Deprecated("Deprecated")
//open abstract class BaseTakePhotoActivity<T : BasePresenter<*>> : TakePhotoActivity(), IBaseView {
//
//    private lateinit var mTakePhoto: TakePhoto
//    private lateinit var mTempFile: File
//
//    protected lateinit var mLoadingDialog: TKLoadingDialog
//
//    private var selectImgDialog: TKSelectImgDialog? = null
//    private var cropOptions: CropOptions? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mTakePhoto = takePhoto
//
//        PublicPracticalMethodFromJAVA.getInstance()
//            .transparentStatusBar(
//                this,
//                true, true,
//                android.R.color.white
//            );
//
////        if (PublicPracticalMethodFromJAVA.getInstance().isPad(this)) {
////            //设置横屏
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
////            TKDensityUtil.setDensity(this, MyApplication.myApplication)
////        } else {
////            //设置竖屏
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
////            TKDensityUtil.setDensity(this, MyApplication.myApplication, 1024f)
////        }
////        TKDensityUtil.setDensity(this, MyApplication.myApplication)
//
//        setContentView(getLayoutId())
//
//        // 加载框
//        mLoadingDialog = TKLoadingDialog(this)
//
//
//        initUiView()
//        initData()
//        initListener()
//    }
//
//    /**
//     * 得到xml布局文件的id
//     */
//    protected abstract fun getLayoutId(): Int
//
//
//    /**
//     * 初始化UI控件
//     */
//    protected abstract fun initUiView()
//
//    /**
//     * 初始化数据
//     */
//    protected abstract fun initData()
//
//    /**
//     * 初始化监听
//     */
//    protected abstract fun initListener()
//
//
//    override fun takeCancel() {
//    }
//
//    override fun takeFail(result: TResult?, msg: String?) {
//    }
//
//
//    protected fun showAlertView(type: Int) {
//
//        if(cropOptions == null) {
//            val size = min(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
//            cropOptions = CropOptions.Builder().setOutputX(size).setOutputX(size).setWithOwnCrop(false).create()
//        }
//        if(selectImgDialog == null) {
//            selectImgDialog = TKSelectImgDialog(this@BaseTakePhotoActivity)
//        }
//
//        selectImgDialog?.setSelectImgListener({
//            when (type) {
//                MyConstant.PARAMS_TAKE_PHOTO_NORMAL -> {
//                    createTempFile()
//                    mTakePhoto.onPickFromCapture(Uri.fromFile(mTempFile))
//                }
//
//                MyConstant.PARAMS_TAKE_PHOTO_CROP -> {
//                    createTempFile()
//                    mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), cropOptions)
//                }
//            }
//        }, {
//            when (type) {
//                MyConstant.PARAMS_TAKE_PHOTO_NORMAL -> {
//                    mTakePhoto.onPickFromGallery()
//                }
//
//                MyConstant.PARAMS_TAKE_PHOTO_CROP -> {
//                    createTempFile()
//                    mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile), cropOptions)
//                }
//            }
//        });
//        selectImgDialog?.show()
//    }
//
////    protected fun showAlertView() {
////        dialog = TKSelectImgDialog(this@BaseTakePhotoActivity)
////        dialog.setSelectImgListener({
////            createTempFile()
////            mTakePhoto.onPickFromCaptureWithCrop(Uri.fromFile(mTempFile), )
////        }, {
////            createTempFile()
////            mTakePhoto.onPickFromGalleryWithCrop(Uri.fromFile(mTempFile),)
////        });
////        dialog.show()
////    }
//
//    /*
//        新建临时文件
//     */
//    private fun createTempFile() {
////        val tempFileName = "${DateUtil.curTime()}.png"
////        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
////            this.mTempFile = File(Environment.getExternalStorageDirectory(), tempFileName)
////            return
////        }
//        val tempFileName = "${DateUtil.curTime()}.png"
//
//        val imgPath = FileUtil.getImageDir(this)
//
//        if (imgPath != null && FileUtil.isExistFile(imgPath)) {
////            FileUtil.createDirs(imgPath)
//            this.mTempFile = File(imgPath, tempFileName)
//        } else {
//            this.mTempFile = File(filesDir, tempFileName)
//        }
////        this.mTempFile = File(filesDir, tempFileName)
//    }
//
//
//    override fun showLoading() {
//        mLoadingDialog.showLoading()
//    }
//
//    override fun hideLoading() {
//        mLoadingDialog.hideLoading()
//    }
//
//
//    override fun hideSuccessLoading() {
//        mLoadingDialog.hideSuccessLoading()
//    }
//
//    override fun showFailed() {
//        mLoadingDialog.showFailed()
//    }
//
//
//    /**
//     * 切换语言使用
//     */
//    override fun attachBaseContext(newBase: Context?) {
////        super.attachBaseContext(newBase);
//        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase))
//    }
//
//}