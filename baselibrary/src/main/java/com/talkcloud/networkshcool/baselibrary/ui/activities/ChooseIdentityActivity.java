package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.UserIdentityEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.ChooseIdentityPresenter;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.ChooseIdentityView;

import org.greenrobot.eventbus.EventBus;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/14
 * Time:9:43
 * author:joker
 * 用户选择身份
 */
public class ChooseIdentityActivity extends BaseActivity implements ChooseIdentityView, View.OnClickListener, CustomAdapt {


    private ChooseIdentityPresenter presenter;

    private ConstraintLayout cl_close;
    private ConstraintLayout cl_teacher;
    private ConstraintLayout cl_student;
    private TextView tv_confirm;
    private ImageView iv_teacher_chosen;
    private ImageView iv_student_chosen;
    private ImageView iv_close;
    private TextView tv_teacher;
    private TextView tv_student;


    private String current_identity;
    private String activity_species = "";


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
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chooseidentity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }*/
    }

    @Override
    protected void initUiView() {
        cl_close = findViewById(R.id.cl_close);
        cl_teacher = findViewById(R.id.cl_teacher);
        cl_student = findViewById(R.id.cl_student);
        tv_confirm = findViewById(R.id.tv_confirm);
        iv_teacher_chosen = findViewById(R.id.iv_teacher_chosen);
        iv_student_chosen = findViewById(R.id.iv_student_chosen);
        iv_close = findViewById(R.id.iv_close);
        tv_teacher = findViewById(R.id.tv_teacher);
        tv_student = findViewById(R.id.tv_student);
    }

    @Override
    protected void initData() {
        presenter = new ChooseIdentityPresenter(this, this);

        if (ScreenTools.getInstance().isPad(this)) {
            //设置返回按钮的背景
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(this, cl_close, getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
        } else {
            iv_close.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.vcodeinput_return));
        }

        //设置确定按钮的背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(this, tv_confirm, getResources().getDimensionPixelSize(R.dimen.dimen_16x), -1, "", VariableConfig.color_button_selected);

        //获取传过来的值
        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            current_identity = bundle.getString("current_identity");
            activity_species = bundle.getString(ConfigConstants.ACTIVITY_SPECIES);
            if (current_identity.equals("7")) {//当前身份是老师
                iv_teacher_chosen.setVisibility(View.VISIBLE);
                iv_student_chosen.setVisibility(View.GONE);
                presenter.setTeacherAndStudentBG(tv_teacher, "#ffffffff", cl_teacher, VariableConfig.color_teacher_bg);
                presenter.setTeacherAndStudentBG(tv_student, "#8F92A1", cl_student, "#ffffffff");
            } else {//当前身份是学生
                iv_teacher_chosen.setVisibility(View.GONE);
                iv_student_chosen.setVisibility(View.VISIBLE);

                presenter.setTeacherAndStudentBG(tv_teacher, "#8F92A1", cl_teacher, "#ffffffff");
                presenter.setTeacherAndStudentBG(tv_student, "#ffffffff", cl_student, VariableConfig.color_student_bg);
            }
        }

    }

    @Override
    protected void initListener() {
        tv_confirm.setOnClickListener(this);
        cl_teacher.setOnClickListener(this);
        cl_student.setOnClickListener(this);
        cl_close.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_confirm) {
            String token = MySPUtilsUser.getInstance().getUserToken();
            presenter.changeloginidentity(token, current_identity);
        } else if (id == R.id.cl_teacher) {//切换老师
            current_identity = ConfigConstants.IDENTITY_TEACHER;
            iv_teacher_chosen.setVisibility(View.VISIBLE);
            iv_student_chosen.setVisibility(View.GONE);

            presenter.setTeacherAndStudentBG(tv_teacher, "#ffffffff", cl_teacher, VariableConfig.color_teacher_bg);
            presenter.setTeacherAndStudentBG(tv_student, "#8F92A1", cl_student, "#ffffffff");

        } else if (id == R.id.cl_student) {//切换学生
            current_identity = ConfigConstants.IDENTITY_STUDENT;
            iv_teacher_chosen.setVisibility(View.GONE);
            iv_student_chosen.setVisibility(View.VISIBLE);
            
            presenter.setTeacherAndStudentBG(tv_teacher, "#8F92A1", cl_teacher, "#ffffffff");
            presenter.setTeacherAndStudentBG(tv_student, "#ffffffff", cl_student, VariableConfig.color_student_bg);
        } else if (id == R.id.cl_close) {
            if (ConfigConstants.CHECKIDENTITYPAGE.equals(activity_species)) {
                //发送刷新事件
                MessageEvent event = new MessageEvent();
                event.setMessage_type(EventConstant.CHANGE_IDENTITY_REFRESH);
                EventBus.getDefault().post(event);
            }
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
    }

    @Override
    public void changeloginidentityCallback(boolean isSuccess, UserIdentityEntity userIdentityEntity, String msg) {
        if (isSuccess) {

            MySPUtilsUser.getInstance().saveUserToken(userIdentityEntity.getToken());

            if (StringUtils.isBlank(userIdentityEntity.getCurrent_identity())) {
                MySPUtilsUser.getInstance().saveUserIdentity("");
                AppPrefsUtil.saveUserIdentity("");
            } else {
                MySPUtilsUser.getInstance().saveUserIdentity(userIdentityEntity.getCurrent_identity());
                AppPrefsUtil.saveUserIdentity(userIdentityEntity.getCurrent_identity());
            }


            if (ConfigConstants.MYFRAGMENT.equals(activity_species) ||
                    ConfigConstants.CHECKIDENTITYPAGE.equals(activity_species)) {//我的页面切换身份、检查身份切换身份
                //发送刷新事件
                MessageEvent event = new MessageEvent();
                event.setMessage_type(EventConstant.CHANGE_IDENTITY_REFRESH);
                EventBus.getDefault().post(event);
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            } else {
                //跳转到首页
                PublicPracticalMethodFromJAVA.getInstance().intentToJump(this, MainMenuActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true,
                        R.anim.activity_xhold, R.anim.activity_right_out);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
/*        //注销EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }*/
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
            if (ConfigConstants.CHECKIDENTITYPAGE.equals(activity_species)) {
                //发送刷新事件
                MessageEvent event1 = new MessageEvent();
                event1.setMessage_type(EventConstant.CHANGE_IDENTITY_REFRESH);
                EventBus.getDefault().post(event1);
            }
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
        return false;
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
}
