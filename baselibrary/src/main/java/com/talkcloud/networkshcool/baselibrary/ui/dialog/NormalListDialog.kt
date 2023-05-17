package com.talkcloud.networkshcool.baselibrary.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.LessonFilesEntity
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import kotlinx.android.synthetic.main.dialog_select_common.*

/**
 * Date:2021/9/13
 * Time:10:21
 * author:joker
 * 普通列表弹框
 */
class NormalListDialog @JvmOverloads constructor(
    var mContext: Context, themeResId: Int = R.style.dialog_style,
    var width: Int = mContext.resources.getDimensionPixelSize(R.dimen.dimen_375x), var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var animationsResId: Int = R.style.AnimBottom
) : Dialog(mContext, themeResId) {


    private var onItemClick: (Parameter: Any) -> Unit = { _: Any -> }

    init {
        initUIView()
        initListener()
    }

    private fun initUIView() {
        setContentView(R.layout.dialog_select_common)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    private fun initListener() {
        mCancelBtn.setOnClickListener {
            dismissDialog()
        }
    }


    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        window!!.setWindowAnimations(animationsResId) //添加动画
        layoutParams.width = width
        layoutParams.height = height
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }


    //设置按钮内容
    fun <T> setItemContentList(mItemList: List<T>) {
        mItemList.forEachIndexed { index, t ->
            val mItemTv: TextView = View.inflate(context, R.layout.dialog_select_common_item, null) as TextView
            if (t is LessonFilesEntity) {//课节课件
                mItemTv.text = t.filename
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
                    onItemClick(t)
                    dismissDialog()
                }
            }
        }
    }

    fun setSelectItemClickListener(itemClick: (Parameter: Any) -> Unit) {
        this.onItemClick = itemClick
    }


}