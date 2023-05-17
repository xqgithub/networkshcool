package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/9/10
 * Time:15:22
 * author:joker
 * 课节课件文件 实体类
 */
public class LessonFilesEntity {


    /**
     * filetype : mp4
     * download_url : https://hwdemocdn.talk-cloud.net/cospath/3ec0bce94b9e808b3ff11b8050b4a639-1.mp4
     * filename : fb7b23c8c210733a2f4555f91c524ee2.mp4
     * preview_url : https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=%2Fcospath%2F3ec0bce94b9e808b3ff11b8050b4a639.webm%2Fnewppt.html&fileId=100720&filetype=mp4&showTip=true
     */

    private String filetype;
    private String download_url;
    private String filename;
    private String preview_url;

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }
}
