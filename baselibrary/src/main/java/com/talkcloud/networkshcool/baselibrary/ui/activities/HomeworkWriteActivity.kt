package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpActivity
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.HWConstant
import com.talkcloud.networkshcool.baselibrary.common.toNumber
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkWritePresenter
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil
import com.talkcloud.networkshcool.baselibrary.views.HomeworkWriteView
import com.talkcloud.networkshcool.baselibrary.weiget.DefaultTextWatcher
import kotlinx.android.synthetic.main.activity_write_job.*
import kotlinx.android.synthetic.main.layout_job_content_view.view.*

/**
 * Author  guoyw
 * Date    2021/6/11
 * Desc    学生写作业
 */
class HomeworkWriteActivity : BaseMvpActivity<HomeworkWritePresenter>(), HomeworkWriteView {

    private val mPresenter: HomeworkWritePresenter by lazy { HomeworkWritePresenter(this, this) }

    private var homeworkId: String = ""
    private var studentId: String = ""

    private var mHomework: HomeworkStudentDetailEntity? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_write_job
    }


    override fun initUiView() {

        mWriteCommonView.visibility = View.GONE

//        val localUserName = AppPrefsUtil.getUserName()
//        if (!TextUtils.isEmpty(localUserName)) {
//            mWriteUserNameTv.text = localUserName
//        } else {
//            mWriteUserNameTv.text = MySPUtilsUser.getInstance().userMobile
//        }
//
//
//        val remoteHeaderUrl = AppPrefsUtil.getRemoteHeaderUrl()
//        if (!TextUtils.isEmpty(remoteHeaderUrl)) {
//            GlideUtils.loadHeaderImg(this, remoteHeaderUrl, mWriteHeadImgIv)
//        }

        setEditText(mJobCommonView.mJobContentLongEt)

    }


    override fun initData() {

        if(intent.hasExtra(BaseConstant.KEY_PARAM1)) {
            homeworkId = intent.getStringExtra(BaseConstant.KEY_PARAM1)!!
        }

        if(intent.hasExtra(BaseConstant.KEY_PARAM2)) {
            studentId = intent.getStringExtra(BaseConstant.KEY_PARAM2)!!
        }


        NSLog.d("homeworkId :  $homeworkId  +  studentId : $studentId")

        if (!TextUtils.isEmpty(homeworkId) && !TextUtils.isEmpty(studentId)) {
            mPresenter.getHomeworkStudentDetails(homeworkId, studentId)
        }
    }

    override fun initListener() {

//        mWriteCommonView.setOnBtnEnableListener { enable ->
//
//            mWriteDraftTv.isEnabled = enable
//            mWriteSubmitBtn.setBtnEnable(enable)
//        }

        mWriteCommonView.mJobContentLongEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)

                val isNotEmpty = !TextUtils.isEmpty(s.toString())

                mWriteDraftTv.isEnabled = isNotEmpty
                mWriteSubmitBtn.setBtnEnable(isNotEmpty)
            }
        })

        // 草稿
        mWriteDraftTv.setOnClickListener {

            val isDraft = HWConstant.DraftStatus.IS_DRAFT

            val mediaList = mWriteCommonView.getMediaList()

            val map: MutableMap<String, Any> = HashMap()
            map["submit_content"] = mWriteCommonView.mJobContentLongEt.text.toString()
            map["is_draft"] = isDraft

            mPresenter.submitHomework(homeworkId, studentId, map, mediaList)

        }


        // 撤回并编辑
        mWriteEditLl.setOnClickListener {
            //TODO 先撤回
            mPresenter.rollbackHomeworkStudentDetails(homeworkId, studentId)
        }


        // 提交
        mWriteSubmitBtn.setOnClickListener {

            val isDraft = HWConstant.DraftStatus.IS_NOT_DRAFT

            val mediaList = mWriteCommonView.getMediaList()

            val map: MutableMap<String, Any> = HashMap()
            map["submit_content"] = mWriteCommonView.mJobContentLongEt.text.toString()
            map["is_draft"] = isDraft

            mPresenter.submitHomework(homeworkId, studentId, map, mediaList)

        }
    }


    // 学生端 显示作业详情
    override fun showHomeworkInfo(entity: HomeworkStudentDetailEntity?) {

        entity?.let {

            this.mHomework = entity

            val homeworkDetail = TKHomeworkDetailEntity.copyHomeworkDetailEntity2Student(it)
            mJobCommonView.setCommonData(homeworkDetail)

            mJobCommonView.visibility = View.VISIBLE
            mWriteContainerLl.visibility = View.VISIBLE

            // TODO 设置后台设置的学生信息
            mWriteUserNameTv.text = it.answer.student?.name
            GlideUtils.loadHeaderImg(this, it.answer.student?.avatar, mWriteHeadImgIv)

            //枚举备注: 0草稿 1未提交 2已提交 3已批阅
            when (it.status) {
                HWConstant.HWStatus.STATUS_DRAFT,
                HWConstant.HWStatus.STATUS_NOT_SUBMIT -> {

                    mWriteTipTv.text = getString(R.string.hw_write)
                    mWriteDraftTv.visibility = View.VISIBLE
                    mWriteEditLl.visibility = View.GONE
                    mWriteSubmitBtn.visibility = View.VISIBLE

                    mWriteCommonView.visibility = View.VISIBLE
                    mWriteCommonView.setJobSubmitType(it.submit_way)

                    mWriteCommonView.setJobHintTitle(true)
                    mWriteCommonView.setIsEditable(true)
//                    mWriteCommonView.setShowFold(false)

                    //TODO "0" 草稿 数据回显到 mWriteCommonView
                    mWriteCommonView.setCommonData(TKHomeworkDetailEntity.copyHomeworkDetailEntity2Answer(it.answer))
                }

                HWConstant.HWStatus.STATUS_SUBMIT -> {
                    mWriteTipTv.text = getString(R.string.myhomework)
                    it.answer.submit_time?.let { time ->
                        mWriteTimeTv.text = StringUtil.stampToMonth(time.toNumber())
                        mWriteTimeTv.visibility = View.VISIBLE
                    }

                    mWriteEditLl.visibility = View.VISIBLE

                    mWriteDraftTv.visibility = View.GONE
                    mWriteSubmitBtn.visibility = View.GONE

                    mWriteCommonView.visibility = View.VISIBLE
                    mWriteCommonView.setJobHintTitle(true)
                    mWriteCommonView.setIsEditable(false)

                    mWriteCommonView.setCommonData(TKHomeworkDetailEntity.copyHomeworkDetailEntity2Answer(it.answer))
                    mWriteCommonView.setShowFold(false)
                }

                HWConstant.HWStatus.STATUS_REVIEW -> {

                    mWriteTipTv.text = getString(R.string.myhomework)
                    it.answer.submit_time?.let { time ->
                        mWriteTimeTv.text = StringUtil.stampToMonth(time.toNumber())
                        mWriteTimeTv.visibility = View.VISIBLE
                    }

                    mWriteEditLl.visibility = View.GONE

                    mWriteDraftTv.visibility = View.GONE
                    mWriteSubmitBtn.visibility = View.GONE

                    mWriteCommonView.visibility = View.VISIBLE
                    mWriteCommonView.setJobHintTitle(true)
                    mWriteCommonView.setIsEditable(false)

                    mWriteCommonView.setCommonData(TKHomeworkDetailEntity.copyHomeworkDetailEntity2Answer(it.answer))
                    mWriteCommonView.setShowFold(false)

                    //TODO 显示老师点评
                    mWriteCommentTopSpaceView.visibility = View.VISIBLE
                    mWriteCommentView.visibility = View.VISIBLE
                    mWriteCommentView.setCommentData(it.remark)
                }
            }
        }
    }

    // 撤回成功
    override fun rollbackHomeworkSuccess() {
        // 将状态改为未提交
        mHomework?.status = HWConstant.HWStatus.STATUS_NOT_SUBMIT

        showHomeworkInfo(mHomework)
    }



    override fun submitHomeworkSuccess(homeworkId: String, isDraft: String?) {

        when (isDraft) {
            // 草稿
            HWConstant.DraftStatus.IS_DRAFT -> {
                mWriteSubmitBtn.setBtnEnable(false)
                mWriteDraftTv.isEnabled = false

                mLoadingDialog.setOnDismissListener {
                    finish()
                }
//                this.homeworkId = homeworkId
            }
            // 提交
            HWConstant.DraftStatus.IS_NOT_DRAFT -> {
                // 作业详情
                mPresenter.getHomeworkStudentDetails(homeworkId, studentId!!)
            }
        }
    }
}