package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class HomeworkDetailListEntity {
    public List<HomeworkDetailInfoEntity> getData() {
        return data;
    }

    public void setData(List<HomeworkDetailInfoEntity> data) {
        this.data = data;
    }

    List<HomeworkDetailInfoEntity> data;
}
