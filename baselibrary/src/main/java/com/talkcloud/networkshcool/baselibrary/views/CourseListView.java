package com.talkcloud.networkshcool.baselibrary.views;


public interface CourseListView<T> {
    //课节列表
    void courseListCallback(boolean isSuccess, T data);
}
