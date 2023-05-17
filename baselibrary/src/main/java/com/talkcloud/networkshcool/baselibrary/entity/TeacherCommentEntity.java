package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/6/29
 * Time:14:54
 * author:joker
 * 老师提交评论实体类
 */
public class TeacherCommentEntity {

    //作业ID
    private String homeworkid;
    //评论内容
    private String commentcontent;
    //是否保存为快捷评语  0否1是
    private String useful_expressions;
    //评级 3优秀 2良好 1中等 0不合格
    private String rank;
    //学生id
    private String[] students;

    //评论语音集合
    private AudioEntinty[] audioentinty;

    //提交内容是 提交第一次评论 true  编辑点评 false
    private boolean isFirstRemark;


    public String getHomeworkid() {
        return homeworkid;
    }

    public void setHomeworkid(String homeworkid) {
        this.homeworkid = homeworkid;
    }

    public String getCommentcontent() {
        return commentcontent;
    }

    public void setCommentcontent(String commentcontent) {
        this.commentcontent = commentcontent;
    }

    public String getUseful_expressions() {
        return useful_expressions;
    }

    public void setUseful_expressions(String useful_expressions) {
        this.useful_expressions = useful_expressions;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String[] getStudents() {
        return students;
    }

    public void setStudents(String[] students) {
        this.students = students;
    }

    public AudioEntinty[] getAudioentinty() {
        return audioentinty;
    }

    public void setAudioentinty(AudioEntinty[] audioentinty) {
        this.audioentinty = audioentinty;
    }

    public boolean isFirstRemark() {
        return isFirstRemark;
    }

    public void setFirstRemark(boolean firstRemark) {
        isFirstRemark = firstRemark;
    }
}
