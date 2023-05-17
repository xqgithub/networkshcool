package com.talkcloud.networkshcool.baselibrary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.corelibrary.TKJoinRoomModel;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.HWConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherOrAssitorEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkDetailActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkPublishActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkWriteActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.LessonReportlActivity;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.StudentListAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class LessonListForMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LessonInfoEntity> mlist;

    private ClickListener listener;

    // private TeacherListAdapter teacherListAdapter;


    private OnRoomItemClickListener roomItemClickListener;

    private final int TYPE_ITEM = 0;//用于设置要填充数据的标识
    private final int TYPE_NOMOREDATE = 1;//用于数据底部没有数据时要显示ViewHolder的标识


    //用于退出activity,避免countdown，造成资源浪费。
    private SparseArray<CountDownTimer> countDownMap;


    public void setOnRoomItemClickListener(OnRoomItemClickListener listener) {
        this.roomItemClickListener = listener;
    }


    public LessonListForMainAdapter(Context context, List<LessonInfoEntity> list, ClickListener listener) {
        mContext = context;
        mlist = list;
        this.listener = listener;
        countDownMap = new SparseArray<>();
    }

    public LessonListForMainAdapter(Context context, List<LessonInfoEntity> list) {
        mContext = context;
        mlist = list;
        countDownMap = new SparseArray<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_lesson_formain, parent, false));
        } else {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.nomore_data, parent, false));

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mlist.get(position).getSerial() == null || mlist.get(position).getSerial().equals("")) {
            return TYPE_NOMOREDATE;
        } else {
            return TYPE_ITEM;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder1, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            ItemViewHolder holder = (ItemViewHolder) holder1;
            final LessonInfoEntity bean = mlist.get(position);
            String user_identity = MySPUtilsUser.getInstance().getUserIdentity();

            //将前一个缓存清除
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
            }

            //设置时间字体样式
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/din.ttf");
            holder.tv_classTime.setTypeface(typeface);


            if (bean.getTeachers() != null && bean.getTeachers().size() > 0) {


                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.rv_teacher_assitor.setLayoutManager(layoutManager);

                List<TeacherOrAssitorEntity> list = new ArrayList<>();
                list.clear();
                TeacherListAdapter teacherListAdapter = new TeacherListAdapter(mContext, list);

                list.addAll(bean.getTeachers());
                holder.rv_teacher_assitor.setHasFixedSize(true);
                holder.rv_teacher_assitor.setNestedScrollingEnabled(false);

                //holder.rv_teacher_assitor.addItemDecoration(new SpaceItemDecoration(20));
                holder.rv_teacher_assitor.setAdapter(teacherListAdapter);
                teacherListAdapter.notifyDataSetChanged();


            }

            //学生列表
            if (!StringUtils.isBlank(bean.getStudents()) && bean.getStudents().size() > 0) {
                holder.rv_student_assitor.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.rv_student_assitor.setLayoutManager(layoutManager);
                StudentListAdapter studentListAdapter = new StudentListAdapter(mContext, 0);

                holder.rv_student_assitor.setHasFixedSize(true);
                holder.rv_student_assitor.setNestedScrollingEnabled(false);

                studentListAdapter.setDatas(bean.getStudents());
                holder.rv_student_assitor.setAdapter(studentListAdapter);
                studentListAdapter.notifyDataSetChanged();
            } else {
                if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                    holder.rv_student_assitor.setVisibility(View.VISIBLE);
                } else {
                    holder.rv_student_assitor.setVisibility(View.GONE);
                }
            }


            if (bean.getState() != null) {
                if (bean.getState().equals("1")) { //未开始


                    long secondsDiff = bean.getStarttime() - System.currentTimeMillis() / 1000;  //时间差值 （秒）
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_time);

                    if (secondsDiff > 2 * 60 * 60) { //如果时差大于两小时以上 显示未开始，
                        holder.tv_classDes.setText(mContext.getString(R.string.course_not_start));
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        holder.tv_classDes.setCompoundDrawablePadding(20);
                        holder.tv_classDes.setCompoundDrawables(drawable, null, null, null);//画在左边
                        holder.tv_timeCountdown.setVisibility(View.GONE);
                        holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                    } else if (secondsDiff <= 0) {  //到了上课时间 老师未点击上课，显示未开始

                        holder.tv_classDes.setText(mContext.getString(R.string.course_not_start));
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                        holder.tv_classDes.setCompoundDrawablePadding(20);
                        holder.tv_classDes.setCompoundDrawables(drawable, null, null, null);//画在左边
                        holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                        holder.tv_timeCountdown.setVisibility(View.GONE);

                    } else { //显示倒计时
                        holder.tv_timeCountdown.setVisibility(View.VISIBLE); //显示倒计时view
                        holder.tv_classDes.setCompoundDrawablePadding(0);
                        holder.tv_classDes.setCompoundDrawables(null, null, null, null);//去掉drawableLeft图片
                        holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.text_next_class));
                        holder.tv_classDes.setText(mContext.getString(R.string.class_countdown));
                    }
                    // holder.tv_classDes.setVisibility(View.VISIBLE);
                    /**
                     * 如果不是今天的课程不开始倒计时，否则开启（未开始的课节）
                     */
                    if (StringUtil.isSameData(System.currentTimeMillis() + "", (long) bean.getStarttime() * 1000 + "")) { //


                        if (secondsDiff > 0) {
                            holder.countDownTimer = new CountDownTimer(secondsDiff * 1000 + 500, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    holder.tv_timeCountdown.setText(StringUtil.getCountTimeByLong(millisUntilFinished));
                                    if (millisUntilFinished / 1000 <= 2 * 60 * 60) {  //如果倒计时小于了2小时，则显示倒计时的ui
                                        holder.tv_timeCountdown.setVisibility(View.VISIBLE); //显示倒计时view
                                        holder.tv_classDes.setCompoundDrawablePadding(0);
                                        holder.tv_classDes.setCompoundDrawables(null, null, null, null);//去掉drawableLeft图片
                                        holder.tv_classDes.setText(mContext.getString(R.string.class_countdown));
                                        holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.text_next_class));

                                    }
                                    if (millisUntilFinished / 1000 <= bean.getBefore_enter()) { //如果倒计时到了提前进入教室时间，设置enter按钮能点击进入
                              /*      PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, holder.tv_enter_room,
                                            mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);
                                    holder.tv_enter_room.setTextColor(mContext.getResources().getColor(R.color.white));*/
//                                        enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                                        teacherStudentUIExhibit(user_identity, bean, holder, true);
                                    }


                                    //显示学习预习按钮
                                    showPrepareLessons(user_identity, bean, millisUntilFinished, holder);

                                }

                                public void onFinish() {
                                    /*               *//**
                                     * 显示正在上课
                                     *//*
                                    holder.tv_classDes.setText(mContext.getString(R.string.course_doing));
                                    holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));
                                    holder.tv_timeCountdown.setVisibility(View.GONE);
                                    holder.tv_enter_room.setVisibility(View.VISIBLE);*/

                                    /**
                                     *  不显示文案，只显示进入教室的按钮  todo  后面会调整逻辑
                                     */
                                    //     holder.tv_classDes.setText(mContext.getString(R.string.course_not_start));
                                    //    holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));
                                    holder.tv_classDes.setVisibility(View.GONE);

                                    holder.tv_timeCountdown.setVisibility(View.GONE);
//                                    holder.tv_enter_room.setVisibility(View.VISIBLE);
                                    if (ScreenTools.getInstance().isPad(mContext)) {
                                        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                                            if ("4".equals(bean.getRoomtype())) {
                                                holder.tv_enter_room_teacher.setVisibility(View.GONE);
                                            } else {
                                                holder.tv_enter_room_teacher.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            holder.tv_enter_room.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        holder.tv_enter_room.setVisibility(View.VISIBLE);
                                    }

                                    //显示学习预习按钮
                                    showPrepareLessons(user_identity, bean, 0L, holder);

                                }
                            }.start();

                            countDownMap.put(holder.tv_timeCountdown.hashCode(), holder.countDownTimer);
                        } else {

                        }
                    } else {
                        //显示学习预习按钮
                        showPrepareLessons(user_identity, bean, 0L, holder);
                    }
                    if (bean.getBefore_enter() > 0) { //设置了提前进入教室时间
                        if (bean.getStarttime() - System.currentTimeMillis() / 1000 <= bean.getBefore_enter()) { //到了提前时间，显示进入房间按钮
           /*             PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, holder.tv_enter_room,
                                mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);
                        holder.tv_enter_room.setTextColor(mContext.getResources().getColor(R.color.white));*/
//                            enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                            teacherStudentUIExhibit(user_identity, bean, holder, true);

                           /* if (bean.getRoomtype() != null) {
                                //不是一对一 一对多课程，则不能在移动端进入房间 ，提示pc进入 (只针对老师身份)
                                if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                                    if (!bean.getRoomtype().equals("0") && !bean.getRoomtype().equals("3")) {
                                        holder.tv_classTips.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.tv_classTips.setVisibility(View.GONE);
                                    }
                                }
                            }*/
                        } else {
//                            enterRoomStatusChange(holder.tv_enter_room, false, bean.getRoomtype(), holder.tv_classTips);
                            //  holder.tv_enter_room.setTextColor(getResources().getColor(R.color.text_main_black_color));
                            teacherStudentUIExhibit(user_identity, bean, holder, false);
                        }
                    } else {  //没有设置时间，尚未开始的课节，可以显示进入房间按钮，并点亮 表示可以直接进入
//                        enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                        teacherStudentUIExhibit(user_identity, bean, holder, true);
                    }
                    holder.ll_complete.setVisibility(View.GONE);
                    holder.tv_classDes.setVisibility(View.VISIBLE);

                    if (bean.getRoomtype() != null) {
                        //不是一对一 一对多课程，则不能在移动端进入房间 ，提示pc进入 (只针对老师身份)
                        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                            if (bean.getRoomtype().equals("4")) {
                                holder.tv_classTips.setVisibility(View.VISIBLE);
                                if (ScreenTools.getInstance().isPad(mContext)) {
                                    holder.tv_enter_room_teacher.setVisibility(View.GONE);
                                } else {
                                    holder.tv_enter_room.setVisibility(View.GONE);
                                }
                            } else {
                                holder.tv_classTips.setVisibility(View.GONE);

                                if (ScreenTools.getInstance().isPad(mContext)) {
                                    holder.tv_enter_room_teacher.setVisibility(View.VISIBLE);
                                } else {
                                    holder.tv_enter_room.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        //  classTips.setVisibility(View.VISIBLE);
                    }


                    //显示老师备课按钮
                    showPreviewLessons(user_identity, bean, holder);

                } else if (bean.getState().equals("2")) { //进行中
                    holder.tv_classDes.setText(mContext.getString(R.string.course_doing));
                    holder.tv_classDes.setVisibility(View.VISIBLE);
                    holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));

                    holder.tv_timeCountdown.setVisibility(View.GONE);
//                    enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                    teacherStudentUIExhibit(user_identity, bean, holder, true);
                    holder.ll_complete.setVisibility(View.GONE);

                    if (bean.getRoomtype() != null) {
                        //不是一对一 一对多课程，则不能在移动端进入房间 ，提示pc进入 (只针对老师身份)
                        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                            if (bean.getRoomtype().equals("4")) {
                                holder.tv_classTips.setVisibility(View.VISIBLE);
                                if (ScreenTools.getInstance().isPad(mContext)) {
                                    holder.tv_enter_room_teacher.setVisibility(View.GONE);
                                } else {
                                    holder.tv_enter_room.setVisibility(View.GONE);
                                }
                            } else {
                                holder.tv_classTips.setVisibility(View.GONE);
                                if (ScreenTools.getInstance().isPad(mContext)) {
                                    holder.tv_enter_room_teacher.setVisibility(View.VISIBLE);
                                } else {
                                    holder.tv_enter_room.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        //  classTips.setVisibility(View.VISIBLE);
                    }

                    /*** 备课 、预习 ***/
                    holder.tv_lesson_preparation.setVisibility(View.GONE);
                    holder.tv_lesson_preview.setVisibility(View.GONE);

                } else if (bean.getState().equals("3")) { //已结束

                    if (bean.getEndtime() > System.currentTimeMillis() / 1000) { //可能未结束
        /*                holder.tv_classDes.setText(mContext.getString(R.string.course_doing));
                        holder.tv_classDes.setVisibility(View.VISIBLE);
                        holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));*/
                        holder.tv_classDes.setVisibility(View.GONE); //不显示状态文案
                        holder.tv_timeCountdown.setVisibility(View.GONE);
//                        enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                        teacherStudentUIExhibit(user_identity, bean, holder, true);
                        holder.ll_complete.setVisibility(View.GONE);

                        if (bean.getRoomtype() != null) {
                            //不是一对一 一对多课程，则不能在移动端进入房间 ，提示pc进入 (只针对老师身份)
                            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                                if (bean.getRoomtype().equals("4")) {
                                    holder.tv_classTips.setVisibility(View.VISIBLE);
                                    if (ScreenTools.getInstance().isPad(mContext)) {
                                        holder.tv_enter_room_teacher.setVisibility(View.GONE);
                                    } else {
                                        holder.tv_enter_room.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.tv_classTips.setVisibility(View.GONE);
                                    if (ScreenTools.getInstance().isPad(mContext)) {
                                        holder.tv_enter_room_teacher.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.tv_enter_room.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            //  classTips.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder.tv_classDes.setVisibility(View.GONE);
                        holder.tv_timeCountdown.setVisibility(View.GONE);
                        holder.ll_complete.setVisibility(View.VISIBLE);
                        if (ScreenTools.getInstance().isPad(mContext)) {
                            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                                holder.tv_enter_room_teacher.setVisibility(View.GONE);
                            } else {
                                holder.tv_enter_room.setVisibility(View.GONE);
                            }
                        } else {
                            holder.tv_enter_room.setVisibility(View.GONE);
                        }
                    }


                    /*** 备课 、预习 ***/
                    holder.tv_lesson_preparation.setVisibility(View.GONE);
                    holder.tv_lesson_preview.setVisibility(View.GONE);

                } else if (bean.getState().equals("4")) { //已过期
                    holder.tv_classDes.setText(mContext.getString(R.string.expired));
                    holder.tv_classDes.setVisibility(View.VISIBLE);
                    holder.tv_classDes.setTextColor(ContextCompat.getColor(mContext, R.color.text_next_class));
                    holder.tv_classDes.setCompoundDrawables(null, null, null, null);//去掉drawableLeft图片


                    holder.tv_timeCountdown.setVisibility(View.GONE);
                    holder.ll_complete.setVisibility(View.GONE);
                    if (ScreenTools.getInstance().isPad(mContext)) {
                        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                            holder.tv_enter_room_teacher.setVisibility(View.GONE);
                        } else {
                            holder.tv_enter_room.setVisibility(View.GONE);
                        }
                    } else {
                        holder.tv_enter_room.setVisibility(View.GONE);
                    }

                    /*** 备课 、预习 ***/
                    holder.tv_lesson_preparation.setVisibility(View.GONE);
                    holder.tv_lesson_preview.setVisibility(View.GONE);
                }
            }
            holder.tv_className.setText(bean.getRoomname());
            holder.tv_classType.setText(bean.getType_name());
            holder.tv_classTime.setText(StringUtil.stampToTime(bean.getStarttime()));

            List<LessonInfoEntity.Homework> homeworkList = bean.getHomeworks();
            //如果是老师，不显示报告按钮
            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                holder.tv_comment.setVisibility(View.GONE);
                if (homeworkList != null && !homeworkList.isEmpty()) {  //老师 有作业显示查作业 否则显示发布作业
                    holder.tv_task.setText(mContext.getString(R.string.review_homework));
                } else {
                    holder.tv_task.setText(mContext.getString(R.string.pub_homework));
                }

            } else {
//                if (homeworkList != null && !homeworkList.isEmpty()) {
//                    holder.tv_task.setVisibility(View.VISIBLE);
//                } else {
//                    holder.tv_task.setVisibility(View.GONE);
//                }
                //  学生作业按钮一直显示 弹提示
                holder.tv_task.setVisibility(View.VISIBLE);
            }
            //   holder.tv_classDes.setText(bean.getType_name());
            //   holder.tv_timeCountdown.setText(bean.getRoomname());

            holder.tv_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 老师
                    if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {

                        //  根据作业的状态 跳转页面
                        if (homeworkList != null && !homeworkList.isEmpty()) {
                            // 0否1是 不是草稿 跳详情
                            if (HWConstant.DraftStatus.IS_NOT_DRAFT.equals(homeworkList.get(0).getIs_draft())) {
                                Intent intent = new Intent(mContext, HomeworkDetailActivity.class);
                                intent.putExtra("homeworkId", homeworkList.get(0).getId());
                                intent.putExtra("serial", bean.getSerial());

                                mContext.startActivity(intent);
                            } else {
                                // 编辑作业
                                Intent intent = new Intent(mContext, HomeworkPublishActivity.class);
                                intent.putExtra(BaseConstant.KEY_PARAM1, homeworkList.get(0).getId());
                                intent.putExtra(BaseConstant.KEY_PARAM2, bean.getSerial());

                                mContext.startActivity(intent);
                            }
                        } else {
                            // 发布作业
                            Intent intent = new Intent(mContext, HomeworkPublishActivity.class);
                            intent.putExtra(BaseConstant.KEY_PARAM2, bean.getSerial());
                            intent.putExtra(BaseConstant.KEY_PARAM3, DateUtil.formatDate2YMD(bean.getStarttime()) + " " + bean.getRoomname());
                            mContext.startActivity(intent);
                        }

                    } else {

                        if (homeworkList != null && !homeworkList.isEmpty()) {

                            Intent intent = new Intent(mContext, HomeworkWriteActivity.class);
                            intent.putExtra(BaseConstant.KEY_PARAM1, homeworkList.get(0).getId());
                            intent.putExtra(BaseConstant.KEY_PARAM2, homeworkList.get(0).getStudent_id());
                            mContext.startActivity(intent);
                        } else {
//                            Toast.makeText(mContext, mContext.getString(R.string.hw_s_nohomework_tip), Toast.LENGTH_LONG).show();
                            ToastUtils.showShortTop(mContext.getString(R.string.hw_s_nohomework_tip));
                        }

                    }
                }
            });
            holder.tv_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LessonReportlActivity.class);
                    if (bean.getSerial() != null)
                        intent.putExtra("lessonId", bean.getSerial());
                    mContext.startActivity(intent);
                }
            });
            holder.tv_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (roomItemClickListener != null) {
                        roomItemClickListener.onJoinPlayBackRoom(bean.getSerial());
                    }
                }
            });
            //  holder.tv_classTips.setText(bean.getRoomname());
            if (holder.tv_enter_room.isClickable()) {
                holder.tv_enter_room.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (roomItemClickListener != null) {

                            if (bean != null && bean.getUser() != null) {

                                TKJoinRoomModel model = new TKJoinRoomModel();
//                                model.setSerial(bean.getSerial());
                                model.setUserId(bean.getUser().getUserid());
//                                String nickName = TextUtils.isEmpty(AppPrefsUtil.getUserName()) ? MySPUtilsUser.getInstance().getUserMobile() : AppPrefsUtil.getUserName();
//                                model.setNickName(nickName);
                                model.setNickName(bean.getUser().getNickname());
                                int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
                                model.setUserRole(role);

                                roomItemClickListener.onJoinRoom(bean.getSerial(), model);
                            } else {
                                roomItemClickListener.onJoinRoom(bean.getSerial(), null);
                            }
                        }
                    }
                });
            }

            if (holder.tv_enter_room_teacher.isClickable()) {
                holder.tv_enter_room_teacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (roomItemClickListener != null) {

                            if (bean != null && bean.getUser() != null) {

                                TKJoinRoomModel model = new TKJoinRoomModel();
//                                model.setSerial(bean.getSerial());
                                model.setUserId(bean.getUser().getUserid());
//                                String nickName = TextUtils.isEmpty(AppPrefsUtil.getUserName()) ? MySPUtilsUser.getInstance().getUserMobile() : AppPrefsUtil.getUserName();
//                                model.setNickName(nickName);
                                model.setNickName(bean.getUser().getNickname());
                                int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
                                model.setUserRole(role);

                                roomItemClickListener.onJoinRoom(bean.getSerial(), model);
                            } else {
                                roomItemClickListener.onJoinRoom(bean.getSerial(), null);
                            }
                        }
                    }
                });
            }

            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(bean);
                    }
                });
            }

            if (bean.getStarttime() > 0) {  //如果在开课前两小时 显示倒计时

            }
        } else {
            //  NoMoreViewHolder holderNomore = (NoMoreViewHolder) holder1;
        }
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_classTime;
        private TextView tv_classType;
        private TextView tv_classDes;
        private TextView tv_timeCountdown;
        private TextView tv_className;
        private RecyclerView rv_teacher_assitor;
        private TextView tv_task;
        private TextView tv_comment;
        private TextView tv_reply;
        private TextView tv_classTips;
        private TextView tv_enter_room;
        private TextView tv_enter_room_teacher;
        private LinearLayout ll_complete;
        public CountDownTimer countDownTimer;
        public TextView tv_lesson_preparation;
        public TextView tv_lesson_preview;
        public RecyclerView rv_student_assitor;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_classTime = itemView.findViewById(R.id.class_time);
            tv_classType = itemView.findViewById(R.id.class_type);
            tv_classDes = itemView.findViewById(R.id.class_des);
            tv_timeCountdown = itemView.findViewById(R.id.time_countdown);
            tv_className = itemView.findViewById(R.id.class_name);
            tv_task = itemView.findViewById(R.id.tv_task);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_reply = itemView.findViewById(R.id.tv_reply);
            tv_classTips = itemView.findViewById(R.id.class_tips);
            tv_enter_room = itemView.findViewById(R.id.enter_room);
            tv_enter_room_teacher = itemView.findViewById(R.id.tv_enter_room_teacher);
            rv_teacher_assitor = itemView.findViewById(R.id.rv_teacher_assitor);
            ll_complete = itemView.findViewById(R.id.ll_complete);
            tv_lesson_preparation = itemView.findViewById(R.id.tv_lesson_preparation);
            tv_lesson_preview = itemView.findViewById(R.id.tv_lesson_preview);
            rv_student_assitor = itemView.findViewById(R.id.rv_student_assitor);
        }
    }

    static class NoMoreViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_des;


        public NoMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_des = itemView.findViewById(R.id.tv_des);

        }
    }

    public interface ClickListener {
        void onClick(LessonInfoEntity bean);
    }

    private void enterRoomStatusChange(TextView view, Boolean isClickable, String roomType, TextView classTips) {
        view.setVisibility(View.VISIBLE);
        if (isClickable) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, view,
                    ScreenUtil.dp2px(mContext, 15), -1, "", VariableConfig.color_button_selected);
            view.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            view.setClickable(true);
        } else {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, view,
                    ScreenUtil.dp2px(mContext, 15), -1, "", "#FFF7F8F9");
            view.setTextColor(ContextCompat.getColor(mContext, R.color.my_role_color));

            view.setClickable(false);
        }

        if (roomType != null) {
            //不是一对一 一对多课程，则不能在移动端进入房间 ，提示pc进入 (只针对老师身份)
            String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                if (roomType.equals("4")) {
//                    classTips.setVisibility(View.VISIBLE);
                    view.setVisibility(View.GONE);
                } else {
//                    classTips.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        Log.d("TAG", "size :  " + countDownMap.size());
        for (int i = 0, length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    /**
     * 显示老师备课按钮
     */
    private void showPreviewLessons(String user_identity, LessonInfoEntity bean, ItemViewHolder holder) {
        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {//老师身份
            if (!StringUtils.isBlank(bean.getPreview_lessons()) && bean.getPreview_lessons().getSwitchX().equals("1")) {
                holder.tv_lesson_preview.setVisibility(View.GONE);
                holder.tv_lesson_preparation.setVisibility(View.VISIBLE);

                //设置备课的背景
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, holder.tv_lesson_preparation,
                        mContext.getResources().getDimensionPixelSize(R.dimen.dimen_20x),
                        -1, "", "#05CBE7");
                holder.tv_lesson_preparation.setOnClickListener(v -> {
                    roomItemClickListener.onLessonPreparation(bean.getSerial());
                });
            } else {
                holder.tv_lesson_preparation.setVisibility(View.GONE);
                holder.tv_lesson_preview.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示学生预习按钮
     */
    private void showPrepareLessons(String user_identity, LessonInfoEntity bean, Long millisUntilFinished, ItemViewHolder holder) {
        //学生身份，预习开关打开
        if (user_identity.equals(ConfigConstants.IDENTITY_STUDENT)) {
            if (!StringUtils.isBlank(bean.getPrepare_lessons()) && bean.getPrepare_lessons().getSwitchX().equals("1")) {
//                LogUtils.i(ConfigConstants.TAG_ALL,
//                        "倒计时 =-= " + millisUntilFinished / 1000,
//                        "预习 配置时间 =-= " + bean.getPrepare_lessons().getTimes(),
//                        "对比时间" + (millisUntilFinished / 1000 <= bean.getPrepare_lessons().getTimes()));
                if (millisUntilFinished / 1000 <= bean.getPrepare_lessons().getTimes()) {
                    holder.tv_lesson_preview.setVisibility(View.VISIBLE);
                    holder.tv_lesson_preparation.setVisibility(View.GONE);


                    float CornerRadius;
                    if (ScreenTools.getInstance().isPad(mContext)) {
                        CornerRadius = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x);
                    } else {
                        CornerRadius = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_22x);
                    }
                    //设置预习的背景
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, holder.tv_lesson_preview,
                            CornerRadius,
                            mContext.getResources().getDimensionPixelSize(R.dimen.dimen_1x), VariableConfig.color_button_selected, VariableConfig.color_transparent_bg);


                    holder.tv_lesson_preview.setOnClickListener(v -> {
                        roomItemClickListener.onLessonPreparation(bean.getSerial());
                    });
                }
            } else {
                holder.tv_lesson_preparation.setVisibility(View.GONE);
                holder.tv_lesson_preview.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 根据老师端或者学生端展示UI
     */
    private void teacherStudentUIExhibit(String user_identity, LessonInfoEntity bean, ItemViewHolder holder, Boolean isClickable) {
        if (ScreenTools.getInstance().isPad(mContext)) {
            if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                enterRoomStatusChange(holder.tv_enter_room_teacher, true, bean.getRoomtype(), holder.tv_classTips);
                holder.tv_enter_room.setVisibility(View.GONE);
            } else {
                enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
                holder.tv_enter_room_teacher.setVisibility(View.GONE);
            }
        } else {
            enterRoomStatusChange(holder.tv_enter_room, true, bean.getRoomtype(), holder.tv_classTips);
        }
    }
}
