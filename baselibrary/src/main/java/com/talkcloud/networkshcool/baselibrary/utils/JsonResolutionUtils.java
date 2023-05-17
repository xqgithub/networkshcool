package com.talkcloud.networkshcool.baselibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by beijixiong on 2018/10/21.
 * json数据解析
 */

public class JsonResolutionUtils {

    /**
     * 从本地获取json数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 将字符串转换为 对象
     *
     * @param json
     * @return
     */
    public static <T> T JsonToObject(String json, Class<T> t) {
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }

    /**
     * 将对象转换成加密json字符串
     */
//    public static <T> void objectToJsonCache(Object src, Class<T> cls, SPUtils spUtils, String spconstantsname) {
//        try {
//            String jsonStu = new Gson().toJson(src, cls);
//            //加密处理
//            String jsonstuencrypt = AESCrypt.encrypt(StaticStateUtils.key, jsonStu);
//            spUtils.put(spconstantsname, jsonstuencrypt);
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
//    }

    /**
     * 将加密json字符串转换成对象
     */
//    public static <T> T jsonCacheToObject(Class<T> cls, SPUtils spUtils, String spconstantsname) {
//        try {
//            //解密数据
//            String jsondecrypt = spUtils.getString(spconstantsname, "");
//            String json = AESCrypt.decrypt(StaticStateUtils.key, jsondecrypt);
//            return JsonToObject(json, cls);
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
//        return null;
//    }

}
