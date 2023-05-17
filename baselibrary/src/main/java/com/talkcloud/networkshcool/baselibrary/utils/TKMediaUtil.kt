package com.talkcloud.networkshcool.baselibrary.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.TextView
import com.luck.picture.lib.tools.DateUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Author  guoyw
 * Date    2021/6/23
 * Desc    媒体工具类
 */
class TKMediaUtil {

    companion object {
        // 获取音视频的展示时间

        fun getShowTime(context: Context, url: String): String {

            val mediaPlayer: MediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(context, Uri.parse(url))
            mediaPlayer.prepare()

            val showTime = DateUtils.formatDurationTime(mediaPlayer.duration.toLong())
            mediaPlayer.release()

            return showTime
        }

        // 显示时间
        fun showMediaTime(context: Context, url: String, view: TextView) {
            Observable.create<String> {
                val emitter = it

                val mediaPlayer: MediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(url))
                    mediaPlayer.prepare()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val showTime = DateUtils.formatDurationTime(mediaPlayer.duration.toLong())
                mediaPlayer.release()

                emitter.onNext(showTime)

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.text = it
                }
        }
    }

}