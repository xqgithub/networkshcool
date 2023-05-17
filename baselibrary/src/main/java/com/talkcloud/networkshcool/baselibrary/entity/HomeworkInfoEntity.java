package com.talkcloud.networkshcool.baselibrary.entity;

public class HomeworkInfoEntity {
    String homework_id;   //作业id
    String student_id;//学生id
    String day;//作业日期
    String title;//作业标题
    String status;//作业状态  1未提交 2已提交 3已批阅
    String serial;//课节id

    /**
     * 学生需要的字段
     */
    int files; //附件数量
    int submits;  //提交数量
    String submit_time;  //提交时间
    String issue_time;  //老师发布作业的时间

    /**
     * 老师需要的字段
     */
    int unsubmits; //未提交数
    //int submits;  //提交数
    int remarks;  //点评数
    int unremarks; //未点评数
    long create_time;//时间戳

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }



    public int getUnremarks() {
        return unremarks;
    }

    public void setUnremarks(int unremarks) {
        this.unremarks = unremarks;
    }


    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

/*    public int getSubmits() {
        return submits;
    }

    public void setSubmits(int submits) {
        this.submits = submits;
    }*/

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }

    public int getUnsubmits() {
        return unsubmits;
    }

    public void setUnsubmits(int unsubmits) {
        this.unsubmits = unsubmits;
    }

    public int getSubmits() {
        return submits;
    }

    public void setSubmits(int submits) {
        this.submits = submits;
    }

    public int getRemarks() {
        return remarks;
    }

    public void setRemarks(int remarks) {
        this.remarks = remarks;
    }


    public String getHomework_id() {
        return homework_id;
    }

    public void setHomework_id(String homework_id) {
        this.homework_id = homework_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }


    public HomeworkInfoEntity() {}

    public HomeworkInfoEntity(String homework_id) {
        this.homework_id = homework_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof HomeworkInfoEntity)) {
            return false;
        }

        HomeworkInfoEntity entity = (HomeworkInfoEntity) o;

        return entity.homework_id.equals(this.homework_id);

    }

}
