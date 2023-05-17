package com.talkcloud.networkshcool.baselibrary.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkInfoEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;

import java.util.List;


public class HomeworkListAdapter extends BaseQuickAdapter<HomeworkInfoEntity, BaseViewHolder> {

    private OnMoreClickListener clickListener;

    public void setOnMoreClickListener(OnMoreClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public HomeworkListAdapter(int layoutResId, @Nullable List<HomeworkInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeworkInfoEntity bean) {
        String user_identity = MySPUtilsUser.getInstance().getUserIdentity();


        LinearLayout teacher_pubulished_ll = helper.getView(R.id.teacher_pubulished_ll);
        LinearLayout goto_publish_ll = helper.getView(R.id.goto_publish_ll);
        LinearLayout goto_complete_ll = helper.getView(R.id.goto_complete_ll);
        LinearLayout complete_ll = helper.getView(R.id.complete_ll);
        LinearLayout item_ll = helper.getView(R.id.item_ll);
        if (helper.getLayoutPosition() == 0) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) helper.getConvertView().getLayoutParams();
            lp.topMargin = ScreenUtil.dp2px(mContext, 10);
            helper.getConvertView().setLayoutParams(lp);
        }

        TextView tv_homework_title = helper.getView(R.id.tv_homework_title);
        ImageView img_top_back = helper.getView(R.id.img_top_back);
        TextView tv_time_des = helper.getView(R.id.tv_time_des);

        TextView tv_nocommit_num = helper.getView(R.id.tv_nocommit_num);
        TextView tv_commit_num = helper.getView(R.id.tv_commit_num);
        TextView tv_norate_num = helper.getView(R.id.tv_norate_num);
        TextView tv_goto_complete = helper.getView(R.id.tv_goto_complete);
        TextView tv_goto_publish = helper.getView(R.id.tv_goto_publish);
        TextView tv_students_draft = helper.getView(R.id.tv_students_draft);

        TextView tv_file_num = helper.getView(R.id.tv_file_num);
        TextView tv_student_complete_staus = helper.getView(R.id.tv_student_complete_staus);

        if (bean != null) {
            tv_homework_title.setText(bean.getTitle());
            if (bean.getStatus() != null && user_identity.equals(ConfigConstants.IDENTITY_STUDENT)) {  //学生身份的作业
                img_top_back.setVisibility(View.GONE);
                String status = bean.getStatus();
                goto_publish_ll.setVisibility(View.GONE);
                teacher_pubulished_ll.setVisibility(View.GONE);


                if (status.equals("1")) { //未提交 (未完成)
                    goto_complete_ll.setVisibility(View.VISIBLE);
                    complete_ll.setVisibility(View.GONE);
                    tv_students_draft.setVisibility(View.GONE);

                    //     bean.setFile_num(2);
                    if (bean.getFiles() > 0) {
                        tv_file_num.setVisibility(View.VISIBLE);
                        tv_file_num.setText(bean.getFiles() + "");
                    } else {
                        tv_file_num.setVisibility(View.GONE);
                    }

                    if (bean.getCreate_time() != 0) {  //03-10 11:21 提交 。 6人已提交
                        tv_time_des.setText(StringUtil.stampToMonth(bean.getCreate_time()) + " " + mContext.getResources().getString(R.string.publish) +
                                " " + mContext.getResources().getString(R.string.course_dot) + " " + bean.getSubmits()
                                + mContext.getResources().getString(R.string.people_submitted));
                    }
                    tv_goto_complete.setText(mContext.getResources().getString(R.string.gotocomplete));

                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_goto_complete,
                            ScreenUtil.dp2px(mContext, 22), -1, "", VariableConfig.color_button_selected);
                } else if (status.equals("0")) {  //已提交草稿 (未完成)
                    goto_complete_ll.setVisibility(View.VISIBLE);
                    complete_ll.setVisibility(View.GONE);
                    tv_students_draft.setVisibility(View.VISIBLE);
                    if (bean.getFiles() > 0) {
                        tv_file_num.setVisibility(View.VISIBLE);
                        tv_file_num.setText(bean.getFiles() + "");
                    } else {
                        tv_file_num.setVisibility(View.GONE);
                    }
                    tv_goto_complete.setText(mContext.getResources().getString(R.string.gotosubmit));

                    if (bean.getCreate_time() != 0) {  //03-10 11:21 提交 。 6人已提交
                        tv_time_des.setText(StringUtil.stampToMonth(bean.getCreate_time()) + " " + mContext.getResources().getString(R.string.publish) +
                                " " + mContext.getResources().getString(R.string.course_dot) + " " + bean.getSubmits()
                                + mContext.getResources().getString(R.string.people_submitted));
                    }

                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_goto_complete,
                            ScreenUtil.dp2px(mContext, 22), -1, "", VariableConfig.color_button_selected);
                } else if (status.equals("2")) {  //已提交 (已完成)
                    goto_complete_ll.setVisibility(View.GONE);

                    complete_ll.setVisibility(View.VISIBLE);
                    tv_student_complete_staus.setText(mContext.getResources().getString(R.string.commited));

                    if (bean.getSubmit_time() != null) {  //03-10 11:21 提交 。 6人已提交
                        tv_time_des.setText(StringUtil.stampToMonth(Long.parseLong(bean.getSubmit_time())) + " " + mContext.getResources().getString(R.string.feedback_submit) +
                                " " + mContext.getResources().getString(R.string.course_dot) + " " + bean.getSubmits()
                                + mContext.getResources().getString(R.string.people_submitted));
                    }
                } else { // 已批阅 (已完成)
                    goto_complete_ll.setVisibility(View.GONE);
                    complete_ll.setVisibility(View.VISIBLE);
                    tv_student_complete_staus.setText(mContext.getResources().getString(R.string.rated));
                    tv_student_complete_staus.setTextColor(ContextCompat.getColor(mContext, R.color.text_next_class));

                    if (bean.getSubmit_time() != null) {  //03-10 11:21 提交 。 6人已提交
                        tv_time_des.setText(StringUtil.stampToMonth(Long.parseLong(bean.getSubmit_time())) + " " + mContext.getResources().getString(R.string.feedback_submit) +
                                " " + mContext.getResources().getString(R.string.course_dot) + " " + bean.getSubmits()
                                + mContext.getResources().getString(R.string.people_submitted));
                    }
                }
            } else if (bean.getStatus() != null && user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {  //老师身份的作业
                String status = bean.getStatus();
                goto_complete_ll.setVisibility(View.GONE);
                complete_ll.setVisibility(View.GONE);
                img_top_back.setVisibility(View.VISIBLE);
                if (status.equals("0")) { //去发布(未完成)
                    goto_publish_ll.setVisibility(View.VISIBLE);
                    teacher_pubulished_ll.setVisibility(View.GONE);
                    img_top_back.setImageResource(R.mipmap.ic_more);
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_goto_publish,
                            ScreenUtil.dp2px(mContext, 22), -1, "", VariableConfig.color_button_selected);
                    img_top_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //删除草稿
                            if (clickListener != null) {
                                clickListener.clickMore(bean, helper.getLayoutPosition());
                            }

                        }
                    });
                    if (bean.getCreate_time() != 0)
                        tv_time_des.setText(StringUtil.stampToMonth(bean.getCreate_time()) + " " + mContext.getResources().getString(R.string.edit));
                } else if (status.equals("2") || status.equals("1")) {  //待点评(未完成) 有部分提交(未完成)
                    goto_publish_ll.setVisibility(View.GONE);
                    teacher_pubulished_ll.setVisibility(View.VISIBLE);
                    if (bean.getCreate_time() != 0)
                        tv_time_des.setText(StringUtil.stampToMonth(bean.getCreate_time()) + " " + mContext.getResources().getString(R.string.publish));
                    tv_nocommit_num.setText(bean.getUnsubmits() + "");
                    tv_commit_num.setText(bean.getSubmits() + "");
                    tv_norate_num.setText(bean.getUnremarks() + "");
                } else if (status.equals("3")) { // 已点评完(已完成)
                    goto_publish_ll.setVisibility(View.GONE);
                    teacher_pubulished_ll.setVisibility(View.VISIBLE);
                    if (bean.getCreate_time() != 0)
                        tv_time_des.setText(StringUtil.stampToMonth(bean.getCreate_time()) + " " + mContext.getResources().getString(R.string.publish));
                    tv_nocommit_num.setText(bean.getUnsubmits() + "");
                    tv_commit_num.setText(bean.getSubmits() + "");
                    tv_norate_num.setText(bean.getUnremarks() + "");
                    tv_nocommit_num.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                    tv_norate_num.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                    tv_commit_num.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                    tv_homework_title.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_92949A));
                }
            }

           /* tv_goto_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            tv_goto_publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        }


    }

    public interface OnMoreClickListener {
        void clickMore(HomeworkInfoEntity info, int index);
    }
}
