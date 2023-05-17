package com.talkcloud.networkshcool.baselibrary.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeworkDetailInfoEntity implements Parcelable {
    String id;   //学生id
    String name;//学生姓名
    String avatar;//学生头像
    long submit_time;//提交时间戳
    String status;//作业状态   0未读1已读2已提交3已点评
    String rank;//评级
    private int unreminds;//剩余提醒次数
    private boolean isreminded = false;//是否已经提醒

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(long submit_time) {
        this.submit_time = submit_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getUnreminds() {
        return unreminds;
    }

    public void setUnreminds(int unreminds) {
        this.unreminds = unreminds;
    }

    public boolean isIsreminded() {
        return isreminded;
    }

    public void setIsreminded(boolean isreminded) {
        this.isreminded = isreminded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeLong(this.submit_time);
        dest.writeString(this.status);
        dest.writeString(this.rank);
    }

    public HomeworkDetailInfoEntity() {
    }

    protected HomeworkDetailInfoEntity(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.submit_time = in.readLong();
        this.status = in.readString();
        this.rank = in.readString();
    }

    public static final Creator<HomeworkDetailInfoEntity> CREATOR = new Creator<HomeworkDetailInfoEntity>() {
        @Override
        public HomeworkDetailInfoEntity createFromParcel(Parcel source) {
            return new HomeworkDetailInfoEntity(source);
        }

        @Override
        public HomeworkDetailInfoEntity[] newArray(int size) {
            return new HomeworkDetailInfoEntity[size];
        }
    };
}
