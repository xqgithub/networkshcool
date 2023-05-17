package com.talkcloud.networkshcool.baselibrary.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;

/**
 * author:wsf
 * createTime:2021/7/29
 * description: 首页页面（客戶定制）
 */
public class HomeFragment extends Fragment {

    private WebView homePage;


    public static HomeFragment newInstance() {
        HomeFragment fg = new HomeFragment();
        Bundle agrs = new Bundle();
        fg.setArguments(agrs);
        return fg;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        if (getActivity() != null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.home_fragment, container, false);

            homePage = view.findViewById(R.id.webview);
            initWebView();
            homePage.setHorizontalScrollBarEnabled(false);//水平不显示
            homePage.setVerticalScrollBarEnabled(false);//垂直不显示

            String url = TKExtManage.getInstance().getHomeExtUrl();
            NSLog.d(" url : " + url);
            homePage.loadUrl(url);
            homePage.setWebViewClient(new WebViewClient() {

            });


        }

        return view;
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        if (homePage != null) {

            WebSettings webSetting = homePage.getSettings();
            // 设置字符编码
            webSetting.setDefaultTextEncodingName("utf-8");
            webSetting.setSupportZoom(false);//支持缩放，默认为true。是下面那个的前提。
            webSetting.setBuiltInZoomControls(false); //设置内置的缩放控件。
            webSetting.setJavaScriptEnabled(true);
            webSetting.setLoadWithOverviewMode(true);
            webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
            webSetting.setAllowFileAccess(true);
            webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSetting.setSupportZoom(true);
            webSetting.setBuiltInZoomControls(false);
            webSetting.setUseWideViewPort(true);
            webSetting.setSupportMultipleWindows(true);
            // webSetting.setLoadWithOverviewMode(true);
            webSetting.setAppCacheEnabled(true);
            // webSetting.setDatabaseEnabled(true);
            webSetting.setDomStorageEnabled(true);
            webSetting.setGeolocationEnabled(true);
            webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
            // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
            webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
            // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSetting.setLoadWithOverviewMode(true);

        }
    }
}
