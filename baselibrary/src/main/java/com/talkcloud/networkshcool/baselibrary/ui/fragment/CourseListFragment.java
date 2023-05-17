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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.adapter.CourseListAdapter;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.entity.CourseInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.CourseListEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.CourseListPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.activities.CourseDetailActivity;
import com.talkcloud.networkshcool.baselibrary.views.CourseListView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/5/14
 * description: 我的课程列表
 */
public class CourseListFragment extends Fragment implements CourseListView {


    private CourseListAdapter courseListAdapter;
    private CourseListPresenter presenter;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerview;
    private String state;
    private List<CourseInfoEntity> list = new ArrayList<>();

    /**
     * 每页数量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int currentPage = 1;

    public static CourseListFragment newInstance(String state) {
        CourseListFragment fg = new CourseListFragment();
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
            courseListAdapter = new CourseListAdapter(R.layout.item_course, list);
            recyclerview.setAdapter(courseListAdapter);
            courseListAdapter.setOnItemClickListener((adapter, view1, position) -> {
                Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                intent.putExtra("courseId", courseListAdapter.getItem(position).getId());
                startActivity(intent);
            });

            presenter = new CourseListPresenter(this, getActivity());
            presenter.getCourseList(state, currentPage, pageSize);
            refreshLayout.autoRefresh();
        }
        return view;
    }

    @Override
    public void courseListCallback(boolean isSuccess, Object data) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        if (isSuccess) {
            ApiResponse<CourseListEntity> info = (ApiResponse<CourseListEntity>) data;
            list = info.getData().getData();

            if (list == null || (list != null && list.size() == 0)) {
                if (currentPage == 1) {
                    courseListAdapter.getData().clear();
                    courseListAdapter.notifyDataSetChanged();
                    if (getActivity() != null) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_list_view, null);
                        courseListAdapter.setEmptyView(view);
                    }

                }

                return;
            }
            if (currentPage == info.getData().getLast_page()) {  //最后一页
                refreshLayout.setEnableLoadMore(false);
                if (getActivity() != null) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.nomore_data, null);
                    courseListAdapter.setFooterView(view);
                }

            } else {
                refreshLayout.setEnableLoadMore(true);
            }
            if (currentPage == 1) {  //第一页就刷新 否则是加载更多
                courseListAdapter.setNewData(list);
            } else {
                courseListAdapter.addData(list);
            }
        } else {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_list_view, null);
            courseListAdapter.setEmptyView(view);
        }

    }


    /**
     * 刷新
     */
    private void refresh() {
        currentPage = 1;
        if (courseListAdapter != null && courseListAdapter.getFooterLayoutCount() > 0) {
            courseListAdapter.removeAllFooterView();
        }
        refreshLayout.setEnableLoadMore(false);
        presenter.getCourseList(state, currentPage, pageSize);
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        currentPage++;
        presenter.getCourseList(state, currentPage, pageSize);
    }
}
