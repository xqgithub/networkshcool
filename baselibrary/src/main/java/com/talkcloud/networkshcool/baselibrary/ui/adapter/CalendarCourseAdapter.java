package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.weiget.CalendarCourseTypeDividerItem;

/**
 * Date:2021/5/20
 * Time:15:10
 * author:joker
 * 日历展示课程页面 课程数据适配器
 */
public class CalendarCourseAdapter extends RecyclerView.Adapter<CalendarCourseAdapter.CalendarCourseHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;

    public CalendarCourseAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
        this.mActivity = activity;
    }

    public class CalendarCourseHolder extends RecyclerView.ViewHolder {

        private TextView class_type;
        private TextView enter_room;
        private TextView tv_reply;
        private TextView tv_comment;
        private TextView tv_task;
        private ConstraintLayout cl_tips;

        private RecyclerView rv_teacher_assitor;
        private CalendarCourseTeacherTypeAdapter teacherTypeAdapter;
        private LinearLayoutManager linearLayoutManager;


        public CalendarCourseHolder(@NonNull View itemView) {
            super(itemView);
            class_type = itemView.findViewById(R.id.class_type);
            enter_room = itemView.findViewById(R.id.enter_room);
            tv_reply = itemView.findViewById(R.id.tv_reply);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_task = itemView.findViewById(R.id.tv_task);
            cl_tips = itemView.findViewById(R.id.cl_tips);
            rv_teacher_assitor = itemView.findViewById(R.id.rv_teacher_assitor);


            //设置class_type的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, class_type,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_4x), 1, "#338f92a1", "");
            //设置enter_room的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, enter_room,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);
            //设置tv_reply的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, tv_reply,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18x), -1, "", "#e4f6fb");
            //设置tv_comment的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, tv_comment,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18x), -1, "", "#fff4f6");
            //设置tv_task的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, tv_task,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_18x), -1, "", "#ebf3ff");
            //设置cl_tips的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mActivity, cl_tips,
                    mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_8x), -1, "", "#f7f8f9");

            //初始化RecyclerView
            linearLayoutManager = new LinearLayoutManager(mActivity);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            rv_teacher_assitor.setLayoutManager(linearLayoutManager);
            teacherTypeAdapter = new CalendarCourseTeacherTypeAdapter(mActivity);
            int divider_width = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_16x);
            rv_teacher_assitor.addItemDecoration(new CalendarCourseTypeDividerItem(divider_width, 0));
            rv_teacher_assitor.setAdapter(teacherTypeAdapter);
            teacherTypeAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CalendarCourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_calendarcourse, parent, false);
        CalendarCourseHolder viewHolder = new CalendarCourseHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarCourseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
