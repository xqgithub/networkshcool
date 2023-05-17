package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpActivity;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.StudentAvatarEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherCommentEntity;
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkCommentPresenter;
import com.talkcloud.networkshcool.baselibrary.presenters.HomeworkWritePresenter;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.StudentAvatarAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.ConstraintUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkCommentView;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkWriteView;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;
import com.talkcloud.networkshcool.baselibrary.weiget.StudentAvatarDividerItem;
import com.talkcloud.networkshcool.baselibrary.weiget.TKJobCommentView;
import com.talkcloud.networkshcool.baselibrary.weiget.TKJobCommonView;
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKLoadingDialog;
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKSelectImgDialog;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/6/17
 * Time:14:43
 * author:joker
 * 作业点评页面
 */
public class HomeworkCommentActivity extends BaseMvpActivity implements HomeworkCommentView, HomeworkWriteView, View.OnClickListener {

    private HomeworkCommentPresenter presenter;
    private ConstraintLayout cl_homeworkcomment;
    private ConstraintLayout cl_reviews;
    private ConstraintLayout cl_title;
    private ImageView iv_close;
    private ImageView iv_carryout;
    private TextView tv_statusbar_top;
    private TextView tv_nums;
    private TextView tv_reviews;
    private ProgressBar pb_nums;
    private TextView tv_student_homework;
    private View dividingline_horizontal2;
    private View dividingline_horizontal;

    private ConstraintUtil constraintUtil;

    //被选中的学生的头像
    private RoundImageView riv_avatordetails;
    private TextView tv_studentnamedetails;
    private TextView tv_worktime;

    //学生列表
    private RecyclerView rv_student;
    private StudentAvatarAdapter studentAvatarAdapter;
    private LinearLayoutManager linearLayoutManager;


    //最大可以评论的数量
    private int maxnums = 0;
    //已经评论的数量
    private int reviewednums = 0;
    //老师点评内容
    private HomeworkStudentDetailEntity.Remark remark;

    //被选择的学生
    private StudentAvatarEntity studentAvatarEntitySelected;
    //学生信息 转化数据
    private List<StudentAvatarEntity> studentAvatarEntities;

    //作业ID
    private String homeworkid;
    //课件ID
    private String serial;
    //被选中的作业位置
    private int homeworkposition;
    //学生信息
    private ArrayList<HomeworkDetailInfoEntity> studentInfoList;
    //被选择的学生ID
    private String studentid;

    //是否可点评 0否 1是
    private int is_remark = 1;


    // 学生作业详情区
    private TKJobCommonView mCommonView;
    // 点评区
    private TKJobCommentView mCommentView;


    private TKSelectImgDialog selectDialog;

    private List<String> mTypeList = new ArrayList<>();

    //提交对话弹框
    private TKLoadingDialog tkLoadingDialog;


    @Override
    protected void onBeforeSetContentLayout() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_homeworkcomment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {

        mCommonView = findViewById(R.id.mCommonView);
        mCommentView = findViewById(R.id.mCommentView);

        iv_close = findViewById(R.id.iv_close);
        tv_statusbar_top = findViewById(R.id.tv_statusbar_top);
        pb_nums = findViewById(R.id.pb_nums);
        tv_nums = findViewById(R.id.tv_nums);
        rv_student = findViewById(R.id.rv_student);
        tv_reviews = findViewById(R.id.tv_reviews);
        riv_avatordetails = findViewById(R.id.riv_avatordetails);
        tv_studentnamedetails = findViewById(R.id.tv_studentnamedetails);
        tv_worktime = findViewById(R.id.tv_worktime);
        cl_reviews = findViewById(R.id.cl_reviews);
        iv_carryout = findViewById(R.id.iv_carryout);
        cl_homeworkcomment = findViewById(R.id.cl_homeworkcomment);
        tv_student_homework = findViewById(R.id.tv_student_homework);
        dividingline_horizontal2 = findViewById(R.id.dividingline_horizontal2);
        dividingline_horizontal = findViewById(R.id.dividingline_horizontal);
        cl_title = findViewById(R.id.cl_title);
    }

    @Override
    protected void initData() {
        presenter = new HomeworkCommentPresenter(this, this);


        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            homeworkid = bundle.getString("homeworkid");
            serial = bundle.getString("serial");
            homeworkposition = bundle.getInt("homeworkposition", 0);
            studentInfoList = bundle.getParcelableArrayList("students");
            is_remark = bundle.getInt("is_remark", 1);
        }

        //初始化ConstraintLayout设定工具类
        constraintUtil = new ConstraintUtil(cl_homeworkcomment);

        //设置状态栏的高度
//        presenter.setBarHeight(tv_statusbar_top);


        //提交对话弹框初始化
        mLoadingDialog = new TKLoadingDialog(this);


        //设置点评按钮背景
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_27x), -1, "", VariableConfig.color_button_selected);
        //设置已经完成图标的背景
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, iv_carryout, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
        iv_carryout.setVisibility(View.GONE);


        //初始化RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_student.setLayoutManager(linearLayoutManager);
        studentAvatarAdapter = new StudentAvatarAdapter(this, this);
        rv_student.addItemDecoration(new StudentAvatarDividerItem());
//        rv_student.setHasFixedSize(true);
//        rv_student.setNestedScrollingEnabled(false);
        rv_student.setAdapter(studentAvatarAdapter);

        if (!StringUtils.isBlank(studentInfoList)) {
            presenter.setStudentDatas(studentInfoList, homeworkposition, homeworkid, serial);
        }

        //判断下集合数量，切换UI显示状态
        if (studentInfoList.size() == 1) {//集合数量只有1，隐藏滑动头像列表等
            if ("3".equals(studentInfoList.get(homeworkposition).getStatus())) {// Status状态为3，表示已经点评
                cl_reviews.setVisibility(View.GONE);
            }
            rv_student.setVisibility(View.GONE);
            pb_nums.setVisibility(View.GONE);
            tv_nums.setVisibility(View.GONE);
            dividingline_horizontal.setVisibility(View.GONE);
            tv_student_homework.setVisibility(View.VISIBLE);
            dividingline_horizontal2.setVisibility(View.VISIBLE);

            //设置nsv_homework的间距
            ConstraintUtil.ConstraintBegin begin = constraintUtil.begin();
            begin.setMarginTop(R.id.nsv_homework, getResources().getDimensionPixelSize(R.dimen.dimen_13x));
            begin.commit();

            // 设置tv_title的UI状态
            ConstraintUtil constraintUtil2 = new ConstraintUtil(cl_title);
            ConstraintUtil.ConstraintBegin begin2 = constraintUtil2.begin();
            begin2.clear(R.id.tv_title);
            begin2.setHeight(R.id.tv_title, WindowManager.LayoutParams.WRAP_CONTENT);
            begin2.setWidth(R.id.tv_title, WindowManager.LayoutParams.WRAP_CONTENT);
            begin2.Left_toLeftOf(R.id.tv_title, R.id.cl_title);
            begin2.Right_toRightOf(R.id.tv_title, R.id.cl_title);
            begin2.Top_toTopOf(R.id.tv_title, R.id.iv_close);
            begin2.Bottom_toBottomOf(R.id.tv_title, R.id.iv_close);
            begin2.commit();
        } else {
            tv_student_homework.setVisibility(View.GONE);
            dividingline_horizontal2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
        cl_reviews.setOnClickListener(this);

        mCommentView.setOnMoreClickListener(() -> {
            showSelectDialog();
            return null;
        });
    }

    /**
     * 点评选择弹框
     */
    private void showSelectDialog() {

        if (selectDialog == null) {
            selectDialog = new TKSelectImgDialog(this);
            mTypeList.add(getResources().getString(R.string.edit));
            mTypeList.add(getResources().getString(R.string.delete));
        }
        selectDialog.setItemContentList(mTypeList);
        selectDialog.setSelectImgListener(
                () -> {//编辑点评
                    if (studentAvatarEntitySelected.isIsfinished()) {
                        if (ScreenTools.getInstance().isPad(this)) {
                            //隐藏状态栏
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                        cl_reviews.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                presenter.showCommentDialog(studentAvatarEntitySelected, remark);
                            }
                        }, 400);
                    }
                    return null;
                },
                () -> {//删除点评
                    //删除确认弹框
                    presenter.showDeleteDialog(studentAvatarEntitySelected);
//                    if (studentAvatarEntitySelected.isIsfinished()) {
//                        presenter.deleteremark(studentAvatarEntitySelected.getHomeworkid(), studentAvatarEntitySelected.getStudentid());
//                    }
                    return null;
                });
        selectDialog.show();
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        } else if (id == R.id.cl_reviews) {
            if (!studentAvatarEntitySelected.isIsfinished()) {

                if (ScreenTools.getInstance().isPad(this)) {
                    //隐藏状态栏
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                cl_reviews.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.showCommentDialog(studentAvatarEntitySelected, null);
                    }
                }, 400);
            }
        }
    }

    @Override
    public void changeReviewedProgressBar() {
        pb_nums.setMax(maxnums);
        pb_nums.setProgress(reviewednums);
        tv_nums.setText(reviewednums + "/" + maxnums);
    }

    @Override
    public void studentWorkInfosCallback(List<StudentAvatarEntity> studentAvatarEntities, int position) {
        this.studentAvatarEntities = studentAvatarEntities;
        homeworkposition = position;

        studentAvatarAdapter.setDatas(studentAvatarEntities);
        studentAvatarAdapter.notifyDataSetChanged();

        Glide.with(this).load(studentAvatarEntities.get(position).getAvatarurl()).error(R.drawable.icon_default_head_img).into(riv_avatordetails);
        tv_studentnamedetails.setText(studentAvatarEntities.get(position).getStudentname());
        tv_worktime.setText(studentAvatarEntities.get(position).getWorkfinishedtime());

        studentAvatarEntitySelected = studentAvatarEntities.get(position);
        studentid = studentAvatarEntitySelected.getStudentid();


        maxnums = studentAvatarEntities.size();
        presenter.setReviewedProgressBar(pb_nums);


        if (studentAvatarEntitySelected.isIsfinished()) {
            //设置点评按钮背景
            ConstraintUtil.ConstraintBegin begin = constraintUtil.begin();
            begin.setHeight(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_74x));
            begin.setMarginEnd(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_10x));
            begin.setMarginBottom(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_40x));
            begin.commit();

            cl_reviews.setBackgroundResource(R.drawable.homeworkcomment_reviewed);
            tv_reviews.setText(R.string.homeworkcomment_reviewed);
            tv_reviews.setTextColor(Color.parseColor("#8F92A1"));
            iv_carryout.setVisibility(View.VISIBLE);
        } else {
            //设置点评按钮背景
            ConstraintUtil.ConstraintBegin begin = constraintUtil.begin();
            begin.setHeight(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_54x));
            begin.setMarginEnd(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_20x));
            begin.setMarginBottom(R.id.cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_50x));
            begin.commit();

            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, cl_reviews, getResources().getDimensionPixelSize(R.dimen.dimen_27x), -1, "", VariableConfig.color_button_selected);
            tv_reviews.setText(R.string.homeworkcomment_gotoreview);
            tv_reviews.setTextColor(Color.parseColor("#FFFFFF"));
            iv_carryout.setVisibility(View.GONE);


            //不可点评直接隐藏点评按钮
            if (is_remark == 0) {
                cl_reviews.setVisibility(View.GONE);
            }
        }


        // TODO 显示学生作业详情
        if (!TextUtils.isEmpty(homeworkid) && !TextUtils.isEmpty(studentid)) {
            HomeworkWritePresenter homeworkWritePresenter = new HomeworkWritePresenter(this, this);
            homeworkWritePresenter.getHomeworkStudentDetails(homeworkid, studentid);
        }
    }

    @Override
    public void showHomeworkInfo(HomeworkStudentDetailEntity entity) {
        // 学生提交了作业 或者老师点评
        if ("2".equals(entity.getStatus()) || "3".equals(entity.getStatus())) {
            // 作业详情
            mCommonView.setVisibility(View.VISIBLE);
            mCommonView.setJobHintTitle(true);
            mCommonView.setIsEditable(false);
//            mCommonView.setShowFold(false);

            mCommonView.setCommonData(TKHomeworkDetailEntity.copyHomeworkDetailEntity2Answer(entity.getAnswer()));

            // 老师点评的内容
            if ("3".equals(entity.getStatus())) {
                mCommentView.setVisibility(View.VISIBLE);
                mCommentView.setCommentData(entity.getRemark());
                this.remark = entity.getRemark();
            } else {
                mCommentView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void completedCommentsNumsCallback() {
        reviewednums++;
    }

    /**
     * 点评成功后的回调
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void homeworkremarkCallback(boolean isSuccess, String msg, TeacherCommentEntity teachercomment) {
        if (isSuccess) {
            //更改状态为已完成
            studentAvatarEntities.get(homeworkposition).setIsfinished(true);
            if (teachercomment.isFirstRemark()) {
                completedCommentsNumsCallback();
            }
            studentWorkInfosCallback(studentAvatarEntities, homeworkposition);

            //通知刷新数据
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setMessage_type(EventConstant.EVENT_REFRESHHOMEWORKDETAIL_DATA);
            EventBus.getDefault().post(messageEvent);
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }


        if (ScreenTools.getInstance().isPad(this)) {
            //显示状态栏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 删除点评回调
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void deleteRemarkCallback(boolean isSuccess, String msg) {
        if (isSuccess) {
            this.remark = null;
            studentAvatarEntities.get(homeworkposition).setIsfinished(false);
            reviewednums--;
            studentWorkInfosCallback(studentAvatarEntities, homeworkposition);
            cl_reviews.setVisibility(View.VISIBLE);


            //通知刷新数据
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setMessage_type(EventConstant.EVENT_REFRESHHOMEWORKDETAIL_DATA);
            EventBus.getDefault().post(messageEvent);
        }
        ToastUtils.showShortToastFromText(msg, ToastUtils.top);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void showLoading() {
        mLoadingDialog.showLoading();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.hideLoading();
    }

    @Override
    public void hideSuccessLoading() {
        mLoadingDialog.hideSuccessLoading();
    }

    @Override
    public void showFailed() {
        mLoadingDialog.showFailed();
    }


    // 没用
    @Override
    public void submitHomeworkSuccess(@NotNull String homeworkId, @org.jetbrains.annotations.Nullable String isDraft) {

    }

    @Override
    public void rollbackHomeworkSuccess() {

    }
}
