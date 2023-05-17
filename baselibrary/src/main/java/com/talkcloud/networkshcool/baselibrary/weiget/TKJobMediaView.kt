package com.talkcloud.networkshcool.baselibrary.weiget

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.broadcast.BroadcastAction
import com.luck.picture.lib.broadcast.BroadcastManager
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.style.PictureParameterStyle
import com.luck.picture.lib.tools.JumpUtils
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.toNumber
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity
import com.talkcloud.networkshcool.baselibrary.ui.activities.BrowserActivity
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.GlideEngine
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.TKLocalMedia
import kotlinx.android.synthetic.main.layout_job_ext_item_view.view.*
import kotlinx.android.synthetic.main.layout_job_media_view.view.*
import java.util.*


/**
 * Author  guoyw
 * Date    2021/6/11
 * Desc    作业媒体区域
 */
class TKJobMediaView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    // 是否可编辑 老师发作业可编辑 ， 学生写作业不可编辑
    private var isEditable: Boolean = true

    private var mImgList: MutableList<LocalMedia> = mutableListOf()
    // 网盘图片
    private var mNetDataImgList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()


    private var mVideoList: MutableList<LocalMedia> = mutableListOf()
    // 网盘视频
    private var mNetDataVideoList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()


    private var mAudioList: MutableList<AudioEntinty> = mutableListOf()
    // 网盘音频
    private var mNetDataAudioList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()


    private var mNetDataList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()
    // 网盘文档
    private var mNetDataDocList: MutableList<NetworkDiskEntity.DataBean> = mutableListOf()

    private var mPictureParameterStyle: PictureParameterStyle? = null


    // 预览的文件类型
    private var mineType: Int = -1

    // 是否展开
//    private var isFold: Boolean = true

    private var showFoldView: (isShow: Boolean) -> Unit = {}

    init {

        initView()

        initListener()
    }

    private fun initView() {
        View.inflate(context, R.layout.layout_job_media_view, this)

        setJobMediaStatus()

    }


    private fun initListener() {

        mJobOperateView.setAudioClickListener {
            val count = mJobAudioContainerLl.childCount;
            if (count > 0) {
                for (index in 0 until count) {
                    var tkJobAudioView: TKJobAudioView = mJobAudioContainerLl.getChildAt(index) as TKJobAudioView
                    // 关闭播放录音
                    tkJobAudioView.closeAudio()
                }
            }
        }

        // 录音 成功
        mJobOperateView.setAudioSuccessListener { audio ->

            showLocalAudio(audio)

        }

        // 网盘成功回调
        mJobOperateView.setNetDiskSuccessListener { result ->

//            NSLog.d("result 网盘 : $result")
//            NSLog.d("mNetDataList 网盘 : $mNetDataList")

            if (result != null && result.isNotEmpty()) {

                mNetDataImgList.forEach {

                    var index = mImgList.size - 1

                    while (index >= 0) {

                        if (mImgList[index].id == it.id.toNumber()) {

                            mJobImageContainerLl.removeViewAt(index)
                            mImgList.remove(mImgList[index])

                            mJobOperateView.setSelectedImgNum(mImgList.size)

                        }
                        index--
                    }
                }


                mNetDataVideoList.forEach {

                    var index = mVideoList.size - 1

                    while (index >= 0) {

                        if (mVideoList[index].id == it.id.toNumber()) {

                            mJobVideoContainerLl.removeViewAt(index)
                            mVideoList.remove(mVideoList[index])

                            mJobOperateView.setSelectedVideoNum(mVideoList.size)

                        }

                        index--
                    }
                }

                mNetDataAudioList.forEach {

                    var index = mAudioList.size - 1

                    while (index >= 0) {

                        if (mAudioList[index].id == it.id) {

                            NSLog.d("audio position : $index")

                            mJobAudioContainerLl.removeViewAt(index)
                            mAudioList.remove(mAudioList[index])

                            mJobOperateView.setSelectedAudioNum(mAudioList.size)

                        }

                        index--
                    }
                }
            }


            mNetDataImgList.clear()
            mNetDataVideoList.clear()
            mNetDataAudioList.clear()
            mNetDataDocList.clear()

            mNetDataList.clear()
            mJobExtContainerLl.removeAllViews()

            result?.forEach {

                if (mNetDataList.contains(it)) {
                    return@forEach
                }
                it.source = it.source
                showLocalNetData(it)
            }

        }

        // 拍照回调
        mJobOperateView.setOnPhotoSuccessListener { type, result ->

//            NSLog.d("result : $result  type : $type")
            when (type) {
                BaseConstant.TYPE_PHOTO -> {
                    mJobImageContainerLl.removeAllViews()
                    mImgList.clear()
                }

                BaseConstant.TYPE_VIDEO -> {
                    mJobVideoContainerLl.removeAllViews()
                    mVideoList.clear()
                }
            }


            result.forEach {

                if (mImgList.contains(it)) {
                    return@forEach
                }

                if (mVideoList.contains(it)) {
                    return@forEach
                }

                showLocalMedia(it)
            }
        }
    }


    // 显示网盘数据 TODO 分类展示
    private fun showLocalNetData(netData: NetworkDiskEntity.DataBean) {

//        NSLog.d("showLocalNetData: $netData")

        mNetDataList.add(netData)

        val type = netData.type.toLowerCase(Locale.getDefault())

        // 文档
        if (type == "xls" || type == "xlsx" ||
            type == "ppt" || type == "pptx" ||
            type == "doc" || type == "docx" ||
            type == "txt" || type == "pdf" || type == "zip"
        ) {

            mNetDataDocList.add(netData)

            val extItemView = TKJobExtItemView(context)
            extItemView.setJobExtData(netData)
            // 设置是否是编辑模式
            extItemView.setIsEditable(isEditable)

            mJobExtContainerLl.addView(extItemView)

            mJobExtContainerLl.visibility = VISIBLE
            mJobExtRootLl.visibility = VISIBLE

            // 设置文档的数量
            mJobOperateView.setSelectedDocNum(mNetDataDocList.size)

        } else if (type == "jpg" || type == "gif" || type == "jpeg"
            || type == "png" || type == "bmp" || type == "webp"
        ) {
            // 图片
            val media = TKLocalMedia()
            media.path = netData.downloadpath
            media.realPath = netData.downloadpath
            media.isCompressed = true
            media.compressPath = netData.downloadpath
            media.mimeType = "image/$type"

            media.source = "2"

            media.id = netData.id.toNumber()

            mNetDataImgList.add(netData)

            showLocalMedia(media as LocalMedia)
        } else if (type == "mp4" || type == "avi" || type == "mov") {
            // 图片
            val media = TKLocalMedia()
            media.path = netData.downloadpath
            media.realPath = netData.downloadpath
            media.isCompressed = true
            media.compressPath = netData.downloadpath
            media.duration = 0L
            media.mimeType = "video/$type"

            media.source = "2"
            media.id = netData.id.toNumber()

            mNetDataVideoList.add(netData)

            showLocalMedia(media as LocalMedia)
        } else if (type == "mp3" || type == "aac" || type == "wav" || type == "amr") {
            // 音频 一般为 mp3 aac    wav/amr用不到
            val audioMedia = AudioEntinty()
            audioMedia.localFilePath = netData.downloadpath
            //  暂时
            audioMedia.duration = 0L

            audioMedia.source = "2"
            audioMedia.id = netData.id

            mNetDataAudioList.add(netData)

            showLocalAudio(audioMedia)
        }

        mJobOperateView.setSelectedNetDataList(mNetDataList)

        mJobExtLabel.text = String.format(context.getString(R.string.hw_attachment), mNetDataDocList.size)
        // 没有了 隐藏全部
        if (mNetDataDocList.size == 0) {
            mJobExtRootLl.visibility = View.GONE
        }
    }


    // 显示音频
    private fun showLocalAudio(audio: AudioEntinty) {

        mAudioList.add(audio)

        val audioView = TKJobAudioView(context)
        audioView.setJobAudioData(audio)
        audioView.setCurrentPosition(mAudioList.size - 1)

        // TODO 隐藏音频删除按钮
        audioView.setIsEditable(isEditable)

        // 音频在当前页面 使用回调
        audioView.setDelAudioListener { audio ->

//            NSLog.d("audio： $audio")

            mJobAudioContainerLl.removeViewAt(mAudioList.indexOf(audio))
            mAudioList.remove(audio)

            mJobOperateView.setSelectedAudioNum(mAudioList.size)

            delNetData(audio.id)
        }

        // 播放当前录音 关闭其他录音
        audioView.setPlayAudioListener { audio ->
            val count = mJobAudioContainerLl.childCount;
            if (count > 0) {
                val playIndex = mAudioList.indexOf(audio)
                for (index in 0 until count) {
                    if (index != playIndex) {
                        val tkJobAudioView: TKJobAudioView = mJobAudioContainerLl.getChildAt(index) as TKJobAudioView
                        tkJobAudioView.closeAudio()
                    }
                }
            }
        }


        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.bottomMargin = ConvertUtils.dp2px(15f)
        audioView.layoutParams = layoutParams

        mJobAudioContainerLl.addView(audioView)

        mJobAudioContainerLl.visibility = View.VISIBLE

        // 设置录音的数量
        mJobOperateView.setSelectedAudioNum(mAudioList.size)
    }


    // 显示图片视频
    private fun showLocalMedia(localMedia: LocalMedia) {

//        NSLog.d(" localMedia id :" + localMedia.id)

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        layoutParams.marginEnd = ConvertUtils.dp2px(12f)

        val videoImg = TKVideoImage(context)
        videoImg.setLocalMedia(localMedia)
        videoImg.layoutParams = layoutParams

        videoImg.setOnItemClickListener { media ->
            // 打开预览页面
            openImgPreview(media)
        }

        // 使用两个LinearLayout 保证视频在图片前面
        if (PictureConfig.TYPE_VIDEO == PictureMimeType.getMimeType(localMedia.mimeType)) {
            mVideoList.add(localMedia)
            mJobVideoContainerLl.addView(videoImg)

            mJobOperateView.setSelectedVideoNum(mVideoList.size)
        } else if (PictureConfig.TYPE_IMAGE == PictureMimeType.getMimeType(localMedia.mimeType)) {
            // 图片类型
            mImgList.add(localMedia)
            mJobImageContainerLl.addView(videoImg)

            mJobOperateView.setSelectedImgNum(mImgList.size)
        }

        mJobMediaContainerLl.visibility = View.VISIBLE

        // 设置已选的图片
        mJobOperateView.setSelectedImgList(mImgList, mVideoList)
    }


    private fun openImgPreview(media: LocalMedia) {

        if (mPictureParameterStyle == null) {
            mPictureParameterStyle = PictureParameterStyle.ofSelectTotalStyle()
            mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = isEditable
            // TODO 预览样式 后期优化
//            // 相册返回箭头
//            mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.picture_icon_back_arrow
//            // 外部预览界面是否显示删除按钮
//            mPictureParameterStyle!!.pictureExternalPreviewGonePreviewDelete = true
//            // 外部预览界面删除按钮样式
//            mPictureParameterStyle!!.pictureExternalPreviewDeleteStyle = R.drawable.icon_trash_del
//            // 自定义相册预览文字大小
//            mPictureParameterStyle!!.picturePreviewTextSize = 16

        }

        mPictureParameterStyle!!.pictureLeftBackIcon = R.drawable.picture_icon_back_arrow

        mineType = PictureMimeType.getMimeType(media.mimeType)

        when (mineType) {
            PictureConfig.TYPE_VIDEO -> {
                // 预览视频
//                PictureSelector.create(context as Activity)
//                    .setPictureStyle(mPictureParameterStyle)
//                    .selectionMode(PictureConfig.MULTIPLE)
//                    .externalPictureVideo(if (TextUtils.isEmpty(media.androidQToPath)) media.path else media.androidQToPath)


                //  需要和下面的预览图片合并么？？
                val position = mVideoList.indexOf(media)

                NSLog.d("position : $position")

                PictureSelector.create(context as Activity)
                    .setPictureStyle(mPictureParameterStyle)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .bindCustomPlayVideoCallback {

//                        NSLog.d("123 : " + (it as LocalMedia).path)

                        // 播放4k视频 有黑屏 ，so使用webview 预览视频
                        val path = (it as LocalMedia).path

                        if (!(path.endsWith(".mov") || path.endsWith(".MOV"))) {

                            val intent = Intent(context, BrowserActivity::class.java)
                            intent.putExtra("url", path)
                            intent.putExtra("isShowTitle", false)
                            context.startActivity(intent)
                        } else {
                            val intent = Intent()
                            val bundle = Bundle()
                            bundle.putString(PictureConfig.EXTRA_VIDEO_PATH, path)
                            intent.putExtras(bundle)
                            PictureSelectionConfig.style.pictureLeftBackIcon = R.drawable.picture_icon_back
                            JumpUtils.startPictureVideoPlayActivity(context, bundle, PictureConfig.PREVIEW_VIDEO_CODE)
                        }
                    }
                    .openExternalPreview(position, mVideoList)
            }

            PictureConfig.TYPE_IMAGE -> {

                val position = mImgList.indexOf(media)

                NSLog.d("position : $position")

                PictureSelector.create(context as Activity)
                    .setPictureStyle(mPictureParameterStyle)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .openExternalPreview(position, mImgList)
            }
        }
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
//            NSLog.d(" action  : $action")
            if (TextUtils.isEmpty(action)) {
                return
            }
            // 外部预览删除按钮回调
            val extras = intent.extras

            when (action) {

                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION -> {
                    if (extras != null) {

                        val position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION)

                        if (mineType == PictureConfig.TYPE_IMAGE) {

                            mJobImageContainerLl.removeViewAt(position)
                            val delMedia = mImgList[position]
                            mImgList.remove(delMedia)

                            mJobOperateView.setSelectedImgNum(mImgList.size)

                            // 删除网盘里的数据
                            delNetData(delMedia.id.toString())


                        } else if (mineType == PictureConfig.TYPE_VIDEO) {
                            mJobVideoContainerLl.removeViewAt(position)
                            val delMedia = mVideoList[position]
                            mVideoList.remove(delMedia)

                            mJobOperateView.setSelectedVideoNum(mVideoList.size)

                            delNetData(delMedia.id.toString())
                        }
                    }
                }

                // 删除网盘
                BaseConstant.ACTION_DELETE_EXT -> {

                    if (extras != null) {

                        val delId = extras.getString(BaseConstant.EXTRA_DELETE_EXT_ID)

                        val deleteData = NetworkDiskEntity.DataBean(delId)
                        val index = mNetDataDocList.indexOf(deleteData)

                        NSLog.d("url : $delId   index : $index")

                        mJobExtContainerLl.removeViewAt(index)

                        mNetDataDocList.remove(deleteData)

                        delNetData(delId)

                        mJobExtLabel.text = String.format(context.getString(R.string.hw_attachment), mNetDataDocList.size)
                        // 没有了 隐藏全部
                        if (mNetDataDocList.size == 0) {
                            mJobExtRootLl.visibility = View.GONE
                        }
                    }
                }


                BaseConstant.ACTION_PLAY_AUDIO -> {

                    if (extras != null) {

                        val count = mJobAudioContainerLl.childCount;
                        if (count > 0) {
                            val url = extras.getString(BaseConstant.EXTRA_PLAY_AUDIO_URL)
//                        NSLog.d(" url : $url")

                            val playIndex = mAudioList.indexOf(AudioEntinty().apply { localFilePath = url })
//                            NSLog.d(" playIndex : $playIndex")
                            for (index in 0 until count) {
                                if (index != playIndex) {
                                    val tkJobAudioView: TKJobAudioView = mJobAudioContainerLl.getChildAt(index) as TKJobAudioView
                                    tkJobAudioView.closeAudio()
                                }
                            }
                        }

//                        mJobAudioContainerLl.removeViewAt(index)
//                        mAudioList.removeAt(index)
//
//                        mJobOperateView.setSelectedAudioNum(mAudioList.size)
                    }
                }

            }
        }
    }

    // 删除网盘中的数据
    private fun delNetData(delId: String?) {
        delId?.let { delId ->
            val netDataImg = NetworkDiskEntity.DataBean().apply {
                id = delId
            }
            // 如果有是从网盘选择的 删除掉
            if (mNetDataList.contains(netDataImg)) {
                mNetDataList.remove(netDataImg)
                mJobOperateView.setSelectedNetDataList(mNetDataList)
            }
        }
    }

    // 设置是否可编辑
    fun setIsEditable(isEditable: Boolean) {
        this.isEditable = isEditable

        setJobMediaStatus()
    }

    // 设置作业内容的状态 是否可以编辑
    private fun setJobMediaStatus() {

        if (isEditable) {
//            mJobPublishTimeTv.visibility = View.GONE
            mJobOperateView.visibility = View.VISIBLE

        } else {
//            mJobPublishTimeTv.visibility = View.VISIBLE
            mJobOperateView.visibility = View.GONE
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        NSLog.d("onAttachedToWindow : onAttachedToWindow")

        // 注册广播
        if (isEditable) {
            BroadcastManager.getInstance(context).registerReceiver(
                broadcastReceiver,
                BroadcastAction.ACTION_DELETE_PREVIEW_POSITION,
//                BaseConstant.ACTION_DELETE_AUDIO,
                BaseConstant.ACTION_DELETE_EXT
            )
        }
        BroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, BaseConstant.ACTION_PLAY_AUDIO)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        NSLog.d("onDetachedFromWindow : onDetachedFromWindow")

        if (isEditable) {
            BroadcastManager.getInstance(context)
                .unregisterReceiver(
                    broadcastReceiver,
                    BroadcastAction.ACTION_DELETE_PREVIEW_POSITION,
//                    BaseConstant.ACTION_DELETE_AUDIO,
                    BaseConstant.ACTION_DELETE_EXT
                )
        }

        BroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver, BaseConstant.ACTION_PLAY_AUDIO)

    }


    // 获取媒体
    fun getMediaList(): MutableList<TKHomeworkResourceEntity> {

        val mediaList = mutableListOf<TKHomeworkResourceEntity>()

        mVideoList.forEach {
            val resourceEntity = TKHomeworkResourceEntity().apply {
                id = it.id.toString()
                url = it.realPath
                duration = (it.duration / 1000).toInt()
//                NSLog.d("it.size: " + it.size)
//                if(it.size > 10) {
//                    Toast.makeText(context, "视频超过了限制", Toast.LENGTH_SHORT).show()
//                    return@forEach
//                }
            }
            mediaList.add(resourceEntity)
        }


        mImgList.forEach {
            val resourceEntity = TKHomeworkResourceEntity().apply {
                id = it.id.toString()
                // 防止compressPath 为空的情况
                url = if (!TextUtils.isEmpty(it.compressPath)) {
                    it.compressPath
                } else {
                    it.realPath
                }

                if(mNetDataImgList.contains(NetworkDiskEntity.DataBean().apply {
                    id = it.id.toString() })) {
                    source = "2"
                }

                duration = (it.duration / 1000).toInt()
            }
            mediaList.add(resourceEntity)
        }

        mAudioList.forEach {
            val resourceEntity = TKHomeworkResourceEntity().apply {
                id = it.id
                url = it.localFilePath
                duration = (it.duration / 1000).toInt()
            }
            mediaList.add(resourceEntity)
        }

        //
        NSLog.d("mNetDataDocList :" + mNetDataDocList)
        mNetDataDocList.forEach {
            val resourceEntity = TKHomeworkResourceEntity().apply {
                id = it.id
                url = it.downloadpath
                source = it.source
            }
            mediaList.add(resourceEntity)
        }

        return mediaList
    }


    // 获取媒体url duration
    fun getMediaUrlDurationList(): MutableMap<String, MutableList<*>> {

        // 媒体资源
        val mediaList = mutableListOf<String>()
        // 时长
        val durationList = mutableListOf<Long>()

        mVideoList.forEach {
            mediaList.add(it.realPath)
            // 接口需要传秒 不能毫秒 接口真他妈垃圾
            durationList.add(it.duration / 1000)
        }

        mImgList.forEach {
            mediaList.add(it.compressPath)
            durationList.add(0)
        }

        mAudioList.forEach {
            mediaList.add(it.localFilePath)
            // 接口需要传秒 不能毫秒 接口真他妈垃圾
            durationList.add(it.duration / 1000)
        }

        val map = mutableMapOf<String, MutableList<*>>()
        map["url"] = mediaList
        map["duration"] = durationList

        return map

    }


    // 根据提交方式 显示按钮 枚举备注: 0不限制反馈方式1图片2视频3录音
    fun setJobSubmitType(submitWay: String) {
        mJobOperateView.setJobSubmitType(submitWay)
    }


    // 发布时间
    fun setPublishJobTime(time: String) {
        mJobPublishTimeTv.visibility = View.VISIBLE
        // 判断格式
        val timeStr = StringUtil.stampToMonth(time.toLong())
        mJobPublishTimeTv.text = "$timeStr ${context.getString(R.string.publish)}"
    }


    fun getMimeType(mimeType: String): Int {
        if (TextUtils.isEmpty(mimeType)) {
            return PictureConfig.TYPE_IMAGE
        }
        return if (mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_VIDEO)) {
            PictureConfig.TYPE_VIDEO
        } else if (mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_AUDIO)) {
            PictureConfig.TYPE_AUDIO
        } else if (mimeType.startsWith(PictureMimeType.MIME_TYPE_PREFIX_IMAGE)) {
            PictureConfig.TYPE_IMAGE
        } else {
            4
        }
    }


    // 设置网络资源
    fun setResourceData(resourceList: List<TKHomeworkResourceEntity>) {

        // 先清空上次资源
        mVideoList.clear()
        mJobVideoContainerLl.removeAllViews()
        mImgList.clear()
        mJobImageContainerLl.removeAllViews()
        mAudioList.clear()
        mJobAudioContainerLl.removeAllViews()
        mNetDataDocList.clear()
        mNetDataList.clear()
        mJobExtContainerLl.removeAllViews()

        resourceList.forEach {

//            NSLog.d("it : " + it)

            if ("1" == it.source) {
                /// 表示本地上传的文件

                val mimeType: String = PictureMimeType.getMimeTypeFromMediaContentUri(context, Uri.parse(it.url))

//                NSLog.d("mimeType :$mimeType")

//                NSLog.d("mimeType :" + PictureMimeType.getMimeType(mimeType))
                when (getMimeType(mimeType)) {

                    PictureConfig.TYPE_VIDEO,
                    PictureConfig.TYPE_IMAGE -> {

                        val media = TKLocalMedia()
                        media.path = it.url
                        media.realPath = it.url
                        media.isCompressed = true
                        media.compressPath = it.url
                        media.duration = (it.duration * 1000).toLong()
                        media.mimeType = mimeType

                        media.id = it.id.toNumber()

                        showLocalMedia(media as LocalMedia)

                    }

                    // 音频
                    PictureConfig.TYPE_AUDIO -> {

                        val audioMedia = AudioEntinty()
                        audioMedia.localFilePath = it.url
                        //  暂时
                        audioMedia.duration = (it.duration * 1000).toLong()

                        audioMedia.id = it.id

                        showLocalAudio(audioMedia)
                    }

                    else -> {
                        val netData = NetworkDiskEntity.DataBean()
                        netData.id = it.id
                        netData.name = it.name
                        netData.size = it.size
                        netData.type = it.type
                        netData.preview_url = it.preview_url
                        netData.downloadpath = it.url
                        netData.source = "1"

                        showLocalNetData(netData)
                    }

                }
            } else if ("2" == it.source) {
                /// 网盘数据
                val netData = NetworkDiskEntity.DataBean()
                netData.id = it.id
                netData.name = it.name
                netData.size = it.size
                netData.type = it.type
                netData.preview_url = it.preview_url
                netData.downloadpath = it.url
                netData.source = "2"

                showLocalNetData(netData)
            }
        }

        val imgFlag: Int = if (mVideoList.isNotEmpty() || mImgList.isNotEmpty()) 1 else 0
        val audioFlag = if (mAudioList.isNotEmpty()) 1 else 0
        val netDataFlag = if (mNetDataList.isNotEmpty()) 1 else 0

        showFoldView((imgFlag + audioFlag + netDataFlag) >= 2)
    }


    // 根据资源内容设置是否显示Fold
    fun setShowFoldViewListener(showFoldView: (isShow: Boolean) -> Unit) {
        this.showFoldView = showFoldView
    }

    fun setJobLessonId(id: String) {
        mJobOperateView.setJobLessonId(id)
    }

}
