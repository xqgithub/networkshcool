package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.talkcloud.networkshcool.baselibrary.R
import kotlinx.android.synthetic.main.dialog_select_img.*

/**
 * Author  guoyw
 * Date    2021/5/17
 * Desc
 */
class TKSelectImgDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.BottomDialog
) :
    Dialog(mContext, themeResId) {


    // 拍照
    private var takePhoto: () -> Unit = {}

    // 相册
    private var photoAlbum: () -> Unit = {}


    init {
        initView()
        initListener()
    }

    private fun initView() {
        setContentView(R.layout.dialog_select_img)
        setCanceledOnTouchOutside(true)
        val window = window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM
        window.attributes = params
    }


    private fun initListener() {
        mCancelBtn.setOnClickListener {
            dismiss()
        }

        mTakePhotoBtn.setOnClickListener {
            takePhoto()
            dismiss()
        }

        mPhotoBtn.setOnClickListener {
            photoAlbum()
            dismiss()
        }
    }

    fun setSelectImgListener(photoAlbum: () -> Unit, takePhoto: () -> Unit) {
        this.takePhoto = takePhoto
        this.photoAlbum = photoAlbum
    }


    // 设置按钮的内容
    fun setItemContentList(mPhotoList: MutableList<String>) {
        if (mPhotoList.size >= 2) {
            mPhotoBtn.text = mPhotoList[0]
            mTakePhotoBtn.text = mPhotoList[1]
        }
    }

}