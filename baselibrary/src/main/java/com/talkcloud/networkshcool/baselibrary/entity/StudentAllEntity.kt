package com.talkcloud.networkshcool.baselibrary.entity

data class StudentAllEntity(
    val current_page: Int,
    val `data`: List<StudentData>,
    val last_page: Int,
    val per_page: Int,
    val total: Int
)

data class StudentData(
    val avatar: String = "",
//    val birthday: Any,
//    val code: String,
//    val companynickname: String,
//    val createtime: Int,
//    val domain_account: Any,
//    val extra_info: ExtraInfo,
//    val http_avatar: String,
    var id: Int = 0,
//    val live_userid: Int,
//    val locale: String,
//    val mobile: String,
    var nickname: String = "",
//    val sex: Int,
//    val ucstate: Int,
//    val userid: Int,
//    val userroleid: Int,
    var isSelect: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other !is StudentData) {
            return false
        }

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return (this.nickname + this.id).hashCode()
    }
}

data class ExtraInfo(
    val p_name: String,
    val relation: String
)