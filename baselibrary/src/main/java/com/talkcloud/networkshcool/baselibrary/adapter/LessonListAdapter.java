package com.talkcloud.networkshcool.baselibrary.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.talkcloud.corelibrary.TKJoinRoomModel;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.HWConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkDetailActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkPublishActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.HomeworkWriteActivity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.LessonReportlActivity;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;

import java.util.List;


public class LessonListAdapter extends BaseQuickAdapter<LessonInfoEntity, BaseViewHolder> {

    public LessonListAdapter(int layoutResId, @Nullable List<LessonInfoEntity> data) {
        super(layoutResId, data);
    }

    private OnRoomItemClickListener roomItemClickListener;

    public void setOnRoomItemClickListener(OnRoomItemClickListener listener) {
        this.roomItemClickListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, LessonInfoEntity bean) {


        TextView tv_classTime = helper.getView(R.id.class_time);
        TextView tv_not_start = helper.getView(R.id.tv_not_start);
        TextView tv_dot = helper.getView(R.id.class_time_dot);
        TextView tv_className = helper.getView(R.id.class_name);
        TextView tv_task = helper.getView(R.id.tv_task);
        TextView tv_comment = helper.getView(R.id.tv_comment);
        TextView tv_reply = helper.getView(R.id.tv_reply);
        ImageView img_status = helper.getView(R.id.img_status);
        TextView tv_enter_room = helper.getView(R.id.enter_room);
        View tv_line_status = helper.getView(R.id.line_status);
        LinearLayout ll_class_complete = helper.getView(R.id.ll_class_complete);
        LinearLayout ll_classtime = helper.getView(R.id.ll_classtime);
        //如果是老师，不显示报告按钮

        String user_identity = MySPUtilsUser.getInstance().getUserIdentity();

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) ll_classtime.getLayoutParams();
        lp1.bottomMargin = ScreenUtil.dp2px(mContext, 0);
        ll_classtime.setLayoutParams(lp1);

        List<LessonInfoEntity.Homework> homeworkList = bean.getHomeworks();


        NSLog.d(" bean: " + bean);

        if (bean.getState().equals("1")) { //未开始
            img_status.setImageResource(R.drawable.shape_oval_gray);
            ll_class_complete.setVisibility(View.GONE);
            tv_line_status.setBackgroundResource(R.drawable.dottde_v_line);
            tv_not_start.setVisibility(View.VISIBLE);
            tv_not_start.setText(R.string.course_not_start);
            tv_dot.setVisibility(View.VISIBLE);
        /*    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_classtime.getLayoutParams();
            lp.bottomMargin = ScreenUtil.dp2px(mContext, 30);
            ll_classtime.setLayoutParams(lp);*/

            //设置了提前进入教室时间并且没到提前时间，设置不可点击
            if (bean.getBefore_enter() > 0 && bean.getStarttime() - System.currentTimeMillis() / 1000 > bean.getBefore_enter()) {
                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_enter_room,
                        ScreenUtil.dp2px(mContext, 15), -1, "", "#FFF7F8F9");
                tv_enter_room.setTextColor(ContextCompat.getColor(mContext, R.color.my_role_color));

                tv_enter_room.setClickable(false);
            } else {

                PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_enter_room,
                        ScreenUtil.dp2px(mContext, 15), -1, "", VariableConfig.color_button_selected);
                tv_enter_room.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                tv_enter_room.setClickable(true);

                //  ll_class_complete.setVisibility(View.GONE);
            }
            if (bean.isIs_join()) {
                tv_enter_room.setVisibility(View.VISIBLE);
            } else {
                tv_enter_room.setVisibility(View.GONE);
            }

        } else if (bean.getState().equals("2")) { //进行中
            img_status.setImageResource(R.drawable.shape_oval_green);
            ll_class_complete.setVisibility(View.GONE);
            if (bean.isIs_join()) {
                tv_enter_room.setVisibility(View.VISIBLE);
            } else {
                tv_enter_room.setVisibility(View.GONE);
            }
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_enter_room.getLayoutParams();
            lp.bottomMargin = ScreenUtil.dp2px(mContext, 30);
            // tv_enter_room.setLayoutParams(lp);
            tv_line_status.setBackgroundResource(R.drawable.dottde_v_line);
            tv_enter_room.setClickable(true);
            // tv_not_start.setVisibility(View.GONE);
            //  tv_dot.setVisibility(View.GONE);
            tv_not_start.setVisibility(View.VISIBLE);
            tv_dot.setVisibility(View.VISIBLE);
            tv_not_start.setText(mContext.getResources().getString(R.string.in_class));
        } else if (bean.getState().equals("3")) { //已结束
            img_status.setImageResource(R.mipmap.ic_complete);
            ll_class_complete.setVisibility(View.VISIBLE);
            tv_line_status.setBackgroundResource(R.color.line_class);
            tv_enter_room.setVisibility(View.GONE);
            tv_not_start.setVisibility(View.GONE);
            tv_dot.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_line_status.getLayoutParams();
            lp.bottomMargin = ScreenUtil.dp2px(mContext, 8);
            lp.topMargin = ScreenUtil.dp2px(mContext, 10);
            tv_line_status.setLayoutParams(lp);
        } else if(bean.getState().equals("4")) { //已过期
            img_status.setImageResource(R.drawable.shape_oval_gray);
            tv_line_status.setBackgroundResource(R.color.line_class);
            ll_class_complete.setVisibility(View.GONE);
            tv_dot.setVisibility(View.VISIBLE);
            tv_not_start.setVisibility(View.VISIBLE);
            tv_not_start.setText(mContext.getResources().getString(R.string.expired));

        }

        /*****  暂时的判断，二期大概率要更改 start *****/
        tv_enter_room.setVisibility(View.GONE); //进入教室暂时都不显示，二期后台做优化
        if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {  //如果是老师，不显示报告按钮，如果是学生，判断是否是当前用户的课节，是的话就显示回放按钮，否则不显示回放按钮
            tv_comment.setVisibility(View.GONE);
            if (bean.isIs_join()) {
                tv_reply.setVisibility(View.VISIBLE);
            } else {
                tv_reply.setVisibility(View.GONE);
            }

            //老师 有作业显示查作业 否则显示发布作业
            if (homeworkList != null && !homeworkList.isEmpty()) {  //老师 有作业显示查作业 否则显示发布作业
                tv_task.setText(mContext.getString(R.string.review_homework));
            } else {
                tv_task.setText(mContext.getString(R.string.pub_homework));
            }
        } else {
            if (bean.isIs_join()) {
                tv_reply.setVisibility(View.VISIBLE);
                tv_comment.setVisibility(View.VISIBLE);
            } else {
                tv_reply.setVisibility(View.GONE);
                tv_comment.setVisibility(View.GONE);
            }
        }

        if (tv_comment.getVisibility() == View.GONE && tv_reply.getVisibility() == View.GONE) { //
            ll_class_complete.setVisibility(View.GONE);
        }

        if (ll_class_complete.getVisibility() == View.GONE) {

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_classtime.getLayoutParams();
            lp.bottomMargin = ScreenUtil.dp2px(mContext, 30);
            ll_classtime.setLayoutParams(lp);
        } else {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_classtime.getLayoutParams();
            lp.bottomMargin = ScreenUtil.dp2px(mContext, 0);
            ll_classtime.setLayoutParams(lp);
        }
        /*****  暂时的判断，二期大概率要更改 end *****/

        if (bean.getRoomname() != null)
            tv_className.setText(bean.getRoomname());
        if (bean.getStart_to_end_time() != null && bean.getStart_date() != null)
            tv_classTime.setText(bean.getStart_date() + " " + bean.getStart_to_end_time());

        tv_task.setOnClickListener(v -> {
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
//                    Toast.makeText(mContext, mContext.getString(R.string.hw_s_nohomework_tip), Toast.LENGTH_LONG).show();
                    ToastUtils.showShortTop(mContext.getString(R.string.hw_s_nohomework_tip));
                }

            }

        });
        tv_comment.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, LessonReportlActivity.class);
            if (bean.getSerial() != null)
                intent.putExtra("lessonId", bean.getSerial());
            mContext.startActivity(intent);
        });
        tv_reply.setOnClickListener(v -> {
            if (roomItemClickListener != null) {

                roomItemClickListener.onJoinPlayBackRoom(bean.getSerial());
            }
        });
        if (tv_enter_room.isClickable()) {

            tv_enter_room.setOnClickListener(v -> {

                if (roomItemClickListener != null) {
                    if (bean != null && bean.getUser() != null) {
                        TKJoinRoomModel model = new TKJoinRoomModel();
                        model.setUserId(bean.getUser().getUserid());
//                        String nickName = TextUtils.isEmpty(AppPrefsUtil.getUserName()) ? MySPUtilsUser.getInstance().getUserMobile() : AppPrefsUtil.getUserName();
//                        model.setNickName(nickName);
                        model.setNickName(bean.getUser().getNickname());
                        int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
                        model.setUserRole(role);
                        roomItemClickListener.onJoinRoom(bean.getSerial(), model);
                    } else {
                        roomItemClickListener.onJoinRoom(bean.getSerial(), null);
                    }
                }
            });
        }
    }
}
