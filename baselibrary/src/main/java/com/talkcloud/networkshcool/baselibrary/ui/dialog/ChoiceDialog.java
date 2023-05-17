package com.talkcloud.networkshcool.baselibrary.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Date:2021/5/25
 * Time:9:12
 * author:joker
 * 自定义:选择或者不选弹框
 */
public class ChoiceDialog extends Dialog {

    //自定义view
    private View mDialogView;
    private Context mContext;


    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_no;
    private TextView tv_yes;
    private ImageView iv_close;
    private ConstraintLayout cl_dialog;

    private int dialog_width = 0;

    public ChoiceDialog(Context context, int width) {
        super(context, R.style.dialog_style);
        this.mContext = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        dialog_width = width;
        initUIView(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        if (ScreenTools.getInstance().isPad(mContext)) {
            layoutParams.gravity = Gravity.CENTER;
        } else {
            layoutParams.gravity = Gravity.BOTTOM;
            getWindow().setWindowAnimations(R.style.AnimBottom);  //添加动画
        }
        int width = dialog_width;
//        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = width;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void initUIView(Context context) {
        mDialogView = View.inflate(context, R.layout.dailog_choice, null);
        setContentView(mDialogView);

        tv_title = mDialogView.findViewById(R.id.tv_title);
        tv_content = mDialogView.findViewById(R.id.tv_content);
        tv_no = mDialogView.findViewById(R.id.tv_no);
        tv_yes = mDialogView.findViewById(R.id.tv_yes);
        iv_close = mDialogView.findViewById(R.id.iv_close);
        cl_dialog = mDialogView.findViewById(R.id.cl_dialog);
    }


    /**
     * 设置文字的内容
     *
     * @param Title   标题
     * @param content 内容
     * @param no      不确认文字
     * @param yes     确认文字
     */
    public void setTextInformation(String Title, String content, String no, String yes) {
        tv_title.setText(Title);
        tv_content.setText(content);
        tv_no.setText(no);
        tv_yes.setText(yes);
    }


    /**
     * 确认按钮
     */
    public void confirmButton(View.OnClickListener listener) {
        tv_yes.setOnClickListener(listener);
    }

    /**
     * 不确认按钮
     */
    public void notSure(View.OnClickListener listener) {
        tv_no.setOnClickListener(listener);
    }

    /**
     * 关闭按钮
     */
    public void closeWindow(View.OnClickListener listener) {
        iv_close.setOnClickListener(listener);
    }


    /**
     * 设置弹框的背景
     */
    public void setDialogBG(float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        if (ScreenTools.getInstance().isPad(mContext)) {
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, cl_dialog, CornerRadius, strokewidth, strokeColor, bgcolor);
        } else {
            setDynamicShapeRECTANGLE(mContext, cl_dialog, CornerRadius, strokewidth, strokeColor, bgcolor);
        }

    }

    /**
     * 设置取消按钮的背景
     */
    public void setCancelBtnBG(float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_no, CornerRadius, strokewidth, strokeColor, bgcolor);
    }

    /**
     * 设置确定按钮的背景
     */
    public void setConfirmBtnBG(float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_yes, CornerRadius, strokewidth, strokeColor, bgcolor);
    }


    /**
     * 关闭弹框
     */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }


    /**
     * 动态设置Shape  RECTANGLE   上下左右角度设置
     */
    public void setDynamicShapeRECTANGLE(Context mContext, View view, float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.RECTANGLE);

        //设置shape的圆角度数
        if (!StringUtils.isBlank(CornerRadius) && CornerRadius != -1) {
//            drawable.setCornerRadius(CornerRadius);
            float[] radius = {CornerRadius, CornerRadius, CornerRadius, CornerRadius, 0f,
                    0f, 0f, 0f};
            drawable.setCornerRadii(radius);
        }

        //设置shape的边的宽度和颜色
        if (!StringUtils.isBlank(strokewidth) && strokewidth != -1
                && !StringUtils.isBlank(strokeColor)) {
//            drawable.setStroke(strokewidth, ContextCompat.getColor(mContext, R.color.appblack));
            drawable.setStroke(strokewidth, Color.parseColor(strokeColor));
        }

        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
//            drawable.setColor(ContextCompat.getColor(mContext, bgcolor));
            drawable.setColor(Color.parseColor(bgcolor));
        }
        view.setBackground(drawable);
    }


}
