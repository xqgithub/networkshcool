package com.talkcloud.networkshcool.baselibrary.weiget

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity
import com.talkcloud.networkshcool.baselibrary.entity.ResourseEntity
import com.talkcloud.networkshcool.baselibrary.ui.activities.BrowserActivity
import kotlinx.android.synthetic.main.layout_job_ext_item_view.view.*


/**
 * Author  guoyw
 * Date    2021/6/16
 * Desc    作业附件
 */
class TKJobExtItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mEditable: Boolean = false

    private var mNetData: NetworkDiskEntity.DataBean? = null

    init {
        initView()

        initListener()
    }

    /*
        初始化视图
     */
    private fun initView() {
        View.inflate(context, R.layout.layout_job_ext_item_view, this)
    }

    private fun initListener() {
        mJobExtRootRl.setOnClickListener {
            val intent = Intent(context, BrowserActivity::class.java)

            var url = mNetData?.preview_url.toString()

            if (TextUtils.isEmpty(url)) {
                ToastUtils.showShortToastFromText(resources.getString(R.string.datagenerate), ToastUtils.top)
                return@setOnClickListener
            }
            intent.putExtra("url", url)
            intent.putExtra("isShowDelete", mEditable)
            if(!mEditable) {
                intent.putExtra(BaseConstant.KEY_PARAM1, DownloadEntity(mNetData?.name, mNetData?.downloadpath, mNetData?.size))
            }

            // 保证删除的时候 唯一
            intent.putExtra("id", mNetData?.id)

            context.startActivity(intent)
        }
    }

    fun setJobExtData(data: NetworkDiskEntity.DataBean) {

        mNetData = data

        if (data.name != null) {
            mJobExtNameTv.text = data.name
        }
        if (data.size != null) {
            mJobExtSizeTv.text = data.size
        }
        if (data.type != null) {
            val type: String = data.type
            if (type == "jpg" || type == "gif" || type == "jpeg" || type == "png" || type == "bmp") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_img)
            } else if (type == "xls" || type == "xlsx") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_excel)
            } else if (type == "ppt" || type == "pptx") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_ppt)
            } else if (type == "doc" || type == "docx") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_word)
            } else if (type == "txt") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_txt)
            } else if (type == "pdf") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_pdf)
            } else if (type == "mp3") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_mp3)
            } else if (type == "mp4") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_mp4)
            } else if (type == "zip") {
                mJobExtTypeIv.setImageResource(R.mipmap.ic_zip)
            }
        } else {
            mJobExtTypeIv.setImageResource(R.mipmap.ic_unknown)
        }
    }



    fun setIsEditable(editable: Boolean) {
        this.mEditable = editable;
    }
}
