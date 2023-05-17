package com.talkcloud.networkshcool.baselibrary.base

import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import com.talkcloud.networkshcool.baselibrary.presenters.BasePresenter
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.views.IBaseView
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKLoadingDialog

open abstract class BaseMvpActivity<T : BasePresenter<*>> : BaseActivity(), IBaseView {

    lateinit var mLoadingDialog: TKLoadingDialog

    private var mEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this)
        } else {
            ScreenTools.getInstance().setPortrait(this)
        }

        PublicPracticalMethodFromJAVA.getInstance().transparentStatusBar(this, true, true, android.R.color.white)

        // 加载框
        mLoadingDialog = TKLoadingDialog(this)

    }

    override fun onBeforeSetContentLayout() {
//        PublicPracticalMethodFromJAVA.getInstance()
//            .transparentStatusBar(
//                this,
//                true, true,
//                android.R.color.white
//            );
    }


    override fun showLoading() {
        mLoadingDialog.showLoading()
    }

    override fun hideLoading() {
        mLoadingDialog.hideLoading()
    }


    override fun hideSuccessLoading() {
        mLoadingDialog.hideSuccessLoading()
    }

    override fun showFailed() {
        mLoadingDialog.showFailed()
    }


    fun setEditText(editText: EditText) {
        this.mEditText = editText
    }

    // 点击空白出  隐藏键盘
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (mEditText != null) {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                val isHide = KeyBoardUtil.getInstance().isShouldHideKeyboard(v, ev)
                if (isHide) {
                    KeyBoardUtil.getInstance().hideKeyBoard(this, mEditText)
                } else {
                    KeyBoardUtil.getInstance().showKeyBoard(this, mEditText)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}