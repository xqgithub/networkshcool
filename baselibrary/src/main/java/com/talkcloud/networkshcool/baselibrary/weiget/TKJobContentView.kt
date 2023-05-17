package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.talkcloud.networkshcool.baselibrary.R
import kotlinx.android.synthetic.main.layout_job_content_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    作业内容区
 */
class TKJobContentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    // 标题是否不为空
    private var isJobTitleNotEmpty = false

    // 内容是否不为空
    private var isJobContentNotEmpty = false

    // 学生写作业 内容是否不为空
    private var isJobLongContentNotEmpty = false

    private var btnEnableListener: (isEnable: Boolean) -> Unit = {}

    // 是否可编辑 老师发作业可编辑 ， 学生写作业不可编辑
    private var isEditable: Boolean = true

    init {

        initView()

        initListener()
    }

    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_job_content_view, this)

        setJobContentStatus()
    }

    private fun initListener() {

        mJobContentEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {

                mJobContentLimitTv.text = "${s.toString().length}/200"

                isJobContentNotEmpty = !TextUtils.isEmpty(s.toString())

                setBtnIsEnable()
            }
        })

        mJobTitleEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)

                isJobTitleNotEmpty = !TextUtils.isEmpty(s.toString())

                setBtnIsEnable()
            }
        })

        mJobContentLongEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)

                isJobLongContentNotEmpty = !TextUtils.isEmpty(s.toString())

                setBtnIsEnable()
            }
        })
    }

    private fun setBtnIsEnable() {

//        btnEnableListener((isJobTitleNotEmpty && isJobContentNotEmpty) || isJobLongContentNotEmpty)

        btnEnableListener(isJobTitleNotEmpty)
    }


    // 设置是否可编辑
    fun setIsEditable(isEditable: Boolean) {
        this.isEditable = isEditable

        setJobContentStatus()
    }


    // 设置是否可编辑
    private fun setJobContentStatus() {

        mJobTitleEt.isEnabled = isEditable
        mJobContentEt.isEnabled = isEditable

        // 学生写作业
        mJobContentLongEt.isEnabled = isEditable

        if (isEditable) {
            mJobContentLimitRl.visibility = View.VISIBLE
            mJobContentEt.minLines = 3

            mJobContentLongEt.minLines = 3
        } else {
            mJobContentLimitRl.visibility = View.GONE
            mJobContentEt.minLines = 1

            mJobTitleEt.hint = ""
            mJobContentEt.hint = ""

            mJobContentLongEt.minLines = 1
            mJobContentLongEt.hint = ""
        }

    }

    // 成功回调
    fun setOnBtnEnableListener(btnEnableListener: (isEnable: Boolean) -> Unit) {
        this.btnEnableListener = btnEnableListener
    }

    // 获取标题
    fun getJobTitle(): String {
        return mJobTitleEt.text.toString().trim()
    }

    // 获取内容
    fun getJobContent(): String {
        return mJobContentEt.text.toString().trim()
    }

    // 获取学生写内容
    fun getJobLongContent(): String {
        return mJobContentLongEt.text.toString().trim()
    }

    // 设置标题
    fun setJobTitle(title: String) {
        mJobTitleEt.setText(title)
    }

    // 设置内容
    fun setJobContent(content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mJobContentEt.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT))
        } else {
            mJobContentEt.setText(Html.fromHtml(content))
        }
    }


    // 设置 学生的内容
    fun setJobLongContent(content: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mJobContentLongEt.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT))
        } else {
            mJobContentLongEt.setText(Html.fromHtml(content))
        }
    }

    // 学生写作业 隐藏标题
    fun setJobHintTitle(b: Boolean) {
        if (b) {
            mJobContentLl.visibility = View.GONE
            mJobContentLongEt.visibility = View.VISIBLE
        } else {
            mJobContentLl.visibility = View.VISIBLE
            mJobContentLongEt.visibility = View.GONE
        }
    }

}
