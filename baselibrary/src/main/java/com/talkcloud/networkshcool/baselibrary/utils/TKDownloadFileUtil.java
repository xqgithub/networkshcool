package com.talkcloud.networkshcool.baselibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PersonalSettingsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date:2021/5/25
 * Time:15:00
 * author:joker
 * APP应用更新
 */
public class TKDownloadFileUtil {

    private volatile static TKDownloadFileUtil instance;

    //    private Activity mActivity;
    //apk保存路径
    private int progressBar = 0;

    private String filePath = "";


    private NotificationManager mManager;
    private static final int NOTIFICATION_DOWNLOAD_ID = 5;

    public static TKDownloadFileUtil getInstance() {
        if (instance == null) {
            synchronized (TKDownloadFileUtil.class) {
                if (instance == null) {
                    instance = new TKDownloadFileUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 更新UI的handler
     *
     * @param msg
     */
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOADFAILED = 3; //下载失败
    private static final int KILLPROCESS = 4;//杀掉程序进程

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    NotificationHelperUtils.getInstance().getBuilder().setProgress(100, progressBar, false);
                    NotificationHelperUtils.getInstance().getBuilder().setContentText(progressBar + "%");
                    Notification notification = NotificationHelperUtils.getInstance().getBuilder().build();
                    mManager.notify(NOTIFICATION_DOWNLOAD_ID, notification);

                    NSLog.d("progressBar: " + progressBar);
//                    LogUtils.i(ConfigConstants.TAG_ALL, "DOWNLOADING =-= " + progressBar);
                    break;
                case DOWNLOADED:
                    mManager.cancel(NOTIFICATION_DOWNLOAD_ID);
                    ToastUtils.showShortToastFromText("已成功下载到" + filePath, ToastUtils.top);
//                    installAPK();
//                    LogUtils.i(ConfigConstants.TAG_ALL, "DOWNLOADED =-= ");
                    break;
                case DOWNLOADFAILED:
                    mManager.cancel(NOTIFICATION_DOWNLOAD_ID);
                    ToastUtils.showShortToastFromRes(R.string.updateapp_failure, ToastUtils.top);
//                    LogUtils.i(ConfigConstants.TAG_ALL, "DOWNLOADFAILED =-= ");
                    break;
                case KILLPROCESS:
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    LogUtils.i(ConfigConstants.TAG_ALL, "KILLPROCESS =-= ");
                    break;
            }
        }
    };


    /**
     * 准备去下载APK
     */
    public void readyDownloadFile(Activity activity, String contenttitile, String contenttext, String url, String filePath) {
//        this.mActivity = activity;
        this.filePath = filePath;
        if (NotificationHelperUtils.getInstance().isNotifacationEnabled(activity)) {//判断是否有通知栏权限
            mManager = NotificationHelperUtils.getInstance().getNotificationManager(activity);
            NotificationHelperUtils.getInstance().sendNotification(
                    activity, PersonalSettingsActivity.class,
                    NOTIFICATION_DOWNLOAD_ID, contenttitile, contenttext,
                    true);
            downloadFile(url, filePath);
        } else {
            NotificationHelperUtils.getInstance().openPermission(activity);
        }
    }


    /**
     * 下载apk
     */
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private OkHttpClient.Builder httpClientBuilder;


    private void downloadFile(String url, String filePath) {

//        String saveFileName = SDCardUtils.getExternalFilesDir(mActivity, "sources").getAbsolutePath() + File.separator + fileName;

        httpClientBuilder = new OkHttpClient.Builder();
        okHttpClient = lgnoreHttps();
        request = new Request.Builder().url(url).build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                PublicPracticalMethodFromJAVA.getInstance().runHandlerFun(mHandler, DOWNLOADFAILED, 10);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(filePath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        progressBar = (int) (sum * 1.0f / total * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (sum == total) {
                            mHandler.removeMessages(DOWNLOADING);
                        }
                        LogUtils.i(ConfigConstants.TAG_ALL, "sum =-= " + sum, "total =-= " + total);
                    }
                    fos.flush();
                    //下载完成通知安装
                    mHandler.sendEmptyMessage(DOWNLOADED);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(DOWNLOADFAILED);
                } finally {
                    is.close();
                    fos.close();
                }
            }
        });
    }

    //获取指定目录的访问权限
    public void startFor(Activity activity, String path, int REQUEST_CODE_FOR_DIR) {
        Uri parse = getUriForFile(activity, path);
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse);
        }
        activity.startActivityForResult(intent, REQUEST_CODE_FOR_DIR);//开始授权
    }


    // 打开文件位置
    public void openFilePosition(Activity activity, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setDataAndType(getUriForFile(activity, filePath), "file/*");
        activity.startActivity(intent);
    }


    /**
     * 将文件转换成uri
     *
     * @return
     */
    public Uri getUriForFile(Activity activity, String filePath) {
        File file = new File(filePath);
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(activity, AppUtils.getAppPackageName(activity) + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    public void onDestroy() {
        //handler销毁，防止内存泄漏
        if (!StringUtils.isBlank(mHandler)) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 忽略https验证
     */

    public OkHttpClient lgnoreHttps() {
        okHttpClient = httpClientBuilder.build();
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        String workerClassName = "okhttp3.OkHttpClient";

        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(okHttpClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(okHttpClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return okHttpClient;
    }


}
