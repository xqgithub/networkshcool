package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity
import kotlinx.android.synthetic.main.layout_preview_doc.view.*


/**
 * Author  guoyw
 * Date    2021/6/15
 * Desc    预览
 */
class TKDocPreView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {

        initView()

    }


    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_preview_doc, this)
    }


    // 设置作业内容是否展开
    fun setPreviewInfo(entity: DownloadEntity) {

        mPreviewNameTv.text = entity.fileName
        mPreviewSizeTv.text = context.getString(R.string.file_size_txt, entity.size)

        if (entity.downloadPath != null) {
            val type: String = entity.downloadPath!!

            if (type.endsWith("xls") || type.endsWith("xlsx")) {
                mPreviewIv.setImageResource(R.mipmap.ic_excel)
            } else if (type.endsWith("ppt") || type.endsWith("pptx")) {
                mPreviewIv.setImageResource(R.mipmap.ic_ppt)
            } else if (type.endsWith("doc") || type.endsWith("docx")) {
                mPreviewIv.setImageResource(R.mipmap.ic_word)
            } else if (type.endsWith("txt")) {
                mPreviewIv.setImageResource(R.mipmap.ic_txt)
            } else if (type.endsWith("pdf")) {
                mPreviewIv.setImageResource(R.mipmap.ic_pdf)
            } else if (type.endsWith("zip")) {
                mPreviewIv.setImageResource(R.mipmap.ic_zip)
            }
        } else {
            mPreviewIv.setImageResource(R.mipmap.ic_unknown)
        }
    }


}
