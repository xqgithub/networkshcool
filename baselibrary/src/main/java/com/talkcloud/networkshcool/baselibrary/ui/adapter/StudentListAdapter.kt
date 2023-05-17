package com.talkcloud.networkshcool.baselibrary.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.findViewOfItem
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity
import com.talkcloud.networkshcool.baselibrary.ui.dialog.StudentListDialog
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils
import me.jessyan.autosize.AutoSizeConfig
import java.util.*

/**
 * Date:2021/9/26
 * Time:17:27
 * author:joker
 * 课节列表---学生列表
 */
class StudentListAdapter constructor(var mContext: Context) : RecyclerView.Adapter<StudentListAdapter.StudentListHolder>() {

    //根据type不同，加载不同的布局
    private var _type: Int = -1

    private var dataBeans: List<LessonInfoEntity.StudentsBean> = ArrayList()

    constructor(mContext: Context, type: Int) : this(mContext) {
        this._type = type
    }

    class StudentListHolder(val view: View, _type: Int) : RecyclerView.ViewHolder(view) {
        /**
         * 获取各个item的id
         */
        fun <T : View> findViewItem(viewid: Int): T {
            return view.findViewOfItem(viewid)
        }


        var iv_avator: ImageView = findViewItem(R.id.iv_avator)
        var tv_students_num: TextView = findViewItem(R.id.tv_students_num)
        var tv_students_age: TextView? = if (_type == 0) null else findViewItem(R.id.tv_students_age)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListHolder {
        var layout = if (_type == 0) R.layout.adapter_studentlist else R.layout.adapter_studentlistdetails
        var view = LayoutInflater.from(mContext).inflate(layout, parent, false)
        return StudentListHolder(view, _type)
    }

    override fun onBindViewHolder(holder: StudentListHolder, position: Int) {
        if (!StringUtils.isBlank(dataBeans[position].avatar)) {
            Glide.with(mContext).load(dataBeans[position].avatar)
                .error(R.drawable.icon_default_head_img)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {//防止RecyclerView 中 使用glide 重复加载图像出现忽大忽小的现象
                        holder.iv_avator.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        if (_type == 0) {
            if (dataBeans.size > 3) {
                if (position == 2) {
                    holder.tv_students_num.visibility = View.VISIBLE

                    //设置学生人数背景
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(
                        mContext, holder.tv_students_num,
                        mContext.resources.getDimensionPixelSize(R.dimen.dimen_100x).toFloat(), -1, "", "#8F92A1"
                    )
                    holder.tv_students_num.text = "+${dataBeans.size - 3}"
                } else {
                    holder.tv_students_num.visibility = View.GONE
                }
            } else {
                holder.tv_students_num.visibility = View.GONE
            }


            holder.itemView.setOnClickListener {
                val studentListDialog: StudentListDialog = if (!ScreenTools.getInstance().isPad(mContext)) {
                    //打开学生列表弹框
                    when (dataBeans.size) {
                        in 1..3 -> {
                            StudentListDialog(mContext, mContext.resources.getDimensionPixelSize(R.dimen.dimen_375x), (0.5 * AutoSizeConfig.getInstance().screenHeight).toInt())
                        }
                        in 4..7 -> {
                            StudentListDialog(mContext, mContext.resources.getDimensionPixelSize(R.dimen.dimen_375x), WindowManager.LayoutParams.WRAP_CONTENT)
                        }
                        else -> {
                            StudentListDialog(mContext, mContext.resources.getDimensionPixelSize(R.dimen.dimen_375x), (0.92 * AutoSizeConfig.getInstance().screenHeight).toInt())
                        }
                    }
                } else {
                    StudentListDialog(mContext, mContext.resources.getDimensionPixelSize(R.dimen.dimen_395x), WindowManager.LayoutParams.MATCH_PARENT, R.style.AnimRight)
                }
                studentListDialog.setDatas(dataBeans)
                studentListDialog.show()
            }
        } else {
            holder.tv_students_num.text = dataBeans[position].name
            if (dataBeans[position].age == 0) {
                holder.tv_students_age?.visibility = View.GONE
            } else {
                holder.tv_students_age?.visibility = View.VISIBLE
            }
            holder.tv_students_age?.text = "${dataBeans[position].age}${mContext.resources.getString(R.string.student_age)}"
        }
    }

    override fun getItemCount(): Int {
        return if (_type == 0) {
            if (dataBeans.size > 3) 3 else dataBeans.size
        } else {
            dataBeans.size
        }
    }

    /**
     * 设置数据
     */
    fun setDatas(dataBeans: List<LessonInfoEntity.StudentsBean>) {
        this.dataBeans = dataBeans
    }

}