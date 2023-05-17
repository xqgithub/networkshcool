package com.talkcloud.networkshcool.baselibrary.entity


data class UserExtInfoEntity(
    val courses: String?,
    val cups: String?,
    val flowers: String?,
    var identitys: List<String?>,
    var config: Config?
)

data class Config (
    var remove_account: Int,
    var phone: String
)
