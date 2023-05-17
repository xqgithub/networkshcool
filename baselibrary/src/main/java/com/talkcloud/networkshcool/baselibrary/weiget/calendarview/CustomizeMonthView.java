package com.talkcloud.networkshcool.baselibrary.weiget.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;

/**
 * Date:2021/5/19
 * Time:17:21
 * author:joker
 * 自定义月视图
 */
public class CustomizeMonthView extends MonthView {

    private int mRadius;

    private Context mContext;
    private Paint mPaint_selected_bg;
    private Paint mPaint_today_bg;

    private float mPointRadius = getResources().getDimensionPixelSize(R.dimen.dimen_2x);
    private Paint mPointPaint;


    //标记点的Y偏移量
    private float point_off_y = getResources().getDimensionPixelSize(R.dimen.dimen_5x);


    public CustomizeMonthView(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);


        mPaint_selected_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_selected_bg.setColor(Color.parseColor(VariableConfig.color_button_selected));
        mPaint_selected_bg.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint_selected_bg.setStrokeWidth(1f);


        mPaint_today_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_today_bg.setColor(Color.parseColor("#F7F8F9"));
        mPaint_today_bg.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint_today_bg.setStrokeWidth(1f);


        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.parseColor(VariableConfig.color_button_selected));
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setStrokeWidth(1f);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    /**
     * 绘制选中的日子
     *
     * @param canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mPaint_selected_bg);

     /*   //圆角矩形
        RectF rectF = new RectF(x + roundrect_off_x, y + roundrect_off_y, x + mItemWidth - roundrect_off_x, y + mItemHeight - roundrect_off_y);
        canvas.drawRoundRect(rectF, rx, ry, mPaint_selected_bg);*/
        return true;
    }

    /**
     * 绘制标记的事件日子
     *
     * @param canvas
     * @param calendar
     * @param x
     * @param y
     */
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);

        boolean isSelected = isSelected(calendar);

        if (compareDay(calendar) == 0) { //今天之前
            if (isSelected) {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.appwhite));
            } else {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.networkshcool_668f92a1));
            }
        } else if (compareDay(calendar) == 1) { //今天
            if (isSelected) {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.appwhite));
            } else {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));
            }
        } else if (compareDay(calendar) == 2) {  //今天之后
            if (isSelected) {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.appwhite));
            } else {
                mPointPaint.setColor(ContextCompat.getColor(mContext, R.color.networkshcool_1d6aff));
            }
        }
        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - point_off_y, mPointRadius, mPointPaint);
    }

    /**
     * 绘制文本
     *
     * @param canvas
     * @param calendar
     * @param x
     * @param y
     * @param hasScheme
     * @param isSelected 是否选中
     */
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;


        if (calendar.isCurrentDay() && !isSelected) {
            //绘制背景
        /*    RectF rectF = new RectF(x + roundrect_off_x, y + roundrect_off_y, x + mItemWidth - roundrect_off_x, y + mItemHeight - roundrect_off_y);
            canvas.drawRoundRect(rectF, rx, ry, mPaint_today_bg);*/
            int cx1 = x + mItemWidth / 2;
            int cy1 = y + mItemHeight / 2;
            canvas.drawCircle(cx1, cy1, mRadius, mPaint_today_bg);

            if (hasScheme) {


                canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - point_off_y, mPointRadius, mPointPaint);
            }
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }


    }

    /**
     * @param calendar
     * @return
     */
    private int compareDay(Calendar calendar) {
        int status = 0;  //0 选择日期在今天前，1 等于今天  2 在今天后
        java.util.Calendar curTime = java.util.Calendar.getInstance();
        if (calendar.isCurrentDay()) {
            status = 1;
        } else if (curTime.getTimeInMillis() > calendar.getTimeInMillis()) {
            status = 0;
        } else if (curTime.getTimeInMillis() < calendar.getTimeInMillis()) {
            status = 2;
        }

        return status;
    }
}
