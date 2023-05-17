package com.talkcloud.networkshcool.baselibrary.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.talkcloud.networkshcool.baselibrary.R

/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
object GlideUtils {

    fun loadRoundImage(context: Context, url: String, imageView: ImageView, r: Float = 15f) {
        // 当fragment或者activity失去焦点或者destroyed的时候，Glide会自动停止加载相关资源
        if (isDestroy(context as Activity)) {
            return
        }
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .transform(GlideRoundTransform(ConvertUtils.dp2px(r))) //圆角

        Glide.with(context).load(url).apply(options).into(imageView)
    }

    @JvmStatic
    fun loadImageFitCenter(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).fitCenter().into(imageView)
    }

    @JvmStatic
    fun loadHeaderImg(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
//            .placeholder(R.drawable.icon_default_head_img)
            .error(R.drawable.icon_default_head_img)
            .fitCenter().into(imageView)
    }


    private fun isDestroy(activity: Activity): Boolean {
        return activity == null || activity.isFinishing || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed)
    }


}

