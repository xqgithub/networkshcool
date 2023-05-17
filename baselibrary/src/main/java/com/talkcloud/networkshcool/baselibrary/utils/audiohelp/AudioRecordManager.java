package com.talkcloud.networkshcool.baselibrary.utils.audiohelp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.FileUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.SDCardUtils;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

import java.io.File;

/**
 * Date:2021/6/10
 * Time:16:39
 * author:joker
 * 声音录制管理类
 */
public class AudioRecordManager {

    private volatile static AudioRecordManager mInstance;

    private Context mContext;

    private MediaRecorder mMediaRecorder;


    //录制文件名称
    private String recordFileName = "";
    //录制的文件路径
    private String recordFilePath = "";

    //录制状态
    private int RECORD_STATUS = ConfigConstants.AUDIO_NORECORDED;
    //默认录制最大时间
    private int RECORD_MAX_DURATION = 0;

    // 标识MediaRecorder准备完毕
    private boolean isPrepared = false;

    private OnAudioUpdateListener audioUpdateListener;

    /**
     * 单例模式
     */
    public static AudioRecordManager getInstance() {
        if (mInstance == null) {
            synchronized (AudioRecordManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecordManager();
                }
            }
        }
        return mInstance;
    }

//    /**
//     * AudioRecordManager 构造
//     */
//    public AudioRecordManager() {
//        //初始化MediaRecorder
//        if (StringUtils.isBlank(mMediaRecorder)) {
//            mMediaRecorder = new MediaRecorder();
//        }
//    }


    /**
     * 设置监听
     *
     * @param audioUpdateListener
     */
    public void setOnAudioUpdateListener(OnAudioUpdateListener audioUpdateListener) {
        this.audioUpdateListener = audioUpdateListener;
    }


    private static final int WHAT_MICSTATUS = 1; //麦克疯的状态
    private static final int WHAT_RECORDEEND = 2; //录制结束
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_MICSTATUS:
                    updateMicStatus();
                    break;
                case WHAT_RECORDEEND:
                    stopAudioRecord();
                    break;
            }
        }
    };


    /**
     * 初始化MediaRecorder的配置,准备录制音频
     *
     * @param context
     * @param fileName            录制的文件名
     * @param record_max_duration 录制的最大时间
     */
    public void prepareAudio(Context context, String fileName, int record_max_duration) {
        isPrepared = false;
        recordFilePath = getsaveDirectory(fileName);
        try {

            if (record_max_duration > 0) {
                RECORD_MAX_DURATION = record_max_duration;
            } else {
                RECORD_MAX_DURATION = ConfigConstants.RECORD_MAX_DURATION;
            }

            //初始化MediaRecorder
            if (StringUtils.isBlank(mMediaRecorder)) {
                mMediaRecorder = new MediaRecorder();
            }


            //设置声音来源，一般传入 MediaRecorder. AudioSource.MIC参数指定录制来自麦克风的声音。（这里如果只录屏可以不设置）
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);    // 设置音频的格式
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mMediaRecorder.setOutputFile(recordFilePath);
            //设置录制会话的最长持续时间（以ms为单位）
            mMediaRecorder.setMaxDuration(RECORD_MAX_DURATION);

            //准备开始录制
            mMediaRecorder.prepare();

            isPrepared = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件保存路径
     *
     * @param flieName
     * @return
     */
    private String getsaveDirectory(String flieName) {
        String resultPath = "";
        if (!StringUtils.isBlank(flieName)) {
            resultPath = SDCardUtils.getExternalFilesDir(mContext, ConfigConstants.AUDIO_DIR).getAbsolutePath() + File.separator + flieName + ".AAC";
            recordFileName = flieName;
        } else {
            long logdate = dateToLong(DateUtil.getCurrentDateTime());// 生成文件时间，也是文件名称
            resultPath = SDCardUtils.getExternalFilesDir(mContext, ConfigConstants.AUDIO_DIR).getAbsolutePath() + File.separator + logdate + ".AAC";
            recordFileName = logdate + "";
        }

        //判断文件是否存在
        if (FileUtils.createOrExistsFile(resultPath)) {
            //如果存在直接删除文件
            FileUtils.deleteFile(resultPath);
        }
        //创建文件
        boolean iscreatesuccs = FileUtils.createOrExistsFile(resultPath);
        if (!iscreatesuccs) {
            resultPath = "";
        }
        return resultPath;
    }

    public Long dateToLong(String date) {
        date = date.replaceAll("-", "");
        date = date.replaceAll(":", "");
        date = date.replaceAll(" ", "");

        return Long.parseLong(date);
    }


    /**
     * 开始录制
     *
     * @param context
     * @param fileName            录制文件名
     * @param record_max_duration 录制的最大时间
     * @param permissionsListener 权限监听
     */
    public void startAudioRecord(Context context, String fileName, int record_max_duration, PermissionsActivity.PermissionsListener permissionsListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mContext = context;
            //1.开始之前先判断权限是否存在
            PermissionsChecker mPermissionsChecker = new PermissionsChecker(mContext);
            String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
            if (mPermissionsChecker.lacksPermissions(permissions)) {
                //去申请权限
                PublicPracticalMethodFromJAVA.getInstance().startPermissionsActivity((Activity) mContext, permissions, permissionsListener, ConfigConstants.PERMISSIONS_GRANTED_STARTAUDIORECORD);
//                ToastUtils.showLongToastFromRes(R.string.lack_of_write_access, ToastUtils.top);
                return;
            }
//            if (mPermissionsChecker.lacksPermissions(new String[]{Manifest.permission.RECORD_AUDIO})) {
//                ToastUtils.showLongToastFromRes(R.string.lack_of_recorded_audio, ToastUtils.top);
//                return;
//            }
            try {
                if (RECORD_STATUS == ConfigConstants.AUDIO_NORECORDED) {
                    prepareAudio(mContext, fileName, record_max_duration);
                    mMediaRecorder.start();
                    RECORD_STATUS = ConfigConstants.AUDIO_RECORDING;

                    //麦克风更新
                    mHandler.sendEmptyMessageDelayed(WHAT_MICSTATUS, SPACE);
                    //录制结束
                    mHandler.sendEmptyMessageDelayed(WHAT_RECORDEEND, RECORD_MAX_DURATION);

                    audioUpdateListener.startAudioRecordStatus(true, mContext.getResources().getString(R.string.audiorecord_ready_end));
                }
            } catch (Exception e) {
                e.printStackTrace();
                audioUpdateListener.startAudioRecordStatus(false, e.getMessage());
            }
        } else {
            audioUpdateListener.startAudioRecordStatus(false, mContext.getResources().getString(R.string.phone_version_is_low));
        }
    }


    /**
     * 停止录制
     *
     * @return
     */
    public boolean stopAudioRecord() {
        boolean result = false;
        try {
            if (RECORD_STATUS == ConfigConstants.AUDIO_RECORDING) {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;


                mHandler.removeCallbacksAndMessages(null);

                RECORD_STATUS = ConfigConstants.AUDIO_NORECORDED;
                result = true;

                audioUpdateListener.endAudioRecordStatus(true, mContext.getResources().getString(R.string.audiorecord_ready_again));
            }
        } catch (Exception e) {
            e.printStackTrace();
            audioUpdateListener.endAudioRecordStatus(false, e.getMessage());
        }
        return result;
    }


    /**
     * 更新话筒的状态
     */
    private int BASE = 5;
    private int SPACE = 200;// 间隔取样时间

    public void updateMicStatus() {
        try {
            if (mMediaRecorder != null) {
                double ratio = Double.valueOf(mMediaRecorder.getMaxAmplitude() / BASE);
                double db = 0;// 分贝
                if (ratio > 1) {
                    db = 20 * Math.log10(ratio);
                }
                if (null != audioUpdateListener) {
                    audioUpdateListener.onMicStatusUpdate(db);
                }

                mHandler.sendEmptyMessageDelayed(WHAT_MICSTATUS, SPACE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getRECORD_STATUS() {
        return RECORD_STATUS;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    public String getRecordFileName() {
        return recordFileName;
    }

    public void setRecordFileName(String recordFileName) {
        this.recordFileName = recordFileName;
    }

    public void setRecordFilePath(String recordFilePath) {
        this.recordFilePath = recordFilePath;
    }

    /**
     * 清理释放
     */
    public void clear() {
        stopAudioRecord();
    }

    public interface OnAudioUpdateListener {
        void onMicStatusUpdate(double db);

        void startAudioRecordStatus(boolean isSuccess, String msg);

        void endAudioRecordStatus(boolean isSuccess, String msg);
    }


}
