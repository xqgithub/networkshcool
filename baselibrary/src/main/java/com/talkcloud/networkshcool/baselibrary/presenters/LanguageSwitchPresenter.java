package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.content.Intent;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.ui.activities.StartUpActivity;
import com.talkcloud.networkshcool.baselibrary.utils.MultiLanguageUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.LanguageSwitchView;

import java.util.Locale;
import java.util.Map;

/**
 * Date:2021/5/18
 * Time:16:58
 * author:joker
 * 语言切换页面 Presenter
 */
public class LanguageSwitchPresenter {

    private Activity mActivity;
    private LanguageSwitchView languageSwitchView;


    public LanguageSwitchPresenter(Activity activity, LanguageSwitchView languageSwitchView) {
        this.mActivity = activity;
        this.languageSwitchView = languageSwitchView;
    }

    /**
     * 修改应用内语言设置
     */
    public void changeLanguage(String name, String language, String area) {
        Locale newLocale;
        if (StringUtils.isBlank(language) && StringUtils.isBlank(area)) {
            //如果语言和地区都是空，那么跟随系统
//            newLocale = new Locale(language, area);
//            MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, false);
//            MultiLanguageUtil.changeAppLanguage(MyApplication.myApplication.getApplicationContext(), newLocale, false);

            Map<String, String> map = PublicPracticalMethodFromJAVA.getInstance().getCurrentLanguageAndCountry();
            String mLanguage = map.get(SPConstants.LOCALE_LANGUAGE);
            String mArea = map.get(SPConstants.LOCALE_COUNTRY);
            newLocale = new Locale(mLanguage, mArea);
            MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, false);
//            MultiLanguageUtil.changeAppLanguage(MyApplication.myApplication.getApplicationContext(), newLocale, false);


            MySPUtilsLanguage.getInstance().saveLocaleLanguage("");
            MySPUtilsLanguage.getInstance().saveLocaleCountry("");
        } else {
            newLocale = new Locale(language, area);
            MultiLanguageUtil.changeAppLanguage(mActivity, newLocale, true);
//            MultiLanguageUtil.changeAppLanguage(MyApplication.myApplication.getApplicationContext(), newLocale, true);
        }
        //重启app,这一步一定要加上，如果不重启app，可能打开新的页面显示的语言会不正确
//        Intent intent = new Intent(mActivity, MainMenuActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        mActivity.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
        PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, StartUpActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true,
                R.anim.activity_xhold, R.anim.activity_xhold);

    }


}
