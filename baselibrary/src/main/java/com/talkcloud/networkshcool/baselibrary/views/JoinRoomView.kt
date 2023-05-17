package com.talkcloud.networkshcool.baselibrary.views

import com.talkcloud.corelibrary.TKJoinBackRoomModel
import com.talkcloud.corelibrary.TKJoinRoomModel

interface JoinRoomView : IBaseView {

    fun joinRoom(joinRoomModel: TKJoinRoomModel)

    fun joinPlaybackRoom(backRoomModelList: List<TKJoinBackRoomModel>)
}