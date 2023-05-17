package com.talkcloud.networkshcool.baselibrary.ui.webview;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Date:2021/5/20
 * Time:10:23
 * author:joker
 */
public class UserAgreementWebView extends BaseActivity implements View.OnClickListener, CustomAdapt {


    private ImageView iv_close;
    private TextView tv_title;
    private WebView wv_publicpage;

    private boolean isOnPause = false;

    // 缓存目录文件夹
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    // 传过来的url值
    private String linkurl = "";
    //标题
    private String title = "";

    //历史栈
    private List<String> loadHistoryUrls = new ArrayList<>();

    //用户协议地址-英文版
    public String USERAGREEMENT_URL_EN = "https://doccdncf.talk-cloud.net/static/Agreement/menke/registration-en.html";
    //用户协议地址-简体中文版
    public String USERAGREEMENT_URL_CN = "https://doccdncf.talk-cloud.net/static/Agreement/menke/registration-cn.html";
    //用户协议地址-繁体版
    public String USERAGREEMENT_URL_TW = "https://doccdncf.talk-cloud.net/static/Agreement/menke/registration-tw.html";

    //用户协议 和 隐私政策  种类判断
    private int TYPE = ConfigConstants.USERAGREEMENT;


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
        return R.layout.activity_useragreement;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
        changeAppLanguage(getResources(), locale_language + "_" + locale_country);
    }

    @Override
    protected void initUiView() {
        iv_close = findViewById(R.id.iv_close);
        wv_publicpage = findViewById(R.id.wv_useragreement);
        tv_title = findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (!StringUtils.isBlank(bundle)) {
            TYPE = bundle.getInt("type", ConfigConstants.USERAGREEMENT);
            linkurl = bundle.getString("linkurl", "");
            title = bundle.getString("title", "");
        }
        //判断展示类型，用户协议还是隐私政策
        if (TYPE == ConfigConstants.USERAGREEMENT) {//用户协议
            linkurl = String.format(getResources().getString(R.string.protocol_useragreement_url), TKExtManage.getInstance().getUserAgreementUrl());
            tv_title.setText(R.string.personalsettings_useragreement);
        } else if (TYPE == ConfigConstants.USERPRIVACYPOLICY) {//隐私政策
            linkurl = String.format(getResources().getString(R.string.protocol_userprivacypolicy_url), TKExtManage.getInstance().getUserAgreementUrl());
            tv_title.setText(R.string.personalsettings_privacypolicy);
        } else if (TYPE == ConfigConstants.LESSONPREVIEW) {
            tv_title.setText(title);
        } else if (TYPE == ConfigConstants.SMS_GRAPHIC_VERIFICATION) {
            tv_title.setText(title);
        }

//        //判断隐私协议是否是定制版本
//        if (!StringUtils.isBlank(TKExtManage.getInstance().getUserAgreementUrl())) {
//            linkurl = TKExtManage.getInstance().getUserAgreementUrl();
//        } else {
//            //判断下当前语言
//            String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
//            String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();
//            if (!StringUtils.isBlank(locale_language) && !StringUtils.isBlank(locale_country)) {
//                //获取本地app的国家语言
//                List<Map<String, String>> datas = PublicPracticalMethodFromJAVA.getInstance().getLanguageDatas(this);
//                if (locale_language.equals(datas.get(0).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(0).get(SPConstants.LOCALE_COUNTRY))) {
//                    linkurl = USERAGREEMENT_URL_EN;
//                } else if (locale_language.equals(datas.get(1).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(1).get(SPConstants.LOCALE_COUNTRY))) {
//                    linkurl = USERAGREEMENT_URL_CN;
//                } else if (locale_language.equals(datas.get(2).get(SPConstants.LOCALE_LANGUAGE)) && locale_country.equals(datas.get(2).get(SPConstants.LOCALE_COUNTRY))) {
//                    linkurl = USERAGREEMENT_URL_TW;
//                } else {
//                    linkurl = USERAGREEMENT_URL_EN;
//                }
//            }
//        }

        initWebView();

        loadHistoryUrls.add(linkurl);
        wv_publicpage.loadUrl(linkurl);
        wv_publicpage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadHistoryUrls.add(url);
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.i("onPageStarted--url--->" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtils.i("onPageFinished--url--->" + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                LogUtils.i("onLoadResource--url--->" + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtils.i("onReceivedError----->" + error.toString());
            }


            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserAgreementWebView.this);
                builder.setMessage("SSL证书验证错误");
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();//表示等待证书响应
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();//表示挂起连接，为默认方式
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        wv_publicpage.setWebChromeClient(new WebChromeClient() {
            //=========多窗口的问题==========================================================
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(view);
                resultMsg.sendToTarget();
                return true;
            }
            //=========多窗口的问题==========================================================
        });

        wv_publicpage.setDownloadListener(new MyWebViewDownLoadListener());
    }

    @Override
    protected void initListener() {
        iv_close.setOnClickListener(this);
    }


    /**
     * 初始化WebView
     */
    private void initWebView() {
        //支持获取手势焦点，输入用户名、密码或其他
        wv_publicpage.requestFocusFromTouch();

        WebSettings webSettings = wv_publicpage.getSettings();
        // 表示webView可以执行服务器端的js的代码
        webSettings.setJavaScriptEnabled(true);
        // 设置字符编码
        webSettings.setDefaultTextEncodingName("utf-8");
        // 提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
//        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
//        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        LogUtils.i("cacheDirPath----->" + cacheDirPath);
        // 设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
//        webSettings.setAppCacheEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片

        webSettings.setSupportZoom(false);//支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。
//        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        // NORMAL：正常显示，没有渲染变化。
//        // SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。 //这个是强制的，把网页都挤变形了
//        // NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。 //好像是默认的
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        webSettings.setAllowFileAccess(true);  //设置可以访问文件
//        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
//        webSettings.setDefaultFontSize(1);//设置 WebView 字体的大小，默认大小为 16
//        webSettings.setMinimumFontSize(getResources().getDimensionPixelSize(R.dimen.dimen_15x));//设置 WebView 支持的最小字体大小，默认为 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setBlockNetworkImage(false);
        } else {
            webSettings.setBlockNetworkImage(true);//图片最后加载，
        }
        /**
         *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
         *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        newWin(webSettings);//多窗口问题

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_right_out);
        }
    }


    /**
     * 当Activity执行onResume()时让WebView执行resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (isOnPause) {
                if (wv_publicpage != null) {
                    wv_publicpage.getClass().getMethod("onResume")
                            .invoke(wv_publicpage, (Object[]) null);
                }
                isOnPause = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当Activity执行onPause()时让WebView执行pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (wv_publicpage != null) {
                wv_publicpage.getClass().getMethod("onPause")
                        .invoke(wv_publicpage, (Object[]) null);
                isOnPause = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该处的处理尤为重要: 应该在内置缩放控件消失以后,再执行mWebView.destroy() 否则报错WindowLeaked
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_publicpage != null) {
            wv_publicpage.getSettings().setBuiltInZoomControls(true);
            wv_publicpage.setVisibility(View.GONE);
            wv_publicpage.setWebChromeClient(null);
            wv_publicpage.setWebViewClient(null);
            wv_publicpage.destroy();
            wv_publicpage = null;
        }
        isOnPause = false;
    }


    /**
     * 清楚webView的缓存
     */
    public void clearWebViewCache() {
        // 清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath()
                + APP_CACAHE_DIRNAME);
        LogUtils.i("appCacheDir path----->" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        LogUtils.i("webviewCacheDir path----->" + webviewCacheDir.getAbsolutePath());

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        LogUtils.i("delete file path----->" + file.getAbsolutePath());
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            LogUtils.i("delete file no exists----->" + file.getAbsolutePath());
        }
    }


    /**
     * WebView下载问题解决方法
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            /**
             * 通过浏览器下载apk
             */
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
            downloadByBrowser(url);

            /**
             * 通过自定义下载方法下载APK
             */
//            DownLoadService.url = url;
////            DownLoadService.url = "http://cdn.xiaoxiongyouhao.com/apps/androilas.apk";
//            PublicPracticalMethod.startNotificationService(App.getApplication(), DownLoadService.class);
        }
    }

    /**
     * 使用浏览器下载
     */
    private void downloadByBrowser(String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            final ComponentName componentName = intent.resolveActivity(getPackageManager());
            LogUtils.e("componentName = " + componentName.getClassName());
            startActivity(Intent.createChooser(intent, "请选择浏览器"));
        }
    }


    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * 这里做个语言处理，防止webview页面开启后，app内语言切换出现混乱。
     *
     * @param resources
     * @param lanAtr
     */
    public static void changeAppLanguage(Resources resources, String lanAtr) {
        String[] datas = lanAtr.split("_");
        Locale locale = new Locale(datas[0], datas[1]);
        Context context = TKBaseApplication.myApplication.getApplicationContext();
        Configuration configuration = resources.getConfiguration();
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        if (Build.VERSION.SDK_INT >= 25) {
            context = context.getApplicationContext().createConfigurationContext(configuration);
            context = context.createConfigurationContext(configuration);
        }

        context.getResources().updateConfiguration(configuration,
                resources.getDisplayMetrics());
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
            clearWebViewCache();
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
