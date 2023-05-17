package com.talkcloud.networkshcool.baselibrary.entity;

/**
 * Date:2021/5/25
 * Time:14:29
 * author:joker
 * 系统版本信息 实体类
 */
public class SysVersionEntity {

    /**
     * result : 0
     * version : 2021062000
     * filename : xxx.apk
     * filetype : 0
     * isupdate : 1
     * updateflag : 2
     * url : /Updatefiles/xxx.apk
     * apptype : 1
     * updateaddr : http://Updatefiles/xxx.apk
     * setupaddr : http://xxx
     */

    //0需要升级，-1无升级信息
    private int result;
    //版本号
    private String version;
    //文件名
    private String filename;
    //
    private String filetype;
    //是否需要升级，0：否、1：是
    private String isupdate;
    //升级标志，1：强制升级、2：不强制
    private String updateflag;
    //地址
    private String url;
    //APP类型，0：拓课云APP、1：门课APP
    private String apptype;
    //更新包地址
    private String updateaddr;
    //安装包地址
    private String setupaddr;
    //版本号1.0.0
    private String versionnum;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

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

    public String getIsupdate() {
        return isupdate;
    }

    public void setIsupdate(String isupdate) {
        this.isupdate = isupdate;
    }

    public String getUpdateflag() {
        return updateflag;
    }

    public void setUpdateflag(String updateflag) {
        this.updateflag = updateflag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApptype() {
        return apptype;
    }

    public void setApptype(String apptype) {
        this.apptype = apptype;
    }

    public String getUpdateaddr() {
        return updateaddr;
    }

    public void setUpdateaddr(String updateaddr) {
        this.updateaddr = updateaddr;
    }

    public String getSetupaddr() {
        return setupaddr;
    }

    public void setSetupaddr(String setupaddr) {
        this.setupaddr = setupaddr;
    }

    public String getVersionnum() {
        return versionnum;
    }

    public void setVersionnum(String versionnum) {
        this.versionnum = versionnum;
    }
}
