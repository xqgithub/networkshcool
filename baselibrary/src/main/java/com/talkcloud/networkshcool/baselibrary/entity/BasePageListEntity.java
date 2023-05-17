package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * author:wsf
 * createTime:2021/5/14
 * description: 翻页数据基类
 */
public class BasePageListEntity {

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }


}
