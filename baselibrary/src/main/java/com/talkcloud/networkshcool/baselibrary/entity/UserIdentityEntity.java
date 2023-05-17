package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

/**
 * Date:2021/5/14
 * Time:14:01
 * author:joker
 * 用户身份 实体类
 */
public class UserIdentityEntity {


    /**
     * identitys : [7,8]
     * current_identity : 7
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjoie1widXNlcmlkXCI6MTAwMjA2NSxcImxvY2FsZVwiOlwiQ05cIixcImFjY291bnRcIjpcIjg2MTc2MDAxNTIwMTZcIixcInVzZXJuYW1lXCI6XCJcXHU2ZDJhXFx1NTM2YlwiLFwiYXZhdGFyXCI6XCJcIixcImN1cnJlbnRfaWRlbnRpdHlcIjpcIjdcIn0iLCJhdWQiOiIiLCJleHAiOjI0ODQ5NzE5ODUsImlhdCI6MTYyMDk3MTk4NSwiaXNzIjoiIiwianRpIjoiOGU4MWJjY2JkM2YyN2QxODQwZGU5YzMzZGE2M2JjMDYiLCJuYmYiOjE2MjA5NzE5ODUsInN1YiI6IiJ9.2B-_9eVRAQ19PicgmDlARd5uaNIJYyGAdnUzl4sVn6g
     */

    private String current_identity;
    private String token;
    private List<Integer> identitys;

    public String getCurrent_identity() {
        return current_identity;
    }

    public void setCurrent_identity(String current_identity) {
        this.current_identity = current_identity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getIdentitys() {
        return identitys;
    }

    public void setIdentitys(List<Integer> identitys) {
        this.identitys = identitys;
    }
}
