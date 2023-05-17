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
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Date:2021/7/6
 * Time:11:05
 * author:joker
 * 版本升级 弹框
 */
public class SysVersionUpdateDialog extends Dialog {

    private WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private View mDialogView;

    private ConstraintLayout cl_dialog;
    private TextView tv_confirm;
    private ImageView iv_close;
    private ImageView iv_logo;
    private TextView tv_version_name;
    private TextView tv_update_content;

    private SysVersionEntity sysVersionEntity;


    public SysVersionUpdateDialog(Context context) {
        super(context, R.style.dialog_style);
        this.mContext = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initUIView();
        initListener();
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
        layoutParams = getWindow().getAttributes();
        if (ScreenTools.getInstance().isPad(mContext)) {
            layoutParams.gravity = Gravity.CENTER;
//            getWindow().setWindowAnimations(windowanimations);  //添加动画
        } else {
            layoutParams.gravity = Gravity.BOTTOM;
            getWindow().setWindowAnimations(R.style.AnimBottom);  //添加动画
        }
        layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_375x);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 初始化UI
     */
    private void initUIView() {
        mDialogView = View.inflate(mContext, R.layout.dailog_sysversionupdate, null);
        setContentView(mDialogView);

        cl_dialog = mDialogView.findViewById(R.id.cl_dialog);
        tv_confirm = mDialogView.findViewById(R.id.tv_confirm);
        iv_close = mDialogView.findViewById(R.id.iv_close);
        tv_version_name = mDialogView.findViewById(R.id.tv_version_name);
        tv_update_content = mDialogView.findViewById(R.id.tv_update_content);
        iv_logo = mDialogView.findViewById(R.id.iv_logo);


        setDialogBG();
        setConfirmBG();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_version_name.setText("V" + sysVersionEntity.getVersionnum());
//        tv_update_content.setText(mContext.getResources().getString(R.string.versionupdate_content));
        String content = String.format(mContext.getResources().getString(R.string.versionupdate_content), mContext.getResources().getString(TKExtManage.getInstance().getAppNameRes(mContext)));
        tv_update_content.setText(content);


        //设置图标
        int drawDrawableID = TKExtManage.getInstance().getStartLogo(mContext);
        iv_logo.setImageDrawable(ContextCompat.getDrawable(mContext, drawDrawableID));


        if ("1".equals(sysVersionEntity.getUpdateflag())) {//强制升级
            iv_close.setVisibility(View.GONE);
        } else {//选择升级
            iv_close.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        iv_close.setOnClickListener(v -> {
            //保存弹框弹出的时间
            MySPUtilsUser.getInstance().saveUserSysversionUpdateShowTime(Integer.parseInt(DateUtil.getCurrentDate(0)));
            dismissDialog();
        });
    }


    /**
     * 设置弹框的背景
     */
    private void setDialogBG() {
        if (ScreenTools.getInstance().isPad(mContext)) {
            setDynamicShapeRECTANGLE(mContext, cl_dialog, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
        } else {
            setDynamicShapeRECTANGLE(mContext, cl_dialog, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_50x), -1, "", "#FFFFFF");
        }
    }

    /**
     * 设置确定按钮背景
     */
    private void setConfirmBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_confirm, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#13C296");
    }


    /**
     * 动态设置Shape  RECTANGLE   上下左右角度设置
     */
    private void setDynamicShapeRECTANGLE(Context mContext, View view, float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.RECTANGLE);

        //设置shape的圆角度数
        if (!StringUtils.isBlank(CornerRadius) && CornerRadius != -1) {
//            drawable.setCornerRadius(CornerRadius);
            if (ScreenTools.getInstance().isPad(mContext)) {
                float[] radius = {CornerRadius, CornerRadius, CornerRadius, CornerRadius, CornerRadius,
                        CornerRadius, CornerRadius, CornerRadius};
                drawable.setCornerRadii(radius);
            } else {
                float[] radius = {CornerRadius, CornerRadius, 0f, 0f, 0f,
                        0f, 0f, 0f};
                drawable.setCornerRadii(radius);
            }
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

    /**
     * 设置数据
     */
    public void setInitDatas(SysVersionEntity sysVersionEntity) {
        this.sysVersionEntity = sysVersionEntity;
        initData();
    }


    /**
     * 立即更新按钮
     */
    public void confirmButton(View.OnClickListener listener) {
        tv_confirm.setOnClickListener(listener);
    }

    /**
     * 关闭按钮点击
     */
    public void closeButton(View.OnClickListener listener) {
        iv_close.setOnClickListener(listener);
    }

    /**
     * 关闭弹框
     */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }
}
