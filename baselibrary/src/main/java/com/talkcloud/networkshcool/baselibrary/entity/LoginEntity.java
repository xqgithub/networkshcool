package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/5/12
 * Time:14:34
 * author:joker
 * 登录实体类
 */
public class LoginEntity {

    String token;
    String user_account_id;//用户id ,用于绑定极光别名

    public String getUser_account_id() {
        return user_account_id;
    }

    public void setUser_account_id(String user_account_id) {
        this.user_account_id = user_account_id;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
