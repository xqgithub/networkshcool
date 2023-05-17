package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity

interface HomeworkWriteView : IBaseView {

    // 学生端 显示作业详情
    fun showHomeworkInfo(entity: HomeworkStudentDetailEntity?)

    // 学生端 显示作业详情 撤回
    fun rollbackHomeworkSuccess()

    // 学生提交作业成功
    fun submitHomeworkSuccess(homeworkId: String, isDraft: String?)

}