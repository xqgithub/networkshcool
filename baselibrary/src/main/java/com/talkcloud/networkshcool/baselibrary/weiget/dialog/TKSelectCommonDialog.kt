package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import kotlinx.android.synthetic.main.dialog_select_common.*
import kotlinx.android.synthetic.main.dialog_select_img.mCancelBtn

/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    通用选择器
 */
class TKSelectCommonDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.BottomDialog
) :
    Dialog(mContext, themeResId) {

    // 拍照
    private var onItemClick: (type: String, index: Int) -> Unit = { _: String, _: Int -> }

    init {
        initView()
        initListener()
    }

    private fun initView() {
        setContentView(R.layout.dialog_select_common)
        setCanceledOnTouchOutside(true)
        val window = window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }


    private fun initListener() {
        mCancelBtn.setOnClickListener {
            dismiss()
        }
    }


    // 设置按钮的内容
    fun setItemContentList(mItemList: MutableList<String>) {

        mItemList.forEachIndexed { index, s ->

            val mItemTv: TextView = View.inflate(context, R.layout.dialog_select_common_item, null) as TextView
            mItemTv.text = s

            mSelectContainerLl.addView(mItemTv)

            // 添加线
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(0.5f))
            val mLineView: View = View(context)
            mLineView.setBackgroundColor(ContextCompat.getColor(context, R.color.divider_line_color))
            mLineView.layoutParams = layoutParams

            // 最后一个不加
            if (index != mItemList.size - 1) {
                mSelectContainerLl.addView(mLineView)
            }

            // 设置点击回调
            mItemTv.setOnClickListener {
                onItemClick(s, index)
                dismiss()
            }
        }
    }

    fun setSelectItemClickListener(itemClick: (type: String, index: Int) -> Unit) {
        this.onItemClick = itemClick
    }
}