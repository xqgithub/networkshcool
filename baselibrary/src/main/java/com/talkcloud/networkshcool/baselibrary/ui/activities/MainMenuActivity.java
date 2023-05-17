package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.eduhdsdk.interfaces.JoinmeetingCallBack;
import com.eduhdsdk.interfaces.MeetingNotify;
import com.eduhdsdk.room.RoomClient;
import com.talkcloud.corelibrary.TKSdkApi;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.base.BaseMvpActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UserInfoEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.MainMenuPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.CourseFragment;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.HomeFragment;
import com.talkcloud.networkshcool.baselibrary.ui.fragment.MyFragment;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils;
import com.talkcloud.networkshcool.baselibrary.utils.HandlerUtils;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.NotificationsUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.MainMenuView;
import com.talkcloud.room.TKRoomManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainMenuActivity extends BaseMvpActivity<MainMenuPresenter> implements View.OnClickListener, MainMenuView, JoinmeetingCallBack, MeetingNotify, PermissionsActivity.PermissionsListener, HandlerUtils.OnReceiveMessageListener {
    public static final String TAG = "MainMenuActivity";

    private static int sequence = 1;


    private TextView tv_course, tv_profile, tv_setting, tv_username, tv_home;
    private LinearLayout tv_coursell, tv_profilell, tv_settingll, user_ll, home_ll;
    private ImageView img_course, img_profile, img_avatar, img_customHome;
    private CourseFragment courseFragment;
    private MyFragment myFragment;
    private HomeFragment homeFragment; //客戶定制的首页

    private MainMenuPresenter mPresenter;

    private int curIndex = 0;
    private int lastIndex = -1;

    public static boolean isFirstOpen = true;

    private ConstraintLayout cl_notify;
    private ImageView iv_notify;
    private TextView tv_notify_num;


    @Override
    protected void onBeforeSetContentLayout() {
        //TKDensityUtil.setDensity(this, MyApplication.myApplication, ScreenTools.getInstance().isPad(this));


        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }

      /*  //设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);*/
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_menu;
    }

    @Override
    protected void initUiView() {
        // 配置sdk参数
//        TKSdkApi.configParam(this, R.string.app_name, R.mipmap.ic_launcher);
        int appNameRes = TKExtManage.getInstance().getAppNameRes(this);
        int appLogoRes = TKExtManage.getInstance().getAppLogoRes(this);
        TKSdkApi.configParam(this, appNameRes, appLogoRes);

        if (ScreenTools.getInstance().isPad(this)) {
            img_avatar = findViewById(R.id.img_avatar);
            tv_username = findViewById(R.id.tv_username);
            tv_setting = findViewById(R.id.tv_setting);
            user_ll = findViewById(R.id.user_ll);
            tv_settingll = findViewById(R.id.tv_settingll);
            tv_settingll.setOnClickListener(this);
            user_ll.setOnClickListener(this);

            cl_notify = findViewById(R.id.cl_notify);
            iv_notify = findViewById(R.id.iv_notify);
            tv_notify_num = findViewById(R.id.tv_notify_num);
            cl_notify.setOnClickListener(this);
        }
        img_course = findViewById(R.id.img_course);
        img_profile = findViewById(R.id.img_mine);
        img_customHome = findViewById(R.id.img_home);
        tv_coursell = findViewById(R.id.tv_coursell);
        tv_profilell = findViewById(R.id.tv_minell);
        home_ll = findViewById(R.id.ll_home);
        tv_course = findViewById(R.id.tv_course);
        tv_profile = findViewById(R.id.tv_mine);
        tv_home = findViewById(R.id.tv_home);
        tv_coursell.setOnClickListener(this);
        tv_profilell.setOnClickListener(this);
        home_ll.setOnClickListener(this);
        if (TKExtManage.getInstance().isShowHomeExtBtn()) {
            ShowHomeFragment();
        } else {
            home_ll.setVisibility(View.GONE);
            ShowCourseFragment();
        }

    }

    @Override
    protected void initData() {
        // 检查推送权限
//        checkNotification();

        //关闭栈的多余的Activities
        PublicPracticalMethodFromJAVA.getInstance().closeStackActivities(1);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


        mPresenter = new MainMenuPresenter(this, this, this);
        // 获取用户详细信息
//        mPresenter.getUserInfo();
        //获取用户详细信息
        String useridentity = MySPUtilsUser.getInstance().getUserIdentity();
        if (!StringUtils.isBlank(useridentity)) {
            //获取用户详情接口
            mPresenter.getUserInfo();
        }

        //初始化handler
        handler = new HandlerUtils.HandlerHolder(Looper.myLooper(), this, this);
        //获取是否需要更新应用数据 TODO
        mPresenter.sysversion();
    }

    private void checkNotification() {

        NSLog.d("NotificationsUtils.isNotificationsEnabled(this) : " + NotificationsUtils.isNotificationsEnabled(this));

        if (!NotificationsUtils.isNotificationsEnabled(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this)
//                    .setCancelable(true)
                    .setTitle(getString(R.string.notify_permission_title))
                    .setMessage(getString(R.string.notify_permission_content))
                    .setNegativeButton(getString(R.string.buttonCancel), (dialog, which) -> dialog.cancel()).setPositiveButton(getString(R.string.notify_permission_btn_open), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", MainMenuActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", MainMenuActivity.this.getPackageName());
                                intent.putExtra("app_uid", MainMenuActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) { //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + MainMenuActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", MainMenuActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    protected void initListener() {

        RoomClient.getInstance().regiestInterface(this, this);

        isFirstOpen = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ScreenTools.getInstance().isPad(this)) {
            // TODO 编辑后 暂时这样处理 后期优化为事件
            String localUserName = AppPrefsUtil.getUserName();
            if (!TextUtils.isEmpty(localUserName)) {
                tv_username.setText(this.getResources().getString(R.string.hi) + "," + localUserName);
            } else {
                String mobile = MySPUtilsUser.getInstance().getUserMobile();
                tv_username.setText(this.getResources().getString(R.string.hi) + "," + mobile);
            }

            String localHeaderUrl = AppPrefsUtil.getRemoteHeaderUrl();
            if (!TextUtils.isEmpty(localHeaderUrl)) {
                GlideUtils.loadHeaderImg(this, localHeaderUrl, img_avatar);
            }
        } else {
            //状态栏状态设置
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            false, true,
                            -1
                    );
        }

        //获取用户身份
        String useridentity = MySPUtilsUser.getInstance().getUserIdentity();
        if (StringUtils.isBlank(useridentity)) {
            PublicPracticalMethodFromJAVA.getInstance().checkIdentity(this);
        } else {
            //获取未读通知信息
            mPresenter.noticeNew();
        }
    }

    private void ShowCourseFragment() {
        if (curIndex != lastIndex) {


            if (ScreenTools.getInstance().isPad(this)) {
                img_course.setImageResource(R.mipmap.ic_course);
                tv_coursell.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_blue_bg));
                img_course.setBackground(null);
                tv_course.setTextColor(getResources().getColor(R.color.appwhite));


                img_customHome.setImageResource(R.mipmap.ic_home_nosel);
                img_customHome.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                home_ll.setBackground(null);
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

                img_profile.setImageResource(R.mipmap.ic_mine_nosel);
                img_profile.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_profilell.setBackground(null);
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));
            } else {
                img_profile.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

                img_customHome.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));


                img_course.setBackgroundColor(ContextCompat.getColor(this, R.color.ns_color_primary));
                tv_course.setTextColor(ContextCompat.getColor(this, R.color.ns_color_primary));
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (myFragment != null) {
                transaction.hide(myFragment);
            } else {
                myFragment = MyFragment.newInstance();
            }

            if (homeFragment != null) {
                transaction.hide(homeFragment);
            }
            if (courseFragment == null || !courseFragment.isAdded()) {
                courseFragment = CourseFragment.newInstance("course");
                transaction.add(R.id.fragment_container, courseFragment);
            } else {
                transaction.show(courseFragment);
                //获取未读通知信息
                mPresenter.noticeNew();
            }


            //   hideAllFragement();

            lastIndex = 0;
            transaction.commitNowAllowingStateLoss();
        }

    }

    private void ShowProfileFragment() {
        if (curIndex != lastIndex) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (courseFragment != null) {
                transaction.hide(courseFragment);
            }
            if (homeFragment != null) {
                transaction.hide(homeFragment);
            }
            if (myFragment == null || !myFragment.isAdded()) {
                myFragment = MyFragment.newInstance();
                transaction.add(R.id.fragment_container, myFragment);
            } else {
                if (isFirstOpen)
                    transaction.add(R.id.fragment_container, myFragment);
                transaction.show(myFragment);
                //发送事件
                MessageEvent event = new MessageEvent();
                event.setMessage_type(EventConstant.MY_FLOWS_REFRESH);
                EventBus.getDefault().post(event);
            }
            lastIndex = 1;

            Log.d(TAG, "ShowContactsFragment ,fg_contacts is not null");
            transaction.commitNowAllowingStateLoss();

            if (ScreenTools.getInstance().isPad(this)) {
                img_profile.setBackground(null);
                img_profile.setImageResource(R.mipmap.ic_mine);
                tv_profilell.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_blue_bg));
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.appwhite));


                img_customHome.setImageResource(R.mipmap.ic_home_nosel);
                img_customHome.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                home_ll.setBackground(null);
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

                img_course.setImageResource(R.mipmap.ic_course_nosel);
                img_course.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_coursell.setBackground(null);
                tv_course.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));
            } else {
                img_profile.setBackgroundColor(ContextCompat.getColor(this, R.color.ns_color_primary));
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.ns_color_primary));

                img_customHome.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

                img_course.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_course.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));
            }


        }

    }

    private void ShowHomeFragment() {
        if (curIndex != lastIndex) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (courseFragment != null) {
                transaction.hide(courseFragment);
            }
            if (myFragment != null) {
                transaction.hide(myFragment);
            }
            if (homeFragment == null || !homeFragment.isAdded()) {
                homeFragment = HomeFragment.newInstance();
                transaction.add(R.id.fragment_container, homeFragment);
            } else {
                if (isFirstOpen)
                    transaction.add(R.id.fragment_container, homeFragment);
                transaction.show(homeFragment);
            }
            lastIndex = 2;

            Log.d(TAG, "ShowContactsFragment ,fg_contacts is not null");
            transaction.commitNowAllowingStateLoss();

            if (ScreenTools.getInstance().isPad(this)) {
                img_customHome.setBackground(null);
                img_customHome.setImageResource(R.mipmap.ic_home_select);
                home_ll.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_blue_bg));
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.appwhite));


                img_course.setImageResource(R.mipmap.ic_course_nosel);
                img_course.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_coursell.setBackground(null);
                tv_course.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));


                img_profile.setImageResource(R.mipmap.ic_mine_nosel);
                img_profile.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_profilell.setBackground(null);
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

            } else {
                img_customHome.setBackgroundColor(ContextCompat.getColor(this, R.color.ns_color_primary));
                tv_home.setTextColor(ContextCompat.getColor(this, R.color.ns_color_primary));

                img_profile.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_profile.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));

                img_course.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_trans));
                tv_course.setTextColor(ContextCompat.getColor(this, R.color.bg_gray_92949A));
            }


        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_coursell) {
            curIndex = 0;
            ShowCourseFragment();
        } else if (id == R.id.tv_minell) {
            curIndex = 1;
            ShowProfileFragment();
        } else if (id == R.id.user_ll) {
            curIndex = 1;
            ShowProfileFragment();
        } else if (id == R.id.tv_settingll) {
            //跳转到个人设置页面
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(MainMenuActivity.this, PersonalSettingsActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        } else if (id == R.id.ll_home) {
            curIndex = 2;
            ShowHomeFragment();
        } else if (id == R.id.cl_notify) {
            //跳转到应用内通知列表页面
            PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, NoticeInAppActivity.class, -1, null, false, R.anim.activity_right_in, R.anim.activity_xhold);
        }
    }


    @Override
    public void showUserInfo(UserInfoEntity userInfo) {
        if (userInfo != null) {

            // 保存用户名头像
            AppPrefsUtil.saveUserName(userInfo.getUsername());
            AppPrefsUtil.saveRemoteHeaderUrl(userInfo.getAvatar());

            // 保存用户id
            AppPrefsUtil.saveUserId(userInfo.getUserid());
         /*   String lastAlis = MMKV.defaultMMKV().getString(TagAliasOperatorHelper.ALIAS_DATA, "");
            if (!TextUtils.isEmpty(userInfo.getUserid()) &&
                    !userInfo.getUserid().equals(lastAlis)) {
                String alias = userInfo.getUserid();
                //    alias = "1001872";
                MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA, alias);
                JPushInterface.setAlias(this, sequence++, alias);
            }*/


            //发送事件
            EventBus.getDefault().post(new MessageEvent(EventConstant.EVENT_EDIT_USER_INFO));


            if (ScreenTools.getInstance().isPad(this)) {

                GlideUtils.loadHeaderImg(this, userInfo.getAvatar(), img_avatar);
                // String localUserName = AppPrefsUtil.getUserName();
                if (!TextUtils.isEmpty(userInfo.getUsername())) {
                    tv_username.setText(this.getResources().getString(R.string.hi) + "," + userInfo.getUsername());
                } else {
                    String mobile = MySPUtilsUser.getInstance().getUserMobile();
                    tv_username.setText(this.getResources().getString(R.string.hi) + "," + mobile);
                }
            }
            // tv_username.setText(userInfo.getUsername());
        }
    }

    //权限回调  requestCode  4
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals("android.permission.CAMERA")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasVideo = true;
                    TKRoomManager.getInstance().getMySelf().setHasVideo(true);
                }
            } else if (permissions[i].equals("android.permission.RECORD_AUDIO")) {
                if (grantResults[i] == 0) {
//                    TKRoomManager.getInstance().getMySelf().hasAudio = true;
                    TKRoomManager.getInstance().getMySelf().setHasAudio(true);
                }
            }
        }
        RoomClient.getInstance().checkPermissionsFinshJoinRoom(this);
    }


    @Override
    public void callBack(int i) {
        Log.d("goyw", "callBack : " + i);
    }

    @Override
    public void onKickOut(int i, String s) {

    }

    @Override
    public void onWarning(int i) {

    }

    @Override
    public void onClassBegin() {

    }

    @Override
    public void onClassDismiss() {

    }

    @Override
    public void onLeftRoomComplete() {
    }

    @Override
    public void onEnterRoomFailed(int i, String s) {

    }

    @Override
    public void joinRoomComplete() {

    }

    @Override
    public void onCameraDidOpenError() {

    }

    @Override
    public void onOpenEyeProtection(boolean isOpen) {

    }

//    @Override
//    public void onOpenEyeProtection(boolean b) {
//
//    }


    /**
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void EventBusMessage(MessageEvent messageEvent) {
        if (messageEvent.getMessage_type().equals(EventConstant.CHANGE_IDENTITY_REFRESH)
                || messageEvent.getMessage_type().equals(EventConstant.NO_IDENTITY_REFRESH)) {
            VariableConfig.checkIdentityFlag = false;
            //获取用户详情接口
            mPresenter.getUserInfo();

            //获取未读通知信息
            mPresenter.noticeNew();
        }
    }


    /**
     * 物理返回键 监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        for (int i = 0; i < TKBaseApplication.myApplication.lifecycleCallbacks.count(); i++) {
            List<Activity> activities = TKBaseApplication.myApplication.lifecycleCallbacks.getActivitys();
            LogUtils.i(ConfigConstants.TAG_ALL, "activities =-= " + TKBaseApplication.myApplication.lifecycleCallbacks.getActivityName(activities.get(i).toString()));
        }

        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
//            Toast.makeText(this, getResources().getString(R.string.toast_double_exit), Toast.LENGTH_SHORT).show();
            ToastUtils.showShortTop(getResources().getString(R.string.toast_double_exit));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }


    /**
     * 系统版本接口 回调
     *
     * @param isSuccess
     * @param sysVersionEntity
     * @param msg
     */
    private SysVersionEntity sysVersionEntity;
    //是否有版本需要更新
    private boolean is_update = false;
    private Handler handler;
    private final int HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS = -10000;
    private final int HANDLERWHAT_SYSVERSIONCALLBACKFAILURE = -10001;
    private final int HANDLERWHAT_EYEPROTECTION = -10002;

    @Override
    public void sysversionCallback(boolean isSuccess, @Nullable SysVersionEntity sysVersionEntity, @Nullable String msg) {
        if (isSuccess) {
            if (!StringUtils.isBlank(sysVersionEntity)) {
                this.sysVersionEntity = sysVersionEntity;
                PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS, 1);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, HANDLERWHAT_SYSVERSIONCALLBACKFAILURE, 1);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
            PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(handler, HANDLERWHAT_SYSVERSIONCALLBACKFAILURE, 1);
        }
    }


    /**
     * @param isSuccess
     * @param data
     * @param msg
     */
    @Override
    public void noticenew(boolean isSuccess, Object data, String msg) {
        if (isSuccess) {
            Map<String, Integer> map = (Map<String, Integer>) data;
            int count = map.get("count");
            if (ScreenTools.getInstance().isPad(this)) {
                if (count > 0) {
                    tv_notify_num.setVisibility(View.VISIBLE);
                    //设置通知数字显示的背景
//                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(TKBaseApplication.myApplication.getApplicationContext(), tv_notify_num, getResources().getDimensionPixelSize(R.dimen.dimen_16x), "#FF2855");
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(TKBaseApplication.myApplication.getApplicationContext(), tv_notify_num,
                            getResources().getDimensionPixelSize(R.dimen.dimen_20x), -1, "", "#FF2855");
                    if (count > 999) {
                        tv_notify_num.setText("···");
                        return;
                    }
                    tv_notify_num.setText(count + "");
                } else {
                    tv_notify_num.setVisibility(View.GONE);
                }
            }

            //发送事件
            MessageEvent event = new MessageEvent();
            event.setMessage(map);
            event.setMessage_type(EventConstant.EVENT_UNREAD_NOTIFICATION_MAIN_UI_UPDATE);
            EventBus.getDefault().post(event);
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    /**
     * 请求权限回调
     *
     * @param mark
     */
    @Override
    public void allPermissionsGranted(int mark) {

    }

    /**
     * 权限拒绝 回调
     */
    @Override
    public void permissionsDenied(int mark) {
    }


    /**
     * handler 回调
     *
     * @param msg
     */
    @Override
    public void handlerMessage(Message msg) {
        switch (msg.what) {
            case HANDLERWHAT_SYSVERSIONCALLBACKSUCCESS:
                is_update = true;
                mPresenter.updateAPP(sysVersionEntity);
                break;
            case HANDLERWHAT_SYSVERSIONCALLBACKFAILURE:
                is_update = false;
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!StringUtils.isBlank(handler)) {
            handler.removeCallbacksAndMessages(null);
        }
        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
