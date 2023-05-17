package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.talkcloud.networkshcool.baselibrary.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Author  guoyw
 * Date    2021/5/19
 * Desc    TODO 状态过多， 后期优化
 */
class TKLoadingDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.BottomDialog
) : Dialog(mContext, themeResId) {

    private var handler = Handler(Looper.getMainLooper())

    private var back: () -> Unit = {}


    init {
        initView()
        initListener()
    }


    private fun initView() {
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(false)
        val window = window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }

    private fun initListener() {
        mTipBackBtn.setOnClickListener {
//            Log.d("goyw", " 返回 ${context is Activity}  ,, " +  context)
            hideLoading()
            if (context is Activity) {
                (context as Activity).finish()
            } else {
                back()
            }
        }
    }

    /*
        显示加载对话框
     */
    fun showLoading() {
        if (!super.isShowing()) {
            mLoadingContainerLl.visibility = View.VISIBLE
            mSuccessContainerLl.visibility = View.GONE
            mTipContainerLl.visibility = View.GONE
            mFailedContainerLl.visibility = View.GONE

            super.show()
        }
    }

    // 隐藏前显示成功
    fun hideSuccessLoading() {

        mFailedContainerLl.visibility = View.GONE
        mTipContainerLl.visibility = View.GONE
        mLoadingContainerLl.visibility = View.GONE
        mSuccessContainerLl.visibility = View.VISIBLE

        handler.postDelayed({
            super.dismiss()
        }, 800)
    }


    // 显示失败
    fun showFailed() {
        
        mTipContainerLl.visibility = View.GONE
        mLoadingContainerLl.visibility = View.GONE
        mSuccessContainerLl.visibility = View.GONE
        mFailedContainerLl.visibility = View.VISIBLE

        handler.postDelayed({
            super.dismiss()
        }, 800)
    }


    // 隐藏前显示成功
    fun hideSuccessLoadingShowTips(back: () -> Unit = {}) {

        this.back = back

        mFailedContainerLl.visibility = View.GONE
        mTipContainerLl.visibility = View.GONE
        mLoadingContainerLl.visibility = View.GONE
        mSuccessContainerLl.visibility = View.VISIBLE

        handler.postDelayed({
            mFailedContainerLl.visibility = View.GONE
            mLoadingContainerLl.visibility = View.GONE
            mSuccessContainerLl.visibility = View.GONE
            mTipContainerLl.visibility = View.VISIBLE

            // 按钮可用
            mTipBackBtn.setBtnEnable(true)
        }, 800)
    }

    /*
        隐藏加载对话框
     */
    fun hideLoading() {
        super.dismiss()
    }

}