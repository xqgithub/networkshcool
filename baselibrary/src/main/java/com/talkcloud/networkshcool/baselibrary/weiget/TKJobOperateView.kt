package com.talkcloud.networkshcool.baselibrary.weiget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorUIStyle
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity
import com.talkcloud.networkshcool.baselibrary.entity.TeacherCommentEntity
import com.talkcloud.networkshcool.baselibrary.ui.dialog.AudioRecordDialog
import com.talkcloud.networkshcool.baselibrary.ui.dialog.NetworkDiskDialog
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.GlideEngine
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectImgDialog
import kotlinx.android.synthetic.main.layout_job_operate_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    作业 操作区域
 */
class TKJobOperateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private var mLessonId: String = ""

    // 选择拍照 相册
    private var selectImgDialog: TKSelectImgDialog? = null

    // 录音
    private var mRecordDialog: AudioRecordDialog? = null


    private var uiStyle: PictureSelectorUIStyle? = null

//    private var mActivity: Activity = context as Activity

    // 已选照片
    private var mSelectedImgList: MutableList<LocalMedia> = mutableListOf()

    // 已选视频
    private var mSelectedVideoList: MutableList<LocalMedia> = mutableListOf()

    // 已选网盘
    private var mSelectedNetDataList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()

    //照片
    private var mPhotoList: MutableList<String> = mutableListOf("相册", "拍照")

    //视频
    private var mVideoList: MutableList<String> = mutableListOf("相册", "录像")


    // 拍照成功回调
    private var mPhotoSuccessListener: (type: String, result: List<LocalMedia>) -> Unit = { _: String, _: List<LocalMedia> -> }

    // 网盘成功回调
    private var mExtSuccessListener: (netDataList: List<NetworkDiskEntity.DataBean>?) -> Unit = {}

    // 音频成功回调
    private var mAudioSuccessListener: (audio: AudioEntinty) -> Unit = {}

    // 语音按钮点击回调
    private var mAudioClickListener: () -> Unit = {}

    // 网盘弹窗
    private var mNetworkDiskDialog: NetworkDiskDialog? = null


    // 已选音频
    private var mAudioSelectNum = 0

    // 已选图片
    private var mImgSelectNum = 0

    // 已选视频
    private var mVideoSelectNum = 0

    // 已选文档
    private var mDocSelectNum = 0


    //布局文件的resid
    private var layoutRes = -1

    //可以选择图片的数量
    private var imgNum = -1

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TKJobOperateView)
        layoutRes = typedArray.getResourceId(R.styleable.TKJobOperateView_layoutRes, R.layout.layout_job_operate_view)
        imgNum = typedArray.getInteger(R.styleable.TKJobOperateView_imgNum, 9)
        typedArray.recycle()

        initView()

        initData()

        initListener()
    }

    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, layoutRes, this)
    }


    private fun initData() {
        mPhotoList = context.resources.getStringArray(R.array.hw_camera_arr).toMutableList()
        mVideoList = context.resources.getStringArray(R.array.hw_video_arr).toMutableList()
    }


    private fun initListener() {
        mOperatePhotoLl.setOnClickListener {
            showSelectImgDialog(imgNum)
        }

        // 拍视频
        mOperateVideoLl.setOnClickListener {
            showSelectVideoDialog(3)
        }

        // 录音
        mOperateAudioLl.setOnClickListener {
            mAudioClickListener()

            if (mAudioSelectNum >= 3) {
//                Toast.makeText(context, context.getString(R.string.hw_audio_limit), Toast.LENGTH_SHORT).show()
                ToastUtils.showShortTop(context.getString(R.string.hw_audio_limit))
                return@setOnClickListener
            }

            showAudioDialog()
        }

        mOperateNetDiskLl.setOnClickListener {

            showNetDataDialog()

        }
    }

    // 显示企业网盘
    private fun showNetDataDialog() {

        mNetworkDiskDialog = NetworkDiskDialog(context)

//        NSLog.d("已选图片 : $mImgSelectNum  已选video : $mVideoSelectNum  已选audio : $mAudioSelectNum  mDocSelectNum : $mDocSelectNum")

        mNetworkDiskDialog?.setInitDatas(mLessonId, mSelectedNetDataList, mImgSelectNum, mVideoSelectNum, mAudioSelectNum, mDocSelectNum)

        mNetworkDiskDialog?.setNetworkDiskDialogListener { networkDiskBeansSelected,
                                                           pics_selected, videos_selected, audios_selected, files_selected ->
            mExtSuccessListener(networkDiskBeansSelected)
        }

        mNetworkDiskDialog?.show()
    }

    private fun showAudioDialog() {

        if (mRecordDialog == null) {
//            mRecordDialog = AudioRecordDialog(context, context.resources.getDimensionPixelSize(R.dimen.dimen_375x))
            mRecordDialog = AudioRecordDialog(context)
        }


        mRecordDialog?.setOnRecordListener(object : AudioRecordDialog.OnRecordListener {
            override fun onConfirm(dialog: AudioRecordDialog, audio: AudioEntinty, mImgList: List<LocalMedia>, teachercomment: TeacherCommentEntity) {
            }

            override fun onRecordComplete(audio: AudioEntinty) {
                mAudioSuccessListener(audio)
            }

        })
//        if (mRecordDialog?.isShowing!!) {
        mRecordDialog?.show()

    }


    // 设置语音回调
    fun setAudioClickListener(audioClickListener: () -> Unit) {
        this.mAudioClickListener = audioClickListener
    }

    // 设置语音回调监听
    fun setAudioSuccessListener(audioSuccessListener: (audio: AudioEntinty) -> Unit) {
        this.mAudioSuccessListener = audioSuccessListener
    }

    // 设置网盘回调监听
    fun setNetDiskSuccessListener(extSuccessListener: (netDataList: List<NetworkDiskEntity.DataBean>?) -> Unit) {
        this.mExtSuccessListener = extSuccessListener
    }


    // 设置已选的图片的集合
    fun setSelectedImgList(imgList: MutableList<LocalMedia>, videoList: MutableList<LocalMedia>) {
        this.mSelectedImgList = imgList
        this.mSelectedVideoList = videoList
    }


    // 设置已选网盘数据
    fun setSelectedNetDataList(netDataList: MutableList<NetworkDiskEntity.DataBean>) {
        this.mSelectedNetDataList = netDataList
    }


    // 成功回调
    fun setOnPhotoSuccessListener(successListener: (type: String, result: List<LocalMedia>) -> Unit) {
        this.mPhotoSuccessListener = successListener
    }

    // 2拍视频
    private fun showSelectVideoDialog(num: Int) {

        if (selectImgDialog == null) {
            selectImgDialog = TKSelectImgDialog(context)
        }
        selectImgDialog!!.setItemContentList(mVideoList)

        if (uiStyle == null) {
            uiStyle = PictureSelectorUIStyle.ofSelectTotalStyle()
            uiStyle!!.isCompleteReplaceNum = true
        }

        selectImgDialog?.setSelectImgListener({
            PictureSelector.create(context as Activity)
                .openGallery(PictureMimeType.ofVideo())
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureUIStyle(uiStyle)
                .maxSelectNum(num)
                .maxVideoSelectNum(num)
                .isCompress(true)
                .isMaxSelectEnabledMask(true)
                .selectionData(mSelectedVideoList)
                .videoMaxSecond(60 * 5)
                .filterMaxFileSize(256 * 1024 * 1024)
//                .recordVideoSecond(10)
                .forResult(TKResultCallback(BaseConstant.TYPE_VIDEO))
        }, {

            if (mSelectedVideoList.size >= 3) {
//                Toast.makeText(context, context.getString(R.string.hw_video_limit), Toast.LENGTH_SHORT).show()
                ToastUtils.showShortTop(context.getString(R.string.hw_video_limit));
                return@setSelectImgListener
            }

            PictureSelector.create(context as Activity)
                .openCamera(PictureMimeType.ofVideo())
                .imageEngine(GlideEngine.createGlideEngine())
                .setPictureUIStyle(uiStyle)
                .maxSelectNum(num)
                .maxVideoSelectNum(num)
                .selectionData(mSelectedVideoList)
                .isCompress(true)
                .recordVideoSecond(60 * 5)
//                .videoQuality(1)
                .forResult(TKResultCallback(BaseConstant.TYPE_VIDEO))
        })
        selectImgDialog?.show()
    }


    // 1拍照一般选择 一次最多选择的图片张数
    private fun showSelectImgDialog(num: Int) {

        if (selectImgDialog == null) {
            selectImgDialog = TKSelectImgDialog(context)
        }
        selectImgDialog!!.setItemContentList(mPhotoList)

        if (uiStyle == null) {
            uiStyle = PictureSelectorUIStyle.ofSelectTotalStyle()
            uiStyle!!.isCompleteReplaceNum = true
        }

        selectImgDialog?.setSelectImgListener({
            // 相册
            PictureSelector.create(context as Activity)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(num)
                .setPictureUIStyle(uiStyle)
                .isMaxSelectEnabledMask(true)
                .isCompress(true)
                .selectionData(mSelectedImgList)
                .forResult(TKResultCallback(BaseConstant.TYPE_PHOTO))
        }, {
            // 拍照
            if (mSelectedImgList.size >= 9) {
//                Toast.makeText(context, context.getString(R.string.hw_img_limit), Toast.LENGTH_SHORT).show()
                ToastUtils.showShortTop(context.getString(R.string.hw_img_limit))
                return@setSelectImgListener
            }

            PictureSelector.create(context as Activity)
                .openCamera(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(num)
                .setPictureUIStyle(uiStyle)
                .isMaxSelectEnabledMask(true)
                .isCompress(true)
                .selectionData(mSelectedImgList)
                .forResult(TKResultCallback(BaseConstant.TYPE_PHOTO))
        });
        selectImgDialog?.show()
    }

    // 拍照相机
    inner class TKResultCallback(var type: String) : OnResultCallbackListener<LocalMedia> {

        override fun onResult(result: List<LocalMedia>) {
            if (result != null && result.isNotEmpty()) {
                mPhotoSuccessListener(type, result)
            }
        }

        override fun onCancel() {

        }
    }


    // 根据提交方式 显示按钮 枚举备注: 0不限制反馈方式1图片2视频3录音
    fun setJobSubmitType(submitWay: String) {
        // 学生写作业没有网盘 语音
//        mOperateAudioLl.visibility = View.GONE
        mOperateNetDiskLl.visibility = View.GONE

        if (layoutRes == R.layout.layout_job_operate_view) {
            val params: LinearLayout.LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.marginStart = ConvertUtils.dp2px(40f)
            params.marginEnd = ConvertUtils.dp2px(40f)

            params.topMargin = ConvertUtils.dp2px(50f)

            this.layoutParams = params
        }

        when (submitWay) {
            "1" -> { // 图片
                mOperatePhotoLl.visibility = View.VISIBLE
                mOperateVideoLl.visibility = View.GONE
                mOperateAudioLl.visibility = View.GONE
            }

            "2" -> { // 视频
                mOperatePhotoLl.visibility = View.GONE
                mOperateVideoLl.visibility = View.VISIBLE
                mOperateAudioLl.visibility = View.GONE
            }

            "3" -> { //语音
                mOperatePhotoLl.visibility = View.GONE
                mOperateVideoLl.visibility = View.GONE
                mOperateAudioLl.visibility = View.VISIBLE
            }
        }
    }

    fun setJobLessonId(id: String) {
        this.mLessonId = id
    }


    fun setSelectedAudioNum(size: Int) {
        mAudioSelectNum = size
    }

    fun setSelectedImgNum(size: Int) {
        mImgSelectNum = size
    }

    fun setSelectedVideoNum(size: Int) {
        mVideoSelectNum = size
    }

    fun setSelectedDocNum(size: Int) {
        mDocSelectNum = size
    }

}
