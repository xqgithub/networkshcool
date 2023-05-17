package com.talkcloud.networkshcool;


import android.text.TextUtils;

import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;

/**
 * Author  guoyw
 * Date    2021/7/29
 * Desc
 */
public class MyApplication extends TKBaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        TKExtManage extManage = TKExtManage.getInstance();
        // 设置第三方
        extManage.setCompanyId(BuildConfig.company_id);
        extManage.setCompanyDomain(BuildConfig.company_domain);

//        extManage.setShowHomeExtBtn(BuildConfig.is_show_home_ext_btn);
        // 没有地址, 就没有按钮
        extManage.setShowHomeExtBtn(!TextUtils.isEmpty(BuildConfig.home_ext_url));
        extManage.setHomeExtUrl(BuildConfig.home_ext_url);
        extManage.setUserAgreementUrl(BuildConfig.user_agreement_ext_url);
    }
}
