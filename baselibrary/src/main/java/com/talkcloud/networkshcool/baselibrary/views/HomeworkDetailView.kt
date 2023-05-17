package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.networkshcool.baselibrary.entity.HomeworkTeacherDetailEntity

interface HomeworkDetailView : IBaseView {

    // 老师端 显示老师作业详情
    fun showHomeworkInfo(entity: HomeworkTeacherDetailEntity?) {}

}