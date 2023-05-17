package com.talkcloud.networkshcool.baselibrary.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.entity.CourseInfoEntity;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CourseListAdapter extends BaseQuickAdapter<CourseInfoEntity, BaseViewHolder> {


    private List<Integer> leftWidths = new ArrayList<>();
    private int maxWidth = 0;


    public CourseListAdapter(int layoutResId, @Nullable List<CourseInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfoEntity bean) {

//        NSLog.d("bean : " + bean);

        if (helper.getLayoutPosition() == 0) {
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) helper.getConvertView().getLayoutParams();
            lp.topMargin = ScreenUtil.dp2px(mContext, 10);
            helper.getConvertView().setLayoutParams(lp);
        }

        TextView tv_courseName = helper.getView(R.id.course_name);
        TextView tv_courseTime = helper.getView(R.id.course_time);
        TextView tv_courseTeacher = helper.getView(R.id.course_teacher);
        TextView tv_courseProcess = helper.getView(R.id.course_process);
        TextView tv_nextclass = helper.getView(R.id.course_next_class);
        LinearLayout ll_next_class = helper.getView(R.id.ll_next_class);
        TextView tv_courseTimeLeft = helper.getView(R.id.tv_course_time_left);
        TextView tv_courseTeacherLeft = helper.getView(R.id.tv_course_teacher_left);
        TextView tv_courseProcessLeft = helper.getView(R.id.tv_course_process_left);
        if (bean.getState() != null && bean.getState().equals("3")) {
            ll_next_class.setVisibility(View.GONE);
        } else {
            ll_next_class.setVisibility(View.VISIBLE);
        }
        if (bean.getName() != null)
            tv_courseName.setText(bean.getName());
        if (bean.getPeriod() != null) {
            if (bean.getPeriod().split("~").length >= 2) {
                tv_courseTime.setText(bean.getPeriod());
            }
        }
        if (bean.getSchedule() != null)
            tv_courseProcess.setText(mContext.getResources().getString(R.string.course_complete)
                    + bean.getSchedule() + mContext.getResources().getString(R.string.course));
        if (bean.getNext_lesson() != null && bean.getNext_lesson().getStarttime() != 0) {
            tv_nextclass.setText(StringUtil.stampToDate(bean.getNext_lesson().getStarttime()));
        } else {
            ll_next_class.setVisibility(View.GONE);
        }

        StringBuilder sb = new StringBuilder();

        String teacherText = "";
        if (bean.getTeachers() != null && bean.getTeachers().size() > 0) {
            for (int i = 0; i < bean.getTeachers().size(); i++) {  //拼接字符串 例： 张三(主讲)、李四(助教)
                if (i < bean.getTeachers().size() - 1) { //不是最后一个
                    if (bean.getTeachers().get(i).getType().equals("1")) { //如果是主讲
                        teacherText = bean.getTeachers().get(i).getName() + "(" + mContext.getResources().getString(R.string.teacher) + ")" + "、";
                    } else {
                        teacherText = bean.getTeachers().get(i).getName() + "(" + mContext.getResources().getString(R.string.assistantor) + ")" + "、";
                    }
                } else {
                    if (bean.getTeachers().get(i).getType().equals("1")) { //如果是主讲
                        teacherText = bean.getTeachers().get(i).getName() + "(" + mContext.getResources().getString(R.string.teacher) + ")";
                    } else {
                        teacherText = bean.getTeachers().get(i).getName() + "(" + mContext.getResources().getString(R.string.assistantor) + ")";
                    }
                }
                sb.append(teacherText);
            }
            tv_courseTeacher.setText(sb.toString());

        }
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv_courseTimeLeft.measure(spec, spec);
        int timeWidth = tv_courseTimeLeft.getMeasuredWidth();
        tv_courseTeacherLeft.measure(spec, spec);
        int teacherWidth = tv_courseTeacherLeft.getMeasuredWidth();
        tv_courseProcessLeft.measure(spec, spec);
        int typeWidth = tv_courseProcessLeft.getMeasuredWidth();
        leftWidths.clear();
        leftWidths.add(timeWidth);
        leftWidths.add(teacherWidth);
        leftWidths.add(typeWidth);
        maxWidth = (int) Collections.max(leftWidths);

        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(maxWidth +
                ScreenUtil.dp2px(mContext, 14), LinearLayout.LayoutParams.WRAP_CONTENT);
        tv_courseTimeLeft.setLayoutParams(Params);
        tv_courseTeacherLeft.setLayoutParams(Params);
        tv_courseProcessLeft.setLayoutParams(Params);
    }


}
