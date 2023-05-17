package com.talkcloud.networkshcool.baselibrary.views;


public interface HomeworkListView<T> {
    //作业列表
    void homeworkListCallback(boolean isSuccess, T data);

    //作业列表
    void homeworkDeleteCallback(boolean isSuccess, T data, int index);
}
