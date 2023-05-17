package com.talkcloud.networkshcool.baselibrary.views;


public interface CourseDetailView<T> {
    //课程详情
    void courseDetailCallback(boolean isSuccess, T data);
}
