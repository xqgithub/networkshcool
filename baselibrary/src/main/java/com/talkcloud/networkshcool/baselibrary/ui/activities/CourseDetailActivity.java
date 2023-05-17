package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.corelibrary.TKJoinBackRoomModel;
import com.talkcloud.corelibrary.TKJoinRoomModel;
import com.talkcloud.corelibrary.TKSdkApi;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.adapter.LessonListAdapter;
import com.talkcloud.networkshcool.baselibrary.adapter.OnRoomItemClickListener;
import com.talkcloud.networkshcool.baselibrary.base.BaseJoinRoomActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.entity.CourseDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.ResourseEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.CourseDetailPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.CourseDetailView;

import org.jetbrains.anko.ToastsKt;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/5/13
 * description: 课程详情
 */
public class CourseDetailActivity extends BaseJoinRoomActivity implements View.OnClickListener, CourseDetailView {
    public static final String TAG = "CourseDetailActivity";


    private TextView tv_courseName, tv_courseNum, tv_courseTime, tv_courseTeacher, tv_courseType, tv_courseFile;
    private RecyclerView rv_lessonList;


    private LessonListAdapter lessonListAdapter;

    private List<LessonInfoEntity> list = new ArrayList<>();

    private String courseId;

    private CourseDetailPresenter courseDetailPresenter;

    private List<ResourseEntity> listResourse = new ArrayList<>();

    private List<Integer> leftWidths = new ArrayList<>();
    private int maxWidth = 0;
    private TextView tv_courseTimeLeft, tv_courseTeacherLeft, tv_courseTypeLeft;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void initUiView() {
        tv_courseName = findViewById(R.id.tv_course_name);
        tv_courseNum = findViewById(R.id.tv_course_num);
        tv_courseTime = findViewById(R.id.tv_course_time);
        tv_courseTeacher = findViewById(R.id.tv_course_teacher);
        tv_courseType = findViewById(R.id.tv_course_type);
        tv_courseFile = findViewById(R.id.tv_course_file);
        rv_lessonList = findViewById(R.id.rv_lessonList);
        tv_courseTimeLeft = findViewById(R.id.tv_course_time_left);
        tv_courseTeacherLeft = findViewById(R.id.tv_course_teacher_left);
        tv_courseTypeLeft = findViewById(R.id.tv_course_type_left);
        findViewById(R.id.img_back).setOnClickListener(v -> finish());
        rv_lessonList.setLayoutManager(new LinearLayoutManager(this));
        lessonListAdapter = new LessonListAdapter(R.layout.item_lesson, list);
        rv_lessonList.setAdapter(lessonListAdapter);
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_courseTimeLeft.measure(spec, spec);
        int timeWidth = tv_courseTimeLeft.getMeasuredWidth();
        tv_courseTeacherLeft.measure(spec, spec);
        int teacherWidth = tv_courseTeacherLeft.getMeasuredWidth();
        tv_courseTypeLeft.measure(spec, spec);
        int typeWidth = tv_courseTypeLeft.getMeasuredWidth();

        leftWidths.add(timeWidth);
        leftWidths.add(teacherWidth);
        leftWidths.add(typeWidth);
        maxWidth = (int) Collections.max(leftWidths);

        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(maxWidth +
                ScreenUtil.dp2px(CourseDetailActivity.this, 14), LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_courseTimeLeft.setLayoutParams(Params);
        tv_courseTeacherLeft.setLayoutParams(Params);
        tv_courseTypeLeft.setLayoutParams(Params);

    }

    @Override
    protected void initData() {
        courseId = getIntent().getStringExtra("courseId");
        if (courseId != null) {
            courseDetailPresenter = new CourseDetailPresenter(this, this);
            courseDetailPresenter.getCourseDetail(courseId);
        }
    }

    @Override
    protected void initListener() {

//        RoomClient.getInstance().regiestInterface(this,this);

        tv_courseFile.setOnClickListener(this);

        lessonListAdapter.setOnRoomItemClickListener(new OnRoomItemClickListener() {

            @Override
            public void onJoinRoom(String serialId, TKJoinRoomModel model) {
//                TKSdkApi.joinRoom(CourseDetailActivity.this, model);
                if (!isJoiningRoom) {
                    isJoiningRoom = true;

                    if (model == null) {

                        model = new TKJoinRoomModel();

                        model.setUserId(AppPrefsUtil.getUserId());
//                        String nickName = TextUtils.isEmpty(AppPrefsUtil.getUserName()) ? MySPUtilsUser.getInstance().getUserMobile() : AppPrefsUtil.getUserName();
//                        model.setNickName(nickName);
                        model.setNickName(AppPrefsUtil.getUserName());

                        int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
                        model.setUserRole(role);

                    }

                    joinRoomPresenter.requestJoinRoom(serialId, model);
                }
            }

            @Override
            public void onJoinPlayBackRoom(String serialId) {
//                TKSdkApi.joinPlayBackRoom(CourseDetailActivity.this, serialId);
                joinRoomPresenter.requestJoinPlaybackRoom(serialId);
            }

            @Override
            public void onLessonPreparation(String serialId) {

            }
        });
    }


    @Override
    public void joinRoom(@NotNull TKJoinRoomModel joinRoomModel) {

        if("3".equals(joinRoomModel.getState())) {
            return;
        }

        Log.d("goyw", joinRoomModel.toString());
        TKSdkApi.joinRoom(this, joinRoomModel);
    }

    @Override
    public void joinPlaybackRoom(@NotNull List<? extends TKJoinBackRoomModel> backRoomModelList) {
        if (backRoomModelList.isEmpty()) {
//            ToastsKt.longToast(this, R.string.join_playback_room_desc);
            ToastUtils.showShortTop(getString(R.string.join_playback_room_desc));
            return;
        }
//        Log.d("goyw", backRoomModelList.get(0).toString());

        // 只有一个直接进入
        if (backRoomModelList.size() == 1) {
            TKSdkApi.joinPlayBackRoom(this, backRoomModelList.get(0));
        } else {

            String[] nameArr = new String[backRoomModelList.size()];

            for (int i = 0; i < backRoomModelList.size(); i++) {
                nameArr[i] = getString(R.string.reply) + (i + 1);
            }

            // 弹出列表
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.join_playback_list_title)).setItems(nameArr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    TKSdkApi.joinPlayBackRoom(CourseDetailActivity.this, backRoomModelList.get(which));
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_course_file) {
            Intent intent = new Intent(this, CourseMaterialActivity.class);
            intent.putExtra("materialList", (Serializable) listResourse);
            startActivity(intent);
        }
    }

    @Override
    protected void onBeforeSetContentLayout() {
        //  TKDensityUtil.setDensity(this, MyApplication.myApplication,ScreenTools.getInstance().isPad(this));
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
    public void courseDetailCallback(boolean isSuccess, Object data) {
        if (isSuccess && data != null) {
            CourseDetailInfoEntity info = (CourseDetailInfoEntity) data;
            listResourse = info.getResources();
            if (((CourseDetailInfoEntity) data).getLessons() != null) {

            }

            if (info.getLessons() != null && info.getLessons().size() > 0) {
                List<LessonInfoEntity> listInfo = new ArrayList<>();
                if (true) { //如果是老师身份，只显示当前用户自己的课节信息
                    for (int i = 0; i < info.getLessons().size(); i++) {

                    }
                } else {

                }
                lessonListAdapter.setNewData(info.getLessons());
                tv_courseNum.setText(info.getLessons().size() + " " + getResources().getString(R.string.course_class));
            } else {
                View view = LayoutInflater.from(this).inflate(R.layout.view_empty_list_view, null);
                lessonListAdapter.setEmptyView(view);
            }

            if (info.getName() != null) {
                tv_courseName.setText(info.getName());
            }
            if (info.getType_name() != null) {
                tv_courseType.setText(info.getType_name());
            }
            if (info.getPeriod() != null) {
                tv_courseTime.setText(info.getPeriod());
            }
            if (info.getTeachers() != null && info.getTeachers().size() > 0) {
                String teacherText = "";
                for (int i = 0; i < info.getTeachers().size(); i++) {
                    if (i == info.getTeachers().size() - 1) {
                        teacherText = teacherText + info.getTeachers().get(i).getName();
                    } else {
                        teacherText = teacherText + info.getTeachers().get(i).getName() + ",";
                    }
                }
                tv_courseTeacher.setText(teacherText);
            }
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.view_empty_list_view, null);
            lessonListAdapter.setEmptyView(view);
        }
    }

}


