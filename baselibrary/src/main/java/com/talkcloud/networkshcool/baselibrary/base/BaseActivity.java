package com.talkcloud.networkshcool.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Window;

import androidx.annotation.Nullable;

import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

/**
 * Date:2021/5/10
 * Time:19:53
 * author:joker
 * Activity 的 基类
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private GestureDetector myDectector;
    private static final String TAG = "GestureBackActivity";
    boolean flingFinishEnabled = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.关闭屏幕顶部的标题
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //2.在加载布局之前做的时候
        onBeforeSetContentLayout();
        //3.加载xml布局
        setContentView(getLayoutId());

        initUiView();
        initData();
        initListener();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(newBase);
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    /**
     * 在设置视图内容之前
     * 需要做什么操作可以写在该方法中
     */
    protected abstract void onBeforeSetContentLayout();

    /**
     * 得到xml布局文件的id
     */
    protected abstract int getLayoutId();


    /**
     * 初始化UI控件
     */
    protected abstract void initUiView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化监听
     */
    protected abstract void initListener();


    @Override
    protected void onResume() {
        super.onResume();
        //判断本应用是否在前台
//        if (MyApplication.myApplication.lifecycleCallbacks.isFront()
//                && !VariableConfig.eyeProtectionisOpen) {
//            //获取护眼模式的状态
//            Boolean eyeprotection = MySPUtilsUser.getInstance().getUserEyeProtectionStatus();
//            if (eyeprotection) {
//                EyeProtectionUtil.openSuspensionWindow(this, true);
//                VariableConfig.eyeProtectionisOpen = true;
//            }
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //判断本应用是否在前台
//        if (!MyApplication.myApplication.lifecycleCallbacks.isFront()) {
//            //获取护眼模式的状态
//            Boolean eyeprotection = MySPUtilsUser.getInstance().getUserEyeProtectionStatus();
//            if (eyeprotection) {//开启状态，关闭护眼模式
//                EyeProtectionUtil.openSuspensionWindow(this, false);
//                VariableConfig.eyeProtectionisOpen = false;
//            }
//        }
    }


}
