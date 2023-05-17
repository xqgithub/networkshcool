package com.talkcloud.corelibrary;

public class TKJoinBackRoomModel {



    /**
     * url : https://hwdemorecord.talk-cloud.net/a86c3f23-93e1-46ae-a799-510a8403370e-224879082/
     * title : 1622085485287
     * name :
     */

//    private String serial;
    private String roomId;
    private String url;
    private String title;
    private String name;
    private String mp4url;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

//    public String getSerial() {
//        return serial;
//    }
//
//    public void setSerial(String serial) {
//        this.serial = serial;
//    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMp4url() {
        return mp4url;
    }

    public void setMp4url(String mp4url) {
        this.mp4url = mp4url;
    }

    @Override
    public String toString() {
        return "TKJoinBackRoomModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", mp4url='" + mp4url + '\'' +
                '}';
    }


}
