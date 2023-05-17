package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/7/1
 * Time:14:37
 * author:joker
 * 网盘数据页数 实体类
 */
public class NetworkDiskPageEntity {

    //总数
    private int total;
    //每页显示多少行
    private int per_page;
    //当前页数
    private int current_page;
    //最后页数
    private int last_page;
    //文件夹id
    private String dir_id;

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

    public String getDir_id() {
        return dir_id;
    }

    public void setDir_id(String dir_id) {
        this.dir_id = dir_id;
    }
}
