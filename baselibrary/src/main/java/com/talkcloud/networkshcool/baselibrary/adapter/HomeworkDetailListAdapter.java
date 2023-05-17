package com.talkcloud.networkshcool.baselibrary.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;

import java.util.List;


public class HomeworkDetailListAdapter extends BaseQuickAdapter<HomeworkDetailInfoEntity, BaseViewHolder> {


    //是否可点评 0否 1是
    private int is_remark = 1;


    public HomeworkDetailListAdapter(int layoutResId, @Nullable List<HomeworkDetailInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeworkDetailInfoEntity bean) {
        String user_identity = MySPUtilsUser.getInstance().getUserIdentity();


        RoundImageView avatar = helper.getView(R.id.img_avator);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_commit_des = helper.getView(R.id.tv_commit_des);
        TextView tv_remind = helper.getView(R.id.tv_remind);


        if (bean != null) {

            if (bean.getAvatar() != null && !bean.getAvatar().equals(""))
                Glide.with(mContext).load(bean.getAvatar()).error(R.drawable.icon_default_head_img).into(avatar);
            if (bean.getName() != null)
                tv_name.setText(bean.getName());

            if (bean.getStatus() != null && user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {  //老师身份的作业,只有老师有这个列表
                String status = bean.getStatus();
                if (bean.getSubmit_time() != 0)
                    tv_time.setText(StringUtil.getTimeAgo(bean.getSubmit_time()));
                if (status.equals("1") || status.equals("0")) { //未提交
                    tv_commit_des.setVisibility(View.GONE);
                    tv_time.setVisibility(View.GONE);

                    //提醒按钮
                    tv_remind.setVisibility(View.VISIBLE);
                    if (bean.getUnreminds() > 0) {
                        if (!bean.isIsreminded()) {//未提醒
                            //设置提醒按钮背景
                            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_remind,
                                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_22x), -1, "", VariableConfig.color_button_selected);
                            tv_remind.setTextColor(Color.parseColor("#FFFFFF"));
                            tv_remind.setText(mContext.getResources().getString(R.string.student_list_remind));
                            //点击提醒学生
                            tv_remind.setOnClickListener(v -> {
                                if (!StringUtils.isBlank(homeworkDetailListListener) && !bean.isIsreminded()) {
                                    homeworkDetailListListener.onRemindStudents(bean, helper.getLayoutPosition());
                                    bean.setIsreminded(true);
                                }
                            });
                        } else {//已经提醒
                            //设置提醒按钮背景
                            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_remind,
                                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_22x), -1, "", "#F7F8F9");
                            tv_remind.setTextColor(Color.parseColor("#8F92A1"));
                            tv_remind.setText(mContext.getResources().getString(R.string.student_list_reminded));
                            bean.setIsreminded(true);
                        }
                    } else {//次数达到上线，显示已提醒
                        //设置提醒按钮背景
                        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_remind,
                                mContext.getResources().getDimensionPixelSize(R.dimen.dimen_22x), -1, "", "#F7F8F9");
                        tv_remind.setTextColor(Color.parseColor("#8F92A1"));
                        tv_remind.setText(mContext.getResources().getString(R.string.student_list_reminded));
                    }
                } else if (status.equals("2") || status.equals("3")) {  //已提交
                    tv_time.setVisibility(View.VISIBLE);
                    tv_remind.setVisibility(View.GONE);
                    if (status.equals("2")) { //未点评
                        tv_commit_des.setTextColor(ContextCompat.getColor(mContext, R.color.bg_blue));
                        tv_commit_des.setText(mContext.getString(R.string.gotorate));
                    } else { //已点评
                        tv_commit_des.setVisibility(View.VISIBLE);
                        tv_commit_des.setTextColor(ContextCompat.getColor(mContext, R.color.bg_gray_text));
                        /**
                         * 枚举: 3,2,1,0
                         * 枚举备注: 3优秀 2良好 1中等 0不合格
                         */
                        if (bean.getRank() != null) {
                            String rankStr = "";
                            if (bean.getRank().equals("0")) {
                                rankStr = mContext.getString(R.string.flunk) + "+";
                            } else if (bean.getRank().equals("1")) {
                                rankStr = mContext.getString(R.string.secondary) + "+";
                            } else if (bean.getRank().equals("2")) {
                                rankStr = mContext.getString(R.string.good) + "+";
                            } else if (bean.getRank().equals("3")) {
                                rankStr = mContext.getString(R.string.excellent) + "+";
                            }
                            tv_commit_des.setText(rankStr + bean.getRank());
                        }
                    }

                }
            }
        }
    }

    /**
     * 设置是否可以显示点评按钮
     */
    public void setIsRemark(int is_remark) {
        this.is_remark = is_remark;
    }

    /**
     * HomeworkDetailListAdapter 接口集合
     */
    private HomeworkDetailListListener homeworkDetailListListener;

    public interface HomeworkDetailListListener {
        void onRemindStudents(HomeworkDetailInfoEntity bean, int position);
    }

    public void setHomeworkDetailListListener(HomeworkDetailListListener homeworkDetailListListener) {
        this.homeworkDetailListListener = homeworkDetailListListener;
    }


}
