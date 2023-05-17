package com.talkcloud.networkshcool.baselibrary.ui.fragment


import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpFragment
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.EventConstant
import com.talkcloud.networkshcool.baselibrary.entity.UserExtInfoEntity
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity
import com.talkcloud.networkshcool.baselibrary.presenters.MyPresenter
import com.talkcloud.networkshcool.baselibrary.ui.activities.*
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import com.talkcloud.networkshcool.baselibrary.views.MyView
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity


/**
 * Author  guoyw
 * Date    2021/5/13
 * Desc    我的页面
 */
class MyFragment : BaseMvpFragment<MyPresenter>(), MyView, View.OnClickListener {

    private val mPresenter: MyPresenter by lazy { MyPresenter(mActivity, this) }

    private var mUserInfo: UserInfoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_my
    }

    override fun onResume() {
        super.onResume()

        // TODO 编辑后 暂时这样处理 后期优化为事件
        val localUserName = AppPrefsUtil.getUserName()
        if (!TextUtils.isEmpty(localUserName)) {
            mUserNameTv.text = localUserName
        } else {
            mUserNameTv.text = MySPUtilsUser.getInstance().userMobile
        }


        val remoteHeaderUrl = AppPrefsUtil.getRemoteHeaderUrl()
        if (!TextUtils.isEmpty(remoteHeaderUrl)) {
            GlideUtils.loadHeaderImg(requireActivity(), remoteHeaderUrl, mHeadImgIv)
        }

        val localUserIdentity = AppPrefsUtil.getUserIdentity()
        var identity = getString(R.string.student_title)

        //目前这版本 不显示红花，无身份、有身份都一样
        if (!TextUtils.isEmpty(localUserIdentity)) {
            if (ConfigConstants.IDENTITY_TEACHER == localUserIdentity) {
                identity = getString(R.string.teacher_title)
                mPrizeContainerFl.visibility = View.GONE

                mUserRoleTv.text = identity
                mUserRoleSelectTv.text = identity
            } else {
                identity = getString(R.string.student_title)
                mPrizeContainerFl.visibility = View.GONE

                mUserRoleTv.text = identity
                mUserRoleSelectTv.text = identity
            }
        } else {
            mPrizeContainerFl.visibility = View.GONE
        }

    }

    override fun initView() {
    }

    override fun initData() {
        mPresenter.getUserExtInfo()
    }

    override fun initListener() {

        mFeedback.setOnClickListener(this)
        mHeadImgIv.setOnClickListener(this)
        mUserInfoLl.setOnClickListener(this)
        mCourse.setOnClickListener(this)

        mUserIdentityLl.setOnClickListener(this)

        mSettingIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.mFeedback -> {
                startActivity<FeedbackActivity>()
            }

            R.id.mHeadImgIv,
            R.id.mUserInfoLl -> {
                val username = mUserNameTv.text.toString().trim()
                val avatarUrl = AppPrefsUtil.getRemoteHeaderUrl()

                // 编辑只需要头像 姓名
                mUserInfo = UserInfoEntity(avatar = avatarUrl, username = username)

                startActivity<EditUserInfoActivity>(BaseConstant.KEY_PARAM1 to mUserInfo)
            }

            R.id.mCourse -> {
                startActivity<MyCourseActivity>()
            }

            R.id.mUserIdentityLl -> {
//                startActivity<ChooseIdentityActivity>(
//                    "current_identity" to AppPrefsUtil.getUserIdentity(),
//                    ConfigConstants.ACTIVITY_SPECIES to ConfigConstants.MYFRAGMENT
//                )

                //用户有多重身份，需要进入选择身份页面
                val bundle = Bundle()
                bundle.putString(ConfigConstants.ACTIVITY_SPECIES, ConfigConstants.MYFRAGMENT)
                bundle.putString("current_identity", AppPrefsUtil.getUserIdentity())
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, ChooseIdentityActivity::class.java, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold)
            }

            R.id.mSettingIv -> {
//                startActivity<PersonalSettingsActivity>()
                //跳转到个人设置页面
                PublicPracticalMethodFromJAVA.getInstance()
                    .intentToJump(activity, PersonalSettingsActivity::class.java, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold)
            }
        }
    }


    // 显示用户信息
//    override fun showUserInfo(userInfo: UserInfoEntity?) {
//
//        this.mUserInfo = userInfo
//
//        Log.d("goyw", "userinfo : $userInfo")
//
//        userInfo?.let { userInfo ->
//
//            GlideUtils.loadHeaderImg(mActivity, userInfo.avatar, mHeadImgIv)
//            mUserNameTv.text = userInfo.username
//        }
//    }

    // 显示额外信息
    override fun showUserExtInfo(userExtInfo: UserExtInfoEntity?) {
        userExtInfo?.let { userExtInfo ->

            val config = userExtInfo.config
            config?.let {
                AppPrefsUtil.saveUnregisterFlag(it.remove_account == 1)
                AppPrefsUtil.saveUnregisterPhone(it.phone)
            }

            // 课程
            mCourseNumTv.text = userExtInfo.courses

            // 奖杯
            mPrizeNumTv.text = userExtInfo.cups

            // 小红花
            mFlowerNumTv.text = userExtInfo.flowers

            // 有多个身份可以切换  国际化
            if (userExtInfo.identitys.size > 1) {
                mUserIdentityLl.visibility = View.VISIBLE
                mDivideView.visibility = View.VISIBLE
            } else {
                mUserIdentityLl.visibility = View.GONE
                mDivideView.visibility = View.GONE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    fun refreshData(messageEvent: MessageEvent) {
        when (messageEvent.message_type) {
            EventConstant.CHANGE_IDENTITY_REFRESH,
            EventConstant.MY_FLOWS_REFRESH -> {
                if (mPresenter != null) {
                    mPresenter.getUserExtInfo()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): MyFragment {
            return MyFragment()
        }
    }


}