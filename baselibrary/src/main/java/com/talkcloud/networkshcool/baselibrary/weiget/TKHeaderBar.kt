package com.talkcloud.networkshcool.baselibrary.weiget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.talkcloud.networkshcool.baselibrary.R
import kotlinx.android.synthetic.main.layout_header_bar.view.*


/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    v1:左则图标 中间标题
 */
class TKHeaderBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //是否显示"返回"图标
    private var isShowBack = true

    //左侧返回按钮的图标
    private var leftSrc: Int = 0
    //Title文字
    private var titleText:String? = null

    // 右边的文字
    private var rightText:String? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar)

        isShowBack = typedArray.getBoolean(R.styleable.HeaderBar_isShowBack,true)

        leftSrc = typedArray.getResourceId(R.styleable.HeaderBar_leftSrc, R.mipmap.vcodeinput_return)

        titleText = typedArray.getString(R.styleable.HeaderBar_titleText)

        // TODO 目前右边主要显示文字 如果后期有图片 在扩展
        rightText = typedArray.getString(R.styleable.HeaderBar_rightText)

        typedArray.recycle()

        initView()
    }

    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context,R.layout.layout_header_bar,this)

        mHeaderBarLeftIv.visibility = if (isShowBack) View.VISIBLE else View.GONE

        //标题不为空，设置值
        titleText?.let {
            mHeaderBarTitleTv.text = it
        }

        mHeaderBarLeftIv.setImageResource(leftSrc)

        //返回图标默认实现（关闭Activity）
        mHeaderBarLeftIv.setOnClickListener {
            if (context is Activity){
                (context as Activity).finish()
            }
        }

        // 右边区域 比如存草稿
        mHeaderBarRightTv.visibility = View.GONE
        rightText?.let {
            mHeaderBarRightTv.visibility = View.VISIBLE
            mHeaderBarRightTv.text = it
        }

    }

    /*
        获取右侧视图
     */
    fun getRightView(): TextView {
        return mHeaderBarRightTv
    }
}
