package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class CourseListEntity extends BasePageListEntity {
    public List<CourseInfoEntity> getData() {
        return data;
    }

    public void setData(List<CourseInfoEntity> data) {
        this.data = data;
    }

    List<CourseInfoEntity> data;
}
