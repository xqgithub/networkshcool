package com.talkcloud.networkshcool.baselibrary.entity;

import java.util.List;

public class LessonListEntity  {
    public List<LessonInfoEntity> getData() {
        return data;
    }

    public void setData(List<LessonInfoEntity> data) {
        this.data = data;
    }

    List<LessonInfoEntity> data;
}
