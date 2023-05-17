package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talkcloud.corelibrary.TKJoinBackRoomModel
import com.talkcloud.corelibrary.TKJoinRoomModel
import com.talkcloud.corelibrary.TKSdkApi
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.base.BaseJoinRoomActivity
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.entity.NoticeInAppEntity
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser
import com.talkcloud.networkshcool.baselibrary.presenters.NoticeInAppPresenter
import com.talkcloud.networkshcool.baselibrary.ui.adapter.NoticeInAppAdapter
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import com.talkcloud.networkshcool.baselibrary.views.NoticeInAppView
import com.talkcloud.networkshcool.baselibrary.weiget.NoticeInappDividerItem
import com.talkcloud.networkshcool.baselibrary.weiget.RxViewUtils
import kotlinx.android.synthetic.main.activity_noticeinapp.*
import kotlinx.android.synthetic.main.view_nodata.*

/**
 * Date:2021/9/16
 * Time:10:31
 * author:joker
 * 应用内通知列表页面
 */
class NoticeInAppActivity : BaseJoinRoomActivity(), RxViewUtils.Action1<View>, NoticeInAppView {


    //线性管理器
    private lateinit var linearlayoutmanager: LinearLayoutManager

    private lateinit var noticeInAppAdapter: NoticeInAppAdapter

    private lateinit var mPresenter: NoticeInAppPresenter

    //刷新标识  true 下拉刷新；false 上推加载
    private var isRefresh = true
    private var page = 1
    private var rows = 10
    private var noticeInAppEntity: NoticeInAppEntity? = null
    private lateinit var noticeInAppEntity_databeans: MutableList<NoticeInAppEntity.DataBean>

    override fun onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this)
            //隐藏状态栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this)
        }
        //状态栏状态设置
        PublicPracticalMethodFromJAVA.getInstance()
            .transparentStatusBar(
                this,
                true, true,
                R.color.appwhite
            )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_noticeinapp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initUiView() {
    }

    override fun initData() {
        mPresenter = NoticeInAppPresenter(this, this)

        noticeInAppEntity_databeans = mutableListOf()

        /** 初始化列表 RecyclerView **/
        linearlayoutmanager = LinearLayoutManager(this)
        linearlayoutmanager.orientation = RecyclerView.VERTICAL
        rv_noticelist.layoutManager = linearlayoutmanager

        noticeInAppAdapter = NoticeInAppAdapter(this, this)
        noticeInAppAdapter.apply {
            rv_noticelist.adapter = this
            rv_noticelist.addItemDecoration(NoticeInappDividerItem())
            notifyDataSetChanged()
        }

        mPresenter.noticeList(page, rows)


        /** 初始化刷新控件 **/
        //下拉刷新监听
        refreshLayout.setOnRefreshListener {
            isRefresh = true
            page = 1
            mPresenter.noticeList(page, rows)
        }

        //上拉加载更多
        refreshLayout.setOnLoadMoreListener {
            noticeInAppEntity?.let {
                if (noticeInAppEntity_databeans.size < it.total) {
                    isRefresh = false
                    page += 1
                    mPresenter.noticeList(page, rows)
                } else {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    override fun initListener() {
        RxViewUtils.getInstance().setOnClickListeners(
            this, 500,
            iv_close
        )
    }

    override fun onRxViewClick(v: View) {
        when (v) {
            iv_close -> {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this@NoticeInAppActivity, R.anim.activity_right_out)
            }
        }
    }

    override fun <T> noticeListCallback(isSuccess: Boolean, data: T, msg: String) {
        if (isSuccess) {
            noticeInAppAdapter.apply {
                noticeInAppEntity = data as NoticeInAppEntity

                if (noticeInAppEntity!!.data.isEmpty()) {
                    cl_nodata.visibility = View.VISIBLE
                    rv_noticelist.visibility = View.GONE
                } else {
                    cl_nodata.visibility = View.GONE
                    rv_noticelist.visibility = View.VISIBLE

                    if (isRefresh) noticeInAppEntity_databeans.clear()
                    noticeInAppEntity_databeans.addAll(noticeInAppEntity!!.data)
                    setDatas(noticeInAppEntity_databeans)
                    notifyDataSetChanged()
                }
                if (isRefresh) refreshLayout.finishRefresh() else refreshLayout.finishLoadMore()
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
            cl_nodata.visibility = View.VISIBLE
            if (isRefresh) refreshLayout.finishRefresh(false) else refreshLayout.finishLoadMore(false)
        }
    }

    override fun readNotice(datas: IntArray, position: Int) {
        mPresenter.readNotice(datas, position)
        val databean = noticeInAppEntity_databeans[position]
        if (databean.type == 3) {//上课提醒
            with(TKJoinRoomModel()) {
                userId = AppPrefsUtil.getUserId()
                nickName = MySPUtilsUser.getInstance().nickName
                userRole = if (AppPrefsUtil.getUserIdentity() == ConfigConstants.IDENTITY_TEACHER) 0 else 2

                val serialId = databean.extras.room_id.toString()

                //进入房间
                joinRoomPresenter.requestJoinRoom(serialId, this)
            }
        } else if (databean.type == 1 || databean.type == 2) {

            if (MySPUtilsUser.getInstance().userIdentity == ConfigConstants.IDENTITY_TEACHER) {
                with(Intent()) {
                    putExtra("homeworkId", databean.extras.homework_id.toString())
                    putExtra("serial", databean.extras.homework_id.toString())
                    setClass(this@NoticeInAppActivity, HomeworkDetailActivity::class.java)//获取intent对象
                    startActivity(this)// 获取class是使用::反射
                }
            } else {
                with(Intent()) {
                    putExtra(BaseConstant.KEY_PARAM1, databean.extras.homework_id.toString())
                    putExtra(BaseConstant.KEY_PARAM2, databean.extras.students_id.toString())
                    setClass(this@NoticeInAppActivity, HomeworkWriteActivity::class.java)//获取intent对象
                    startActivity(this)// 获取class是使用::反射
                }
            }

        }
    }

    override fun <T> readNoticeCallback(isSuccess: Boolean, data: T, msg: String) {
        if (isSuccess) {
            val position = data as Int
            noticeInAppEntity_databeans[position].is_read = 1
            noticeInAppAdapter.notifyItemChanged(position)
//            mPresenter.noticeList(page, rows)
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top)
        }
    }


    /**
     * 进入教室回调
     */
    override fun joinRoom(joinRoomModel: TKJoinRoomModel) {
//        if ("3" == joinRoomModel.state) {
//            return
//        }
        TKSdkApi.joinRoom(this, joinRoomModel)
    }

    /**
     * 播放回放回调
     */
    override fun joinPlaybackRoom(backRoomModelList: List<TKJoinBackRoomModel>) {

    }


}