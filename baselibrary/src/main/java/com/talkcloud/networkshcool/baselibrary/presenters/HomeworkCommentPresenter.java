package com.talkcloud.networkshcool.baselibrary.presenters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.luck.picture.lib.entity.LocalMedia;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkDetailInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.StudentAvatarEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherCommentEntity;
import com.talkcloud.networkshcool.baselibrary.entity.UploadEntity;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.AudioRecordDialog;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.ChoiceDialog;
import com.talkcloud.networkshcool.baselibrary.utils.BarUtils;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.HomeworkCommentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Response;

/**
 * Date:2021/6/17
 * Time:15:02
 * author:joker
 * 作业点评的 Presenter
 */
public class HomeworkCommentPresenter extends BasePresenter<HomeworkCommentView> implements AudioRecordDialog.OnRecordListener {

    private Activity mActivity;
    private HomeworkCommentView homeworkCommentView;

    public HomeworkCommentPresenter(Activity activity, HomeworkCommentView homeworkCommentView) {
        super(activity, homeworkCommentView);
        this.mActivity = activity;
        this.homeworkCommentView = homeworkCommentView;
    }

    /**
     * 设置状态栏的高度
     */
    public void setBarHeight(View view) {
        //获取状态的高度
        int BarHeight = BarUtils.getStatusBarHeight(mActivity);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = BarHeight;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 初始化ProgressBar
     *
     * @param pb_nums
     */
    public void setReviewedProgressBar(ProgressBar pb_nums) {
        //圆角度数
        int radius = mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_2x);
        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape roundRectShape1 = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable1 = new ShapeDrawable();
        shapeDrawable1.setShape(roundRectShape1);
        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable1.getPaint().setColor(Color.parseColor("#f2f2f2"));


        RoundRectShape roundRectShape0 = new RoundRectShape(outerR, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setShape(roundRectShape0);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(Color.parseColor(VariableConfig.color_button_selected));
        ClipDrawable clipDrawable = new ClipDrawable(shapeDrawable, Gravity.START, ClipDrawable.HORIZONTAL);

        Drawable[] layers = new Drawable[]{shapeDrawable1, clipDrawable};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);
        pb_nums.setProgressDrawable(layerDrawable);

        homeworkCommentView.changeReviewedProgressBar();
    }


    /**
     * 显示点评弹框
     *
     * @param studentAvatarEntitySelected 被选择将要评论的学生
     * @param remark                      老师点评的内容
     */
    public void showCommentDialog(StudentAvatarEntity studentAvatarEntitySelected, HomeworkStudentDetailEntity.Remark remark) {
        if (!StringUtils.isBlank(studentAvatarEntitySelected)) {
            AudioRecordDialog audioRecordDialog = new AudioRecordDialog(mActivity);
            audioRecordDialog.setDialogMode(ConfigConstants.DIALOG_COMMENT);
            audioRecordDialog.setOnRecordListener(this);
            audioRecordDialog.setCommentDatas(studentAvatarEntitySelected, remark);


            //右上角X按钮
            audioRecordDialog.closeWindow(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioRecordDialog.resetStatus();
                    audioRecordDialog.dismissDialog();

                    if (ScreenTools.getInstance().isPad(mActivity)) {
                        //显示状态栏
                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    }
                }
            });

//            //麦克风录制按钮
//            audioRecordDialog.micClick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    audioRecordDialog.startOrEndRecord();
//                }
//            });

            //我的录音按钮
            audioRecordDialog.myAudioClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioRecordDialog.playOrPauseAudio("");
                }
            });

            //完成按钮
//            audioRecordDialog.carryOutClick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //保存录音文件
//                    audioRecordDialog.saveAudioData2();
//
//
//
//                    audioRecordDialog.resetStatus();
//                    audioRecordDialog.dismissDialog();
//                }
//            });


            if (!audioRecordDialog.isShowing()) {
                audioRecordDialog.show();
            }
        }
    }


    /**
     * 设置学生信息
     */
//    int test1[] = {1, 2, 3, 4, 5, 6, 7};
//    String test2[] = {"路飞", "索隆", "娜美", "乌索普", "乔巴", "罗宾", "弗兰奇"};
//    String test3[] = {"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg1.3lian.com%2Fgif%2Fmore%2F11%2F2012%2F02%2F4aecb96a358ef278c8344885ffde1e06.jpg&refer=http%3A%2F%2Fimg1.3lian.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626832910&t=5e34a464675980a717bb088c0004462f",
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg61.nipic.com%2Ffile%2F20141216%2F19643701_145524273199_1.jpg&refer=http%3A%2F%2Fimg61.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626832959&t=7c6a0c765976aaf722636b4c08630843",
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2Foptuji%2Flvtou%2F22.jpg&refer=http%3A%2F%2Fdmimg.5054399.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626832982&t=b089139848822bc68ba4b099408ad72f",
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg86.nipic.com%2Ffile%2F20170914%2F22448825_165859901001_1.jpg&refer=http%3A%2F%2Fimg86.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626832999&t=3d73b52f9d2b97013a7162bb08dab0cd",
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.name2012.com%2Fuploads%2Fallimg%2F130424%2F24-020705_326.jpg&refer=http%3A%2F%2Fimg.name2012.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1626833007&t=480424f364e2cc51513f3e9895d08f96",
//            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2998091890,3180506611&fm=26&gp=0.jpg",
//            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=591682255,2742736324&fm=26&gp=0.jpg"};
//    boolean test4[] = {true, false, false, false, false, false, false,};
//    boolean test5[] = {false, true, false, false, false, false, false,};
//    String test6[] = {"03-21 19:30", "03-22 19:30", "03-23 19:30", "03-24 19:30", "03-25 19:30", "03-26 19:30", "03-27 19:30"};
    public void setStudentDatas(ArrayList<HomeworkDetailInfoEntity> entityArrayList, int homeworkposition, String homeworkid, String serial) {
        //测试数据
//        List<StudentAvatarEntity> studentAvatarEntities = new ArrayList<>();
//        for (int i = 0; i < test1.length; i++) {
//            StudentAvatarEntity studentAvatarEntity = new StudentAvatarEntity();
//            studentAvatarEntity.setStudentid(test1[i]);
//            studentAvatarEntity.setStudentname(test2[i]);
//            studentAvatarEntity.setAvatarurl(test3[i]);
//            studentAvatarEntity.setIsselected(test4[i]);
//            studentAvatarEntity.setIsfinished(test5[i]);
//            studentAvatarEntity.setWorkfinishedtime(test6[i]);
//            studentAvatarEntities.add(studentAvatarEntity);
//        }

        List<StudentAvatarEntity> studentAvatarEntities = new ArrayList<>();
        for (int i = 0; i < entityArrayList.size(); i++) {
            StudentAvatarEntity studentAvatarEntity = new StudentAvatarEntity();
            studentAvatarEntity.setStudentid(entityArrayList.get(i).getId());
            studentAvatarEntity.setStudentname(entityArrayList.get(i).getName());
            studentAvatarEntity.setAvatarurl(entityArrayList.get(i).getAvatar());

            if (i == homeworkposition) {
                studentAvatarEntity.setIsselected(true);
            } else {
                studentAvatarEntity.setIsselected(false);
            }

            if ("3".equals(entityArrayList.get(i).getStatus())) {
                studentAvatarEntity.setIsfinished(true);
                homeworkCommentView.completedCommentsNumsCallback();
            } else {
                studentAvatarEntity.setIsfinished(false);
            }

            String submit_time = DateUtil.getDateToString(entityArrayList.get(i).getSubmit_time(), 3);
            studentAvatarEntity.setWorkfinishedtime(submit_time);

            studentAvatarEntity.setHomeworkid(homeworkid);
            studentAvatarEntity.setSerial(serial);

            studentAvatarEntities.add(studentAvatarEntity);
        }
        homeworkCommentView.studentWorkInfosCallback(studentAvatarEntities, homeworkposition);
    }


    /**
     * 上传文件
     */

    public void uploadResourceFiles(AudioRecordDialog dialog, AudioEntinty audio, List<LocalMedia> mImgList, TeacherCommentEntity teachercomment) {
        List<String> list = new ArrayList<>();
        List<AudioEntinty> listAudioEntinty = new ArrayList<>();

        if (teachercomment.isFirstRemark()) {//初次点评
            if (!StringUtils.isBlank(audio)) {
                list.add(audio.getLocalFilePath());
            }
            if (mImgList.size() > 0) {
                for (int i = 0; i < mImgList.size(); i++) {
                    list.add(mImgList.get(i).getCompressPath());
                }
            }
        } else {//编辑点评

            if (!StringUtils.isBlank(audio)) {
                if (StringUtils.isBlank(audio.getId())) {
                    list.add(audio.getLocalFilePath());
                } else {
                    audio.setDuration(audio.getDuration() / 1000);
                    listAudioEntinty.add(audio);
                }
            }

            if (mImgList.size() > 0) {
                for (int i = 0; i < mImgList.size(); i++) {
                    if (mImgList.get(i).getPath().startsWith("https") ||
                            mImgList.get(i).getPath().startsWith("http")) {
                        AudioEntinty audio_other = new AudioEntinty();
                        audio_other.setId(String.valueOf(mImgList.get(i).getId()));
                        audio_other.setDuration(0);
                        listAudioEntinty.add(audio_other);
                    } else {
                        list.add(mImgList.get(i).getCompressPath());
                    }
                }
            }

            //表示没有需要上传的数据
            if (list.size() == 0) {
                AudioEntinty[] audioentintys = new AudioEntinty[listAudioEntinty.size()];
                for (int i = 0; i < listAudioEntinty.size(); i++) {
                    audioentintys[i] = listAudioEntinty.get(i);
                }
                teachercomment.setAudioentinty(audioentintys);
                editremark(dialog, teachercomment);
                return;
            }
        }
        uploadImg(list, new Function1<List<UploadEntity>, Unit>() {
            @Override
            public Unit invoke(List<UploadEntity> uploadEntities) {
//                LogUtils.i(ConfigConstants.TAG_ALL, "uploadEntities =-= " + uploadEntities.toString());
                for (int i = 0; i < uploadEntities.size(); i++) {
                    String name = uploadEntities.get(i).getName();
                    int index = name.lastIndexOf(".");
                    if (index > 0) {
                        String suffixName = name.substring(index + 1);
                        if (suffixName.toLowerCase().equals("aac")) {//录音文件
                            audio.setId(uploadEntities.get(i).getId());
                            audio.setDuration(audio.getDuration() / 1000);
                            listAudioEntinty.add(audio);
                        } else {//其他文件
                            AudioEntinty audio_other = new AudioEntinty();
                            audio_other.setId(uploadEntities.get(i).getId());
                            audio_other.setDuration(0);
                            listAudioEntinty.add(audio_other);
                        }
                    }
                }

                AudioEntinty[] audioentintys = new AudioEntinty[listAudioEntinty.size()];
                for (int i = 0; i < listAudioEntinty.size(); i++) {
                    audioentintys[i] = listAudioEntinty.get(i);
                }
                teachercomment.setAudioentinty(audioentintys);

                if (teachercomment.isFirstRemark()) {
                    homeworkremark(dialog, teachercomment);
                } else {
                    editremark(dialog, teachercomment);
                }
                return null;
            }
        });
    }

    /**
     * 提交点评
     */
    private void homeworkremark(AudioRecordDialog dialog, TeacherCommentEntity teachercomment) {
        // 显示loading
        homeworkCommentView.showLoading();


        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("content", teachercomment.getCommentcontent());
        map_bodys.put("useful_expressions", teachercomment.getUseful_expressions());
        map_bodys.put("rank", teachercomment.getRank());
        map_bodys.put("students", teachercomment.getStudents());
        map_bodys.put("resources", teachercomment.getAudioentinty());

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.homeworkremark(teachercomment.getHomeworkid(), map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        try {
                            String msg = apiResponseResponse.body().getMsg();
                            String data = apiResponseResponse.body().getData().toString();
//                            JSONObject jsonObject_data = new JSONObject(data);
//                            boolean exists = jsonObject_data.optBoolean("exists", false);
                            homeworkCommentView.homeworkremarkCallback(true, msg, teachercomment);
                            dialog.dismissDialog();
                            homeworkCommentView.hideSuccessLoading();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            homeworkCommentView.homeworkremarkCallback(false, e.getMessage(), null);
                        }
                        homeworkCommentView.showFailed();
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        homeworkCommentView.homeworkremarkCallback(false, message, null);
                        homeworkCommentView.showFailed();
                    }
                });
    }


    /**
     * 编辑点评
     */
    public void editremark(AudioRecordDialog dialog, TeacherCommentEntity teachercomment) {
        // 显示loading
        homeworkCommentView.showLoading();

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("content", teachercomment.getCommentcontent());
        map_bodys.put("useful_expressions", teachercomment.getUseful_expressions());
        map_bodys.put("rank", teachercomment.getRank());
        map_bodys.put("resources", teachercomment.getAudioentinty());

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.editremark(teachercomment.getHomeworkid(), teachercomment.getStudents()[0], map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        try {
                            String msg = apiResponseResponse.body().getMsg();
                            int result = apiResponseResponse.body().getResult();
                            if (result == 0) {
                                homeworkCommentView.homeworkremarkCallback(true, msg, teachercomment);
                                dialog.dismissDialog();
                                homeworkCommentView.hideSuccessLoading();
                                return;
                            }
                            homeworkCommentView.homeworkremarkCallback(false, msg, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            homeworkCommentView.homeworkremarkCallback(false, e.getMessage(), null);
                        }
                        homeworkCommentView.showFailed();
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        homeworkCommentView.homeworkremarkCallback(false, message, null);
                        homeworkCommentView.showFailed();
                    }
                });
    }

    /**
     * 删除点评
     *
     * @param homeworkId 作业ID
     * @param studentid  学生ID
     */
    public void deleteremark(String homeworkId, String studentid) {

        String[] students = new String[1];
        students[0] = studentid;

        Map<String, Object> map_bodys = new HashMap<>();
        map_bodys.put("students", students);
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.deleteremark(homeworkId, map_bodys)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        try {
                            String msg = apiResponseResponse.body().getMsg();
                            int result = apiResponseResponse.body().getResult();
                            if (result == 0) {
                                homeworkCommentView.deleteRemarkCallback(true, msg);
                                return;
                            }
                            homeworkCommentView.deleteRemarkCallback(false, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            homeworkCommentView.deleteRemarkCallback(false, e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        homeworkCommentView.deleteRemarkCallback(false, message);
                    }
                });
    }


    /**
     * AudioRecordDialog 提交按钮回调
     *
     * @param teachercomment
     */
    @Override
    public void onConfirm(AudioRecordDialog dialog, AudioEntinty audio, List<LocalMedia> mImgList, TeacherCommentEntity teachercomment) {
        if (teachercomment.isFirstRemark()) {//初次点评
            if (!StringUtils.isBlank(audio) || mImgList.size() > 0) {
                uploadResourceFiles(dialog, audio, mImgList, teachercomment);
            } else {
                homeworkremark(dialog, teachercomment);
            }
        } else {//编辑点评
            if (!StringUtils.isBlank(audio) || mImgList.size() > 0) {
                //不清楚数据是否是新增的，所以先上,在 uploadResourceFiles 中去判断
                uploadResourceFiles(dialog, audio, mImgList, teachercomment);
//                if (!StringUtils.isBlank(audio.getId())) {
//                    AudioEntinty[] audioentintys = new AudioEntinty[1];
//                    audio.setDuration(audio.getDuration() / 1000);
//                    audioentintys[0] = audio;
//                    teachercomment.setAudioentinty(audioentintys);
//                    editremark(dialog, teachercomment);
//                } else {//先上传本地文件
//                    uploadResourceFiles(dialog, audio, mImgList, teachercomment);
//                }
            } else {//没有录音文件，直接编辑
                editremark(dialog, teachercomment);
            }
        }
    }

    /**
     * AudioRecordDialog 录音完成按钮回调
     *
     * @param audio
     */
    @Override
    public void onRecordComplete(AudioEntinty audio) {

    }

    /**
     * 删除提示弹框
     */
    public void showDeleteDialog(StudentAvatarEntity studentAvatarEntitySelected) {
        ChoiceDialog choiceDialog = new ChoiceDialog(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_375x));
        String title = mActivity.getResources().getString(R.string.logout_title);
        String content = mActivity.getResources().getString(R.string.homeworkcomment_delete);
        String no = mActivity.getResources().getString(R.string.logout_no);
        String yes = mActivity.getResources().getString(R.string.logout_yes);
        choiceDialog.setTextInformation(title, content, no, yes);
        choiceDialog.setDialogBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
        choiceDialog.setCancelBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
        choiceDialog.setConfirmBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);

        choiceDialog.confirmButton(v -> {
            if (studentAvatarEntitySelected.isIsfinished()) {
                deleteremark(studentAvatarEntitySelected.getHomeworkid(), studentAvatarEntitySelected.getStudentid());
            }
            choiceDialog.dismissDialog();
        });

        choiceDialog.notSure(v -> choiceDialog.dismissDialog());

        choiceDialog.closeWindow(v -> choiceDialog.dismissDialog());

        if (!choiceDialog.isShowing()) {
            choiceDialog.show();
        }
    }


}
