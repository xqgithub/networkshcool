package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.res.ColorStateList;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.CommonFragmentPagerAdapter;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.CourseListFragment;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;

/**
 * author:wsf
 * createTime:2021/5/14
 * description: 我的课程
 */
public class MyCourseActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "MyCourseActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mycourse;
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
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        initTabLayout();
        CourseListFragment f1 = CourseListFragment.newInstance("1,2");
        CourseListFragment f2 = CourseListFragment.newInstance("3");
        adapter.addFragment(f1);
        adapter.addFragment(f2);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    @Override
    protected void initData() {

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
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.course_notcomplete) + "  "));
        tabLayout.addTab(tabLayout.newTab().setText("  " + getResources().getString(R.string.course_complete) + "  "));
        //去掉tab点击效果
        tabLayout.setTabRippleColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.transparent)));
    }

}


