package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;

/**
 * Date:2021/5/20
 * Time:16:57
 * author:joker
 * 日历展示课程页面  课程老师适配器
 */
public class CalendarCourseTeacherTypeAdapter extends RecyclerView.Adapter<CalendarCourseTeacherTypeAdapter.CalendarCourseTeacherTypeHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;


    public CalendarCourseTeacherTypeAdapter(Activity activity) {
        inflater = LayoutInflater.from(activity);
        this.mActivity = activity;
    }

    public class CalendarCourseTeacherTypeHolder extends RecyclerView.ViewHolder {

        private RoundImageView img_avator;
        private TextView tv_name;
        private TextView tv_type;


        public CalendarCourseTeacherTypeHolder(@NonNull View itemView) {
            super(itemView);
            img_avator = itemView.findViewById(R.id.img_avator);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }

    @NonNull
    @Override
    public CalendarCourseTeacherTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_calendarcourseteachertype, parent, false);
        CalendarCourseTeacherTypeHolder viewHolder = new CalendarCourseTeacherTypeHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarCourseTeacherTypeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
