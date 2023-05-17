package com.talkcloud.networkshcool.baselibrary.base

import android.os.Bundle
import com.talkcloud.networkshcool.baselibrary.presenters.BasePresenter
import com.talkcloud.networkshcool.baselibrary.views.IBaseView
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKLoadingDialog


/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc
 */
abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), IBaseView {

    private lateinit var mLoadingDialog: TKLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 加载框
        mLoadingDialog = TKLoadingDialog(mActivity)

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
}