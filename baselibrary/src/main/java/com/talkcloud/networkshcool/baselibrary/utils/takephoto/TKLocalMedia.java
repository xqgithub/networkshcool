package com.talkcloud.networkshcool.baselibrary.utils.takephoto;

import com.luck.picture.lib.entity.LocalMedia;

/**
 * Author  guoyw
 * Date    2021/6/28
 * Desc     适用
 */
public class TKLocalMedia extends LocalMedia {

    // 资源id
    private String mediaId;
    // 来源 网盘2 本地1
    private String source = "1";

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocalMedia)) {
            return false;
        }

        LocalMedia media = (LocalMedia) obj;

        return this.getId() == media.getId();
    }

}
