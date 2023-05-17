package com.talkcloud.networkshcool.baselibrary.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.talkcloud.networkshcool.baselibrary.R;


/**
 * Date:2021/6/21
 * Time:16:42
 * author:joker
 * 自定义RatingBarV
 */
public class RatingBarView extends LinearLayout {

    public interface OnRatingListener {
        void onRating(Object bindObject, int RatingScore);
    }

    private boolean mClickable = true;
    private OnRatingListener onRatingListener;
    private Object bindObject;
    private float starImageSize;
    private int starCount;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private int mStarCount;
    private Context mContext;

    // 间距
    private float mStarSpace = 30;

    public void setClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBarView);
        starImageSize = ta.getDimension(R.styleable.RatingBarView_starImageSize, 20);
        starCount = ta.getInteger(R.styleable.RatingBarView_starCount, 5);
        starEmptyDrawable = ta.getDrawable(R.styleable.RatingBarView_starEmpty);
        starFillDrawable = ta.getDrawable(R.styleable.RatingBarView_starFill);
        mStarSpace = ta.getDimension(R.styleable.RatingBarView_starSpace, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x));
        ta.recycle();

        initStarView(true);
    }

    // 初始化star
    private void initStarView(boolean isEditable) {
        this.removeAllViews();

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(mContext, i);
            if(isEditable) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickable) {
                            mStarCount = indexOfChild(v) + 1;
                            setStar(mStarCount, false);
                            if (onRatingListener != null) {
                                onRatingListener.onRating(bindObject, mStarCount);
                            }
                        }
                    }
                });
            }

            addView(imageView);
        }
    }

    private ImageView getStarImageView(Context context, int position) {

        ImageView imageView = new ImageView(context);
//        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(Math.round(starImageSize), Math.round(starImageSize));
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                Math.round(starImageSize), Math.round(starImageSize));
        if (position == (starCount - 1)) {
            layout.setMargins(0, 0, 0, 0);
        } else {
            layout.setMargins(0, 0, (int) mStarSpace, 0);
        }
        imageView.setLayoutParams(layout);
        // TODO:you can change gap between two stars use the padding
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setMaxWidth(10);
        imageView.setMaxHeight(10);
        return imageView;
    }

    public void setStar(int starCount, boolean animation) {
        starCount = starCount > this.starCount ? this.starCount : starCount;
        starCount = starCount < 0 ? 0 : starCount;
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
            if (animation) {
                ScaleAnimation sa = new ScaleAnimation(0, 0, 1, 1);
                getChildAt(i).startAnimation(sa);
            }
        }
        for (int i = this.starCount - 1; i >= starCount; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    public int getStarCount() {
        return mStarCount;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setBindObject(Object bindObject) {
        this.bindObject = bindObject;
    }

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }


    // 设置数量 都是亮的
    public void setStarCount2(int starCount) {
        this.starCount = starCount;

        initStarView(false);

        setStar(starCount, false);
    }
}