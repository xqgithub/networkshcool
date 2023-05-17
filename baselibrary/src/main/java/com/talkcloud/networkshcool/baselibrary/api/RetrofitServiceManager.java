package com.talkcloud.networkshcool.baselibrary.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.talkcloud.networkshcool.baselibrary.BuildConfig;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date:2021/5/10
 * Time:19:33
 * author:joker
 * Retrofit的接口管理类
 */
public class RetrofitServiceManager {

    private OkHttpClient.Builder httpClientBuilder;
    private OkHttpClient sClient;
    public static ApiService apiService;


    /**
     * httpClientBuilder 单例
     */
    public OkHttpClient.Builder gethttpClientBuilder() {
        if (httpClientBuilder == null) {
            synchronized (RetrofitServiceManager.class) {
                if (httpClientBuilder == null) {
                    httpClientBuilder = new OkHttpClient.Builder();
                }
            }
        }
        return httpClientBuilder;
    }

    public static RetrofitServiceManager getInstance() {
        return new RetrofitServiceManager();
    }

    public ApiService getApiService(VariableConfig.UrlPathHelp type, String version) {

        //1.初始化httpClientBuilder
        gethttpClientBuilder();
        httpClientBuilder.writeTimeout(ConfigConstants.OKHTTP_WRITE_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
        httpClientBuilder.connectTimeout(ConfigConstants.OKHTTP_CONNECT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        httpClientBuilder.readTimeout(ConfigConstants.OKHTTP_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        //2.添加url拦截
        httpClientBuilder.addInterceptor(new OkHttpInterceptor(version));

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


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

//        String url = "";
//        if (type == VariableConfig.UrlPathHelp.USER) {
//            url = VariableConfig.base_url_user;
//        } else if (type == VariableConfig.UrlPathHelp.TEACHERS_STUDENTS) {
//            url = VariableConfig.base_url_teachers_students;
//        }
        apiService = new Retrofit.Builder()
                .baseUrl(type.getUrl())
                .client(lgnoreHttps())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(ApiService.class);

        return apiService;
    }

    public class HeaderInterceptor implements Interceptor {

        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoie1widXNlcmlkXCI6MTAwMTA2MCxcImxvY2FsZVwiOlwiQ05cIixcImFjY291bnRcIjpcIjg2MTg1MDcxMDMwMTZcIixcInVzZXJuYW1lXCI6XCJcXHU2NzMxXFx1NWUwOFxcdTVmYjdcIixcImF2YXRhclwiOlwiaHR0cHM6XFxcL1xcXC9od2RlbW9jZG4udGFsay1jbG91ZC5uZXRcXFwvdXBsb2FkXFxcLzIwMjEwNDIzXFxcLzUzMGM4MDAwMjcwZDcyMmYzYTI4YTQxMzljMjA4NTI3LnBuZ1wiLFwiY3VycmVudF9pZGVudGl0eVwiOjh9IiwiYXVkIjoiIiwiZXhwIjoyNDg1MjM1OTY1LCJpYXQiOjE2MjEyMzU5NjUsImlzcyI6IiIsImp0aSI6ImM3NmFjMGU0OGEyNTg2MGZkNDk1ODY5NjVkYzI1ZTUxIiwibmJmIjoxNjIxMjM1OTY1LCJzdWIiOiIifQ.apYgXUDtm7Weu0vHe1TTG9uexGKu5ded_e16swSmk-4";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Content_Type", "application/json")
                    .addHeader("version", "v1")
                    .addHeader("Authorization", token)
                    .build();

            return chain.proceed(request);
        }
    }


    /**
     * OkHttp拦截器 改变 请求的url
     */
    public class OkHttpInterceptor implements Interceptor {
        String version = "v1";  //默认v1

        public OkHttpInterceptor(String version) {
            this.version = version;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            //获取request
            Request request = chain.request();
//            LogUtils.i(ConfigConstants.TAG_ALL, "host_Url =-= " + _hostUrl, "method_Url =-= " + _methodUrl);
            return chain.proceed(changeHttpUrl(request, version));
        }
    }

    /**
     * 拦截器改变url
     *
     * @return
     */
    private Request changeHttpUrl(Request request, String version) {
        Request requestnew = null;
        //1.从request中获取原有的HttpUrl实例oldHttpUrl
        HttpUrl oldHttpUrl = request.url().newBuilder()
                .addQueryParameter("timezone", PublicPracticalMethodFromJAVA.getInstance().getCurrentTimeZone())
                .addQueryParameter("lang", PublicPracticalMethodFromJAVA.getInstance().getCurrentLanguage().toLowerCase())
                .build();
        //2.获取request的创建者builder
        Request.Builder builder = request.newBuilder();
        //3.获取旧地址中的参数值
        String query = oldHttpUrl.query();


        //4.新请求地址组装
        HttpUrl newBaseUrl = oldHttpUrl;

        //获取token值，接口规定token可以一直传
        String token = MySPUtilsUser.getInstance().getUserToken();
//        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoie1widXNlcmlkXCI6MTAwMTA2MCxcImxvY2FsZVwiOlwiQ05cIixcImFjY291bnRcIjpcIjg2MTg1MDcxMDMwMTZcIixcInVzZXJuYW1lXCI6XCJcXHU2NzMxXFx1NWUwOFxcdTVmYjdcIixcImF2YXRhclwiOlwiaHR0cHM6XFxcL1xcXC9od2RlbW9jZG4udGFsay1jbG91ZC5uZXRcXFwvdXBsb2FkXFxcLzIwMjEwNDIzXFxcLzUzMGM4MDAwMjcwZDcyMmYzYTI4YTQxMzljMjA4NTI3LnBuZ1wiLFwiY3VycmVudF9pZGVudGl0eVwiOjh9IiwiYXVkIjoiIiwiZXhwIjoyNDg1MjM1OTY1LCJpYXQiOjE2MjEyMzU5NjUsImlzcyI6IiIsImp0aSI6ImM3NmFjMGU0OGEyNTg2MGZkNDk1ODY5NjVkYzI1ZTUxIiwibmJmIjoxNjIxMjM1OTY1LCJzdWIiOiIifQ.apYgXUDtm7Weu0vHe1TTG9uexGKu5ded_e16swSmk-4";

        //添加header头文件
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("charset", "UTF-8");
        builder.addHeader("Authorization", PublicPracticalMethodFromJAVA.getInstance().getToken(token));
//        builder.addHeader("Authorization", token);
        builder.addHeader("version", version);
        // 给服务端区分
        builder.addHeader("terminal-type", "1");

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
