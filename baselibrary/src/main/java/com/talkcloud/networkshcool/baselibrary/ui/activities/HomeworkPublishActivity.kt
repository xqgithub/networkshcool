package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.text.TextUtils
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpActivity
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.EventConstant
import com.talkcloud.networkshcool.baselibrary.common.HWConstant
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkTeacherDetailEntity
import com.talkcloud.networkshcool.baselibrary.entity.StudentData
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity.Companion.copyHomeworkDetailEntity2Teacher
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkDetailPresenter
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkPublishPresenter
import com.talkcloud.networkshcool.baselibrary.utils.NSLog
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.HomeworkDetailView
import com.talkcloud.networkshcool.baselibrary.views.HomeworkPublishView
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectCommonDialog
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectStudentDialog
import kotlinx.android.synthetic.main.activity_publish_job.*
import kotlinx.android.synthetic.main.layout_job_content_view.view.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.StringBuilder
import java.util.*

/**
 * Author  guoyw
 * Date    2021/6/11
 * Desc    发布作业
 */
class HomeworkPublishActivity : BaseMvpActivity<HomeworkPublishPresenter>(), HomeworkPublishView, HomeworkDetailView {

    // 选择作业提交类型
    private var selectTypeDialog: TKSelectCommonDialog? = null

    // 默认值
    private var mTypeList: MutableList<String> = mutableListOf("不限制", "图片", "视频", "录音")


    private val mPresenter: HomeworkPublishPresenter by lazy { HomeworkPublishPresenter(this, this) }


    // 枚举备注: 0不限制反馈方式1图片2视频3录音 默认不限制
    private var submitWay: String = "0"


    private var lessonId: String? = null

    // 作业id 草稿编辑作业用
    private var homeworkId: String? = null


    //作业标题 默认
    private var mHwTitle: String? = null


    private var studentList: MutableList<StudentData> = mutableListOf()


    override fun getLayoutId(): Int {
        return R.layout.activity_publish_job
    }


    override fun initUiView() {
//        mJobCommonView.setShowFold(false)
        // 设置收起键盘edittext
        setEditText(mJobCommonView.mJobTitleEt)
        setEditText(mJobCommonView.mJobContentEt)

        // 默认可以点击
        mJobSubmitBtn.setBtnEnable(true)
        mHeaderBar.getRightView().isEnabled = true
    }


    override fun initData() {

        mTypeList = resources.getStringArray(R.array.hw_submit_way_arr).toMutableList()
        mJobSubmitTypeTv.text = mTypeList[0]


        mHwTitle = intent.getStringExtra(BaseConstant.KEY_PARAM3)

        mHwTitle?.let {
            mJobCommonView.mJobTitleEt.setText(it)
        }

        // 获取homeworkId
        homeworkId = intent.getStringExtra(BaseConstant.KEY_PARAM1)
        // 获取lessonId
        lessonId = intent.getStringExtra(BaseConstant.KEY_PARAM2)


        // 设置lessonId 网盘数据需要
        mJobCommonView.setJobLessonId(lessonId!!)

        // homeworkId 不为空 是草稿
        if (!TextUtils.isEmpty(homeworkId)) {
            val mHomeworkDetailPresenter = HomeworkDetailPresenter(this, this);
            mHomeworkDetailPresenter.getHomeworkTeacherDetails(homeworkId!!)
        } else {
            // 发布作业学生列表
            mPresenter.getLessonStudentList(lessonId!!)
        }


        NSLog.d("lessonId :  $lessonId  +  homeworkId : $homeworkId")
    }

    override fun initListener() {

        mJobSelectStudentLl.setOnClickListener {
//            mPresenter.getStudentList(lessonId!!)
            val dialog = TKSelectStudentDialog(this)
            dialog.setSelectedStudents(studentList).setLessonId(lessonId!!)
            dialog.setOnConfirmListener {
                handleLessonStudentInfo(it)
            }
            dialog.show()
        }


        // 提交
        mJobSubmitBtn.setOnClickListener {

            val isDraft = HWConstant.DraftStatus.IS_NOT_DRAFT

//            val mediaList = mJobCommonView.getMediaUrlDurationList()
            val mediaList: MutableList<TKHomeworkResourceEntity> = mJobCommonView.getMediaList()
            val jobContent = mJobCommonView.getJobContent()

            if (TextUtils.isEmpty(jobContent) && mediaList.size <= 0) {
//                toast(getString(R.string.hw_must_desc))
                ToastUtils.showShortTop(getString(R.string.hw_must_desc))
                return@setOnClickListener
            }

            val map: MutableMap<String, Any> = HashMap()
            map["title"] = mJobCommonView.getJobTitle()
            map["content"] = jobContent
            map["submit_way"] = submitWay
            map["is_draft"] = isDraft

            // 学生列表
            val studentIdList: MutableList<Int> = mutableListOf()
            studentList.forEach {
                studentIdList.add(it.id)
            }
            map["students"] = studentIdList

            if (!TextUtils.isEmpty(homeworkId)) {
                mPresenter.putEditHomework(homeworkId!!, lessonId!!, map, mediaList)
            } else {
                mPresenter.publishHomework(lessonId!!, map, mediaList)
            }

        }


        // 存草稿
        mHeaderBar.getRightView().setOnClickListener {

            val isDraft = HWConstant.DraftStatus.IS_DRAFT

            val mediaList: MutableList<TKHomeworkResourceEntity> = mJobCommonView.getMediaList()
            val jobContent = mJobCommonView.getJobContent()

            if (TextUtils.isEmpty(jobContent) && mediaList.size <= 0) {
//                toast(getString(R.string.hw_must_desc))
                ToastUtils.showShortTop(getString(R.string.hw_must_desc))
                return@setOnClickListener
            }

            val map: MutableMap<String, Any> = HashMap()
            map["title"] = mJobCommonView.getJobTitle()
            map["content"] = jobContent
            map["submit_way"] = submitWay
            map["is_draft"] = isDraft

            // 学生列表
            val studentIdList: MutableList<Int> = mutableListOf()
            studentList.forEach {
                studentIdList.add(it.id)
            }

            map["students"] = studentIdList

            if (!TextUtils.isEmpty(homeworkId)) {
                mPresenter.putEditHomework(homeworkId!!, lessonId!!, map, mediaList)
            } else {
                mPresenter.publishHomework(lessonId!!, map, mediaList)
            }

        }


        mJobSubmitTypeLl.setOnClickListener {

            if (selectTypeDialog == null) {
                selectTypeDialog = TKSelectCommonDialog(this)
                selectTypeDialog!!.setItemContentList(mTypeList)
            }

            selectTypeDialog!!.setSelectItemClickListener { s: String, i: Int ->
                mJobSubmitTypeTv.text = s
                submitWay = "$i"
            }

            selectTypeDialog!!.show()
        }


        mJobCommonView.setOnBtnEnableListener { enable ->
            mJobSubmitBtn.setBtnEnable(enable)
            mHeaderBar.getRightView().isEnabled = enable
        }

    }

    override fun showHomeworkInfo(entity: HomeworkTeacherDetailEntity?) {
        if (entity != null) {
            // 草稿
            val homeworkDetail = copyHomeworkDetailEntity2Teacher(entity)
            mJobCommonView.setCommonData(homeworkDetail)

            if (entity.submit_way <= mTypeList.size) {
                mJobSubmitTypeTv.text = mTypeList[entity.submit_way]
                submitWay = entity.submit_way.toString()
            }

            // 选择学生
            val studentList = mutableListOf<StudentData>()
            val studentEntityList = entity.students
            studentEntityList.forEach {
                studentList.add(StudentData().apply {
                    id = it.student_id
                    nickname = it.nickname
                })
            }
            NSLog.d("studentList:  $studentList")
            handleLessonStudentInfo(studentList)

        }
    }

    override fun publishHomeworkSuccess(homeworkId: String?, isDraft: String?) {
        //  处理作业发布成功
        when (isDraft) {
            // 草稿
            HWConstant.DraftStatus.IS_DRAFT -> {
                mJobSubmitBtn.setBtnEnable(false)
                mHeaderBar.getRightView().isEnabled = false

                val map = mutableMapOf<String, String?>()
                map["lessonId"] = lessonId
                map["homeworkId"] = homeworkId

                EventBus.getDefault().post(MessageEvent(EventConstant.EVENT_PUBLISH_HOMEWORK_SUCCESS, map))

//                this.homeworkId = homeworkId
                mLoadingDialog.setOnDismissListener {
                    finish()
                }

            }
            // 正常提交
            HWConstant.DraftStatus.IS_NOT_DRAFT -> {

                //发布成功 通知首页课节刷新数据
                val map = mutableMapOf<String, String?>()
                map["lessonId"] = lessonId
                map["homeworkId"] = homeworkId
                EventBus.getDefault().post(MessageEvent(EventConstant.EVENT_PUBLISH_HOMEWORK_SUCCESS, map))
                // 关闭当前页面
                mLoadingDialog.setOnDismissListener {
                    // 跳转到作业详情
                    this.startActivity<HomeworkDetailActivity>("serial" to lessonId, "homeworkId" to homeworkId)
                    finish()
                }
            }
        }
    }

    override fun handleLessonStudentInfo(studentList: List<StudentData>) {
        this.studentList.clear()
        this.studentList.addAll(studentList)

        val sb = StringBuilder()
        if (studentList.isNotEmpty()) {
            sb.append(studentList[0].nickname)
        }

        if (studentList.size > 1) {
            sb.append("、")
            sb.append(studentList[1].nickname)
        }

        if (studentList.size > 2) {
            sb.append("等${studentList.size}人")
        }

        mJobSelectStudentTv.text = sb.toString()

    }
}


