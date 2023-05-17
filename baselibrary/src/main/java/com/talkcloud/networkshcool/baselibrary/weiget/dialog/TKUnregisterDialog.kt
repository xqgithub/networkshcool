package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.WindowManager
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import kotlinx.android.synthetic.main.dialog_unregister.*
import org.jetbrains.anko.toast

/**
 * Author  guoyw
 * Date    2021/10/12
 * Desc    注销
 */
class TKUnregisterDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.BottomDialog
) : Dialog(mContext, themeResId) {

    private var mContext: Context = mContext

    private val phone: String = AppPrefsUtil.getUnregisterPhone()

    init {
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        setContentView(R.layout.dialog_unregister)
        setCanceledOnTouchOutside(true)
        val window = window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }

    private fun initData() {

        mUnregisterPhoneTv.text = mContext.getString(R.string.unregister_result5, phone)
    }


    private fun initListener() {
        mCancelBtn2.setOnClickListener {
            dismiss()
        }
        mCancelBtn.setOnClickListener {
            dismiss()
        }

        mConfirmBtn.setOnClickListener {
            if (!ScreenTools.getInstance().isPad(mContext)) {
                val intent = Intent()
                intent.action = Intent.ACTION_DIAL
                intent.data = Uri.parse("tel:$phone")
                mContext.startActivity(intent)
            } else {
                ToastUtils.showShortTop(context.getString(R.string.unregister_pad_desc, phone))
//                mContext.toast("请使用手机拨打$phone, 联系客服")
            }
        }
    }


}