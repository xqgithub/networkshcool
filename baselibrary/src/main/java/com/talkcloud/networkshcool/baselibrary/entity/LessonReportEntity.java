package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class LessonReportEntity {
    String roomname;   //课节名
    long starttime;//课节开始时间戳
    long endtime;//课节结束时间戳
    String companyid; //企业id
    UserInfo userinfo;
    List<Remark> remark;  //老师评价
    Performance performance;  //课堂表现


    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }


    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<Remark> getRemark() {
        return remark;
    }

    public void setRemark(List<Remark> remark) {
        this.remark = remark;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }


    public class UserInfo {

        String nickname;   //学生姓名
        String avatar;   //学生头像
        int cups;   //获得奖杯数
        String surpass;   //超越同学

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getCups() {
            return cups;
        }

        public void setCups(int cups) {
            this.cups = cups;
        }

        public String getSurpass() {
            return surpass;
        }

        public void setSurpass(String surpass) {
            this.surpass = surpass;
        }


    }

    public class Remark {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        String name;   //点评项名称
        int score;   //点评项分数
    }

    public class Performance {


        int time;   //实际上课时长（秒）

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getTotal_time() {
            return total_time;
        }

        public void setTotal_time(int total_time) {
            this.total_time = total_time;
        }

        public List<Room> getRooms() {
            return rooms;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

        public List<TimeLine> getTime_line() {
            return time_line;
        }

        public void setTime_line(List<TimeLine> time_line) {
            this.time_line = time_line;
        }

        int total_time;   //课节总时长(秒)
        List<Room> rooms;   //点评项名称
        List<TimeLine> time_line;   //点评项分数
    }

    public class Room {


        String avg_cups;   //班级活跃度均值(奖杯数)

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;   //课节名
        String my_cups;   //我的活跃度(奖杯数)
    }

    public class TimeLine {


        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        long starttime;   //
        long endtime;   //
    }
}

