package com.talkcloud.networkshcool.baselibrary.entity

data class TKHomeworkDetailEntity(
    var content: String = "",
    var create_time: String = "",
    var id: String = "",
    var resources: List<TKHomeworkResourceEntity> = mutableListOf(),
    var title: String = ""
) {
    companion object {
        @JvmStatic
        fun copyHomeworkDetailEntity2Teacher(entity: HomeworkTeacherDetailEntity): TKHomeworkDetailEntity {
            return  TKHomeworkDetailEntity().apply {
                title = entity.title
                content = entity.content
                create_time = entity.create_time
                resources = entity.resources
            }
        }

        @JvmStatic
        fun copyHomeworkDetailEntity2Student(entity: HomeworkStudentDetailEntity): TKHomeworkDetailEntity {
            return  TKHomeworkDetailEntity().apply {
                title = entity.title
                content = entity.content
                create_time = entity.create_time
                resources = entity.resources
            }
        }


        @JvmStatic
        fun copyHomeworkDetailEntity2Answer(entity: HomeworkStudentDetailEntity.Answer): TKHomeworkDetailEntity {
            return  TKHomeworkDetailEntity().apply {
//                title = entity.title
                content = entity.content
//                create_time = entity.create_time
                resources = entity.resources
            }
        }
    }
}
