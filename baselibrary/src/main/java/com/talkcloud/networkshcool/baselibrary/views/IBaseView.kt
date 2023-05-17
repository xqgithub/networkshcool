package com.talkcloud.networkshcool.baselibrary.views


/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 *          v2 改名为IBaseView 避免重名
 */
interface IBaseView {
    fun showLoading()
    fun hideLoading()

    fun hideSuccessLoading()
    fun showFailed()
}