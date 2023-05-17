package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class CourseDetailInfoEntity {
    String name;   //课程名称
    String type_name;//课程类型

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<TeacherOrAssitorEntity> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherOrAssitorEntity> teachers) {
        this.teachers = teachers;
    }

    public List<LessonInfoEntity> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonInfoEntity> lessons) {
        this.lessons = lessons;
    }

    public List<ResourseEntity> getResources() {
        return resources;
    }

    public void setResources(List<ResourseEntity> resources) {
        this.resources = resources;
    }

    String state;//状态  1未开始 2进行中 3已结束
    String period;//课程周期
    List<TeacherOrAssitorEntity> teachers; //老师
    List<LessonInfoEntity> lessons; //课节列表
    List<ResourseEntity> resources; //资料包


}
