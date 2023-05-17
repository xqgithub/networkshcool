package com.talkcloud.networkshcool.baselibrary.views;


public interface LessonListView<T> {
    //课节列表
    void lessonListCallback(boolean isSuccess, T data);

    //课节课件
    void lessonFilesCallback(boolean isSuccess, T data, String msg);
}
