package com.talkcloud.networkshcool.baselibrary.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.talkcloud.networkshcool.baselibrary.adapter.HomeworkListAdapter;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkListEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkListPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkDetailActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkPublishActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkWriteActivity;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.TKDoubleTapHelper;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkListView;
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectCommonDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * author:wsf
 * createTime:2021/6/10
 * description: 我的作业列表
 */
public class HomeworkListFragment extends Fragment implements HomeworkListView, HomeworkListAdapter.OnMoreClickListener {


    private HomeworkListAdapter homeworkListAdapter;
    private HomeworkListPresenter presenter;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private String state;
    private List<HomeworkInfoEntity> list = new ArrayList<>();

    private TKSelectCommonDialog dialog;
    private List<String> mTypeList = new ArrayList<>();

    private int deleteInfoIndex = -1;  //如果有删除操作 记录删除的对象位置

    /**
     * 每页数量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int currentPage = 1;

    public static HomeworkListFragment newInstance(String state) {
        HomeworkListFragment fg = new HomeworkListFragment();
        Bundle agrs = new Bundle();
        agrs.putString("state", state);
        fg.setArguments(agrs);
        return fg;
    }


    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getActivity() != null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.course_list_fragment, container, false);
            if (getArguments() != null) {
                state = getArguments().getString("state");
            }
            refreshLayout = view.findViewById(R.id.refreshLayout);
            recyclerview = view.findViewById(R.id.rv_course_list);
            ClassicsHeader mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();

            // mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));

            Drawable mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
            if (mDrawableProgress instanceof LayerDrawable) {
                mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
            }

            refreshLayout.setEnableLoadMore(true);

            refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    loadMore();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    refresh();
                }
            });


            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            homeworkListAdapter = new HomeworkListAdapter(R.layout.item_homework, list);
            recyclerview.setAdapter(homeworkListAdapter);

            homeworkListAdapter.setOnItemClickListener((adapter, view1, position) -> {

                NSLog.d("AppPrefsUtil.getUserIdentity() :" + AppPrefsUtil.getUserIdentity());

                // 防止快速点击 进入多次
                if (TKDoubleTapHelper.doubleClick()) {
                    return;
                }

                HomeworkInfoEntity homeworkInfoEntity = homeworkListAdapter.getItem(position);

                if (ConfigConstants.IDENTITY_TEACHER.equals(AppPrefsUtil.getUserIdentity())) {

                    Intent intent;
                    // 草稿
                    if ("0".equals(homeworkInfoEntity.getStatus())) {
                        intent = new Intent(getActivity(), HomeworkPublishActivity.class);
                        intent.putExtra(BaseConstant.KEY_PARAM1, homeworkInfoEntity.getHomework_id());
                        intent.putExtra(BaseConstant.KEY_PARAM2, homeworkInfoEntity.getSerial());
                    } else {
                        intent = new Intent(getActivity(), HomeworkDetailActivity.class);
                        intent.putExtra("homeworkId", homeworkInfoEntity.getHomework_id());
                        intent.putExtra("serial", homeworkInfoEntity.getSerial());
                    }

                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getActivity(), HomeworkWriteActivity.class);
                    intent.putExtra(BaseConstant.KEY_PARAM1, homeworkInfoEntity.getHomework_id());
                    intent.putExtra(BaseConstant.KEY_PARAM2, homeworkInfoEntity.getStudent_id());

                    startActivity(intent);
                }

            });
            homeworkListAdapter.setOnMoreClickListener(this);

            presenter = new HomeworkListPresenter(this, getActivity());
            presenter.getHomeworkList(state, currentPage, pageSize);
            refreshLayout.autoRefresh();
        }
        return view;
    }

    @Override
    public void homeworkListCallback(boolean isSuccess, Object data) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (isSuccess) {
            ApiResponse<HomeworkListEntity> info = (ApiResponse<HomeworkListEntity>) data;
            list = info.getData().getData();

            if (list == null || (list != null && list.size() == 0)) {
                if (currentPage == 1) {
                    homeworkListAdapter.getData().clear();
                    homeworkListAdapter.notifyDataSetChanged();
                    if (getActivity() != null) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view, null);
                        ((TextView) view.findViewById(R.id.tv_empty_view_text)).setText(R.string.nodata);
                        homeworkListAdapter.setEmptyView(view);
                    }

                }

                return;
            }
            if (currentPage == info.getData().getLast_page()) {  //最后一页
                refreshLayout.setEnableLoadMore(false);
                if (getActivity() != null) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.nomore_data, null);
                    homeworkListAdapter.setFooterView(view);
                }

            } else {
                refreshLayout.setEnableLoadMore(true);
            }
            if (currentPage == 1) {  //第一页就刷新 否则是加载更多
                homeworkListAdapter.setNewData(list);
            } else {
                homeworkListAdapter.addData(list);
            }
        } else {
            if (getActivity() != null) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view, null);
                ((TextView) view.findViewById(R.id.tv_empty_view_text)).setText(R.string.nodata);
                homeworkListAdapter.setEmptyView(view);
            }
        }

    }

    @Override
    public void homeworkDeleteCallback(boolean isSuccess, Object data, int index) {
        if (isSuccess) {
            //  Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            deleteInfoIndex = index;
            if (homeworkListAdapter != null && deleteInfoIndex >= 0) {
                homeworkListAdapter.remove(deleteInfoIndex);
                homeworkListAdapter.notifyDataSetChanged();
            }
        }

    }


    /**
     * 刷新
     */
    private void refresh() {
        currentPage = 1;
        if (homeworkListAdapter != null && homeworkListAdapter.getFooterLayoutCount() > 0) {
            homeworkListAdapter.removeAllFooterView();
        }
        refreshLayout.setEnableLoadMore(false);
        presenter.getHomeworkList(state, currentPage, pageSize);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        currentPage++;
        presenter.getHomeworkList(state, currentPage, pageSize);
    }

    @Override
    public void clickMore(HomeworkInfoEntity info, int index) {
        if (dialog == null && getActivity() != null) {
            dialog = new TKSelectCommonDialog(getActivity());
            mTypeList.clear();
            if (info != null && info.getTitle() != null)
                mTypeList.add(info.getTitle());
            mTypeList.add(getResources().getString(R.string.delete));
            mTypeList.add(getResources().getString(R.string.edit));
            dialog.setItemContentList(mTypeList);
            dialog.setSelectItemClickListener(new Function2<String, Integer, Unit>() {
                @Override
                public Unit invoke(String s, Integer integer) {
                    if (s != null && mTypeList != null && mTypeList.size() == 3) {
                        if (mTypeList.get(1).equals(s)) { //点击了删除
//                            deleteInfoIndex = index;
//                            if (info != null && info.getHomework_id() != null)
//                                presenter.deleteHomework(info.getHomework_id());
                            presenter.showDeleteDialog(info, index);
                        } else if (mTypeList.get(2).equals(s)) { //点击编辑 直接进入详情页
//                            Intent intent = new Intent(getActivity(), HomeworkDetailActivity.class);
//                            intent.putExtra("homeworkId", info.getHomework_id());
//                            intent.putExtra("serial", info.getSerial());
                            Intent intent = new Intent(getActivity(), HomeworkPublishActivity.class);
                            intent.putExtra(BaseConstant.KEY_PARAM1, info.getHomework_id());
                            intent.putExtra(BaseConstant.KEY_PARAM2, info.getSerial());
                            startActivity(intent);
                        }
                    }
                    return null;
                }
            });
        }
        dialog.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // 作业发布成功 刷新数据 TODO 这里有几个tab 就好请求几次
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handlerData(MessageEvent messageEvent) {

        if (EventConstant.EVENT_PUBLISH_HOMEWORK_SUCCESS.equals(messageEvent.getMessage_type())) {
            // 有当前课节的tab 才刷新
            Map<String, String> map = (Map<String, String>) messageEvent.getMessage();
            String homeworkId = map.get("homeworkId");
            if (list.contains(new HomeworkInfoEntity(homeworkId))) {
                refresh();
            }
        }
    }
}
