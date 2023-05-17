package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.presenters.LanguageSwitchPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.LanguageSwitchAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.views.LanguageSwitchView;

import java.util.Map;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/18
 * Time:16:21
 * author:joker
 * 语言切换页面
 */
public class LanguageSwitchActivity extends BaseActivity implements LanguageSwitchView, View.OnClickListener, CustomAdapt {

    private LanguageSwitchPresenter presenter;

    private ImageView iv_close;

    private RecyclerView rv_language;
    private LanguageSwitchAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onBeforeSetContentLayout() {
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //隐藏状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }

        //状态栏状态设置
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        false, true,
                        -1
                );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_languageswitch;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initUiView() {
        rv_language = findViewById(R.id.rv_language);
        iv_close = findViewById(R.id.iv_close);
    }

    @Override
    protected void initData() {
        presenter = new LanguageSwitchPresenter(this, this);

        //初始化RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        rv_language.setLayoutManager(linearLayoutManager);
        adapter = new LanguageSwitchAdapter(this, this);
        rv_language.setAdapter(adapter);
        adapter.setDatas(PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(this));
        adapter.updateUIbg(VariableConfig.color_button_selected);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
    }

    @Override
    public void LanguageSwitchAdapterOnClick(int position) {
        //当前的选择的语言的数据
        Map<String, String> map = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(this).get(position);
        String name = map.get("name");
        String language = map.get(SPConstants.LOCALE_LANGUAGE);
        String area = map.get(SPConstants.LOCALE_COUNTRY);
        presenter.changeLanguage(name, language, area);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
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
