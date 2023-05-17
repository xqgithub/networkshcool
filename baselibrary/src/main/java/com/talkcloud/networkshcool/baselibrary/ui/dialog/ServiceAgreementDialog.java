package com.talkcloud.networkshcool.baselibrary.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.weiget.MaxHeightScrollView;
import com.talkcloud.networkshcool.baselibrary.weiget.RxViewUtils;

import java.util.List;
import java.util.Map;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Date:2021/8/2
 * Time:16:23
 * author:joker
 * 用户协议和隐私政策 弹框
 */
public class ServiceAgreementDialog extends Dialog implements RxViewUtils.Action1<View> {

    //自定义view
    private View mDialogView;
    private Context mContext;

    private ConstraintLayout cl_dialog;
    private TextView tv_no;
    private TextView tv_yes;
    private TextView tv_content;
    private TextView tv_title;
    private TextView tv_content3;
    private MaxHeightScrollView mhsv_content;


    private int dialog_width = 0;
    private WindowManager.LayoutParams layoutParams;
    private SpannableString spannableString;

    private ServiceAgreementDialogListener serviceAgreementDialogListener;

    public ServiceAgreementDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        this.mContext = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initUIView();

        setDialogBG();
        setCancelBtnBG();
        setConfirmBtnBG();

        initListener();
        initProtocolContent();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        int width = dialog_width;
//        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = width;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }


    /**
     * 初始化UI
     */
    private void initUIView() {
        mDialogView = View.inflate(mContext, R.layout.dailog_serviceagreement, null);
        setContentView(mDialogView);
        tv_no = mDialogView.findViewById(R.id.tv_no);
        tv_yes = mDialogView.findViewById(R.id.tv_yes);
        cl_dialog = mDialogView.findViewById(R.id.cl_dialog);
        tv_content = mDialogView.findViewById(R.id.tv_content);
        tv_title = mDialogView.findViewById(R.id.tv_title);
        mhsv_content = mDialogView.findViewById(R.id.mhsv_content);
        tv_content3 = mDialogView.findViewById(R.id.tv_content3);
        MaxHeightScrollView.mMaxHeight = AutoSizeConfig.getInstance().getScreenHeight() / 4;
    }


    /**
     * 初始化监听
     */
    private void initListener() {
        RxViewUtils.getInstance().setOnClickListeners(this, 500, tv_no, tv_yes);
    }

    public void setServiceAgreementDialogListener(ServiceAgreementDialogListener serviceAgreementDialogListener) {
        this.serviceAgreementDialogListener = serviceAgreementDialogListener;
    }


    /**
     * 点击回调
     *
     * @param view
     */
    @Override
    public void onRxViewClick(View view) {
        if (view == tv_no) {
            if (tv_title.getVisibility() == View.VISIBLE) {
                tv_title.setVisibility(View.GONE);
                tv_content.setVisibility(View.GONE);
                tv_content3.setVisibility(View.VISIBLE);
                tv_no.setText(R.string.service_and_privacy_disagree2);
                tv_yes.setText(R.string.service_and_privacy_agree2);
            } else {
                //退出应用
                PublicPracticalMethodFromJAVA.getInstance().activityFinish((Activity) mContext, R.anim.activity_xhold);
                System.exit(0);
            }
        } else if (view == tv_yes) {
            if (tv_title.getVisibility() == View.VISIBLE) {
                dismissDialog();
                if (!StringUtils.isBlank(serviceAgreementDialogListener)) {
                    serviceAgreementDialogListener.agreeServiceAgreementAndPrivacyPolicy();
                }
            } else {
                tv_title.setVisibility(View.VISIBLE);
                tv_content.setVisibility(View.VISIBLE);
                tv_content3.setVisibility(View.GONE);
                tv_no.setText(R.string.service_and_privacy_disagree);
                tv_yes.setText(R.string.service_and_privacy_agree);
            }
        }
    }

    /**
     * 设置弹框的背景
     */
    private void setDialogBG() {
        setDynamicShapeRECTANGLE(mContext, cl_dialog, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
    }

    /**
     * 设置取消按钮的背景
     */
    private void setCancelBtnBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_no, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
    }

    /**
     * 设置确定按钮的背景
     */
    private void setConfirmBtnBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_yes, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);
    }

    /**
     * 设置Dialog的宽度
     */
    public void setDialogWidth(int width) {
        this.dialog_width = width;
    }


    /**
     * 初始化协议内容
     */
    private void initProtocolContent() {
        int startindex_useragreement = 0;
        int endindex_useragreement = 0;
        int startindex_userprivacypolicy = 0;
        int endindex_userprivacypolicy = 0;


        String content = mContext.getResources().getString(R.string.service_and_privacy_content, mContext.getResources().getString(TKExtManage.getInstance().getAppNameRes(mContext)));
        spannableString = new SpannableString(content);

        //点击用户协议跳转
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", ConfigConstants.USERAGREEMENT);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump((Activity) mContext, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//去掉下划线
            }
        };

        //点击跳转到隐私政策页面
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", ConfigConstants.USERPRIVACYPOLICY);
                PublicPracticalMethodFromJAVA.getInstance().intentToJump((Activity) mContext, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);//去掉下划线
            }
        };


        //判断当前语言
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
            //获取本地app的国家语言
            List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(mContext);
            if (locale_language.equals(datas.get(0).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(0).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("\"User Agreement\"");
                endindex_useragreement = startindex_useragreement + 16;

                startindex_userprivacypolicy = endindex_useragreement + 5;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16;

            } else if (locale_language.equals(datas.get(1).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(1).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("《用户协议》");
                endindex_useragreement = startindex_useragreement + 6;

                startindex_userprivacypolicy = endindex_useragreement + 1;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6;

            } else if (locale_language.equals(datas.get(2).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(2).get(SPConstants.LOCALE_COUNTRY))) {
                startindex_useragreement = content.indexOf("《用戶協議》");
                endindex_useragreement = startindex_useragreement + 6;

                startindex_userprivacypolicy = endindex_useragreement + 1;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 6;
            } else {
                startindex_useragreement = content.indexOf("\"User Agreement\"");
                endindex_useragreement = startindex_useragreement + 16;

                startindex_userprivacypolicy = endindex_useragreement + 5;
                endindex_userprivacypolicy = startindex_userprivacypolicy + 16;
            }
        }


        spannableString.setSpan(clickableSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF3997F8"));
        spannableString.setSpan(colorSpan, startindex_useragreement, endindex_useragreement, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        spannableString.setSpan(clickableSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.parseColor("#FF3997F8"));
        spannableString.setSpan(colorSpan2, startindex_userprivacypolicy, endindex_userprivacypolicy, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        tv_content.setText(spannableString);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());
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
            drawable.setCornerRadius(CornerRadius);

//            if (ScreenTools.getInstance().isPad(mContext)) {
//                float[] radius = {CornerRadius, CornerRadius, 0f, 0f, 0f,
//                        0f, CornerRadius, CornerRadius};
//                drawable.setCornerRadii(radius);
//            } else {
//                float[] radius = {CornerRadius, CornerRadius, CornerRadius, CornerRadius, 0f,
//                        0f, 0f, 0f};
//                drawable.setCornerRadii(radius);
//            }
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
     * 关闭弹框
     */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }


    public interface ServiceAgreementDialogListener {
        void agreeServiceAgreementAndPrivacyPolicy();
    }

}
