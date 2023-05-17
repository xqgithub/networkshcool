package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.text.Editable
import android.text.TextUtils
import com.luck.picture.lib.entity.LocalMedia
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseTakePhotoActivity2
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.EventConstant
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity
import com.talkcloud.networkshcool.baselibrary.presenters.EditUserInfoPresenter
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.EditUserInfoView
import com.talkcloud.networkshcool.baselibrary.weiget.DefaultTextWatcher
import kotlinx.android.synthetic.main.activity_edit_userinfo.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    编辑用户资料
 */
class EditUserInfoActivity : BaseTakePhotoActivity2<EditUserInfoPresenter>(), EditUserInfoView {


    private var mImgList: MutableList<String> = mutableListOf()
    private var mUserName: String = ""

    private var mUserInfo: UserInfoEntity? = null

    // 是否修改了头像
    private var isFixHeaderImg = false;

    // 是否修改了姓名
    private var isFixName = false;

    // 头像本地地址
    private var localImgUrl = ""

    private val mPresenter: EditUserInfoPresenter by lazy { EditUserInfoPresenter(this, this) }

    override fun getLayoutId(): Int {
//        TKDensityUtil.setDensity(this, MyApplication.myApplication)
        return R.layout.activity_edit_userinfo
    }

    override fun initUiView() {
        //根据订单状态设置当前页面
        mUserInfo = intent.getParcelableExtra(BaseConstant.KEY_PARAM1)

        mUserInfo?.let {

            GlideUtils.loadHeaderImg(this, it.avatar, mUserHeaderIv)

            mUserNameEt.setText(it.username)
        }
    }


    override fun initData() {
        setBtnIsEnable()
    }


    override fun initListener() {
        mUserHeaderRl.setOnClickListener {
//            showAlertView(MyConstant.PARAMS_TAKE_PHOTO_CROP)
            showSelectHeadImgAlertView()
        }

        mUserNameEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {

//                isFixName = !TextUtils.isEmpty(s.toString())

                isFixName = s.toString().trim() != mUserInfo?.username

                setBtnIsEnable()
            }
        })

        mUserSaveBtn.setOnClickListener {

            mUserName = mUserNameEt.text.toString().trim()

            if (TextUtils.isEmpty(mUserName)) {
//                toast(R.string.edit_name_empty_desc)
                ToastUtils.showShortTop(getString(R.string.edit_name_empty_desc))
                return@setOnClickListener
            }
//
//            if (mUserName == mUserInfo?.username) {
//                toast("姓名和之前一样, 请输入后提交")
//                return@setOnClickListener
//            }

            // 提交意见
            mPresenter.saveUserInfo(mImgList, mUserName)
        }
    }


    override fun takeSuccess(result: List<LocalMedia>) {

        if(result != null && result.isNotEmpty()) {

            // 获取剪切的path
            localImgUrl = result[0].cutPath
            AppPrefsUtil.saveLocalHeaderUrl(localImgUrl)


            GlideUtils.loadImageFitCenter(this, localImgUrl, mUserHeaderIv)
            mImgList.add(localImgUrl)

            isFixHeaderImg = true

            setBtnIsEnable()
        }
    }

//    override fun takeSuccess(result: TResult) {
//
//        localImgUrl = result.image.originalPath
//        AppPrefsUtil.saveLocalHeaderUrl(localImgUrl)
//
//
//        GlideUtils.loadImageFitCenter(this, result.image.originalPath, mUserHeaderIv)
//        mImgList.add(result.image.originalPath)
//
//        isFixHeaderImg = true
//
//        setBtnIsEnable()
//
//    }

    // 设置按钮是否可点击
    private fun setBtnIsEnable() {
        mUserName = mUserNameEt.text.toString().trim()
        val isNotEmptyName = !TextUtils.isEmpty(mUserName)

        mUserSaveBtn.setBtnEnable(isNotEmptyName && (isFixName || isFixHeaderImg))
    }

    // 处理成功之后的逻辑
    override fun handlerSuccess() {
        mUserSaveBtn.setBtnEnable(false)
        // 通知 修改主页的头像
        AppPrefsUtil.saveUserName(mUserName)

        //发送事件
        EventBus.getDefault().post(MessageEvent(EventConstant.EVENT_EDIT_USER_INFO))


        mLoadingDialog.setOnDismissListener {
            finish()
        }
    }

}