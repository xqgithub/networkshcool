package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.StudentAvatarEntity;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkCommentView;
import com.talkcloud.networkshcool.baselibrary.weiget.CircleProgressbar;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;

import java.util.List;

/**
 * Date:2021/6/18
 * Time:9:30
 * author:joker
 * 作业点评 学生头像 适配器
 */
public class StudentAvatarAdapter extends RecyclerView.Adapter<StudentAvatarAdapter.StudentAvatarHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private HomeworkCommentView homeworkCommentView;
    private List<StudentAvatarEntity> studentAvatarEntities;

    public StudentAvatarAdapter(Activity activity, HomeworkCommentView homeworkCommentView) {
        this.mActivity = activity;
        this.homeworkCommentView = homeworkCommentView;
        inflater = LayoutInflater.from(mActivity);
    }

    public class StudentAvatarHolder extends RecyclerView.ViewHolder {
        private RoundImageView riv_avator;
        private ConstraintLayout cl_carryout;
        private TextView tv_studentname;
        private CircleProgressbar cp_avator;

        public StudentAvatarHolder(@NonNull View itemView) {
            super(itemView);
            riv_avator = itemView.findViewById(R.id.riv_avator);
            cl_carryout = itemView.findViewById(R.id.cl_carryout);
            tv_studentname = itemView.findViewById(R.id.tv_studentname);
            cp_avator = itemView.findViewById(R.id.cp_avator);


            /**图像外框设置**/
            cp_avator.setProgressType(CircleProgressbar.ProgressType.COUNT_BACK);
            cp_avator.setOutLineWidth(0);
            cp_avator.setOutLineColor(Color.parseColor("#00000000"));
            cp_avator.setInCircleColor(Color.parseColor("#00000000"));
            cp_avator.setProgressColor(Color.parseColor(VariableConfig.color_button_selected));
            cp_avator.setProgressLineWidth(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_2x));
        }
    }

    @NonNull
    @Override
    public StudentAvatarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_studentavatar, parent, false);
        StudentAvatarHolder studentAvatarHolder = new StudentAvatarHolder(view);
        return studentAvatarHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAvatarHolder holder, int position) {


        //学生的名称
        holder.tv_studentname.setText(studentAvatarEntities.get(position).getStudentname());


        //是否被选中
        if (studentAvatarEntities.get(position).isIsselected()) {
            holder.cp_avator.setVisibility(View.VISIBLE);
        } else {
            holder.cp_avator.setVisibility(View.INVISIBLE);
        }

        //是否已经完成
        if (studentAvatarEntities.get(position).isIsfinished()) {
            //设置已经完成图标的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, holder.cl_carryout, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
            holder.cl_carryout.setVisibility(View.VISIBLE);
        } else {
            holder.cl_carryout.setVisibility(View.INVISIBLE);
        }


        //图像
        if (!StringUtils.isBlank(studentAvatarEntities.get(position).getAvatarurl())) {
            Glide.with(mActivity).load(studentAvatarEntities.get(position).getAvatarurl())
                    .error(R.drawable.icon_default_head_img)
                    .into(new CustomTarget<Drawable>() {//防止RecyclerView 中 使用glide 重复加载图像出现忽大忽小的现象
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.riv_avator.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentAvatarEntities.get(position).isIsselected()) {
                    return;
                } else {
                    for (int i = 0; i < studentAvatarEntities.size(); i++) {
                        if (studentAvatarEntities.get(i).isIsselected()) {
                            studentAvatarEntities.get(i).setIsselected(false);
//                            notifyItemChanged(i);
                        }
                    }
                    studentAvatarEntities.get(position).setIsselected(true);
                    homeworkCommentView.studentWorkInfosCallback(studentAvatarEntities, position);
//                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentAvatarEntities.size();
    }


    /**
     * 设置数据
     */
    public void setDatas(List<StudentAvatarEntity> studentAvatarEntities) {
        this.studentAvatarEntities = studentAvatarEntities;
    }

}
