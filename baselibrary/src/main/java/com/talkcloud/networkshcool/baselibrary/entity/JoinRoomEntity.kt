package com.talkcloud.networkshcool.baselibrary.entity


data class JoinRoomEntity(
    val room_id: String,
    val enter_room_url: String,
    val pwd: String,
    val state: String
)