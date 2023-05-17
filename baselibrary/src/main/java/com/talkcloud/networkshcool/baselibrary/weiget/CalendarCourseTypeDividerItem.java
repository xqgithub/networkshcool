package com.talkcloud.networkshcool.baselibrary.weiget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Date:2021/5/20
 * Time:17:27
 * author:joker
 * 课程老师类型分割线
 */
public class CalendarCourseTypeDividerItem extends RecyclerView.ItemDecoration {
    private int width;
    private int height;

    /**
     * @param width  横向间隔
     * @param height 纵向间隔
     */
    public CalendarCourseTypeDividerItem(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = width;
    }
}
