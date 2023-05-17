package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonReportEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.LessonReportPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.MPCharUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;
import com.talkcloud.networkshcool.baselibrary.views.LessonReportView;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/5/19
 * description: 课节报告
 */
public class LessonReportlActivity extends BaseActivity implements LessonReportView {
    public static final String TAG = "LessonReportlActivity";


    private String lessonId;

    private LessonReportPresenter lessonReportPresenter;


    private TextView tv_teacher_name, tv_lesson_title, tv_course_time, tv_total_cups, tv_total_minus,
            tv_inroom_minus2, tv_inroom_minus, tv_leaveroom_minus, tv_lateroom_minus, tv_earlyroom_minus;
    private RoundImageView teacher_avatar;
    private PieChart chart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_report;
    }

    @Override
    protected void initUiView() {
        teacher_avatar = findViewById(R.id.tv_teacher_avatar);
        tv_teacher_name = findViewById(R.id.tv_teacher_name);
        tv_lesson_title = findViewById(R.id.tv_lesson_title);
        tv_course_time = findViewById(R.id.tv_course_time);
        tv_total_cups = findViewById(R.id.tv_total_cups);
        tv_total_minus = findViewById(R.id.tv_total_minus);
        tv_inroom_minus2 = findViewById(R.id.tv_inroom_minus2);
        tv_inroom_minus = findViewById(R.id.tv_inroom_minus);
        tv_leaveroom_minus = findViewById(R.id.tv_leaveroom_minus);
        tv_lateroom_minus = findViewById(R.id.tv_lateroom_minus);
        tv_earlyroom_minus = findViewById(R.id.tv_earlyroom_minus);

        //设置时间字体样式
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/din.ttf");
        tv_inroom_minus2.setTypeface(typeface);
    }

    @Override
    protected void initData() {
        lessonId = getIntent().getStringExtra("lessonId");
        if (lessonId != null) {
            lessonReportPresenter = new LessonReportPresenter(this, this);
            lessonReportPresenter.getLessonReport(lessonId);
        }

        chart = findViewById(R.id.pie_chart);
        MPCharUtil.initPieChar(chart);

    }

    @Override
    protected void initListener() {

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onBeforeSetContentLayout() {
        // TKDensityUtil.setDensity(this, MyApplication.myApplication, ScreenTools.getInstance().isPad(this));
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //状态栏状态设置
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            false, true,
                            -1
                    );
            //隐藏状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }
    }


    @Override
    public void lessonReportCallback(boolean isSuccess, Object data) {
        LessonReportEntity info = (LessonReportEntity) data;
        if (info != null) {
            if (info.getUserinfo() != null && info.getUserinfo().getAvatar() != null && !info.getUserinfo().getAvatar().equals("")) {
                Glide.with(getApplicationContext()).load(info.getUserinfo().getAvatar()).into(teacher_avatar);
            }
            //  GlideUtils.loadHeaderImg(this, info.getUserinfo().getAvatar(), teacher_avatar);
            //teacher_avatar.setImageResource(R.drawable.man_student);
            //
            if (info.getRoomname() != null) {
                tv_lesson_title.setText(info.getRoomname());
            }
            if (info.getStarttime() != 0 && info.getEndtime() != 0) {
                tv_course_time.setText(StringUtil.stampToMonth(info.getStarttime()) + "-" + StringUtil.stampToTime(info.getEndtime()));
            }
            if (info.getUserinfo() != null && info.getUserinfo().getNickname() != null) {
                tv_teacher_name.setText(info.getUserinfo().getNickname());
            }
            if (info.getUserinfo() != null) {
                tv_total_cups.setText(getResources().getString(R.string.class_gain_totle) + info.getUserinfo().getCups() +
                        getResources().getString(R.string.class_cups_num));
            }
            if (info.getPerformance() != null) {  //stirng：听课时长（共30分钟）
                tv_total_minus.setText(getResources().getString(R.string.class_time_long) + "  (" +
                        getResources().getString(R.string.class_total) +
                        info.getPerformance().getTotal_time() / 60 + getResources().getString(R.string.class_minus) + ")");
            }
            if (info.getPerformance() != null) {
                if (info.getPerformance().getTime() % 60 >= 30) { //余数超过30秒 四舍五入
                    tv_inroom_minus2.setText(((info.getPerformance().getTime() / 60) + 1) + "");
                } else {
                    tv_inroom_minus2.setText(info.getPerformance().getTime() / 60 + "");
                }

            }

            if (info.getPerformance() != null) {
                tv_inroom_minus.setText(StringUtil.getTimeByLong(info.getPerformance().getTime(), this));
            }

            long inroomMinutes = info.getPerformance().getTime(); //在教室时长
            long leaveroomMinutes = 0;  //离开教室时长
            long lateroomMinutes = 0;// 迟到时长
            long earlyroomMinutes = 0;//早退时长


            List<Long> times = new ArrayList<>();

            if (info.getPerformance() != null && info.getPerformance().getTime_line() != null) {
                List<LessonReportEntity.TimeLine> timeLines = info.getPerformance().getTime_line();
                if (timeLines.size() > 0) {
                    if (timeLines.get(0).getStarttime() > info.getStarttime()) {  //第一个时间段如果开始时间大于开课时间 说明有迟到
                        lateroomMinutes = timeLines.get(0).getStarttime() - info.getStarttime();
                    }
                    if (timeLines.get(timeLines.size() - 1).getEndtime() < info.getEndtime()) {  //最后一个时间如果小于课节结束时间，说明有早退
                        earlyroomMinutes = info.getEndtime() - timeLines.get(timeLines.size() - 1).getEndtime();
                    }
                    //离开时间 = 总时间-在教室时间 -早退时间 -迟到时间
                    leaveroomMinutes = info.getPerformance().getTotal_time() - inroomMinutes - earlyroomMinutes - lateroomMinutes;
                }
            }

            tv_leaveroom_minus.setText(StringUtil.getTimeByLong(leaveroomMinutes, this));
            tv_lateroom_minus.setText(StringUtil.getTimeByLong(lateroomMinutes, this));
            tv_earlyroom_minus.setText(StringUtil.getTimeByLong(earlyroomMinutes, this));

            /**
             * 设置piechart数据
             */
            ArrayList<PieEntry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();

            times.add(inroomMinutes);
            times.add(leaveroomMinutes);
            if (inroomMinutes == 0) {  //如果上课时间是0，则随便给一个值 画出迟到时间，默认如果上课时间是0，就是迟到了整堂课
                times.add((long) 1);
            } else {
                times.add(lateroomMinutes);
            }

            times.add(earlyroomMinutes);
            final int[] VORDIPLOM_COLORS = {
                    Color.parseColor("#FF1D6AFF"), Color.parseColor("#FF06DEF9"),
                    Color.parseColor("#FFF9CF06"), Color.parseColor("#338F92A1")};

            for (int c : VORDIPLOM_COLORS)
                colors.add(c);


            for (int i = 0; i < times.size(); i++) {
                entries.add(new PieEntry((float) (times.get(i)),
                        ""));
            }

            MPCharUtil.setPieDate(chart, entries, colors);
        }
    }
}


