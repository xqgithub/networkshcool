package com.talkcloud.networkshcool.baselibrary.weiget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eduhdsdk.ui.live.TKBaseQARecyclerViewAdapter
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.StudentAllEntity
import com.talkcloud.networkshcool.baselibrary.entity.StudentData
import com.talkcloud.networkshcool.baselibrary.presenters.SelectStudentPresenter
import com.talkcloud.networkshcool.baselibrary.ui.adapter.NetworkDiskHistoryRecordAdapter
import com.talkcloud.networkshcool.baselibrary.utils.*
import com.talkcloud.networkshcool.baselibrary.views.SelectStudentView
import com.talkcloud.networkshcool.baselibrary.weiget.DefaultTextWatcher
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView
import kotlinx.android.synthetic.main.dialog_select_student.*
import kotlinx.android.synthetic.main.dialog_select_student_adapter_item.view.*
import kotlinx.android.synthetic.main.view_empty_list_view.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.toast

/**
 * Author  guoyw
 * Date    2021/9/27
 * Desc    学生选择器
 */
class TKSelectStudentDialog @JvmOverloads constructor(
    mContext: Context,
    themeResId: Int = R.style.dialog_style
) :
    Dialog(mContext, themeResId), SelectStudentView {

    private var mContext: Context = mContext

    private var mLessonId: String = ""

    // 页码从1开始
    private var pageNum = 1
    private var pageSearchNum = 1

    // 默认50
    private var pageSize = 50

    /// adapter
    private var mSelectAdapter: MySelectAdapter? = null

    // 搜索
    private var historyRecordAdapter: NetworkDiskHistoryRecordAdapter? = null

    // 学生集合
    private var studentAllList: MutableList<StudentData> = mutableListOf()

    // 学生搜索集合
    private var studentSearchList: MutableList<StudentData> = mutableListOf()


    // 以选择的学生
    private var studentSelectedList: MutableList<StudentData> = mutableListOf()

    // 确认回调
    private var onConfirmClick: (studentSelectedList: MutableList<StudentData>) -> Unit = {}

    // 搜索字符
    private var searchStr: String = ""

    // 学生总数
    private var totalSize: Int = 0

    // 是否全选
    private var isSelectAll = false

    private val mPresenter: SelectStudentPresenter by lazy { SelectStudentPresenter(mContext, this) }

    init {
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        setContentView(R.layout.dialog_select_student)
        setCanceledOnTouchOutside(false)
        val window = window
        val params = window!!.attributes

        //  适配pad
        if (ScreenTools.getInstance().isPad(mContext)) {
            params.width = ConvertUtils.dp2px(395f)
            params.height = WindowManager.LayoutParams.MATCH_PARENT

            params.gravity = Gravity.RIGHT
            window.setWindowAnimations(R.style.AnimRight) //添加动画

            //隐藏导航栏
            PublicPracticalMethodFromJAVA.getInstance().hideNavigationBar(window)

            mSelectStudentRootView.setBackgroundResource(R.drawable.bg_select_img_h)
        } else {

            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
            //设置dialog的位置在底部
            params.gravity = Gravity.BOTTOM
            window.setWindowAnimations(R.style.AnimBottom) //添加动画

            mSelectStudentRootView.setBackgroundResource(R.drawable.bg_select_img)

        }

        window.attributes = params
    }


    private fun initData() {

        val mLayoutManger = LinearLayoutManager(mContext)
        mRecyclerView.layoutManager = mLayoutManger

        mSelectAdapter = MySelectAdapter(mContext)
        mSelectAdapter?.setOnSelectListener {
            showConfirmInfo(it, totalSize)
        }
        mSelectAdapter?.setOnSelectListener2 {

            val list = studentAllList.intersect(it)
            if (list.size == studentAllList.size) {
                setSelectAllStatus(true)
            } else {
                setSelectAllStatus(false)
            }
        }
        mRecyclerView.adapter = mSelectAdapter

        // 不要刷新
        mRefreshLayout.setEnableRefresh(false)


        // 搜索 recycleView
        val mLayoutManger2 = LinearLayoutManager(mContext)
        mSearchRecycleView.layoutManager = mLayoutManger2
        historyRecordAdapter = NetworkDiskHistoryRecordAdapter(mContext as Activity) {
            searchStudentName(it)
        }
        mSearchRecycleView.adapter = historyRecordAdapter

    }

    private fun initListener() {

        // 确认
        mConfirmBtn.setOnClickListener {
            onConfirmClick(mSelectAdapter?.getSelectStudentList()!!)
            dismiss()
        }

        // 全选
        mAllSelectLl.setOnClickListener {

            // 全选按钮
            if (isSelectAll) {
                isSelectAll = false
                mAllSelectIv.imageResource = R.drawable.cb_select_unchecked

                mSelectAdapter?.setSelectedReverseStudent()
            } else {
                isSelectAll = true
                mAllSelectIv.imageResource = R.drawable.cb_select_student_checked
                studentAllList.forEach {
                    it.isSelect = true
                }
                mSelectAdapter?.setSelectedStudentList(studentAllList)
            }


            mSelectAdapter?.notifyDataSetChanged()
        }

        // 取消
        mCancelBtn.setOnClickListener {
            KeyBoardUtil.getInstance().hideKeyBoard(mContext, mSearchEt)
            dismiss()
        }


        //软键盘搜索监听
        mSearchEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val name = v.text.toString().trim()
                searchStudentName(name)
            }
            false
        }

        // 准备输入
        mSearchEt.setOnFocusChangeListener { _, hasFocus ->
//            NSLog.d(" hasFocus : $hasFocus")
            if (hasFocus) {
                mSearchLl.visibility = View.VISIBLE
                mEmptyView.visibility = View.GONE

                // 全选
                mAllSelectLl.visibility = View.GONE
                // 搜索记录
                historyRecordAdapter?.setDatas(AppPrefsUtil.getSearchNameList())
                historyRecordAdapter?.notifyDataSetChanged()

            } else {
//                mSearchLl.visibility = View.GONE
//                mEmptyView.visibility = View.GONE
            }
        }

        mSearchEt.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                if (TextUtils.isEmpty(s.toString())) {
                    mSearchClearIv.visibility = View.GONE
                } else {
                    mSearchClearIv.visibility = View.VISIBLE
                }
            }
        })

        // 清空
        mSearchClearIv.setOnClickListener {
            mSearchEt.setText("")
            searchStr = ""
            mSearchEt.clearFocus()
            //隐藏软键盘
            KeyBoardUtil.getInstance().hideKeyBoard(mContext, mSearchEt)

            // 恢复数据
            mSelectAdapter?.setData(studentAllList)
            mRefreshLayout.setNoMoreData(false)

            // 全选
            mAllSelectLl.visibility = View.VISIBLE

            mEmptyView.visibility = View.GONE
            mSearchLl.visibility = View.GONE
        }


        // 加载更多
        mRefreshLayout.setOnLoadMoreListener {
            val page = if (searchStr.isNotEmpty()) {
                ++pageSearchNum
            } else {
                ++pageNum
            }

//            NSLog.d("setOnLoadMoreListener :  $searchStr   page :  $page")

            mPresenter.getStudentList(mLessonId, page, pageSize, searchStr)
        }

    }


    // 搜索根据学生姓名
    private fun searchStudentName(name: String) {

        // 隐藏软键盘
        KeyBoardUtil.getInstance().hideKeyBoard(mContext, mSearchEt)

//        NSLog.d("搜索  : $name")

        this.searchStr = name

        mSearchEt.setText(name)
        mSearchEt.clearFocus()

        // 清空集合
        studentSearchList.clear()
        // TODO 搜索
        pageSearchNum = 1
        mPresenter.getStudentList(mLessonId, pageSearchNum, pageSize, name)
    }


    // 设置确认回调
    fun setOnConfirmListener(onConfirmClick: (studentSelectedList: MutableList<StudentData>) -> Unit = {}) {
        this.onConfirmClick = onConfirmClick
    }


    fun setSelectedStudents(studentList: MutableList<StudentData>): TKSelectStudentDialog {
        studentSelectedList = studentList
        mSelectAdapter?.setSelectedStudentList(studentSelectedList)
        return this
    }


    fun setLessonId(lessonId: String): TKSelectStudentDialog {
        this.mLessonId = lessonId
        mPresenter.getStudentList(mLessonId, pageNum, pageSize)

        return this
    }


    // adapter
    internal class MySelectAdapter(var mContext: Context) : TKBaseQARecyclerViewAdapter<StudentData, MySelectAdapter.MySelectViewHolder?>() {

        private var inflater: LayoutInflater = LayoutInflater.from(mContext)
        private var mStudentSelectedList = mutableListOf<StudentData>()

        private var onSelect: (num: Int) -> Unit = {}
        private var onSelect2: (list: MutableList<StudentData>) -> Unit = {}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySelectViewHolder {
            val view = inflater.inflate(R.layout.dialog_select_student_adapter_item, parent, false)
            return MySelectViewHolder(view)
        }

        override fun onBindViewHolder(holder: MySelectViewHolder, position: Int) {
            val student = dataList[position]!!

            student.isSelect = mStudentSelectedList.contains(student)

            holder.apply {
                GlideUtils.loadHeaderImg(mContext, student.avatar, mUserHeaderIv)
                mUserNameTv.text = student.nickname
                if (student.isSelect) {
                    mUserSelectIv.imageResource = R.drawable.cb_select_student_checked
                } else {
                    mUserSelectIv.imageResource = R.drawable.cb_select_unchecked
                }

                mUserSelectIv.setOnClickListener {

                    if (student.isSelect) {
                        if (mStudentSelectedList.contains(student)) {
                            mStudentSelectedList.remove(student)
                        }
                        student.isSelect = false
                        mUserSelectIv.imageResource = R.drawable.cb_select_unchecked
                    } else {
                        if (!mStudentSelectedList.contains(student)) {
                            mStudentSelectedList.add(student)
                        }

                        student.isSelect = true
                        mUserSelectIv.imageResource = R.drawable.cb_select_student_checked
                    }

                    onSelect(mStudentSelectedList.size)
                    onSelect2(mStudentSelectedList)
                }
            }
        }

        override fun setData(dataList: MutableList<StudentData>?) {
            super.setData(dataList)

        }

        // 设置反选
        fun setSelectedReverseStudent() {
            this.mStudentSelectedList.clear()
            onSelect(mStudentSelectedList.size)
            onSelect2(mStudentSelectedList)
        }

        // 设置全选
        fun setSelectedStudentList(studentSelectedList: MutableList<StudentData>) {
            this.mStudentSelectedList.clear()
            this.mStudentSelectedList.addAll(studentSelectedList)

            onSelect(mStudentSelectedList.size)
//            onSelect2(mStudentSelectedList)
        }

        fun getSelectStudentList(): MutableList<StudentData> {
            return mStudentSelectedList
        }

        fun setOnSelectListener(onSelect: (num: Int) -> Unit = {}) {
            this.onSelect = onSelect
        }

        fun setOnSelectListener2(onSelect2: (list: MutableList<StudentData>) -> Unit = {}) {
            this.onSelect2 = onSelect2
        }

        internal inner class MySelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mUserHeaderIv: RoundImageView = itemView.mUserHeaderIv
            var mUserNameTv: TextView = itemView.mUserNameTv
            var mUserSelectIv: ImageView = itemView.mUserSelectIv
//            var mUserSelectCb: CheckBox = itemView.mUserSelectCb

        }
    }


    override fun handlerStudentInfo(studentEntity: StudentAllEntity, name: String) {

        // 加载完成
        mRefreshLayout.finishLoadMore()

        if (name.isNotEmpty()) {
            // 保存搜索记录
            AppPrefsUtil.saveSearchName(name)
        } else {
            totalSize = studentEntity.total
            showConfirmInfo(mSelectAdapter!!.getSelectStudentList().size, totalSize)
        }

        val studentList: List<StudentData> = studentEntity.data

        if (studentList != null && studentList.isNotEmpty()) {
            // 数据加载完了
            if (studentList.size < pageSize) {
                mRefreshLayout.setNoMoreData(true)
            }

            if (name.isNotEmpty()) {
                // 搜索的学生
                studentSearchList.addAll(studentList)
                mSelectAdapter?.setData(studentSearchList)
            } else {
                // 加载更多
                studentAllList.addAll(studentList)
                mSelectAdapter?.setData(studentAllList)

                if (pageNum == 1) {
                    val list = studentAllList.intersect(studentSelectedList)
                    if (list.size == studentAllList.size) {
                        setSelectAllStatus(true)
                    }
                }
            }

            mSearchLl.visibility = View.GONE

        } else {
            if (pageNum == 1 || pageSearchNum == 1) {
                mEmptyView.tv_empty_view_text.textSize = 14f
                mEmptyView.tv_empty_view_text.setText(R.string.emptydata_text)
                mEmptyView.visibility = View.VISIBLE
            }
            mRefreshLayout.setNoMoreData(true)
//            mRefreshLayout.setEnableLoadMore(false)
        }
    }

    override fun handlerStudentInfoFailed(message: String) {
//        mContext.toast(message)
        ToastUtils.showShortTop(message)

        if (pageNum > 1) {
            pageNum--
        }
    }

    private fun showConfirmInfo(selectSize: Int, total: Int) {
        val confirmStr = mContext.getString(R.string.logout_yes) + "($selectSize/$total)"
        mConfirmBtn.setBtnText(confirmStr)

        if (selectSize <= 0) {
            mConfirmBtn.setBtnEnable(false)
        } else {
            mConfirmBtn.setBtnEnable(true)
        }
    }


    private fun setSelectAllStatus(flag: Boolean) {
        if (flag) {
            isSelectAll = true
            mAllSelectIv.imageResource = R.drawable.cb_select_student_checked
        } else {
            isSelectAll = false
            mAllSelectIv.imageResource = R.drawable.cb_select_unchecked
        }

    }

}