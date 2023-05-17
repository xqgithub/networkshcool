package com.talkcloud.networkshcool.baselibrary.entity;

import java.io.Serializable;

import kotlin.jvm.JvmStatic;

public class ResourseEntity implements Serializable {
    String filename;   //文件名
    String filetype;//类型  xls,xlsx,ppt,pptx,doc,docx,txt,pdf,jpg,gif,jpeg,png,bmp,mp3,mp4,zip  为空代表是未知格式
    String size;//大小
    String uploadtime;//修改时间
    String preview_url;//预览地址
    String downloadpath;//下载地址


    @JvmStatic
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public void setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
    }


}
