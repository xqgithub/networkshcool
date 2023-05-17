package com.talkcloud.networkshcool.baselibrary.utils.audiohelp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

/**
 * Date:2021/6/16
 * Time:15:23
 * author:joker
 * 音频播放器管理类
 */
public class AudioPlayManager implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private volatile static AudioPlayManager mInstance;

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean mediaPlayerIsReady;


    //音频播放状态
    private int AUDIO_PLAY_STATUS = ConfigConstants.AUDIO_STOP;

    private AudioManagerListener audioManagerListener;


    /**
     * 单例模式
     */
    public static AudioPlayManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioPlayManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioPlayManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置监听
     *
     * @param audioManagerListener
     */
    public void setOnAudioManagerListener(AudioManagerListener audioManagerListener) {
        this.audioManagerListener = audioManagerListener;
    }

    private static final int WHAT_UPDATETIMESTATUS = 1; //更新播放进度
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_UPDATETIMESTATUS:
                    updateTimeStatus();
                    break;
            }
        }
    };


    /**
     * 初始化MediaPlayer
     *
     * @param url 本地地址或者网络url
     */
    public void initMediaPlayer(String url) {
        mediaPlayerIsReady = false;

        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
            mMediaPlayer = new MediaPlayer();

            //设置音乐地址
//            mMediaPlayer.setDataSource("https://hwdemocdn.talk-cloud.net/cospath/100361_20210226_161749_nrxvoolp-1.mp3");
            mMediaPlayer.setDataSource(url);

            //设置类型
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            setPlayerVolume(0.5f);
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置程序音量
     */
    public void setPlayerVolume(float volume) {
        mMediaPlayer.setVolume(volume, volume);
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            AUDIO_PLAY_STATUS = ConfigConstants.AUDIO_PLAY;
            if (!StringUtils.isBlank(audioManagerListener)) audioManagerListener.changePlayStatus();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            AUDIO_PLAY_STATUS = ConfigConstants.AUDIO_PAUSE;
            if (!StringUtils.isBlank(audioManagerListener)) audioManagerListener.changePlayStatus();
        }
    }


    /**
     * 停止播放
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            AUDIO_PLAY_STATUS = ConfigConstants.AUDIO_STOP;
            if (!StringUtils.isBlank(audioManagerListener)) audioManagerListener.changePlayStatus();
        }
    }

    /**
     * 释放MediaPlayer
     */
    public void clear() {
        if (mMediaPlayer != null) {
            stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 更新总时长、当前时长，播放进度条
     */
    public void updateTimeStatus() {
        //文件总时长
        int totalTime = mMediaPlayer.getDuration();
        //文件当前时长
        int currenttime = mMediaPlayer.getCurrentPosition();
        if (!StringUtils.isBlank(audioManagerListener)) audioManagerListener.updateTimeStatus(currenttime, totalTime);
        mHandler.sendEmptyMessageDelayed(WHAT_UPDATETIMESTATUS, 200);
    }

    /**
     * 设置MP3播放的位置
     */
    public void setMP3PlaySeekTo(int time) {
        if (mediaPlayerIsReady) {
            mMediaPlayer.seekTo(time);
        }
    }


    public int getAUDIO_PLAY_STATUS() {
        return AUDIO_PLAY_STATUS;
    }


    /**
     * 播放过程中到达媒体源末尾时要调用的回调
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        //发送最后一针
        int totalTime = mMediaPlayer.getDuration();//文件总时长
        int currenttime = mMediaPlayer.getCurrentPosition();//文件当前时长
        audioManagerListener.updateTimeStatus(currenttime, totalTime);

        clear();
    }

    /**
     * 发生错误时要调用的回调
     *
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtils.e(ConfigConstants.TAG_ALL, "what =-= " + what, "extra =-= " + extra);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayerIsReady = true;
        start();
        updateTimeStatus();
    }

    public interface AudioManagerListener {
        void updateTimeStatus(int currenttime, int totalTime);

        void changePlayStatus();
    }


}
