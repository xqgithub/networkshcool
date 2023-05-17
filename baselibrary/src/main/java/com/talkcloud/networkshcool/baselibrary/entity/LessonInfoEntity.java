package com.talkcloud.networkshcool.baselibrary.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LessonInfoEntity {
    String serial;   //课节id
    String type_name;//课节类型
    String roomname;//课节名称
    String roomtype;//课节类型  0一对一 3一对多 4大直播 6微录课
    String student_num;//学生数量
    List<TeacherOrAssitorEntity> teachers; //老师
    int starttime;//开课时间戳
    int endtime;//结束时间戳
    int current_time;//服务端当前时间戳
    String start_date;//开课日期
    String week;//开课周
    String start_to_end_time;//课节时间范围
    String times;//时长
    String state;//状态  1未开始 2进行中 3已结束
    String enter_room_url;//进入教室地址
    int before_enter;//提前多少秒可进入教室
    String company_id;//企业id
    boolean is_join;

    // 教室密码
    private String pwd;

    // 回放地址
    List<String> record_url;

    // 作业id
    List<Homework> homeworks;

    // 用户信息 进入sdk教室用
    User user;


    //学生预习课件
    private PrepareLessonsBean prepare_lessons;
    //老师备课
    private PreviewLessonsBean preview_lessons;
    //学生列表信息
    private List<StudentsBean> students;


    // 回放 包括常规和mp4
//    List<RecordEntity> record;


//    public List<RecordEntity> getRecord() {
//        return record;
//    }
//
//    public void setRecord(List<RecordEntity> record) {
//        this.record = record;
//    }


    public List<Homework> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<Homework> homeworks) {
        this.homeworks = homeworks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getRecord_url() {
        return record_url;
    }

    public void setRecord_url(List<String> record_url) {
        this.record_url = record_url;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isIs_join() {
        return is_join;
    }

    public void setIs_join(boolean is_join) {
        this.is_join = is_join;
    }

    public int getEndtime() {
        return endtime;
    }

    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }

    public int getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(int current_time) {
        this.current_time = current_time;
    }


    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getRoomname() {
        return roomname;
    }

    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public String getStudent_num() {
        return student_num;
    }

    public void setStudent_num(String student_num) {
        this.student_num = student_num;
    }

    public int getStarttime() {
        return starttime;
    }

    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getStart_to_end_time() {
        return start_to_end_time;
    }

    public void setStart_to_end_time(String start_to_end_time) {
        this.start_to_end_time = start_to_end_time;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnter_room_url() {
        return enter_room_url;
    }

    public void setEnter_room_url(String enter_room_url) {
        this.enter_room_url = enter_room_url;
    }

    public int getBefore_enter() {
        return before_enter;
    }

    public void setBefore_enter(int before_enter) {
        this.before_enter = before_enter;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }


    public List<TeacherOrAssitorEntity> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherOrAssitorEntity> teachers) {
        this.teachers = teachers;
    }

    public PrepareLessonsBean getPrepare_lessons() {
        return prepare_lessons;
    }

    public void setPrepare_lessons(PrepareLessonsBean prepare_lessons) {
        this.prepare_lessons = prepare_lessons;
    }

    public PreviewLessonsBean getPreview_lessons() {
        return preview_lessons;
    }

    public void setPreview_lessons(PreviewLessonsBean preview_lessons) {
        this.preview_lessons = preview_lessons;
    }


    public List<StudentsBean> getStudents() {
        return students;
    }

    public void setStudents(List<StudentsBean> students) {
        this.students = students;
    }

    public static class User {

        private String nickname;

        private String userid;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        @Override
        public String toString() {
            return "User{" +
                    "nickname='" + nickname + '\'' +
                    ", userid='" + userid + '\'' +
                    '}';
        }
    }

    public static class Homework {

        String id;

        String is_draft;

        // 学生才有
        String student_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_draft() {
            return is_draft;
        }

        public void setIs_draft(String is_draft) {
            this.is_draft = is_draft;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

    }

    public LessonInfoEntity() {
    }

    public LessonInfoEntity(String serial) {
        this.serial = serial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof LessonInfoEntity)) {
            return false;
        }

        LessonInfoEntity entity = (LessonInfoEntity) o;

        if (entity.serial == null) {
            return false;
        }

        return entity.serial.equals(this.serial);

    }

    public static class PrepareLessonsBean {
        /**
         * switch : 0
         * times : 0
         */

        @SerializedName("switch")
        private String switchX;
        private int times;

        public String getSwitchX() {
            return switchX;
        }

        public void setSwitchX(String switchX) {
            this.switchX = switchX;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }
    }

    public static class PreviewLessonsBean {
        /**
         * switch : 0
         */

        @SerializedName("switch")
        private String switchX;

        public String getSwitchX() {
            return switchX;
        }

        public void setSwitchX(String switchX) {
            this.switchX = switchX;
        }
    }

    public static class StudentsBean {
        /**
         * name : wsf学生1
         * avatar : https://demoapi.51menke.com/image/man_student.png
         * age : 0
         */

        private String name;
        private String avatar;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


    @Override
    public String toString() {
        return "LessonInfoEntity{" +
                "serial='" + serial + '\'' +
                ", type_name='" + type_name + '\'' +
                ", roomname='" + roomname + '\'' +
                ", roomtype='" + roomtype + '\'' +
                ", state='" + state + '\'' +
                ", enter_room_url='" + enter_room_url + '\'' +
                ", is_join=" + is_join +
                ", pwd='" + pwd + '\'' +
                ", record_url=" + record_url +
                ", homeworks=" + homeworks +
                ", user=" + user +
                '}';
    }
}
