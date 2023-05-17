package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.eduhdsdk.room.RoomClient;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.MonthViewPager;
import com.haibin.calendarview.WeekViewPager;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonMonthEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.CalendarCoursePresenter;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.LessonListFragment;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.views.CalendarCourseView;
import com.talkcloud.room.TKRoomManager;

import java.util.List;

/**
 * Date:2021/5/19
 * Time:15:22
 * author:joker
 * 日历展示课程页面
 */
public class CalendarCourseActivity extends BaseActivity implements CalendarCourseView, View.OnClickListener, CalendarView.OnCalendarSelectListener {


    private CalendarCoursePresenter presenter;

    private TextView tv_statusbar_top;
    private TextView tv_month_year;
    private TextView tv_backtoday;
    private ImageView iv_close;
    private ConstraintLayout cl_next;
    private ConstraintLayout cl_pre;
    private CalendarView calendarView;


//    private RecyclerView rv_course;
//    private CalendarCourseAdapter courseAdapter;
//    private LinearLayoutManager linearLayoutManager;

    private LessonListFragment lessonListFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;

    private LinearLayout calendarFoldLL;
    private ImageView imgFold;
    private boolean isFold = false;

    @Override
    protected void onBeforeSetContentLayout() {
        //  TKDensityUtil.setDensity(this, MyApplication.myApplication, ScreenTools.getInstance().isPad(this));


        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //隐藏状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
            //   getWindow().getDecorView().setFitsSystemWindows(true);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendarcourse;
    }

    @Override
    protected void initUiView() {
        tv_statusbar_top = findViewById(R.id.tv_statusbar_top);
        cl_next = findViewById(R.id.cl_next);
        cl_pre = findViewById(R.id.cl_pre);
        calendarView = findViewById(R.id.calendarView);
        iv_close = findViewById(R.id.iv_close);
        tv_month_year = findViewById(R.id.tv_month_year);
        tv_backtoday = findViewById(R.id.tv_backtoday);
        calendarFoldLL = findViewById(R.id.ll_fold);
        imgFold = findViewById(R.id.img_fold);
        if (ScreenTools.getInstance().isPad(this)) {
            calendarFoldLL.setVisibility(View.GONE);
        } else {
            isFold = true;

            calendarFoldLL.setVisibility(View.VISIBLE);

            //第一次进入显示周视图
            MonthViewPager mMonthView = calendarView.getMonthViewPager();
            WeekViewPager mWeekPager = calendarView.getWeekViewPager();
            mWeekPager.setVisibility(View.VISIBLE);
            mMonthView.setVisibility(View.GONE);
            imgFold.setRotation(180);

            calendarFoldLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MonthViewPager mMonthView = calendarView.getMonthViewPager();
                    WeekViewPager mWeekPager = calendarView.getWeekViewPager();
                    imgFold.setPivotX(imgFold.getWidth() / 2);
                    imgFold.setPivotY(imgFold.getHeight() / 2);//支点在图片中心

                    if (isFold) {
                        mWeekPager.setVisibility(View.GONE);
                        mMonthView.setVisibility(View.VISIBLE);
                        imgFold.setRotation(0);
                    } else {
                        mWeekPager.setVisibility(View.VISIBLE);
                        mMonthView.setVisibility(View.GONE);
                        imgFold.setRotation(180);
                    }
                    isFold = !isFold;
                }
            });
        }
    }

    @Override
    protected void initData() {
        presenter = new CalendarCoursePresenter(this, this);
        if (ScreenTools.getInstance().isPad(this))
            presenter.setBarHeight(tv_statusbar_top);

        //日历上一个背景
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL2(this, cl_pre, ScreenUtil.dp2px(this, 40),
                ScreenUtil.dp2px(this, 1.5f), "#668F92A1", "");
        //日历下一个背景
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL2(this, cl_next, ScreenUtil.dp2px(this, 40),
                ScreenUtil.dp2px(this, 1.5f), "#668F92A1", "");

        mYear = calendarView.getCurYear();
        mMonth = calendarView.getCurMonth();
        mDay = calendarView.getCurDay();
        presenter.setCalendarYearAndMonthUI(mYear, mMonth, tv_month_year);


        presenter.getLessonMonthDatas(mYear, mMonth);


        //初始化fragment
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();


        Bundle bundle = new Bundle();
        String day = "";
        if (mDay < 10) {
            day = "0" + mDay;
        } else {
            day = mDay + "";
        }
   /*     bundle.putString("date", mYear + "-" + presenter.getConversionMonth(mMonth) + "-" + day);
        lessonListFragment.setArguments(bundle);*/
        lessonListFragment = LessonListFragment.newInstance(mYear + "-" + presenter.getConversionMonth(mMonth) + "-" + day, false);

        transaction.add(R.id.fl_lessonlistfragment_container, lessonListFragment);
        transaction.commit();
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
        tv_backtoday.setOnClickListener(this);
        cl_pre.setOnClickListener(this);
        cl_next.setOnClickListener(this);
        calendarView.setOnCalendarSelectListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        } else if (id == R.id.tv_backtoday) {
            calendarView.scrollToCurrent();
        } else if (id == R.id.cl_pre) {
            calendarView.scrollToPre(false);
        } else if (id == R.id.cl_next) {
            calendarView.scrollToNext(false);
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    /**
     * 月视图日选择回调
     *
     * @param calendar
     * @param isClick
     */
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        mMonth = calendar.getMonth();
        mDay = calendar.getDay();
        presenter.setCalendarYearAndMonthUI(mYear, mMonth, tv_month_year);
        String day = "";
        if (mDay < 10) {
            day = "0" + mDay;
        } else {
            day = mDay + "";
        }
        if (isClick) {
            lessonListFragment.refresh(mYear + "-" + presenter.getConversionMonth(mMonth) + "-" + day, true);
        } else {
            presenter.getLessonMonthDatas(mYear, mMonth);
        }
    }

    @Override
    public void lessonMonthCallback(boolean isSuccess, String msg, List<LessonMonthEntity> lessonMonthEntity) {
        if (isSuccess) {
            //先清理数据
            calendarView.clearSchemeDate();
            //再设置数据
            presenter.setLessonDays(calendarView, lessonMonthEntity);
            //刷新课程列表数据
            String day = "";
            if (mDay < 10) {
                day = "0" + mDay;
            } else {
                day = mDay + "";
            }
            lessonListFragment.refresh(mYear + "-" + presenter.getConversionMonth(mMonth) + "-" + day, true);
        } else {
            //ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals("android.permission.CAMERA")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasVideo = true;
                    TKRoomManager.getInstance().getMySelf().setHasVideo(true);
                }
            } else if (permissions[i].equals("android.permission.RECORD_AUDIO")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasAudio = true;
                    TKRoomManager.getInstance().getMySelf().setHasAudio(true);
                }
            }
        }
        RoomClient.getInstance().checkPermissionsFinshJoinRoom(this);
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
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!ScreenTools.getInstance().isPad(this)) {
            //状态栏状态设置
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            false, true,
                            R.color.appwhite
                    );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
