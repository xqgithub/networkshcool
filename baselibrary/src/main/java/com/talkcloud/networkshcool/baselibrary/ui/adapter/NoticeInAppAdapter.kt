package com.talkcloud.networkshcool.baselibrary.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.SPConstants
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.common.findViewOfItem
import com.talkcloud.networkshcool.baselibrary.entity.NoticeInAppEntity
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils
import com.talkcloud.networkshcool.baselibrary.views.NoticeInAppView
import java.util.*

/**
 * Date:2021/9/16
 * Time:11:19
 * author:joker
 * 应用内通知列表 适配器
 */
class NoticeInAppAdapter constructor(var mContext: Context, var noticeInAppView: NoticeInAppView) : RecyclerView.Adapter<NoticeInAppAdapter.NoticeInAppViewHolder>() {

    private var dataBeans: List<NoticeInAppEntity.DataBean> = ArrayList()

    class NoticeInAppViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        /**
         * 获取各个item的id
         */
        fun <T : View> findViewItem(viewid: Int): T {
            return view.findViewOfItem(viewid)
        }

        var iv_notice_type: ImageView = findViewItem(R.id.iv_notice_type)
        var tv_notice_title: TextView = findViewItem(R.id.tv_notice_title)
        var tv_notice_content: TextView = findViewItem(R.id.tv_notice_content)
        var tv_notice_time: TextView = findViewItem(R.id.tv_notice_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeInAppViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.adapter_noticeinapp, parent, false)
        return NoticeInAppViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoticeInAppViewHolder, position: Int) {

        var icon = -1
        var bgcolor = ""

        when (dataBeans[position].type) {
            1 -> {
                icon = R.drawable.icon_homeworkreminder
                bgcolor = VariableConfig.color_button_selected
            }
            2 -> {
                icon = R.drawable.icon_commentreminder
                bgcolor = "#FFC630"
            }
            3 -> {
                icon = R.drawable.icon_classreminder
                bgcolor = "#13C296"
            }
        }
        holder.iv_notice_type.setImageDrawable(ContextCompat.getDrawable(mContext, icon))
        //设置图标的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, holder.iv_notice_type, mContext.resources.getDimensionPixelSize(R.dimen.dimen_12x).toFloat(), -1, "", bgcolor)


        var color_words = if (dataBeans[position].is_read == 0) {//未读消息
            "#1E1F20"
        } else {//已读消息
            "#8F92A1"
        }

        holder.tv_notice_title.apply {
            text = dataBeans[position].title
            setTextColor(Color.parseColor(color_words))
        }
        holder.tv_notice_content.apply {
            text = dataBeans[position].content
            setTextColor(Color.parseColor(color_words))
        }
        holder.tv_notice_time.text = showNoticeTime(dataBeans[position].create_time)


        holder.itemView.setOnClickListener {
            noticeInAppView?.let {
                IntArray(1).apply {
                    this[0] = dataBeans[position].id
                    noticeInAppView.readNotice(this, position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        dataBeans?.let {
            return it.size
        }
        return 0
    }

    /**
     * 设置数据
     */
    fun setDatas(dataBeans: List<NoticeInAppEntity.DataBean>) {
        this.dataBeans = dataBeans
    }


    /**
     * 显示通知时间
     * 规则：
     * 1.一分钟内显示刚刚
     * 2.一小时内显示几分钟前
     * 3. 24小时内显示今天几点几分
     * 4. 24 - 48小时内显示昨天几点几分
     * 5.一年内显示几月几日
     * 6.一年前显示 年-月-日
     */
    fun showNoticeTime(createTime: Long): String {
        var result = ""

        //创建通知的时间
        var createTime_temp = DateUtil.getDateToString(createTime, 4)
        var year = createTime_temp.substring(0, 4)
        var month = createTime_temp.substring(4, 6)
        var day = createTime_temp.substring(6, 8)
        var hour = createTime_temp.substring(8, 10)
        var minute = createTime_temp.substring(10, 12)

        //当前时间
        var currentTime_temp = DateUtil.getDateToString(System.currentTimeMillis(), 4)
        var day2 = currentTime_temp.substring(6, 8)




        timePartsCollection(createTime).let {
            if (it["day"]!! >= 365) {//规则6
                //判断当前语言
                val locale_language = MySPUtilsLanguage.getInstance().localeLanguage
                val locale_country = MySPUtilsLanguage.getInstance().localeCountry
                val datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(mContext)
                if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
                    if (locale_language == datas[1][SPConstants.LOCALE_LANGUAGE] && locale_country == datas[1][SPConstants.LOCALE_COUNTRY]) {//中文
                        result = mContext.resources.getString(R.string.noticeinapp_createtime_rule6, year, month, day)
                        return@let
                    } else if (locale_language == datas[2][SPConstants.LOCALE_LANGUAGE] && locale_country == datas[2][SPConstants.LOCALE_COUNTRY]) {
                        result = mContext.resources.getString(R.string.noticeinapp_createtime_rule6, year, month, day)
                        return@let
                    }
                }
                result = mContext.resources.getString(R.string.noticeinapp_createtime_rule6, month, day, year)
            } else if (it["day"]!! in 2..364) {//规则5
                result = mContext.resources.getString(R.string.noticeinapp_createtime_rule5, month, day)
            } else if (it["day"]!! in 1..1) {//规则4
                result = if ((day2.toLong() - day.toLong()) == 2L) {
                    //跨了2天
//                    mContext.resources.getString(R.string.noticeinapp_createtime_rule4_2, hour, minute)
                    mContext.resources.getString(R.string.noticeinapp_createtime_rule5, month, day)
                } else {
                    mContext.resources.getString(R.string.noticeinapp_createtime_rule4_1, hour, minute)
                }
            } else {
                if (it["hour"]!! in 1..24) {//规则3
                    result = if ((day2.toLong() - day.toLong()) == 1L) {
                        //跨天了
                        mContext.resources.getString(R.string.noticeinapp_createtime_rule4_1, hour, minute)
                    } else {
                        mContext.resources.getString(R.string.noticeinapp_createtime_rule3, hour, minute)
                    }

                } else if (it["hour"]!! < 1) {
                    result = if (it["minute"]!! in 1..60) {//规则2
                        mContext.resources.getString(R.string.noticeinapp_createtime_rule2, it["minute"].toString())
                    } else {//规则1
                        mContext.resources.getString(R.string.noticeinapp_createtime_rule1)
                    }
                }
            }
        }

        return result
    }

    /**
     * 时间差的各个部件（天，小时，分钟，秒）
     */
    var timePartsCollection = { createtime: Long ->
        var timeDifference = System.currentTimeMillis() - createtime * 1000L
        val yushu_day: Long = timeDifference % (1000 * 60 * 60 * 24)
        val yushu_hour: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60))
        val yushu_minute: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60) % (1000 * 60))
        val yushu_second: Long = (timeDifference % (1000 * 60 * 60 * 24)
                % (1000 * 60 * 60) % (1000 * 60) % 1000)

        val day: Long = timeDifference / (1000 * 60 * 60 * 24)
        val hour = yushu_day / (1000 * 60 * 60)
        val minute = yushu_hour / (1000 * 60)
        val second = yushu_minute / 1000

        val mutableMap = mutableMapOf<String, Long>()
        mutableMap["day"] = day
        mutableMap["hour"] = hour
        mutableMap["minute"] = minute
        mutableMap["second"] = second
        mutableMap
    }


}