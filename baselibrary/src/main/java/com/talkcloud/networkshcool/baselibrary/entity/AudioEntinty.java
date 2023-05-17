package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/6/17
 * Time:9:40
 * author:joker
 * 音频实体类，用来保存临时数据
 */
public class AudioEntinty {

    // 资源id
    private String id = "";
    // 来源 网盘2 本地1
    private String source = "1";

    //本地文件名称
    private String localFileName;

    //本地文件地址
    private String localFilePath;

    //音频录制时间
    private long duration;

    //音频录制时间 00:00
    private String audioRecordTime;


    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof AudioEntinty)) {
            return false;
        }

        AudioEntinty entinty = (AudioEntinty) o;

        return entinty.localFilePath.equals(this.localFilePath);

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }


    public void setAudioRecordTime(String audioRecordTime) {
        this.audioRecordTime = audioRecordTime;
    }


    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }


    public String getLocalFileName() {
        return localFileName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAudioRecordTime() {
        return audioRecordTime;
    }

    @Override
    public String toString() {
        return "AudioEntinty{" +
                "localFileName='" + localFileName + '\'' +
                ", localFilePath='" + localFilePath + '\'' +
                ", audioRecordDuration=" + duration +
                ", audioRecordTime='" + audioRecordTime + '\'' +
                '}';
    }


}
