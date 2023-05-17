package com.talkcloud.networkshcool.baselibrary.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.adapter.HomeworkDetailListAdapter;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailListEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkDetailListPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkCommentActivity;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkDetailListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/6/16
 * description: 作业详情列表
 */
public class HomeworkDetailListFragment extends Fragment implements HomeworkDetailListView, HomeworkDetailListAdapter.HomeworkDetailListListener {


    private HomeworkDetailListAdapter homeworkListAdapter;
    private HomeworkDetailListPresenter presenter;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private TextView tv_notify;
    private View notifyView;
    private String is_submit;  //0未提交1已提交
    private String homeworkId;
    private String serial;
    private List<HomeworkDetailInfoEntity> list = new ArrayList<>();

    //批量提醒按钮
    private TextView tv_bulknotice;

    /**
     * 每页数量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int currentPage = 1;


    //是否可点评 0否 1是
    private int is_remark = 1;


    public static HomeworkDetailListFragment newInstance(String is_submit, String homeworkId, String serial) {
        HomeworkDetailListFragment fg = new HomeworkDetailListFragment();
        Bundle agrs = new Bundle();
        agrs.putString("is_submit", is_submit);
        agrs.putString("homeworkId", homeworkId);
        agrs.putString("serial", serial);
        fg.setArguments(agrs);
        return fg;
    }


    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getActivity() != null) {
            //注册EventBus
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }


            view = LayoutInflater.from(getActivity()).inflate(R.layout.homewrok_list_fragment, container, false);
            if (getArguments() != null) {
                is_submit = getArguments().getString("is_submit");
                homeworkId = getArguments().getString("homeworkId");
                serial = getArguments().getString("serial");
            }
            refreshLayout = view.findViewById(R.id.refreshLayout);
            recyclerview = view.findViewById(R.id.rv_course_list);
            tv_bulknotice = view.findViewById(R.id.tv_bulknotice);

            notifyView = LayoutInflater.from(getActivity()).inflate(R.layout.view_notify_footer, null);
            tv_notify = notifyView.findViewById(R.id.tv_notify);


            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(getActivity(), tv_notify,
                    ScreenUtil.dp2px(getActivity(), 22), -1, "", VariableConfig.color_button_selected);

            ClassicsHeader mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();

            // mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

            Drawable mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
            if (mDrawableProgress instanceof LayerDrawable) {
                mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
            }

            refreshLayout.setEnableLoadMore(false);

            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    //    loadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    refresh();
                }
            });

            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            homeworkListAdapter = new HomeworkDetailListAdapter(R.layout.item_homework_detail, list);
            homeworkListAdapter.setHomeworkDetailListListener(this);
            recyclerview.setAdapter(homeworkListAdapter);
            homeworkListAdapter.setOnItemClickListener((adapter, view1, position) -> {
        /*    Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
            intent.putExtra("courseId", courseListAdapter.getItem(position).getId());
            startActivity(intent);*/

                String status = list.get(position).getStatus();

                Bundle bundle = new Bundle();
                if ("2".equals(status)) {//已经提交的作业
                    bundle.putParcelableArrayList("students", (ArrayList) list);
                    bundle.putInt("homeworkposition", position);
                    bundle.putInt("is_remark", is_remark);
                } else if ("3".equals(status)) {
                    List<HomeworkDetailInfoEntity> templist = new ArrayList<>();
                    templist.add(list.get(position));
                    bundle.putParcelableArrayList("students", (ArrayList) templist);
                    bundle.putInt("homeworkposition", 0);
                    bundle.putInt("is_remark", is_remark);
                } else {
                    return;
                }
                bundle.putString("homeworkid", homeworkId);
                bundle.putString("serial", serial);
                //跳转到作业评论页面
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(getActivity(), HomeworkCommentActivity.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            });

            presenter = new HomeworkDetailListPresenter(this, getActivity());
            presenter.getHomeworkDetailList(is_submit, homeworkId, currentPage, pageSize);
            refreshLayout.autoRefresh();
        }
        return view;
    }

    @Override
    public void homeworkDetailListCallback(boolean isSuccess, Object data) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (isSuccess) {
            ApiResponse<HomeworkDetailListEntity> info = (ApiResponse<HomeworkDetailListEntity>) data;
            list = (List<HomeworkDetailInfoEntity>) (info.getData());


            if (list == null || (list != null && list.size() == 0)) {
                if (currentPage == 1) {
                    homeworkListAdapter.getData().clear();
                    homeworkListAdapter.notifyDataSetChanged();
                    if (getActivity() != null) {
                        LinearLayout view;
                        if (ScreenTools.getInstance().isPad(getActivity())) {
                            view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view, null);
                            view.setLayoutParams(new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, TKBaseApplication.myApplication.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_400x)));
                        } else {
                            view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view_center, null);
                        }
                        TextView des = view.findViewById(R.id.tv_empty_view_text);
                        des.setText(R.string.nohomeworkcommite);

                  /*      if (!ScreenTools.getInstance().isPad(getActivity())) {
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                           // lp.topMargin = ScreenUtil.dp2px(getActivity(), 30);
                            lp.gravity= Gravity.CENTER;
                            view.setLayoutParams(lp);
                        }*/


                        homeworkListAdapter.setEmptyView(view);
                    }

                }

                return;
            } else {
                if (is_submit != null && is_submit.equals("0")) { //未提交显示提醒按钮
//                    homeworkListAdapter.setFooterView(notifyView);
                    tv_notify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<String> studentIds = new ArrayList<String>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getUnreminds() != 0) {
                                    studentIds.add(list.get(i).getId());
                                }
                            }
                            if (homeworkId != null && studentIds.size() > 0)
                                presenter.homeworkNotify(studentIds, homeworkId);
                        }
                    });

                    //批量提醒
                    tv_bulknotice.setVisibility(View.VISIBLE);

                    List<String> studentIds = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getUnreminds() != 0) {
                            studentIds.add(list.get(i).getId());
                        }
                    }

                    if(studentIds.size() > 0) {
                        tv_bulknotice.setTextColor(Color.parseColor(VariableConfig.color_button_selected));
                    } else {
                        tv_bulknotice.setTextColor(Color.parseColor("#8F92A1"));
                    }

                    tv_bulknotice.setOnClickListener(v -> {

                        if (homeworkId != null && studentIds.size() > 0)
                            presenter.homeworkNotify(studentIds, homeworkId);
                    });
                }
            }
            homeworkListAdapter.setNewData(list);
        } else {
            if (getActivity() != null) {
                LinearLayout view;
                if (ScreenTools.getInstance().isPad(getActivity())) {
                    view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view, null);
                } else {
                    view = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view_center, null);
                }
                TextView des = view.findViewById(R.id.tv_empty_view_text);
                des.setText(R.string.nodata);
                homeworkListAdapter.setEmptyView(view);
            }
        }

    }

    @Override
    public void homeworkNotifyCallback(boolean isSuccess, Object data) {
        if (isSuccess) {
            if (getActivity() != null) {
                homeworkListAdapter.setNewData(list);
                ToastUtils.showShortToastFromRes(R.string.nofity_sucuess, ToastUtils.top);
            }
        }
    }


    /**
     * 刷新
     */
    public void refresh() {

        if (homeworkListAdapter != null && homeworkListAdapter.getFooterLayoutCount() > 0) {
            homeworkListAdapter.removeAllFooterView();
        }
        currentPage = 1;
        refreshLayout.setEnableLoadMore(false);
        presenter.getHomeworkDetailList(is_submit, homeworkId, currentPage, pageSize);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        currentPage++;
        presenter.getHomeworkDetailList(is_submit, homeworkId, currentPage, pageSize);
    }

    /**
     * 单个提醒学生回调
     *
     * @param bean
     * @param position
     */
    @Override
    public void onRemindStudents(HomeworkDetailInfoEntity bean, int position) {
        List<String> studentIds = new ArrayList<>();
        studentIds.add(bean.getId());
        if (homeworkId != null && studentIds.size() > 0) presenter.homeworkNotify(studentIds, homeworkId);
    }

    /**
     * priority 数值越大，优先级越高,默认优先级为0
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING, priority = 100)
    public void EventBusMessage(MessageEvent messageEvent) {
        if (messageEvent.getMessage_type().equals(EventConstant.EVENT_HOMEWORKDETAIL_PASS_VALUE)) {
            if (!StringUtils.isBlank(homeworkListAdapter)) {
                this.is_remark = (int) messageEvent.getMessage();
                homeworkListAdapter.setIsRemark(is_remark);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
