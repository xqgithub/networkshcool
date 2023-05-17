package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import kotlinx.android.synthetic.main.layout_del_img.view.*

/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class TKDelImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mImgUrl: String = ""

    private var delImg: () -> Unit = {}

    init {
//        TKDensityUtil.setDensity(context as Activity, MyApplication.myApplication)
        initView()
        initListener()
    }


    private fun initView() {
        View.inflate(context, R.layout.layout_del_img, this)
    }

    private fun initListener() {
        mDelImgCloseIv.setOnClickListener {
            delImg()
        }
    }

    // 设置显示图片
    fun setImageUrl(src: String) {
        this.mImgUrl = src
        GlideUtils.loadRoundImage(context, src, mDelImgSrcIv)
    }

    fun getImageUrl(): String {
        return this.mImgUrl
    }

    // 设置删除回调
    fun setDelImgListener(delImg: () -> Unit) {
        this.delImg = delImg
    }
}
