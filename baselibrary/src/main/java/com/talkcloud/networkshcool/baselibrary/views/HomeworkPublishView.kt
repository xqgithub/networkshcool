package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.StudentData

interface HomeworkPublishView : IBaseView {

    // 老师发布作业成功
    fun publishHomeworkSuccess(homeworkId: String?, isDraft: String?)

    // 当前课节的学生列表
    fun handleLessonStudentInfo(studentList: List<StudentData>)

}