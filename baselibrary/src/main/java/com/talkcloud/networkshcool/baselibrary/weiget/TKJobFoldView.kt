package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.talkcloud.networkshcool.baselibrary.R
import kotlinx.android.synthetic.main.layout_job_fold_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    作业折叠操作区
 */
class TKJobFoldView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // 是否可编辑 老师发作业可编辑 ， 学生写作业不可编辑
    private var isOpen: Boolean = true

    init {

        initView()

    }


    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_job_fold_view, this)

        // 默认展开？
        setJobCommonFold(true)
    }


//    private fun initListener() {
//
//        mJobOperateFoldLl.setOnClickListener {
//
//            setJobCommonFold()
//
//        }
//    }

    // 设置作业内容是否展开
    fun setJobCommonFold(isOpen: Boolean) {
        this.isOpen = isOpen

        if (isOpen) {
            mJobOperateFoldTv.text = context.getString(R.string.hw_fold)
            mJobOperateFoldIv.setImageResource(R.drawable.ic_job_fold_close)
        } else {
            mJobOperateFoldTv.text = context.getString(R.string.hw_unfold)
            mJobOperateFoldIv.setImageResource(R.drawable.ic_job_fold_open)
        }
    }


}
