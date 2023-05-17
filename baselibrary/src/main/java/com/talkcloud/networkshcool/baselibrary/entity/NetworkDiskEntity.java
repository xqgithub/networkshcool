package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;
import java.util.Objects;

/**
 * Date:2021/7/1
 * Time:11:11
 * author:joker
 * 企业网盘 实体类
 */
public class NetworkDiskEntity {


    /**
     * total : 8
     * per_page : 50
     * current_page : 1
     * last_page : 1
     * data : [{"id":"4154","name":"ABBA","type_id":1,"type":"","size":"0KB","update_time":"2021-06-01 10:37:20","preview_url":"","status":0,"downloadpath":""},{"id":"4153","name":"rrrr文件夹","type_id":1,"type":"","size":"0KB","update_time":"2021-06-01 10:10:58","preview_url":"","status":0,"downloadpath":""},{"id":"4118","name":"45tt","type_id":1,"type":"","size":"0KB","update_time":"2021-03-17 15:35:32","preview_url":"","status":0,"downloadpath":""},{"id":"115102","name":"v2-4465c10b718e4ce3721df9e35bc98f72_r.jpeg","type_id":2,"type":"jpeg","size":"4.22M","update_time":"2021-06-11 10:29:20","preview_url":"https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/4465c10b718e4ce3721df9e35bc98f72-1.jpeg&&filetype=jpeg&pagenum=0","status":"1","downloadpath":"https://hwdemocdn.talk-cloud.net/cospath/4465c10b718e4ce3721df9e35bc98f72-1.jpeg"},{"id":"115101","name":"v2-abdc2de175b7bf0ed358ee5828cdf858_r.jpeg","type_id":2,"type":"jpeg","size":"529.17K","update_time":"2021-06-11 10:28:19","preview_url":"https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/abdc2de175b7bf0ed358ee5828cdf858-1.jpeg&&filetype=jpeg&pagenum=0","status":"1","downloadpath":"https://hwdemocdn.talk-cloud.net/cospath/abdc2de175b7bf0ed358ee5828cdf858-1.jpeg"},{"id":"115099","name":"反反复复付.jpeg","type_id":2,"type":"jpeg","size":"4.22M","update_time":"2021-06-11 10:22:18","preview_url":"https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/1001365_20210611_102217_dxqztsci-1.jpeg&&filetype=jpeg&pagenum=1","status":"1","downloadpath":"https://hwdemocdn.talk-cloud.net/cospath/1001365_20210611_102217_dxqztsci-1.jpeg"},{"id":"100739","name":"幼儿语言表达力课程220181110193917.pptx","type_id":2,"type":"pptx","size":"36.92M","update_time":"2021-03-17 15:55:16","preview_url":"https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/dab016d24f87e0055d43aa63e87faa4a/newppt.html&&filetype=pptx&pagenum=0&fileId=100739&showTip=true","status":"1","downloadpath":"https://hwdemocdn.talk-cloud.net/cospath/dab016d24f87e0055d43aa63e87faa4a.pptx"},{"id":"100720","name":"fb7b23c8c210733a2f4555f91c524ee2.mp4","type_id":2,"type":"mp4","size":"6.79M","update_time":"2021-03-16 18:27:24","preview_url":"https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/3ec0bce94b9e808b3ff11b8050b4a639-1.webm&&filetype=mp4&pagenum=0","status":"1","downloadpath":"https://hwdemocdn.talk-cloud.net/cospath/3ec0bce94b9e808b3ff11b8050b4a639-1.mp4"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        public DataBean() {
        }

        public DataBean(String id) {
            this.id = id;
        }

        /**
         * id : 4154
         * name : ABBA
         * type_id : 1
         * type :
         * size : 0KB
         * update_time : 2021-06-01 10:37:20
         * preview_url :
         * status : 0
         * downloadpath :
         */

        private String id;
        //文件名
        private String name;
        //类型 1文件夹 2文件
        private int type_id;
        //文件类型
        private String type;
        //文件大小
        private String size;
        //修改时间
        private String update_time;
        //文件预览图绝对地址
        private String preview_url;
        //状态  0排队中1转换完成2转换失败3转换中
        private int status;
        //下载地址
        private String downloadpath;

        private String source = "2";

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!(o instanceof DataBean)) {
                return false;
            }

            DataBean bean = (DataBean) o;

            return bean.id.equals(this.id);

        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getPreview_url() {
            return preview_url;
        }

        public void setPreview_url(String preview_url) {
            this.preview_url = preview_url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDownloadpath() {
            return downloadpath;
        }

        public void setDownloadpath(String downloadpath) {
            this.downloadpath = downloadpath;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", size='" + size + '\'' +
                    ", downloadpath='" + downloadpath + '\'' +
                    ", source='" + source + '\'' +
                    '}';
        }
    }
}
