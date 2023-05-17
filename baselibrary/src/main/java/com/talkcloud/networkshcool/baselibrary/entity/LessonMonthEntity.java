package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/5/20
 * Time:11:15
 * author:joker
 * 我的课表（月） 实体类
 */
public class LessonMonthEntity {

    /**
     * result : 0
     * data : [{"day":"2021-05-19","roomname":"课节11"},{"day":"2021-05-20","roomname":"00005"},{"day":"2021-05-22","roomname":"英语01"},{"day":"2021-05-23","roomname":"033d测"},{"day":"2021-05-24","roomname":"后为0002"}]
     * msg : success
     */


    private String day;
    private String roomname;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }


}
