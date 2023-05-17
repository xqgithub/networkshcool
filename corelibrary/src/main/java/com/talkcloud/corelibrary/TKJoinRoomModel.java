package com.talkcloud.corelibrary;

public class TKJoinRoomModel {

    // 课节
    private String serialId;

    // 教室号
    private String roomId;
//    private String serial;
    // 密码
    private String pwd;
    // 昵称
    private String nickName;

    //角色 学生2，老师0
    private int userRole;

    // 用户id，可选(相同id 会互踢慎用)
    private String userId;

    private String state = "0";


    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

//    public String getSerial() {
//        return serial;
//    }
//
//    public void setSerial(String serial) {
//        this.serial = serial;
//    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "TKJoinRoomModel{" +
                "roomId='" + roomId + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userRole=" + userRole +
                ", userId='" + userId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
