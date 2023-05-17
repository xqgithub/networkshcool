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
 */
public class StudentAvatarDividerItem extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = TKBaseApplication.myApplication.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_20x);
        }
        outRect.right = TKBaseApplication.myApplication.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.dimen_20x);
    }
}
