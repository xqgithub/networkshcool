package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.LessonMonthEntity;

import java.util.List;

/**
 * Date:2021/5/19
 * Time:16:21
 * author:joker
 */
public interface CalendarCourseView {
    void lessonMonthCallback(boolean isSuccess, String msg, List<LessonMonthEntity> lessonMonthEntity);
}
