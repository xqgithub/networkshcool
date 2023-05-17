package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.res.ColorStateList;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.CommonFragmentPagerAdapter;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.HomeworkListFragment;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;

/**
 * author:wsf
 * createTime:2021/6/10
 * description: 作业列表
 */
public class HomeworkListActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "HomeworkListActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_homework;
    }

    @Override
    protected void initUiView() {
        tabLayout = findViewById(R.id.tp_tabLayout);
        viewPager = findViewById(R.id.contact_viewPager);
        title = findViewById(R.id.tv_title);
        String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) { //如果是老师
            title.setText(getString(R.string.homework));
        } else {
            title.setText(getString(R.string.myhomework));
        }
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(HomeworkListActivity.this, R.anim.activity_right_out);
            }
        });
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        initTabLayout();

        String unCompleteStatus = "0";
        String completeStatus = "1";

        HomeworkListFragment f1 = HomeworkListFragment.newInstance(unCompleteStatus);
        HomeworkListFragment f2 = HomeworkListFragment.newInstance(completeStatus);
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
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.homework_notcomplete) + "  "));
        tabLayout.addTab(tabLayout.newTab().setText("  " + getResources().getString(R.string.homework_complete) + "  "));
        //去掉tab点击效果
        tabLayout.setTabRippleColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.transparent)));
    }


    /**
     * 物理返回键 监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(HomeworkListActivity.this, R.anim.activity_right_out);
        }
        return false;
    }

}


