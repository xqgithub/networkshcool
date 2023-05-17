package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.presenters.CountryAreaPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.CountryAreaAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.CountryAreaView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/13
 * Time:9:12
 * author:joker
 * 国家区域号页面
 */
public class CountryAreaActivity extends BaseActivity implements CountryAreaView, View.OnClickListener, CustomAdapt {


    private CountryAreaPresenter presenter;
    private ImageView iv_close;
    private ImageView iv_left_bg;
    private TextView tv_currentcountryname;
    private TextView tv_currentcountrycode;

    private RecyclerView rv_countrycode;
    private CountryAreaAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private String localename = "";
    private String localecode = "";


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
        return R.layout.activity_countryarea;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initUiView() {
        iv_close = findViewById(R.id.iv_close);
        rv_countrycode = findViewById(R.id.rv_countrycode);
        tv_currentcountryname = findViewById(R.id.tv_currentcountryname);
        tv_currentcountrycode = findViewById(R.id.tv_currentcountrycode);
        iv_left_bg = findViewById(R.id.iv_left_bg);
    }

    @Override
    protected void initData() {
        presenter = new CountryAreaPresenter(this, this);

        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            localename = bundle.getString("localename");
            localecode = bundle.getString("localecode");

        }
        tv_currentcountryname.setText(localename);
        tv_currentcountrycode.setText("+" + localecode);


        presenter.getCountryCodeDatas();

        //初始化RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        rv_countrycode.setLayoutManager(linearLayoutManager);
        adapter = new CountryAreaAdapter(CountryAreaActivity.this, this);
        adapter.setDatas(null);
        rv_countrycode.setAdapter(adapter);

        if (VariableConfig.login_process == VariableConfig.login_process_changemobile ||
                VariableConfig.login_process == VariableConfig.login_process_changepassword) {

            iv_left_bg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            }
        }
    }

    @Override
    public void CountryCodeCallback(boolean isSuccess, List<CountryAreaEntity> countryAreaEntityList, String msg) {
        if (isSuccess) {
            //获取到国家区域的值
            adapter.setDatas(countryAreaEntityList);
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    /**
     * item点击回调
     *
     * @param bean
     * @param position
     */
    @Override
    public void countryAreaitemClick(CountryAreaEntity bean, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("locale", bean.getLocale());
        bundle.putString("localename", bean.getCountry());
        bundle.putString("localecode", bean.getCode());

        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMessage_type(EventConstant.EVENT_COUNTRYAREA);
        messageEvent.setMessage(bundle);
        EventBus.getDefault().post(messageEvent);

        finish();
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
            if (ScreenTools.getInstance().isPad(this)) {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
            } else {
                PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
            }
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
