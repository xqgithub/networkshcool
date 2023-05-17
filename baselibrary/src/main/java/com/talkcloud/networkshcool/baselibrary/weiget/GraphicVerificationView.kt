package com.talkcloud.networkshcool.baselibrary.weiget

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import androidx.constraintlayout.widget.ConstraintLayout
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage
import com.talkcloud.networkshcool.baselibrary.utils.JsInterface
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils
import kotlinx.android.synthetic.main.layout_graphicverification.view.*
import java.io.File
import java.util.*

/**
 * Date:2021/10/12
 * Time:10:28
 * author:joker
 *  图文验证 自定义View
 */
class GraphicVerificationView @JvmOverloads constructor(
    var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {

    private lateinit var mView: View

    //历史栈
    private lateinit var loadHistoryUrls: MutableList<String>

//    private var linkurl = "file:///android_asset/web/graphicverification.html"

    // 缓存目录文件夹
    private val APP_CACAHE_DIRNAME = "/webcache"

    init {
        initView()

        initData()

        initListener()
    }

    /**
     * 初始化UI控件
     */
    fun initView() {
        mView = View.inflate(mContext, R.layout.layout_graphicverification, this)
        loadHistoryUrls = mutableListOf()
    }

    /**
     * 初始化监听
     */
    fun initListener() {
    }

    /**
     * 初始化数据
     */
    fun initData() {
        val locale_language = MySPUtilsLanguage.getInstance().localeLanguage
        val locale_country = MySPUtilsLanguage.getInstance().localeCountry
        changeAppLanguage(resources, locale_language + "_" + locale_country)

        initWebView()


        wv_graphicverification.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                loadHistoryUrls.add(request.url.toString())
                view.loadUrl(request.url.toString())
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                LogUtils.i("onPageStarted--url--->$url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                LogUtils.i("onPageFinished--url--->$url")
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                LogUtils.i("onLoadResource--url--->$url")
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                LogUtils.i("onReceivedError----->$error")
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//                super.onReceivedSslError(view, handler, error)
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage("SSL证书验证错误")
                builder.setPositiveButton("继续") { dialog, which ->
                    handler!!.proceed() //表示等待证书响应
                    dialog.dismiss()
                }
                builder.setNegativeButton("取消") { dialog, which ->
                    handler!!.cancel() //表示挂起连接，为默认方式
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        wv_graphicverification.webChromeClient = object : WebChromeClient() {
            //=========多窗口的问题==========================================================
            override fun onCreateWindow(view: WebView, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message): Boolean {
                val transport = resultMsg.obj as WebViewTransport
                transport.webView = view
                resultMsg.sendToTarget()
                return true
            } //=========多窗口的问题==========================================================
        }


        wv_graphicverification.setDownloadListener(MyWebViewDownLoadListener())


        wv_graphicverification.setBackgroundColor(0) // 设置背景色
//        wv_graphicverification.background.alpha = 0 // 设置填充透明度 范围：0-255
    }

    /**
     * 初始化WebView
     */
    private fun initWebView() {
        //支持获取手势焦点，输入用户名、密码或其他
        wv_graphicverification.requestFocusFromTouch()
        with(wv_graphicverification.settings) {
            // 表示webView可以执行服务器端的js的代码
            javaScriptEnabled = true
            // 设置字符编码
            defaultTextEncodingName = "uft-8"
            // 提高渲染的优先级  @deprecated
//            setRenderPriority(WebSettings.RenderPriority.HIGH)
            //设置 缓存模式
            cacheMode = WebSettings.LOAD_NO_CACHE
            // 开启 DOM storage API 功能
//        webSettings.setDomStorageEnabled(true);
            // 开启 database storage API 功能
//        webSettings.setDatabaseEnabled(true);
            val cacheDirPath: String = mContext.filesDir.absolutePath + APP_CACAHE_DIRNAME
            LogUtils.i("cacheDirPath----->$cacheDirPath")
            // 设置数据库缓存路径
//        webSettings.setDatabasePath(cacheDirPath);
            // 设置 Application Caches 缓存目录
//        webSettings.setAppCachePath(cacheDirPath);
            // 开启 Application Caches 功能
//        webSettings.setAppCacheEnabled(true);
            //设置自适应屏幕，两者合用
            useWideViewPort = true //将图片调整到适合webview的大小
            loadWithOverviewMode = true // 缩放至屏幕的大小
            loadsImagesAutomatically = true //支持自动加载图片
            setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
            builtInZoomControls = false //设置内置的缩放控件。
            //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        // NORMAL：正常显示，没有渲染变化。
//        // SINGLE_COLUMN：把所有内容放到WebView组件等宽的一列中。 //这个是强制的，把网页都挤变形了
//        // NARROW_COLUMNS：可能的话，使所有列的宽度不超过屏幕宽度。 //好像是默认的
//                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        webSettings.setAllowFileAccess(true);  //设置可以访问文件
//        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
//        webSettings.setDefaultFontSize(1);//设置 WebView 字体的大小，默认大小为 16
//        webSettings.setMinimumFontSize(getResources().getDimensionPixelSize(R.dimen.dimen_15x));//设置 WebView 支持的最小字体大小，默认为 8
            blockNetworkImage = Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT //图片最后加载
            /**
             *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
             *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

            //多窗口问题
            newWin(this)

            //webview的debug模式
//            setWebContentsDebuggingEnabled(true)
        }
    }

    /**
     * 加载url地址
     */
    fun loadUrl(url: String = "file:///android_asset/web/graphicverification.html") {
        loadHistoryUrls.add(url)
        wv_graphicverification.loadUrl(url)
    }


    /**
     * WebView下载问题解决方法
     */
    inner class MyWebViewDownLoadListener : DownloadListener {

        override fun onDownloadStart(
            url: String, userAgent: String,
            contentDisposition: String, mimetype: String, contentLength: Long
        ) {
            /**
             * 通过浏览器下载apk
             */
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
            downloadByBrowser(url)
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
    fun downloadByBrowser(url: String) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        if (intent.resolveActivity(mContext.packageManager) != null) {
            val componentName = intent.resolveActivity(mContext.packageManager)
            LogUtils.e("componentName = " + componentName.className)
            mContext.startActivity(Intent.createChooser(intent, "请选择浏览器"))
        }
    }

    /**
     * 多窗口的问题
     */
    private fun newWin(mWebSettings: WebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false)
        mWebSettings.javaScriptCanOpenWindowsAutomatically = true
    }


    /**
     * 这里做个语言处理，防止webview页面开启后，app内语言切换出现混乱。
     *
     * @param resources
     * @param lanAtr
     */
    fun changeAppLanguage(resources: Resources, lanAtr: String) {
        val datas = lanAtr.split("_").toTypedArray()
        val locale = Locale(datas[0], datas[1])
        var context = TKBaseApplication.myApplication.applicationContext
        val configuration = resources.configuration
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        if (Build.VERSION.SDK_INT >= 25) {
            context = context.applicationContext.createConfigurationContext(configuration)
            context = context.createConfigurationContext(configuration)
        }
        context.resources.updateConfiguration(
            configuration,
            resources.displayMetrics
        )
    }

    /**
     * 清楚webView的缓存
     */
    fun clearWebViewCache() {
        // 清理Webview缓存数据库
        try {
            mContext.deleteDatabase("webview.db")
            mContext.deleteDatabase("webviewCache.db")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // WebView 缓存文件
        val appCacheDir: File = File(
            mContext.filesDir.absolutePath
                    + APP_CACAHE_DIRNAME
        )
        LogUtils.i("appCacheDir path----->" + appCacheDir.absolutePath)
        val webviewCacheDir: File = File(mContext.filesDir.absolutePath + "/webviewCache")
        LogUtils.i("webviewCacheDir path----->" + webviewCacheDir.absolutePath)

        // 删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir)
        }
        // 删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir)
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    fun deleteFile(file: File) {
        LogUtils.i("delete file path----->" + file.absolutePath)
        if (file.exists()) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                val files = file.listFiles()
                for (i in files.indices) {
                    deleteFile(files[i])
                }
            }
            file.delete()
        } else {
            LogUtils.i("delete file no exists----->" + file.absolutePath)
        }
    }


    interface GraphicVerificationListener {
        fun jsReturnResults(msg: Int, ticket: String, randstr: String)
    }

    private var graphicverificationlistener: GraphicVerificationListener? = null
    fun setGraphicVerificationListener(graphicverificationlistener: GraphicVerificationListener) {
        this.graphicverificationlistener = graphicverificationlistener
        graphicverificationlistener?.let {
            // 通过addJavascriptInterface()将Java对象映射到JS对象
            //参数1：Javascript对象名
            //参数2：Java对象名
            wv_graphicverification.addJavascriptInterface(JsInterface(graphicverificationlistener), "GraphicVerificationView") //JS类对象映射到js的t对象
        }
    }
}