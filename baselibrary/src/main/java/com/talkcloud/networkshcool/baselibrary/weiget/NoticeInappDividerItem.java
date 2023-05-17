package com.talkcloud.networkshcool.baselibrary.weiget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;

/**
 * Date:2021/6/18
 * Time:11:00
 * author:joker
 * 应用内通知列表页面 adapter 分割规则
 */
public class NoticeInappDividerItem extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = TKBaseApplication.myApplication.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_20x);
        }
    }
}
