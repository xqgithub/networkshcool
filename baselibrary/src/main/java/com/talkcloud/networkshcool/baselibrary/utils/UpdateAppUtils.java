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

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
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
public class UpdateAppUtils {

    private volatile static UpdateAppUtils appUtils;

    private Activity mActivity;
    private String updateUrl;
    private String latestVersion;
    private String apkName;
    //apk保存路径
    private String saveFileName = "";
    private int progressBar = 0;


    private NotificationManager mManager;
    private static final int NOTIFICATION_DOWNLOAD_ID = 5;

    public static UpdateAppUtils getInstance() {
        if (appUtils == null) {
            synchronized (UpdateAppUtils.class) {
                if (appUtils == null) {
                    appUtils = new UpdateAppUtils();
                }
            }
        }
        return appUtils;
    }

    /**
     * 初始化
     */
    public void initUtils(Activity activity, SysVersionEntity sysVersionEntity) {
        this.mActivity = activity;

        updateUrl = sysVersionEntity.getUpdateaddr();
//        updateUrl = "http://192.168.3.171:18080/examples/app101.apk";
        latestVersion = sysVersionEntity.getVersionnum();
        apkName = "networkshcool" + latestVersion + ".apk";
        saveFileName = SDCardUtils.getExternalFilesDir(mActivity, ConfigConstants.APK_DIR).getAbsolutePath() + File.separator + apkName;
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
//                    LogUtils.i(ConfigConstants.TAG_ALL, "DOWNLOADING =-= " + progressBar);
                    break;
                case DOWNLOADED:
                    mManager.cancel(NOTIFICATION_DOWNLOAD_ID);
                    installAPK();
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
    public void readyDownloadApk(String contenttitile, String contenttext) {
        if (NotificationHelperUtils.getInstance().isNotifacationEnabled(mActivity)) {//判断是否有通知栏权限
            mManager = NotificationHelperUtils.getInstance().getNotificationManager(mActivity);
            NotificationHelperUtils.getInstance().sendNotification(
                    mActivity, PersonalSettingsActivity.class,
                    NOTIFICATION_DOWNLOAD_ID, contenttitile, contenttext,
                    true);
            downloadAPK();
        } else {
            NotificationHelperUtils.getInstance().openPermission(mActivity);
        }
    }


    /**
     * 下载apk
     */
    private OkHttpClient okHttpClient;
    private Request request;
    private Call call;
    private OkHttpClient.Builder httpClientBuilder;


    private void downloadAPK() {
        httpClientBuilder = new OkHttpClient.Builder();
        okHttpClient = lgnoreHttps();//okHttpClient = new OkHttpClient();
        request = new Request.Builder().url(updateUrl).build();
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
                    File file = new File(saveFileName);
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

    /**
     * 下载完成后自动安装apk
     */
    private void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            mHandler.sendEmptyMessage(DOWNLOADFAILED);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);//添加flags，表明我们要被授予什么样的临时权限
            Uri uri = FileProvider.getUriForFile(mActivity, AppUtils.getAppPackageName(mActivity) + ".fileprovider", apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mActivity.startActivityForResult(intent, 0);

            Message message = Message.obtain();
            message.what = KILLPROCESS;
            mHandler.sendMessageDelayed(message, 500);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
            mActivity.startActivity(intent);

            Message message = Message.obtain();
            message.what = KILLPROCESS;
            mHandler.sendMessageDelayed(message, 500);
        }

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri contentUri = getUriForFile(apkFile);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        }
//
//        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            mActivity.startActivityForResult(intent, 0);
//        } else {
//            mActivity.startActivity(intent);
//        }
//
//        Message message = Message.obtain();
//        message.what = KILLPROCESS;
//        mHandler.sendMessageDelayed(message, 500);

    }

    /**
     * 将文件转换成uri
     *
     * @return
     */
    public Uri getUriForFile(File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mActivity, AppUtils.getAppPackageName(mActivity) + ".fileprovider", file);
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
