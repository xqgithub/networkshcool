package com.talkcloud.networkshcool.baselibrary.entity

data class HomeworkStudentDetailEntity(
    val answer: Answer,
    val content: String,
    val create_time: String,
    val homework_id: String,
    val remark: Remark,
    val resources: List<TKHomeworkResourceEntity>,
    val serial: String,
    var status: String,
    val student_id: String,
    val submit_way: String,
    val title: String
) {
    data class Answer(
        val content: String,
        val submit_time: String?,
        val resources: List<TKHomeworkResourceEntity>,
        val student: Student?
    ) {
        data class Student(val name: String?, val avatar: String?)
    }

    data class Remark(
        val content: String,
        val rank: Int,
        val resources: List<TKHomeworkResourceEntity>,
        val remark_time: String,
        val teacher: Teacher?
    )

    data class Teacher(
        val name: String,
        val avatar: String
    )
}



