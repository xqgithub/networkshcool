package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.classroomsdk.Config;
import com.eduhdsdk.room.RoomSession;
import com.eduhdsdk.room.RoomVariable;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.StartUpPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.StartUpView;
import com.talkcloud.room.TKRoomManager;

import java.util.HashMap;
import java.util.Map;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/11
 * Time:10:12
 * author:joker
 * 启动页
 */
public class StartUpActivity extends BaseActivity implements StartUpView, CustomAdapt, PermissionsActivity.PermissionsListener {

    private StartUpPresenter presenter;

    private ImageView iv_logo;
    private ConstraintLayout cl_start;

    @Override
    protected void onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //状态栏状态设置
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            false, true,
                            -1
                    );
            //隐藏状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }
        //设置横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_startup;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        PublicPracticalMethodFromJAVA.getInstance().restartApp(this);
        initScheme();
    }

    private void initScheme() {

        Intent intent = getIntent();
        //判断是否从H5链接地址唤起APP
        String scheme = TKExtManage.getInstance().getAppScheme(this) + "://";
        NSLog.d("scheme: " +  scheme);
        if (null != intent && null != intent.getData() && intent.getData().toString().startsWith(scheme)) {//是从H5唤起APP
            Uri uri = intent.getData();
            Config.uriUrl = uri.toString();
            Map<String, Object> mapUriParams = getH5Params(Config.uriUrl);
            NSLog.d("mapUriParams: " +  mapUriParams);
            String h5RoomId = (String) mapUriParams.get("serial");
            AppPrefsUtil.saveFormH5RoomId(h5RoomId);
            if (RoomSession.isInRoom) {//教室已经存在
                if (mapUriParams != null && mapUriParams.size() > 0) {
                    if (mapUriParams.get("serial").equals(RoomVariable.serial)) {      //链接访问的是同一个教室
                        Config.isFreedTKRoomManager = false;
                    } else {
                        Config.isFreedTKRoomManager = true;
                        TKRoomManager.getInstance().leaveRoom();
                    }
                }
//                finish();
            }
        } else {
            PublicPracticalMethodFromJAVA.getInstance().restartApp(this);
        }
    }

    /**
     * 获取H5传递过来的值
     */
    private Map<String, Object> getH5Params(String uri) {
        Map<String, Object> map = new HashMap<>();
        String temp = Uri.decode(uri);
        String[] temps = temp.split("&");
        for (int i = 0; i < temps.length; i++) {
            String[] t = temps[i].split("=");
            if (t.length > 1) {
                map.put(t[0], t[1]);
            }
        }
        return map;
    }

    @Override
    protected void initUiView() {
        iv_logo = findViewById(R.id.iv_logo);
        cl_start = findViewById(R.id.cl_start);
    }

    @Override
    protected void initData() {
        //获得手机屏幕信息
        PublicPracticalMethodFromJAVA.getInstance().getPhoneScreenInfo(this);
        //初始化StartUpPresenter
        presenter = new StartUpPresenter(this, this, this);

        VariableConfig.login_process = VariableConfig.login_process_normal;

        //开启计时器
        presenter.startTimerTask();


        //设置语言
        presenter.setLanguage();

        //清理录音遗留文件
        PublicPracticalMethodFromJAVA.getInstance().clearAudioFiles();
        //设置启动页的图标
        presenter.setLogo(iv_logo, cl_start);

        //设置护眼模式
//        Boolean eyeprotection = MySPUtilsUser.getInstance().getUserEyeProtectionStatus();
//        if (eyeprotection) {
//            EyeProtectionUtil.openSuspensionWindow(this, eyeprotection);
//        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean isBaseOnWidth() {
        boolean isBaseOnWidth;
        if (ScreenTools.getInstance().isPad(this)) {
            isBaseOnWidth = false;
        } else {
            isBaseOnWidth = true;
        }
        return isBaseOnWidth;
    }

    @Override
    public float getSizeInDp() {
        float sizeInDp;
        if (ScreenTools.getInstance().isPad(this)) {
            sizeInDp = ConfigConstants.PAD_HEIGHT;
        } else {
            sizeInDp = ConfigConstants.PHONE_WIDTH;
        }
        return sizeInDp;
    }

    /**
     * 权限授权成功 回调
     *
     * @param mark
     */
    @Override
    public void allPermissionsGranted(int mark) {
        if (mark == ConfigConstants.PERMISSIONS_GRANTED_STARTUPACTIVITY) {
            String token = MySPUtilsUser.getInstance().getUserToken();
            if (!StringUtils.isBlank(token)) {
                iv_logo.postDelayed(() -> {
                    //跳转到首页
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(StartUpActivity.this, MainMenuActivity.class, -1, null, true,
                            R.anim.activity_xhold, R.anim.activity_xhold);
                }, 100);
            } else {
                iv_logo.postDelayed(() -> {
                    //跳转到登录页
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(StartUpActivity.this, LoginPlusActivity.class, -1, null, true,
                            R.anim.activity_xhold, R.anim.activity_xhold);
                }, 100);

            }
        }
    }

    /**
     * 权限拒绝 回调
     */
    @Override
    public void permissionsDenied(int mark) {
        String token = MySPUtilsUser.getInstance().getUserToken();
        if (!StringUtils.isBlank(token)) {
            iv_logo.postDelayed(() -> {
                //跳转到首页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(StartUpActivity.this, MainMenuActivity.class, -1, null, true,
                        R.anim.activity_xhold, R.anim.activity_xhold);
            }, 100);
        } else {
            iv_logo.postDelayed(() -> {
                //跳转到登录页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(StartUpActivity.this, LoginPlusActivity.class, -1, null, true,
                        R.anim.activity_xhold, R.anim.activity_xhold);
            }, 100);

        }
    }
}

