package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.luck.picture.lib.broadcast.BroadcastManager
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.isAudio
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil
import kotlinx.android.synthetic.main.layout_job_comment_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/29
 * Desc    点评区域
 */
class TKJobCommentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var isEditable: Boolean = false

    // 拍照成功回调
    private var mMoreClickListener: () -> Unit = {}


    private var audioEntity: AudioEntinty? = null


    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TKJobCommentView)

        isEditable = typedArray.getBoolean(R.styleable.TKJobCommentView_isCommentEditable, true)

        typedArray.recycle()

        initView()

        initListener()
    }

    /*
        初始化视图
     */
    private fun initView() {

        View.inflate(context, R.layout.layout_job_comment_view, this)

        setIsEditable(isEditable)
    }


    private fun initListener() {

        mJobCommentMore.setOnClickListener {
            mMoreClickListener()
        }
    }

    fun setOnMoreClickListener(moreClickListener: () -> Unit = {}) {
        this.mMoreClickListener = moreClickListener
    }

    // 设置是否可编辑
    private fun setIsEditable(isEditable: Boolean) {
        if (isEditable) {
            mJobCommentMore.visibility = View.VISIBLE
        } else {
            mJobCommentMore.visibility = View.GONE
        }
    }


    // 设置评价数据
    fun setCommentData(remark: HomeworkStudentDetailEntity.Remark?) {

        remark?.let {
            mJobCommentRbView.isEnabled = false
            mJobCommentRbView.setStarCount2(remark.rank + 1)

            /**
             * 枚举: 3,2,1,0
             * 枚举备注: 3优秀 2良好 1中等 0不合格
             */
            var rankStr = ""
            when (remark.rank) {
                0 -> {
                    rankStr = context.getString(R.string.flunk) + "+" + 0
                }
                1 -> {
                    rankStr = context.getString(R.string.secondary) + "+" + 1
                }
                2 -> {
                    rankStr = context.getString(R.string.good) + "+" + 2
                }
                3 -> {
                    rankStr = context.getString(R.string.excellent) + "+" + 3
                }
            }
            mJobCommentTv.text = rankStr

            GlideUtils.loadHeaderImg(context, remark.teacher?.avatar, mJobCommentHeadImgIv)
            mJobCommentUsernameTv.text = remark.teacher?.name
            mJobCommentTimeTv.text = StringUtil.stampToMonth(remark.remark_time.toLong())

            mJobCommentContentTv.text = remark.content

            if (remark.resources != null && remark.resources.isNotEmpty()) {

                // TODO 评论带图片
                mJobCommentMediaView.setIsEditable(false)
                mJobCommentMediaView.setResourceData(remark.resources)
                mJobCommentMediaView.visibility = View.VISIBLE

//                remark.resources.forEach {
//
//                    if(it.url.isAudio()) {
//
//                        audioEntity = AudioEntinty().apply {
//                            localFilePath = it.url
//                            duration = (it.duration * 1000).toLong()
//                        }
//                        mJobCommentAudioView.setJobAudioData(audioEntity!!)
//                        mJobCommentAudioView.setIsEditable(false)
//                        mJobCommentAudioView.visibility = View.VISIBLE
//                    }
//                }


            } else {
                mJobCommentAudioView.visibility = View.GONE
                mJobCommentMediaView.visibility = View.GONE
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        BroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, BaseConstant.ACTION_PLAY_AUDIO)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        BroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver, BaseConstant.ACTION_PLAY_AUDIO)

    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
//            NSLog.d(" action  : $action")
            if (TextUtils.isEmpty(action)) {
                return
            }
            val extras = intent.extras

            when (action) {

                BaseConstant.ACTION_PLAY_AUDIO -> {

                    if (extras != null) {

                        if (mJobCommentAudioView.visibility == View.VISIBLE) {
                            val url = extras.getString(BaseConstant.EXTRA_PLAY_AUDIO_URL)
//                            NSLog.d(" url : $url")

                            audioEntity?.let {
                                if (it != AudioEntinty().apply { localFilePath = url }) {
                                    mJobCommentAudioView.closeAudio()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
