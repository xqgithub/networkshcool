package com.talkcloud.networkshcool.baselibrary.views;


public interface HomeworkDetailListView<T> {
    //作业列表
    void homeworkDetailListCallback(boolean isSuccess, T data);

    //提醒回调
    void homeworkNotifyCallback(boolean isSuccess, T data);
}
