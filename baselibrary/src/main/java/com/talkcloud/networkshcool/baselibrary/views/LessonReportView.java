package com.talkcloud.networkshcool.baselibrary.views;


public interface LessonReportView<T> {
    //课节报告
    void lessonReportCallback(boolean isSuccess, T data);
}
