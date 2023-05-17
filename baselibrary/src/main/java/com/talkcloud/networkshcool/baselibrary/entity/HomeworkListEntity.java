package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class HomeworkListEntity extends BasePageListEntity {
    public List<HomeworkInfoEntity> getData() {
        return data;
    }

    public void setData(List<HomeworkInfoEntity> data) {
        this.data = data;
    }

    List<HomeworkInfoEntity> data;
}
