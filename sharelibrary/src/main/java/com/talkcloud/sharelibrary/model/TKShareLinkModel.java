package com.talkcloud.sharelibrary.model;

public class TKShareLinkModel {

    private String url;
    private String title;
    private String desc;
    private int icon;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "TKShareLinkModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", icon=" + icon +
                '}';
    }
}
