package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity
import kotlinx.android.synthetic.main.layout_job_common_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/11
 * Desc    作业公用区域 包含顶部标题内容 中部媒体 底部折叠
 */
class TKJobCommonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // 是否可编辑 老师发作业可编辑 ， 学生写作业不可编辑
    private var isEditable: Boolean = true

    // 展开
    private var isOpen: Boolean = true

    // 是否显示展开 折叠
//    private var isShow: Boolean = false


    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TKJobCommonView)

        isEditable = typedArray.getBoolean(R.styleable.TKJobCommonView_isEditable, true)

        typedArray.recycle()


        initView()

        initListener()
    }

    private fun initView() {
        View.inflate(context, R.layout.layout_job_common_view, this)

        setJobCommonStatus()
    }

    // 设置是否可编辑
    fun setIsEditable(isEditable: Boolean) {
        this.isEditable = isEditable

        setJobCommonStatus()
    }


    private fun setJobCommonStatus() {

        mJobContentView.setIsEditable(isEditable)
        mJobMediaView.setIsEditable(isEditable)

    }



    private fun initListener() {

        mJobFoldView.setOnClickListener {

            if (isOpen) {
                isOpen = false

                mJobMediaView.visibility = View.GONE

            } else {
                isOpen = true

                mJobMediaView.visibility = View.VISIBLE
            }

            mJobFoldView.setJobCommonFold(isOpen)
        }
    }


    // 设置数据
    fun setCommonData(entity: TKHomeworkDetailEntity) {

        if (entity.title.isNotEmpty()) {
            mJobContentView.setJobTitle(entity.title)
            mJobContentView.setJobContent(entity.content)
        } else {
            mJobContentView.setJobLongContent(entity.content)
        }

        // TODO 设置折叠按钮是否显示
        if (isEditable) {
            mJobFoldView.visibility = View.GONE
        } else {
            mJobMediaView.setShowFoldViewListener { isShow ->
                mJobFoldView.visibility = if(isShow) View.VISIBLE else View.GONE
            }
        }


        // 设置资源
        if (entity.resources != null && entity.resources.isNotEmpty()) {
            mJobMediaView.visibility = View.VISIBLE
            mJobMediaView.setResourceData(entity.resources)
        } else {
//            if(!isEditable) {
//                mJobMediaView.visibility = View.GONE
//            } else {
//                mJobMediaView.visibility = View.VISIBLE
//            }
        }

        if (entity.create_time.isNotEmpty() && !isEditable) {
            mJobMediaView.setPublishJobTime(entity.create_time)
        }
    }


    // 设置折叠是否显示 学生写作业 不显示展开折叠 // TODO 需要在 setCommonData 之后调用才生效
    fun setShowFold(isShow: Boolean) {
//        this.isShow = isShow

        if (isShow) {
            mJobFoldView.visibility = View.VISIBLE
        } else {
            mJobFoldView.visibility = View.GONE
        }
    }

    // 获取资源
    fun getMediaList(): MutableList<TKHomeworkResourceEntity> {
        return mJobMediaView.getMediaList()
    }


    // 获取资源 和 时长
//    fun getMediaUrlDurationList(): MutableMap<String, MutableList<*>> {
//        return mJobMediaView.getMediaUrlDurationList()
//    }

    // 获取作业标题
    fun getJobTitle(): String {
        return mJobContentView.getJobTitle()
    }

    // 获取作业内容
    fun getJobContent(): String {
        return mJobContentView.getJobContent()
    }

    // 获取学生写作业
    fun getJobLongContent(): String {
        return mJobContentView.getJobLongContent()
    }


    // 按钮状态回调
    fun setOnBtnEnableListener(btnEnableListener: (isEnable: Boolean) -> Unit) {
        mJobContentView.setOnBtnEnableListener(btnEnableListener)
    }

    // 学生写作业 隐藏标题
    fun setJobHintTitle(b: Boolean) {
        mJobContentView.setJobHintTitle(b)
    }

    // 设置学生写作业 提交方式
    fun setJobSubmitType(submitWay: String) {
        mJobMediaView.setJobSubmitType(submitWay)
    }

    // TODO 这样一层层传 好麻烦 后期优化
    fun setJobLessonId(id: String) {
        mJobMediaView.setJobLessonId(id)
    }
}
