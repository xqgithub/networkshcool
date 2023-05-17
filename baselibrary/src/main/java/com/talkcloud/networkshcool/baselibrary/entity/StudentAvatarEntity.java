package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/6/21
 * Time:9:51
 * author:joker
 * 作业点评 学生 头像信息实体类
 */
public class StudentAvatarEntity {

    private String studentid;
    private String studentname;
    private String avatarurl;
    private boolean isselected;
    private boolean isfinished;
    private String workfinishedtime;
    private String homeworkid;
    private String serial;


    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    public boolean isIsfinished() {
        return isfinished;
    }

    public void setIsfinished(boolean isfinished) {
        this.isfinished = isfinished;
    }

    public String getWorkfinishedtime() {
        return workfinishedtime;
    }

    public void setWorkfinishedtime(String workfinishedtime) {
        this.workfinishedtime = workfinishedtime;
    }

    public String getHomeworkid() {
        return homeworkid;
    }

    public void setHomeworkid(String homeworkid) {
        this.homeworkid = homeworkid;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
