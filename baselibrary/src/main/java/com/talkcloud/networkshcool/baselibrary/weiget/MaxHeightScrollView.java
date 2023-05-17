package com.talkcloud.networkshcool.baselibrary.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Date:2021/8/2
 * Time:16:38
 * author:joker
 * 自定义ScrollView最大内容显示高度
 */
public class MaxHeightScrollView extends ScrollView {

    public static int mMaxHeight = 500;

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
