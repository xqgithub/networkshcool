package com.talkcloud.networkshcool.baselibrary.entity

data class HomeworkTeacherDetailEntity(
    val content: String,
    val create_time: String,
    val resources: List<TKHomeworkResourceEntity>,
    val title: String,
    val serial: String,
    val submit_way: Int,
    val unsubmits: Int,
    val submits: Int,
    val is_remark: Int,
    val students: List<StudentEntity>
)

data class StudentEntity(
    val homework_id: Int,
    val nickname: String,
    val student_id: Int
)
