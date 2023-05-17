package com.talkcloud.networkshcool.baselibrary.utils

import android.webkit.JavascriptInterface
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.weiget.GraphicVerificationView

/**
 * Date:2021/10/12
 * Time:15:45
 * author:joker
 */
class JsInterface constructor(var graphicverificationlistener: GraphicVerificationView.GraphicVerificationListener) {

    /**
     * 1.定义JS需要调用的方法
     * 2.被JS调用的方法必须加入@JavascriptInterface注解
     */
    @JavascriptInterface
    fun graphicVerificationResults(msg: Int, ticket: String, randstr: String) {
//        LogUtils.i(
//            ConfigConstants.TAG_ALL, "msg =-= $msg",
//            "ticket =-= $ticket",
//            "randstr =-= $randstr"
//        )
        graphicverificationlistener.jsReturnResults(msg, ticket, randstr)
    }
}