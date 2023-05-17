package com.talkcloud.networkshcool.baselibrary.common

/**
 * Author  guoyw
 * Date    2021/5/12
 * Desc
 */
class BaseConstant{
    companion object {
        //SP表名
        const val TABLE_PREFS = "network_school"

        //资料包 路径
        const val SOURCES_PATH = "sources"


        // 播放当前音频
        const val ACTION_PLAY_AUDIO = "action_play_audio"
        const val EXTRA_PLAY_AUDIO_URL = "extra_play_audio_url"

        // 删除网盘数据
        const val ACTION_DELETE_EXT = "action_delete_ext"
        const val EXTRA_DELETE_EXT_ID = "extra_delete_ext_id"


        const val KEY_PARAM1 = "KEY_PARAM1"
        const val KEY_PARAM2 = "KEY_PARAM2"

        const val KEY_PARAM3 = "KEY_PARAM3"

        // 拍照和视频
        const val TYPE_PHOTO = "0"
        const val TYPE_VIDEO = "1"
    }
}
