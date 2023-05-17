package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.luck.picture.lib.broadcast.BroadcastManager
import com.luck.picture.lib.tools.DateUtils
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.TKMediaUtil
import com.talkcloud.networkshcool.baselibrary.utils.audiohelp.AudioPlayManager
import kotlinx.android.synthetic.main.layout_job_audio_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/16
 * Desc    作业音频
 */
class TKJobAudioView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 是否正在播放
    var isPlaying: Boolean = false

    private var mAudio: AudioEntinty? = null

    private var mPosition: Int = 0

    private lateinit var audioPlayManager: AudioPlayManager

    private var delAudio: (audio: AudioEntinty) -> Unit = {}

    private var playAudio: (audio: AudioEntinty) -> Unit = {}

    init {
        initView()

        initData()

        initListener()
    }


    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_job_audio_view, this)
    }


    private fun initData() {
        // TODO 目前先创建的时候 创建对象
        audioPlayManager = AudioPlayManager()
    }

    private fun initListener() {

        mJobAudioPlayIv.setOnClickListener {

//            NSLog.d("localFilePath:  " + mAudio?.localFilePath + "  status: " + ConfigConstants.AUDIO_STOP)

            if (!isPlaying) {
                isPlaying = true
                mJobAudioPlayIv.setImageResource(R.drawable.ic_job_audio_pause)

                // 正在播放回调
                mAudio?.let { audio -> playAudio(audio) }

                val bundle = Bundle()
                bundle.putString(BaseConstant.EXTRA_PLAY_AUDIO_URL, mAudio?.localFilePath)
                BroadcastManager.getInstance(context)
                    .action(BaseConstant.ACTION_PLAY_AUDIO)
                    .extras(bundle).broadcast()

                if (audioPlayManager.audiO_PLAY_STATUS == ConfigConstants.AUDIO_STOP) {
                    audioPlayManager.initMediaPlayer(mAudio?.localFilePath)
                }

                if (audioPlayManager.audiO_PLAY_STATUS == ConfigConstants.AUDIO_PAUSE) {
                    audioPlayManager.start()
                }

            } else {
                isPlaying = false
                mJobAudioPlayIv.setImageResource(R.drawable.ic_job_audio_play)

                audioPlayManager.pause()
            }

        }

        mJobAudioDelIv.setOnClickListener {

            closeAudio()

            mAudio?.let { audio -> delAudio(audio) }

            // 删除通知用户更新
//            val bundle = Bundle()
//            bundle.putString(BaseConstant.EXTRA_DELETE_AUDIO_URL, mAudio?.localFilePath)
//            BroadcastManager.getInstance(context)
//                .action(BaseConstant.ACTION_DELETE_AUDIO)
//                .extras(bundle).broadcast()
        }
    }

    fun setJobAudioData(audio: AudioEntinty) {
        this.mAudio = audio
//        NSLog.d("audio : $audio")

        if (audio.duration == 0L) {
//            mJobAudioDurationIv.visibility = View.GONE
            TKMediaUtil.showMediaTime(context, audio.localFilePath, mJobAudioDurationIv)
        } else {
//            mJobAudioDurationIv.visibility = View.VISIBLE
            mJobAudioDurationIv.text = DateUtils.formatDurationTime(audio.duration)
        }



        audioPlayManager.setOnAudioManagerListener(object : AudioPlayManager.AudioManagerListener {

            override fun updateTimeStatus(currenttime: Int, totalTime: Int) {
                mJobAudioProgress.max = totalTime
                mJobAudioProgress.progress = currenttime
            }

            override fun changePlayStatus() {
                NSLog.d(" status : " + audioPlayManager.audiO_PLAY_STATUS)
                if (audioPlayManager.audiO_PLAY_STATUS == ConfigConstants.AUDIO_STOP) {

                    // 播放完成
                    isPlaying = false
                    mJobAudioPlayIv.setImageResource(R.drawable.ic_job_audio_play)

                    mJobAudioProgress.progress = 0
                }
            }

        })
    }

    fun setCurrentPosition(i: Int) {
        this.mPosition = i
    }

    fun setIsEditable(editable: Boolean) {

        if (editable) {
            mJobAudioDelIv.visibility = View.VISIBLE
        } else {
            mJobAudioDelIv.visibility = View.GONE
        }
    }


    /**
     * 录音删除按钮点击事件
     */
    fun jobAudioDel(listener: OnClickListener) {
        mJobAudioDelIv.setOnClickListener(listener)
    }


    /**
     * 关闭回放录音文件
     */
    fun closeAudio() {
        if (isPlaying) {
            audioPlayManager.clear()
        }
    }

    // 设置删除回调
    fun setDelAudioListener(delAudio: (audio: AudioEntinty) -> Unit) {
        this.delAudio = delAudio
    }


    // 播放录音
    fun setPlayAudioListener(playAudio: (audio: AudioEntinty) -> Unit) {
        this.playAudio = playAudio
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        closeAudio()
    }
}
