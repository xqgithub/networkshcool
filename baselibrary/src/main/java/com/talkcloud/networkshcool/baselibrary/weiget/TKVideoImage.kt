package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.tools.DateUtils
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import com.talkcloud.networkshcool.baselibrary.utils.TKMediaUtil
import kotlinx.android.synthetic.main.layout_job_audio_view.view.*
import kotlinx.android.synthetic.main.layout_video_img.view.*
import java.util.concurrent.TimeUnit

/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc    视频缩略图
 */
class TKVideoImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mImgUrl: String = ""
    private var mMedia: LocalMedia? = null

    private var itemClickListener: (media: LocalMedia) -> Unit = {}

    init {
        initView()
        initListener()
    }


    private fun initView() {
        View.inflate(context, R.layout.layout_video_img, this)
    }


    private fun initListener() {

        mVideoRootRl.setOnClickListener {
            if (mMedia != null) {
                itemClickListener(mMedia!!)
            }

            // 视频
//            if (PictureMimeType.ofVideo() == mMedia.chooseModel) {
//                // 预览视频
//                PictureSelector.create(context)
//                    .themeStyle(R.style.picture_default_style)
////                    .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
//                    .externalPictureVideo(if (TextUtils.isEmpty(mMedia.getAndroidQToPath())) mMedia.getPath() else mMedia.getAndroidQToPath())
//
//            } else {
//
//                // 预览图片 可自定长按保存路径
////                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
////                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
////                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
//                PictureSelector.create(context)
//                    .themeStyle(R.style.picture_default_style) // xml设置主题
////                    .setPictureStyle(mPictureParameterStyle) // 动态自定义相册主题
//                    //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
//                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
//                    .isNotPreviewDownload(true) // 预览图片长按是否可以下载
//                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
//                    .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
//                    .openExternalPreview(position, selectList)
//            }
        }
    }


    fun setLocalMedia(media: LocalMedia) {
        this.mMedia = media;
        if (PictureConfig.TYPE_VIDEO == PictureMimeType.getMimeType(media.mimeType)) {
            mVideoPlayIv.visibility = View.VISIBLE
            mVideoDurationTv.visibility = View.VISIBLE


            if (media.duration == 0L) {
//                mVideoDurationTv.visibility = View.GONE
                TKMediaUtil.showMediaTime(context, media.path, mVideoDurationTv)
            } else {
//                mVideoDurationTv.visibility = View.VISIBLE
                val showTime = DateUtils.formatDurationTime(media.duration)
                mVideoDurationTv.text = showTime
            }


//            NSLog.d(" TimeUnit.MILLISECONDS.toSeconds(it.duration) : " +  TimeUnit.MILLISECONDS.toSeconds(media.duration))
//            NSLog.d(" media.duration : " +  media.duration)


        } else {
            mVideoPlayIv.visibility = View.GONE
            mVideoDurationTv.visibility = View.GONE
        }

//        NSLog.d("compressPath :" + media.compressPath)
        setImageUrl(media.realPath)
    }

    // 设置显示图片
    private fun setImageUrl(src: String) {
        this.mImgUrl = src
        GlideUtils.loadRoundImage(context, src, mVideoImgIv)
    }


    fun getImageUrl(): String {
        return this.mImgUrl
    }

    // 设置删除回调
    fun setOnItemClickListener(itemClickListener: (media: LocalMedia) -> Unit) {
        this.itemClickListener = itemClickListener
    }

}
