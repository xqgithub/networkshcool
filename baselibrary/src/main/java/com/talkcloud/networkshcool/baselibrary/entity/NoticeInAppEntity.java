package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

/**
 * Date:2021/9/16
 * Time:16:45
 * author:joker
 * 通知列表实体类
 */
public class NoticeInAppEntity {


    /**
     * total : 16
     * per_page : 50
     * current_page : 1
     * last_page : 1
     * data : [{"id":985,"title":"上课提醒","content":"Hi~您报名的111111还有1时59分59秒就开始啦！点击进入","create_time":1631607001,"extras":{"type":"course.go_to_class","room_id":5512},"type":3,"is_read":0},{"id":980,"title":"上课提醒","content":"Hi~您报名的111111还有2时4分58秒就开始啦！点击进入","create_time":1631606702,"extras":{"type":"course.go_to_class","room_id":5512},"type":3,"is_read":0},{"id":977,"title":"上课提醒","content":"Hi~您报名的老师备课，学生预习测试6还有1时59分59秒就开始啦！点击进入","create_time":1631606401,"extras":{"type":"course.go_to_class","room_id":"5947"},"type":3,"is_read":0},{"id":978,"title":"上课提醒","content":"老师您好，老师备课，学生预习测试6直播课还有1时59分59秒就开始啦！点击进入","create_time":1631606401,"extras":{"type":"course.go_to_class","room_id":"5947"},"type":3,"is_read":0},{"id":975,"title":"上课提醒","content":"老师您好，老师备课，学生预习测试6直播课还有2时4分58秒就开始啦！点击进入","create_time":1631606102,"extras":{"type":"course.go_to_class","room_id":"5947"},"type":3,"is_read":0},{"id":974,"title":"上课提醒","content":"Hi~您报名的老师备课，学生预习测试6还有2时4分59秒就开始啦！点击进入","create_time":1631606101,"extras":{"type":"course.go_to_class","room_id":"5947"},"type":3,"is_read":0},{"id":922,"title":"上课提醒","content":"Hi~您报名的老师备课，学生预习测试6还有1时59分59秒就开始啦！点击进入","create_time":1631592001,"extras":{"type":"course.go_to_class","room_id":"5946"},"type":3,"is_read":0},{"id":923,"title":"上课提醒","content":"老师您好，老师备课，学生预习测试6直播课还有1时59分59秒就开始啦！点击进入","create_time":1631592001,"extras":{"type":"course.go_to_class","room_id":"5946"},"type":3,"is_read":0},{"id":921,"title":"上课提醒","content":"老师您好，老师备课，学生预习测试6直播课还有2时4分58秒就开始啦！点击进入","create_time":1631591702,"extras":{"type":"course.go_to_class","room_id":"5946"},"type":3,"is_read":0},{"id":920,"title":"上课提醒","content":"Hi~您报名的老师备课，学生预习测试6还有2时4分59秒就开始啦！点击进入","create_time":1631591701,"extras":{"type":"course.go_to_class","room_id":"5946"},"type":3,"is_read":0},{"id":891,"title":"上课提醒","content":"老师您好，预习备课测试5直播课还有1时59分58秒就开始啦！点击进入","create_time":1631538002,"extras":{"type":"course.go_to_class","room_id":"5944"},"type":3,"is_read":0},{"id":890,"title":"上课提醒","content":"Hi~您报名的预习备课测试5还有1时59分59秒就开始啦！点击进入","create_time":1631538001,"extras":{"type":"course.go_to_class","room_id":"5944"},"type":3,"is_read":0},{"id":888,"title":"上课提醒","content":"Hi~您报名的预习备课测试5还有2时4分58秒就开始啦！点击进入","create_time":1631537702,"extras":{"type":"course.go_to_class","room_id":"5944"},"type":3,"is_read":0},{"id":889,"title":"上课提醒","content":"老师您好，预习备课测试5直播课还有2时4分58秒就开始啦！点击进入","create_time":1631537702,"extras":{"type":"course.go_to_class","room_id":"5944"},"type":3,"is_read":0},{"id":836,"title":"上课提醒","content":"Hi~您报名的学生预习，老师备课测试4还有2时4分58秒就开始啦！点击进入","create_time":1631517902,"extras":{"type":"course.go_to_class","room_id":"5937"},"type":3,"is_read":0},{"id":837,"title":"上课提醒","content":"老师您好，学生预习，老师备课测试4直播课还有2时4分58秒就开始啦！点击进入","create_time":1631517902,"extras":{"type":"course.go_to_class","room_id":"5937"},"type":3,"is_read":0}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 985
         * title : 上课提醒
         * content : Hi~您报名的111111还有1时59分59秒就开始啦！点击进入
         * create_time : 1631607001
         * extras : {"type":"course.go_to_class","room_id":5512}
         * type : 3
         * is_read : 0
         */

        private int id;
        private String title;
        private String content;
        private long create_time;
        private ExtrasBean extras;
        private int type;
        private int is_read;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public ExtrasBean getExtras() {
            return extras;
        }

        public void setExtras(ExtrasBean extras) {
            this.extras = extras;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIs_read() {
            return is_read;
        }

        public void setIs_read(int is_read) {
            this.is_read = is_read;
        }

        public static class ExtrasBean {
            /**
             * type : course.go_to_class
             * room_id : 5512
             */

            private String type;
            private int room_id;
            private int homework_id;
            private int students_id;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getRoom_id() {
                return room_id;
            }

            public void setRoom_id(int room_id) {
                this.room_id = room_id;
            }

            public int getHomework_id() {
                return homework_id;
            }

            public void setHomework_id(int homework_id) {
                this.homework_id = homework_id;
            }

            public int getStudents_id() {
                return students_id;
            }

            public void setStudents_id(int students_id) {
                this.students_id = students_id;
            }
        }
    }
}
