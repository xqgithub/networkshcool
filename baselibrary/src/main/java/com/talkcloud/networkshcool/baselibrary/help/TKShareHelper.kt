package com.talkcloud.networkshcool.baselibrary.help

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.talkcloud.sharelibrary.TKShareApi2
import com.talkcloud.sharelibrary.model.TKShareLinkModel


/**
 * Author  guoyw
 * Date    2021/8/19
 * Desc    分享统一管理类 方便后续管理
 */
object TKShareHelper {

    // 与加载
    fun preInit(context: Context) {
        TKShareApi2.getInstance().preInit(context)
    }

    // 初始化
    fun init(context: Context) {
        TKShareApi2.getInstance().init(context)
    }

    // 回调
    fun onActResult(context: Context, requestCode: Int, resultCode: Int, data: Intent?) {
        TKShareApi2.getInstance().onActResult(context, requestCode, resultCode, data)
    }

    // 分享链接
    fun shareLink(context: Context, type: Int, linkModel: TKShareLinkModel) {
        TKShareApi2.getInstance().shareLink(context, type, linkModel)
    }

    // 分享图片
    fun shareImg(context: Context, type: Int, bitmap: Bitmap) {
        TKShareApi2.getInstance().shareImg(context, type, bitmap)
    }

    //判断是否安装了app
    fun isInstallApp(context: Context, type: Int): Boolean {
        return TKShareApi2.getInstance().isInstallApp(context, type)
    }


    // 系统分享
    fun shareLink(context: Context, shareStr: String) {
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND;
        shareIntent.type = "text/plain";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareStr)
        shareIntent = Intent.createChooser(shareIntent, "share");
        context.startActivity(shareIntent);
    }

}