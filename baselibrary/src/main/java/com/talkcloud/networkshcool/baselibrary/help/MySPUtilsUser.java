package com.talkcloud.networkshcool.baselibrary.help;


import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.utils.SPUtils;

/**
 * Date:2021/5/24
 * Time:14:00
 * author:joker
 * user.xml SharedPreferences 工具类
 */
public class MySPUtilsUser {

    private volatile static MySPUtilsUser mysputilsuser;

    private SPUtils sputils_user;

    public static MySPUtilsUser getInstance() {
        if (mysputilsuser == null) {//第一次检查，避免不必要的同步
            synchronized (MySPUtilsUser.class) {//synchronized对MySPUtilsUser加全局锁，保证每次只要一个线程创建实例
                if (mysputilsuser == null) {//第二次检查，为null时才创建实例
                    mysputilsuser = new MySPUtilsUser();
                }
            }
        }
        return mysputilsuser;
    }

    public MySPUtilsUser() {
        sputils_user = new SPUtils(SPConstants.USER_NAME);
    }

    /**
     * 保存用户的token
     *
     * @param token
     */
    public void saveUserToken(String token) {
        sputils_user.put(SPConstants.TOKEN, token);
    }

    /**
     * 获取用户的token
     *
     * @return
     */
    public String getUserToken() {
        return sputils_user.getString(SPConstants.TOKEN, "");
    }

    /**
     * 保存用户的手机号
     *
     * @param usermobile
     */
    public void saveUserMobile(String usermobile) {
        sputils_user.put(SPConstants.USER_MOBILE, usermobile);
    }

    /**
     * 获取用户的手机号
     *
     * @return
     */
    public String getUserMobile() {
        return sputils_user.getString(SPConstants.USER_MOBILE, "");
    }

    /**
     * 保存用户账号
     */
    public void saveUserAccount(String useraccount) {
        sputils_user.put(SPConstants.USER_ACCOUNT, useraccount);
    }

    /**
     * 获取用户账号
     *
     * @return
     */
    public String getUserAccount() {
        return sputils_user.getString(SPConstants.USER_ACCOUNT, "");
    }


    /**
     * 保存用户的登录方式
     */
    public void saveUserLoginMethod(int method) {
        sputils_user.put(SPConstants.USER_LOGINMETHOD, method);
    }

    /**
     * 获取用户的登录方式
     *
     * @return
     */
    public int getUserLoginMethod() {
        return sputils_user.getInt(SPConstants.USER_LOGINMETHOD, ConfigConstants.LOGINMETHOD_MOBILE);
    }


    /**
     * 保存用户的密码
     *
     * @param userpwd
     */
    public void saveUserPwd(String userpwd) {
        sputils_user.put(SPConstants.USER_PWD, userpwd);
    }

    /**
     * 获取用户的密码
     *
     * @return
     */
    public String getUserPwd() {
        return sputils_user.getString(SPConstants.USER_PWD, "");
    }


    /**
     * 保存用户的国家代号
     *
     * @param locale
     */
    public void saveUserLocale(String locale) {
        sputils_user.put(SPConstants.USER_LOCALE, locale);
    }

    /**
     * 获取用户的国家代号
     *
     * @return
     */
    public String getUserLocale() {
        return sputils_user.getString(SPConstants.USER_LOCALE, "");
    }

    /**
     * 保存用户的国家区号
     */
    public void saveUserLocaleCode(String localecode) {
        sputils_user.put(SPConstants.USER_LOCALECODE, localecode);
    }

    /**
     * 获取用户的国家区号
     *
     * @return
     */
    public String getUserLocaleCode() {
        return sputils_user.getString(SPConstants.USER_LOCALECODE, "");
    }

    /**
     * 保存用户的国家名称
     */
    public void saveUserLocaleName(String localename) {
        sputils_user.put(SPConstants.USER_LOCALENAME, localename);
    }

    /**
     * 获取用户的国家名称
     *
     * @return
     */
    public String getUserLocaleName() {
        return sputils_user.getString(SPConstants.USER_LOCALENAME, "");
    }

    /**
     * 保存用户当前登录的身份
     *
     * @param useridentity
     */
    public void saveUserIdentity(String useridentity) {
        sputils_user.put(SPConstants.USER_IDENTITY, useridentity);
    }

    /**
     * 获取用户当前的登录身份
     *
     * @return
     */
    public String getUserIdentity() {
        return sputils_user.getString(SPConstants.USER_IDENTITY, "");
    }

    /**
     * 保存用户验证身份第一次验证码
     *
     * @param verificationcodefirst
     */
    public void saveUserVerificationCodeFirst(String verificationcodefirst) {
        sputils_user.put(SPConstants.USER_VERIFICATIONCODE_FIRST, verificationcodefirst);
    }


    /**
     * 获取用户验证身份第一次验证码
     *
     * @return
     */
    public String getUserVerificationCodeFirst() {
        return sputils_user.getString(SPConstants.USER_VERIFICATIONCODE_FIRST, "");
    }


    /**
     * 保存用户护眼模式状态
     */
    public void saveUserEyeProtectionStatus(boolean isOpen) {
        sputils_user.put(SPConstants.USER_EYEPROTECTION, isOpen);
    }

    /**
     * 获取用户护眼模式的状态
     */
    public Boolean getUserEyeProtectionStatus() {
        return sputils_user.getBoolean(SPConstants.USER_EYEPROTECTION, false);
    }

    /**
     * 保存验证发送读秒期间手机号
     */
    public void saveMobileTemp(String mobiletemp) {
        sputils_user.put(SPConstants.MOBILE_TEMP, mobiletemp);
    }

    /**
     * 获取验证发送读秒期间手机号
     */
    public String getMobileTemp() {
        return sputils_user.getString(SPConstants.MOBILE_TEMP, "");
    }

    /**
     * 保存用户权限申请状态
     */
    public void saveAccessRequest(boolean isAgree) {
        sputils_user.put(SPConstants.IsRefuseAccessRequest, isAgree);
    }


    /**
     * 获取用户权限申请状态
     *
     * @return
     */
    public boolean getAccessRequest() {
        return sputils_user.getBoolean(SPConstants.IsRefuseAccessRequest, false);
    }


    /**
     * 保存用户升级框弹出时间
     */
    public void saveUserSysversionUpdateShowTime(int time) {
        sputils_user.put(SPConstants.USER_SYSVERSIONUPDATE_SHOWTIME, time);
    }

    /**
     * 获取用户升级框弹出时间
     *
     * @return
     */
    public int getUserSysversionUpdateShowTime() {
        return sputils_user.getInt(SPConstants.USER_SYSVERSIONUPDATE_SHOWTIME, 0);
    }


    /**
     * 保存用户是否同意 用户协议和隐私政策
     */
    public void saveUserPrivacyAgreementStatus(boolean isAgree) {
        sputils_user.put(SPConstants.USERPRIVACYAGREEMENT_STATUS, isAgree);
    }

    /**
     * 获取用户是否同意 用户协议和隐私政策
     *
     * @return
     */
    public Boolean getUserPrivacyAgreementStatus() {
        return sputils_user.getBoolean(SPConstants.USERPRIVACYAGREEMENT_STATUS, false);
    }


    /**
     * 保存用户的userid
     */
    public void saveUserID(String userid) {
        sputils_user.put(SPConstants.USER_ID, userid);
    }

    /**
     * 获取用户的userid
     */
    public String getUserID() {
        return sputils_user.getString(SPConstants.USER_ID, "");
    }

    /**
     * 保存用户的nickname
     */
    public void saveNickName(String nickname) {
        sputils_user.put(SPConstants.USER_NICKNAME, nickname);
    }

    /**
     * 获取用户的nickname
     */
    public String getNickName() {
        return sputils_user.getString(SPConstants.USER_NICKNAME, "");
    }
}
