package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.res.ColorStateList;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpActivity;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkTeacherDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkDetailPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.CommonFragmentPagerAdapter;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.HomeworkDetailListFragment;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkDetailView;
import com.talkcloud.networkshcool.baselibrary.weiget.AutoHeightViewPager;
import com.talkcloud.networkshcool.baselibrary.weiget.TKJobCommonView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

/**
 * author:wsf
 * createTime:2021/6/16
 * description: 作业详情 TODO WriteJobPresenter 后期优化
 */
public class HomeworkDetailActivity extends BaseMvpActivity<HomeworkDetailPresenter> implements View.OnClickListener, HomeworkDetailView {
    public static final String TAG = "HomeworkDetailActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String homeworkId;
    //课件ID
    private String serial;

    // 通用区
    TKJobCommonView mJobCommonView;

    private HomeworkDetailPresenter mPresenter;

    private HomeworkDetailListFragment f1;
    private HomeworkDetailListFragment f2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_homework_detail;
    }

    @Override
    protected void initUiView() {
        tabLayout = findViewById(R.id.tp_tabLayout);
        viewPager = findViewById(R.id.contact_viewPager);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mJobCommonView = findViewById(R.id.mJobCommonView);

    }

    @Override
    protected void initData() {
        homeworkId = getIntent().getStringExtra("homeworkId");
        serial = getIntent().getStringExtra("serial");

        // 老师端 获取作业详情
        mPresenter = new HomeworkDetailPresenter(this, this);
        mPresenter.getHomeworkTeacherDetails(homeworkId);


        if (homeworkId != null) {
            CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
            initTabLayout();
            f1 = HomeworkDetailListFragment.newInstance("1", homeworkId, serial);
            f2 = HomeworkDetailListFragment.newInstance("0", homeworkId, serial);
            adapter.addFragment(f1);
            adapter.addFragment(f2);

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);

            viewPager.addOnPageChangeListener(new ChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
            if (!ScreenTools.getInstance().isPad(HomeworkDetailActivity.this)) {
                ((AutoHeightViewPager) viewPager).setScroll(false);
            }
        }

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

    }

    @Override
    protected void initListener() {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_course_file) {
        }
    }

    @Override
    protected void onBeforeSetContentLayout() {
        //  TKDensityUtil.setDensity(this, MyApplication.myApplication, ScreenTools.getInstance().isPad(this));
        //1.状态栏状态设置
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        true, true,
                        R.color.appwhite
                );

      /*  //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);*/
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.commited) + "  "));
        tabLayout.addTab(tabLayout.newTab().setText(" " + getResources().getString(R.string.nocommite)));
        //去掉tab点击效果
        tabLayout.setTabRippleColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.transparent)));

        if (ScreenTools.getInstance().isPad(this)) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

    }


    /**
     * 老师端显示作业详情
     *
     * @param entity 作业相关实体类
     */
    @Override
    public void showHomeworkInfo(@Nullable HomeworkTeacherDetailEntity entity) {

        if (entity != null) {

            TKHomeworkDetailEntity homeworkDetail = TKHomeworkDetailEntity.copyHomeworkDetailEntity2Teacher(entity);
            mJobCommonView.setCommonData(homeworkDetail);
           /* if (ScreenTools.getInstance().isPad(HomeworkDetailActivity.this)) {
                mJobCommonView.setShowFold(false); //pad不显示折叠
            } else {
                mJobCommonView.setShowFold(true); //
            }*/

            mJobCommonView.setVisibility(View.VISIBLE);
//            mJobCommonView.setCommonData(entity);

            if (tabLayout != null && tabLayout.getTabCount() == 2) {
                tabLayout.getTabAt(0).setText(getResources().getString(R.string.commited) + "(" + entity.getSubmits() + ") ");
                tabLayout.getTabAt(1).setText("  " + getResources().getString(R.string.nocommite) + "(" + entity.getUnsubmits() + ")");
            }

            //发送事件
            MessageEvent event = new MessageEvent();
            event.setMessage(entity.is_remark());
            event.setMessage_type(EventConstant.EVENT_HOMEWORKDETAIL_PASS_VALUE);
            EventBus.getDefault().post(event);
        }
    }

    private class ChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

        public ChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            // 切换到当前页面，重置高度
//            if (!ScreenTools.getInstance().isPad(HomeworkDetailActivity.this))
//                viewPager.requestLayout();

            viewPager.requestLayout();
        }
    }

    /**
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void EventBusRefreshHomeworkDetailMessage(MessageEvent messageEvent) {
        if (EventConstant.EVENT_REFRESHHOMEWORKDETAIL_DATA.equals(messageEvent.getMessage_type())) {
            if (!StringUtils.isBlank(f1)) {
                f1.refresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}


