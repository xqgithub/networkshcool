package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity
import com.talkcloud.networkshcool.baselibrary.help.TKShareHelper.shareLink
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil.Companion.getUserName
import kotlinx.android.synthetic.main.layout_share_bottom_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    作业 操作区域
 */
class TKShareBottomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    private var downloadEntity: DownloadEntity? = null

    // 音频成功回调
    private var mPrintListener: () -> Unit = {}


    init {
        initView()

        initData()

        initListener()
    }

    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_share_bottom_view, this)
    }


    private fun initData() {
    }


    private fun initListener() {
        mBottomPrintLl.setOnClickListener {
            mPrintListener()
        }

        mBottomShareLl.setOnClickListener {

            val title = getUserName() + context.getString(R.string.share_text) + downloadEntity?.fileName + context.getString(R.string.share_enclosure_text)
            val desc: String = context.getString(R.string.share_desc_text) + downloadEntity?.downloadPath

            shareLink(context, "$title。$desc")

        }
    }


    // 设置数据
    fun setDownloadInfo(entity: DownloadEntity?) {
        this.downloadEntity = entity
    }


    fun setOnPrintListener(printListener: () -> Unit = {}) {
        this.mPrintListener = printListener
    }


}
