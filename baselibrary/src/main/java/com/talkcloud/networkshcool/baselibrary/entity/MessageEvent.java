package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/5/12
 * Time:18:10
 * author:joker
 * EventBus 消息事件实体类
 */
public class MessageEvent {

    private String message_type;

    private Object message;

    public MessageEvent() {}

    public MessageEvent(String message_type) {
        this.message_type = message_type;
    }

    public MessageEvent(String message_type, Object message) {
        this.message_type = message_type;
        this.message = message;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
