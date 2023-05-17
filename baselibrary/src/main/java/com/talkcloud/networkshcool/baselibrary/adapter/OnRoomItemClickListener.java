package com.talkcloud.networkshcool.baselibrary.adapter;

import com.talkcloud.corelibrary.TKJoinRoomModel;

public interface OnRoomItemClickListener {
    // 进入教室
//    void onJoinRoom(String serial, String pwd);

    // 回放
//    void onJoinPlayBackRoom(String url);

    // 进入教室
    void onJoinRoom(String serialId, TKJoinRoomModel model);

//    void onJoinPlayBackRoom(TKJoinBackRoomModel backRoomModel);

    void onJoinPlayBackRoom(String serialId);

    //备课
    void onLessonPreparation(String serialId);
}
