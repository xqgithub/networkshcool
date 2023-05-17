package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.StudentAvatarEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherCommentEntity;

import java.util.List;

/**
 * Date:2021/6/17
 * Time:15:00
 * author:joker
 * 作业点评页面
 */
public interface HomeworkCommentView extends IBaseView {
    void changeReviewedProgressBar();

//    void studentAvatarAdapterClick(List<StudentAvatarEntity> studentAvatarEntities, int position);

    void studentWorkInfosCallback(List<StudentAvatarEntity> studentAvatarEntities, int position);

    void completedCommentsNumsCallback();

    void homeworkremarkCallback(boolean isSuccess, String msg, TeacherCommentEntity teacherCommentEntity);

    void deleteRemarkCallback(boolean isSuccess, String msg);
}
