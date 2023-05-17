package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout.LayoutParams
import android.widget.RadioButton
import com.luck.picture.lib.entity.LocalMedia
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseTakePhotoActivity2
import com.talkcloud.networkshcool.baselibrary.presenters.FeedbackPresenter
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.FeedbackView
import com.talkcloud.networkshcool.baselibrary.weiget.DefaultTextWatcher
import com.talkcloud.networkshcool.baselibrary.weiget.TKDelImage
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    意见反馈
 */
class FeedbackActivity : BaseTakePhotoActivity2<FeedbackPresenter>(), FeedbackView {

    private var mImgList: MutableList<String> = mutableListOf()

    private var mFbContent: String = ""
    private var mFbLabel: String = ""

    private val mPresenter: FeedbackPresenter by lazy { FeedbackPresenter(this, this) }

    override fun getLayoutId(): Int {
//        TKDensityUtil.setDensity(this, MyApplication.myApplication)
        return R.layout.activity_feedback
    }


    override fun initUiView() {
    }


    override fun initData() {
//        mPresenter = FeedbackPresenter(this, this)
    }


    override fun initListener() {

        mFbContentEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    mFbSubmitBtn.setBtnEnable(false)
                } else {
                    mFbSubmitBtn.setBtnEnable(true)
                }
            }
        })

        // 图片选择
        mFbImageQuestionRl.setOnClickListener {
//            showAlertView(MyConstant.PARAMS_TAKE_PHOTO_NORMAL)
            showSelectImgAlertView(6)
        }

        // 分类标签
        mFbRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val checkedRadioBtn = findViewById<RadioButton>(checkedId)
            mFbLabel = checkedRadioBtn.text.toString().trim()
        }

        // 提交
        mFbSubmitBtn.setOnClickListener {
            mFbContent = mFbContentEt.text.toString().trim()
            if (TextUtils.isEmpty(mFbContent)) {
//                toast("意见内容不能为空, 请输入后提交")
                ToastUtils.showShortTop("意见内容不能为空, 请输入后提交")
                return@setOnClickListener
            }

            // 提交意见
            mPresenter.requestSuggestion(mFbContent, mFbLabel, mImgList)

        }

    }


    override fun takeSuccess(result: List<LocalMedia>) {

        if (result != null && result.isNotEmpty()) {

            result.forEach {

                mImgList.add(it.realPath)

                var index = mImgList.size - 1;

                val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

                layoutParams.marginEnd = ConvertUtils.dp2px(12f)

                if(!ScreenTools.getInstance().isPad(this)) {
                    // 手机一排最后的一个不加
                    Log.d("goyw", " mImgList.size ：" + mImgList.size)

                    if (mImgList.size == 3 || mImgList.size == 6) {
                        layoutParams.marginEnd = ConvertUtils.dp2px(0f)
                    } else {
                        layoutParams.marginEnd = ConvertUtils.dp2px(12f)
                    }
                }

                layoutParams.bottomMargin = ConvertUtils.dp2px(12f)


                val delImg = TKDelImage(this)
                delImg.setImageUrl(it.realPath)
                delImg.layoutParams = layoutParams


                delImg.setDelImgListener {

                    mFbImageContainerFbl.removeViewAt(mImgList.indexOf(delImg.getImageUrl()))
                    mImgList.remove(delImg.getImageUrl())

                    index = mImgList.size - 1;

                    showAddImgBtn(mImgList.size)
                }

                mFbImageContainerFbl.addView(delImg, index)

                // 控制添加图片按钮是否显示
                showAddImgBtn(mImgList.size)
            }
        }

    }

    private fun showAddImgBtn(size: Int) {
        if (size >= 6) {
            mFbImageQuestionRl.visibility = View.GONE
        } else {
            mFbImageQuestionRl.visibility = View.VISIBLE
         }
    }

    // 显示成功提示
    override fun hideSuccessLoadingShowTips() {
        mLoadingDialog.hideSuccessLoadingShowTips {
            finish()
        }
    }


    override fun handlerSuccess() {
        // 处理成功之后
        mFbSubmitBtn.setBtnEnable(false)
    }


}