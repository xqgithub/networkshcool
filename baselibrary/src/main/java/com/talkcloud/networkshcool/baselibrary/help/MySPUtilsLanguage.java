package com.talkcloud.networkshcool.baselibrary.help;


import android.content.Context;

import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.utils.SPUtils;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Date:2021/5/24
 * Time:14:00
 * author:joker
 * language.xml SharedPreferences 工具类
 */
public class MySPUtilsLanguage {

    private volatile static MySPUtilsLanguage mysputilslanguage;

    private SPUtils sputils_language;

    public static MySPUtilsLanguage getInstance() {
        if (mysputilslanguage == null) {//第一次检查，避免不必要的同步
            synchronized (MySPUtilsLanguage.class) {//synchronized对MySPUtilsUser加全局锁，保证每次只要一个线程创建实例
                if (mysputilslanguage == null) {//第二次检查，为null时才创建实例
                    mysputilslanguage = new MySPUtilsLanguage();
                }
            }
        }
        return mysputilslanguage;
    }

    public MySPUtilsLanguage() {
        if (!StringUtils.isBlank(TKBaseApplication.myApplication)) {
            sputils_language = new SPUtils(SPConstants.LANGUAGE_NAME);
        }
    }

    public void mySPUtilsLanguageInit(Context context) {
        sputils_language = new SPUtils(SPConstants.LANGUAGE_NAME, context);
    }


    /**
     * 保存用户当前的LANGUAGE
     *
     * @param localelanguage
     */
    public void saveLocaleLanguage(String localelanguage) {
        sputils_language.put(SPConstants.LOCALE_LANGUAGE, localelanguage);
    }

    /**
     * 获取用户当前的LANGUAGE
     *
     * @return
     */
    public String getLocaleLanguage() {
        return sputils_language.getString(SPConstants.LOCALE_LANGUAGE, "");
    }

    /**
     * 保存用户当前的国家信息
     */
    public void saveLocaleCountry(String localecountry) {
        sputils_language.put(SPConstants.LOCALE_COUNTRY, localecountry);
    }

    /**
     * 获取用户当前的国家信息
     *
     * @return
     */
    public String getLocaleCountry() {
        return sputils_language.getString(SPConstants.LOCALE_COUNTRY, "");
    }
}
