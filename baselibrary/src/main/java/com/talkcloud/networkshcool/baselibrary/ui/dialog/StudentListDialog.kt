package com.talkcloud.networkshcool.baselibrary.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity
import com.talkcloud.networkshcool.baselibrary.ui.adapter.StudentListAdapter
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import kotlinx.android.synthetic.main.dialog_studentlist.*

/**
 * Date:2021/9/27
 * Time:10:15
 * author:joker
 * 课节学生列表弹框
 */
class StudentListDialog @JvmOverloads constructor(
    var mContext: Context,
    var width: Int = mContext.resources.getDimensionPixelSize(R.dimen.dimen_375x),
    var height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    var animationsResId: Int = R.style.AnimBottom,
    var themeResId: Int = R.style.dialog_style
) : Dialog(mContext, themeResId) {


//    private var dataBeans: List<LessonInfoEntity.StudentsBean> = ArrayList()

    init {
        initUIView()
        initListener()
    }


    private fun initUIView() {
        setContentView(R.layout.dialog_studentlist)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setDialogBG()
    }

    private fun initListener() {
        iv_close.setOnClickListener {
            dismissDialog()
        }
    }

    override fun show() {
        super.show()
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        val layoutParams = window!!.attributes
        if (!ScreenTools.getInstance().isPad(mContext)) {
            layoutParams.gravity = Gravity.BOTTOM
        } else {
            layoutParams.gravity = Gravity.RIGHT
            //隐藏导航栏
            PublicPracticalMethodFromJAVA.getInstance().hideNavigationBar(window)
        }
        window!!.setWindowAnimations(animationsResId) //添加动画
        layoutParams.width = width
        layoutParams.height = height
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams
    }

    /**
     * 设置弹框的背景
     */
    private fun setDialogBG() {
        if (!ScreenTools.getInstance().isPad(mContext)) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE2(
                cl_dialog,
                mContext.resources.getDimensionPixelSize(R.dimen.dimen_30x).toFloat(), mContext.resources.getDimensionPixelSize(R.dimen.dimen_30x).toFloat(),
                0f, 0f,
                -1, "", null, "#FFFFFF"
            )
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE2(
                cl_dialog,
                mContext.resources.getDimensionPixelSize(R.dimen.dimen_30x).toFloat(), 0f,
                0f, mContext.resources.getDimensionPixelSize(R.dimen.dimen_30x).toFloat(),
                -1, "", null, "#FFFFFF"
            )
        }
    }


    /**
     * 设置数据
     */
    fun setDatas(dataBeans: List<LessonInfoEntity.StudentsBean>) {
        tv_title.text = "${mContext.resources.getString(R.string.chooseidentity_student)}（${dataBeans.size}）"

        //初始化学生列表
        val linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_student_list.layoutManager = linearLayoutManager

        val studentListAdapter = StudentListAdapter(mContext, 1)
        studentListAdapter.apply {
            setDatas(dataBeans)
            rv_student_list.adapter = this
            notifyDataSetChanged()
        }
    }

    /**
     * 关闭弹框
     */
    fun dismissDialog() {
        if (isShowing) {
            dismiss()
        }
    }

}