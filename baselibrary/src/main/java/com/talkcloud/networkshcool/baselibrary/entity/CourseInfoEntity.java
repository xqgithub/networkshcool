package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class CourseInfoEntity {
    String id;   //课程id
    String name;//课程名称
    String type;//课程类型id
    String period;//课程周期
    List<TeacherOrAssitorEntity> teachers; //老师
    String schedule;//课程进度
    String type_name;//课程类型id
    String state;//课程状态  1未开始 2进行中 3已结束

    public NextLesson getNext_lesson() {
        return next_lesson;
    }

    public void setNext_lesson(NextLesson next_lesson) {
        this.next_lesson = next_lesson;
    }

    NextLesson next_lesson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
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

    public class NextLesson{
        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        String serial;   //课节id
        long starttime;//开课时间
    }

    @Override
    public String toString() {
        return "CourseInfoEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", period='" + period + '\'' +
                ", teachers=" + teachers +
                ", schedule='" + schedule + '\'' +
                ", type_name='" + type_name + '\'' +
                ", state='" + state + '\'' +
                ", next_lesson=" + next_lesson +
                '}';
    }
}
