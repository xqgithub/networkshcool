package com.talkcloud.networkshcool.baselibrary.api;

import com.talkcloud.networkshcool.baselibrary.BuildConfig;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Date:2021/5/10
 * Time:19:33
 * author:joker
 * Retrofit的接口管理类
 * PS：请求系统更新接口 单独使用 因为返回的值类型结构跟之前的API类型结构不一样无法使用框架
 */
public class CallManager {

    private OkHttpClient.Builder httpClientBuilder;
    private OkHttpClient sClient;


    /**
     * httpClientBuilder 单例
     */
    public OkHttpClient.Builder gethttpClientBuilder() {
        if (httpClientBuilder == null) {
            synchronized (CallManager.class) {
                if (httpClientBuilder == null) {
                    httpClientBuilder = new OkHttpClient.Builder();
                }
            }
        }
        return httpClientBuilder;
    }

    public static CallManager getInstance(String host_Url) {
        return new CallManager(host_Url);
    }

    private CallManager(String host_Url) {
        //1.初始化httpClientBuilder
        gethttpClientBuilder();
        httpClientBuilder.writeTimeout(ConfigConstants.OKHTTP_WRITE_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        httpClientBuilder.connectTimeout(ConfigConstants.OKHTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        httpClientBuilder.readTimeout(ConfigConstants.OKHTTP_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        //2.添加url拦截
        httpClientBuilder.addInterceptor(new OkHttpInterceptor(host_Url));

        //3.添加http调试拦截器
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            //这里可以选择拦截级别
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //设置 Debug Log 模式
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        //4.设置okhttp的连接池保活时间为3秒中
        httpClientBuilder.connectionPool(new ConnectionPool(3, 1, TimeUnit.SECONDS));
    }

    public Call getPostRequest(Map<String, String> map_bodys) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String kk : map_bodys.keySet()) {
            builder.add(kk, map_bodys.get(kk));
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(VariableConfig.base_url_teachers_students)
                .post(body)
                .build();

        Call call = lgnoreHttps().newCall(request);
        return call;
    }

    /**
     * OkHttp拦截器 改变 请求的url
     */
    public class OkHttpInterceptor implements Interceptor {
        String _hostUrl = "";

        public OkHttpInterceptor(String host_Url) {
            this._hostUrl = host_Url;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取request
            Request request = chain.request();
            return chain.proceed(changeHttpUrl(request, _hostUrl));
        }
    }

    /**
     * 拦截器改变url
     *
     * @return
     */
    private Request changeHttpUrl(Request request, String host_Url) {
        Request requestnew = null;
        //1.从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url().newBuilder()
                .build();
        //2.获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //3.获取旧地址中的参数值
        String query = oldHttpUrl.query();


        //4.新请求地址组装
        HttpUrl newBaseUrl = HttpUrl.parse(host_Url);

        //添加header头文件
//        builder.addHeader("Content-Type", "application/json");
//        builder.addHeader("charset", "UTF-8");

        //6.生成新的Request
        requestnew = builder.url(newBaseUrl)
                .build();
        return requestnew;
    }


    /**
     * 忽略https验证
     */
    public OkHttpClient lgnoreHttps() {
        if (sClient == null) {
            sClient = httpClientBuilder.build();
        }
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
            hostnameVerifier.set(sClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(sClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sClient;
    }


}
