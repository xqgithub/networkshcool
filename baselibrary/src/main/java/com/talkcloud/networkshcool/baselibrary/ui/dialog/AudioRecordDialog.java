package com.talkcloud.networkshcool.baselibrary.ui.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.AudioEntinty;
import com.talkcloud.networkshcool.baselibrary.entity.HomeworkStudentDetailEntity;
import com.talkcloud.networkshcool.baselibrary.entity.StudentAvatarEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TKHomeworkResourceEntity;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherCommentEntity;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity;
import com.talkcloud.networkshcool.baselibrary.utils.BarUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ConstraintUtil;
import com.talkcloud.networkshcool.baselibrary.utils.DateUtil;
import com.talkcloud.networkshcool.baselibrary.utils.DeviceUtils;
import com.talkcloud.networkshcool.baselibrary.utils.FileUtils;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.utils.audiohelp.AudioPlayManager;
import com.talkcloud.networkshcool.baselibrary.utils.audiohelp.AudioRecordManager;
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.GlideEngine;
import com.talkcloud.networkshcool.baselibrary.utils.takephoto.TKLocalMedia;
import com.talkcloud.networkshcool.baselibrary.weiget.CircleProgressbar;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;
import com.talkcloud.networkshcool.baselibrary.weiget.RatingBarView;
import com.talkcloud.networkshcool.baselibrary.weiget.RxViewUtils;
import com.talkcloud.networkshcool.baselibrary.weiget.TKJobAudioView;
import com.talkcloud.networkshcool.baselibrary.weiget.TKJobOperateView;
import com.talkcloud.networkshcool.baselibrary.weiget.TKVideoImage;
import com.weigan.loopview.LoopView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import me.jessyan.autosize.AutoSizeConfig;
import retrofit2.Response;

/**
 * Date:2021/6/11
 * Time:9:46
 * author:joker
 * 声音录制弹框,兼容作业点评弹框
 */
public class AudioRecordDialog extends Dialog implements AudioRecordManager.OnAudioUpdateListener, Chronometer.OnChronometerTickListener, AudioPlayManager.AudioManagerListener, PermissionsActivity.PermissionsListener,

        RatingBarView.OnRatingListener, EditTextCustomize.onEditTextListener, RxViewUtils.Action1<View> {

    //自定义view
    private View mDialogView;
    private Context mContext;
    private Window window;
    //弹框的模式
    private int model = 0;

    //快捷语或者软件盘  true 软键盘，fales 快捷语
    private boolean shortcut_softinput_model = true;

    //获取状态的高度
    private int BarHeight = 0;

    private ImageView iv_close;
    private ConstraintLayout cl_dialog;
    private ConstraintLayout cl_mic;
    private ConstraintLayout cl_replay;
    private ConstraintLayout cl_carryout;
    private ConstraintLayout cl_comment;
    private ConstraintLayout cl_audiorecord;
    private ImageView iv_mic;
    private ImageView iv_replay;
    private TextView tv_record_status_prompt;
    private TextView tv_myaudio;
    private TextView tv_carryout;
    private TextView tv_comment_description;
    private TextView tv_comment_title;
    private EditTextCustomize et_comment_content;
    private ImageView iv_shortcut;
    private TextView tv_shortcut;
    private ConstraintLayout cl_shortcut_softinputmode;
    private ImageView iv_shortcut_softinputmode;
    private TextView tv_selecte_hide;
    private TextView tv_confirm;


    private CircleProgressbar cp_mic_animation;
    //录制计时器
    private Chronometer tv_time;
    //每一次录制开始的时间
    private long record_starttime = 0L;
    //每一次录制音频时长
    private long audioRecordDuration;
    private String audioRecordTime;

    //麦克风图标数据
    private int[] arrayImageId = new int[]{R.drawable.ns_mic1, R.drawable.ns_mic2, R.drawable.ns_mic3, R.drawable.ns_mic4,
            R.drawable.ns_mic5, R.drawable.ns_mic6, R.drawable.ns_mic7};


    private RatingBarView rbv_comment;
    //评级数
    private int ratingscore = 2;

    private LoopView loopView;

    //被选择的快捷语的index
    private int shortcut_index = 0;

    //录音文件展示View
    private TKJobAudioView jav_audio;

    //被选择的学生的信息
    private StudentAvatarEntity studentAvatarEntitySelected;
    //老师点评内容
    private HomeworkStudentDetailEntity.Remark remark;
    //提交内容是 提交第一次评论 true  编辑点评 false
    private boolean isFirstRemark = true;

    //快捷语集合数据
    private List<String> expressions = new ArrayList<>();

    //ConstraintLayout布局修改工具类
    private ConstraintUtil constraintUtil;

    //获得手机的型号
    String phonemodel = DeviceUtils.getModel();


    //图片上传
    private TKJobOperateView mJobOperateView;
    private LinearLayout mJobMediaContainerLl;
    private LinearLayout mJobImageContainerLl;
    //图片集合
    private List<LocalMedia> mImgList = new ArrayList<>();

    private PictureParameterStyle mPictureParameterStyle;

    // 预览的文件类型
    private int mineType = -1;
    //图片是否可以删除
    private boolean isEditable = true;

    //作业点评是否保存为快捷评语状态
    private boolean HomeWorkCommentShortcutStatus = false;


    public AudioRecordDialog(Context context) {
        super(context, R.style.dialog_style);
        this.mContext = context;
        this.window = getWindow();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initUIView(context);
        initData();
        initListener();
    }


    private WindowManager.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void show() {
//        focusNotAle(window);
        super.show();
//        clearFocusNotAle(window);

        /**
         * 设置宽度全屏，要设置在show的后面
         */
        layoutParams = window.getAttributes();

        if (ScreenTools.getInstance().isPad(mContext)) {
            layoutParams.gravity = Gravity.RIGHT;
            window.setWindowAnimations(R.style.AnimRight);  //添加动画
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_395x);
        } else {
            layoutParams.gravity = Gravity.BOTTOM;
            window.setWindowAnimations(R.style.AnimBottom);  //添加动画
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_375x);
        }

        if (model == ConfigConstants.DIALOG_COMMENT) {
            if (ScreenTools.getInstance().isPad(mContext)) {
                int height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_768x);
                if (height > AutoSizeConfig.getInstance().getScreenHeight()) {
                    height = AutoSizeConfig.getInstance().getScreenHeight();
                }
                layoutParams.height = height;

                if (BarUtils.isStatusBarExists((Activity) mContext)) {
                    //状态栏的高度
                    BarHeight = BarUtils.getStatusBarHeight(mContext);
                }
                //隐藏导航栏
                PublicPracticalMethodFromJAVA.getInstance().hideNavigationBar(window);
            } else {
//                int height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_718x);
                int height = (int) (AutoSizeConfig.getInstance().getScreenHeight() * 0.88);
//                if (height > AutoSizeConfig.getInstance().getScreenHeight()) {
//                    height = AutoSizeConfig.getInstance().getScreenHeight();
//                }
                layoutParams.height = height;
            }

            window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        } else {
            if (ScreenTools.getInstance().isPad(mContext)) {
                int height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_768x);
                layoutParams.height = height;
            } else {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
        }
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(layoutParams);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE |
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    /**
     * 初始化UI
     *
     * @param context
     */
    private void initUIView(Context context) {
        mDialogView = View.inflate(context, R.layout.dailog_audiorecord, null);
        setContentView(mDialogView);


        iv_close = mDialogView.findViewById(R.id.iv_close);
        tv_time = mDialogView.findViewById(R.id.tv_time);
        cl_mic = mDialogView.findViewById(R.id.cl_mic);
        cl_dialog = mDialogView.findViewById(R.id.cl_dialog);
        iv_mic = mDialogView.findViewById(R.id.iv_mic);
        tv_record_status_prompt = mDialogView.findViewById(R.id.tv_record_status_prompt);
        cl_replay = mDialogView.findViewById(R.id.cl_replay);
        iv_replay = mDialogView.findViewById(R.id.iv_replay);
        cl_carryout = mDialogView.findViewById(R.id.cl_carryout);
        tv_myaudio = mDialogView.findViewById(R.id.tv_myaudio);
        tv_carryout = mDialogView.findViewById(R.id.tv_carryout);
        cp_mic_animation = mDialogView.findViewById(R.id.cp_mic_animation);
        rbv_comment = mDialogView.findViewById(R.id.rbv_comment);
        tv_comment_description = mDialogView.findViewById(R.id.tv_comment_description);
        tv_comment_title = mDialogView.findViewById(R.id.tv_comment_title);
        cl_comment = mDialogView.findViewById(R.id.cl_comment);
        et_comment_content = mDialogView.findViewById(R.id.et_comment_content);
        iv_shortcut = mDialogView.findViewById(R.id.iv_shortcut);
        tv_shortcut = mDialogView.findViewById(R.id.tv_shortcut);
        cl_shortcut_softinputmode = mDialogView.findViewById(R.id.cl_shortcut_softinputmode);
        iv_shortcut_softinputmode = mDialogView.findViewById(R.id.iv_shortcut_softinputmode);
        tv_selecte_hide = mDialogView.findViewById(R.id.tv_selecte_hide);
        tv_confirm = mDialogView.findViewById(R.id.tv_confirm);
        loopView = mDialogView.findViewById(R.id.loopView);
        cl_audiorecord = mDialogView.findViewById(R.id.cl_audiorecord);
        jav_audio = mDialogView.findViewById(R.id.jav_audio);
        mJobOperateView = mDialogView.findViewById(R.id.mJobOperateView);
        mJobMediaContainerLl = mDialogView.findViewById(R.id.mJobMediaContainerLl);
        mJobImageContainerLl = mDialogView.findViewById(R.id.mJobImageContainerLl);

        /** 初始化隐藏的UI控件 **/
        cl_comment.setVisibility(View.GONE);
        iv_shortcut.setVisibility(View.INVISIBLE);
        tv_shortcut.setVisibility(View.INVISIBLE);
        cl_shortcut_softinputmode.setVisibility(View.GONE);
        loopView.setVisibility(View.GONE);
        jav_audio.setVisibility(View.GONE);


        /**倒计时动画设置**/
        cp_mic_animation.setTimeMillis(ConfigConstants.RECORD_MAX_DURATION);
        cp_mic_animation.setProgressType(CircleProgressbar.ProgressType.COUNT_BACK);
        cp_mic_animation.setOutLineWidth(0);
        cp_mic_animation.setOutLineColor(Color.parseColor("#00000000"));
        cp_mic_animation.setInCircleColor(Color.parseColor("#00000000"));
        cp_mic_animation.setProgressColor(Color.parseColor("#1a1d6aff"));
        cp_mic_animation.setProgressLineWidth(context.getResources().getDimensionPixelSize(R.dimen.dimen_10x));


        setDialogBG();
        setMicBG();
        setReplayBG(false);
        setFinshBG();
        setSelecteHideDetermineBG();
        setConfirmBtnBG(VariableConfig.color_button_unselected);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        setConfirmBtnHeight(tv_confirm);
        setStarRating(3);

        constraintUtil = new ConstraintUtil(cl_shortcut_softinputmode);


        //设置只显示图片上传
        mJobOperateView.setJobSubmitType("1");
    }

    /**
     * 初始化监听
     */
    @SuppressLint({"ClickableViewAccessibility", "CheckResult"})
    private void initListener() {
        AudioRecordManager.getInstance().setOnAudioUpdateListener(this);
        AudioPlayManager.getInstance().setOnAudioManagerListener(this);
        tv_time.setOnChronometerTickListener(this);
        rbv_comment.setOnRatingListener(this);
        et_comment_content.setOnEditTextListener(this);


        // 关闭
        iv_close.setOnClickListener(v -> {
            AudioRecordManager.getInstance().clear();
            AudioPlayManager.getInstance().clear();
            dismissDialog();
//            showAudioRecordConfirmDialog(audioRecordDialog)
        });

        // 录音
//        cl_mic.setOnClickListener(v -> {
//            startOrEndRecord();
//        });
        RxViewUtils.getInstance().setOnClickListeners(this, 1000, cl_mic);


        // 试听录音
        cl_replay.setOnClickListener(v -> {
            playOrPauseAudio("");
        });

        // 完成
        cl_carryout.setOnClickListener(v -> {
            audio = saveAudioData2();
            if (recordListener != null) {
                recordListener.onRecordComplete(audio);
            }

            if (model == ConfigConstants.DIALOG_COMMENT) {
                showLocalAudio(audio);
            } else {
                dismissDialog();
            }
            resetStatus();
        });


        //快捷评论语点击
        iv_shortcut.setOnClickListener(v -> {
            setShortcutStatus();
        });
        //快捷评论语点击
        tv_shortcut.setOnClickListener(v -> {
            setShortcutStatus();
        });

        //快捷语和键盘切换  确定按钮点击
        tv_selecte_hide.setOnClickListener(v -> {
            if (shortcut_softinput_model) {
                KeyBoardUtil.getInstance().hideKeyBoard(mContext, et_comment_content);
                disappearShortcutAndSoftinputmode();
            } else {
                String shortcut_content = expressions.get(shortcut_index);
                disappearShortcutAndSoftinputmode();
                //先获取输入框的值
                String et_comment_content1 = et_comment_content.getText().toString();
                et_comment_content.setText(et_comment_content1 + shortcut_content);

                et_comment_content.setFocusable(true);
                et_comment_content.setFocusableInTouchMode(true);
            }

            shortcut_softinput_model = true;
        });

        //快捷语软件盘切换按钮
        iv_shortcut_softinputmode.setOnClickListener(v -> {
            if (shortcut_softinput_model) {//软键盘切换到快捷语
                shortcut_softinput_model = false;
                iv_shortcut_softinputmode.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.homeworkcomment_softinput));

                //隐藏键盘
                KeyBoardUtil.getInstance().hideKeyBoard(mContext, et_comment_content);
                et_comment_content.setFocusable(false);
                et_comment_content.setFocusableInTouchMode(false);


                showShortcut();
            } else {//快捷语切换到软键盘
                shortcut_softinput_model = true;
                iv_shortcut_softinputmode.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.homeworkcomment_shortcut));

                KeyBoardUtil.getInstance().showKeyBoard(mContext, et_comment_content);
                et_comment_content.setFocusable(true);
                et_comment_content.setFocusableInTouchMode(true);

//                showSoftinputmode();
            }
        });


        //监听 解决EditText和NestedScrollView 冲突
        et_comment_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    //通知父控件不要干扰
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });


        //确定按钮提交点评
        tv_confirm.setOnClickListener(v -> {
            if (AudioRecordManager.getInstance().getRECORD_STATUS() == ConfigConstants.AUDIO_RECORDING) {
                return;
            }

            if (!StringUtils.isBlank(audio) && FileUtils.isFileExists(audio.getLocalFilePath()) ||
                    !StringUtils.isBlank(et_comment_content.getText().toString().trim()) ||
                    mImgList.size() > 0) {
                String content = et_comment_content.getText().toString();
                String useful_expressions = HomeWorkCommentShortcutStatus ? "1" : "0";
                String rank = String.valueOf(ratingscore);
                String[] students = new String[1];
                students[0] = studentAvatarEntitySelected.getStudentid();

                TeacherCommentEntity teacherComment = new TeacherCommentEntity();
                teacherComment.setCommentcontent(content);
                teacherComment.setUseful_expressions(useful_expressions);
                teacherComment.setRank(rank);
                teacherComment.setStudents(students);
                teacherComment.setHomeworkid(studentAvatarEntitySelected.getHomeworkid());
                teacherComment.setFirstRemark(isFirstRemark);

                if (recordListener != null) {
                    recordListener.onConfirm(this, audio, mImgList, teacherComment);
                }
            }
        });


        //图片选择回调监听
        mJobOperateView.setOnPhotoSuccessListener(new Function2<String, List<? extends LocalMedia>, Unit>() {
            @Override
            public Unit invoke(String type, List<? extends LocalMedia> result) {
                if (type.equals(BaseConstant.TYPE_PHOTO)) {//相册选择
                    mJobImageContainerLl.removeAllViews();
                    mImgList.clear();
                }

                for (LocalMedia localMedia : result) {
                    showLocalMedia(localMedia);
                }
                setConfirmBtnBG(VariableConfig.color_button_selected);
                return null;
            }
        });

        //注册删除图片监听
        BroadcastManager.getInstance(mContext).registerReceiver(broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
    }

    /**
     * 防抖动点击回调
     */
    @Override
    public void onRxViewClick(View view) {
        if (view == cl_mic) {
            startOrEndRecord();
        }
    }


    /**
     * 设置弹框模式
     */
    public void setDialogMode(int mode) {
        this.model = mode;
        if (model == ConfigConstants.DIALOG_COMMENT) {
            cl_comment.setVisibility(View.VISIBLE);
            tv_confirm.setVisibility(View.VISIBLE);
        } else {
            cl_comment.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.INVISIBLE);
        }
        setConfirmBtnHeight(tv_confirm);
    }


    /**
     * 设置弹框的背景
     */
    private void setDialogBG() {
        setDynamicShapeRECTANGLE(mContext, cl_dialog, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
    }

    /**
     * 设置麦克疯背景
     */
    private void setMicBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mContext, cl_mic, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
    }


    /**
     * 设置回放按钮背景
     *
     * @param isPlay
     */
    private void setReplayBG(boolean isPlay) {
        if (isPlay) {
            iv_replay.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.audioreplay_pause));
        } else {
            iv_replay.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.audioreplay_start));
        }
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mContext, cl_replay, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
    }


    /**
     * 设置完成按钮背景
     */
    private void setFinshBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mContext, cl_carryout, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
    }


    /**
     * 麦克风点击事件
     */

    public void micClick(View.OnClickListener listener) {
        cl_mic.setOnClickListener(listener);
    }


    /**
     * 我的录音点击事件
     *
     * @param listener
     */
    public void myAudioClick(View.OnClickListener listener) {
        cl_replay.setOnClickListener(listener);
    }

    /**
     * 关闭按钮
     */
    public void closeWindow(View.OnClickListener listener) {
        iv_close.setOnClickListener(listener);
    }

    /**
     * 完成按钮
     *
     * @param listener
     */
    public void carryOutClick(View.OnClickListener listener) {
        cl_carryout.setOnClickListener(listener);
    }


    /**
     * 开始录制/停止录制
     */
    public void startOrEndRecord() {
        if (AudioRecordManager.getInstance().getRECORD_STATUS() == ConfigConstants.AUDIO_NORECORDED) {
            //开始录制
            AudioRecordManager.getInstance().startAudioRecord(mContext, "", ConfigConstants.RECORD_MAX_DURATION, this);
        } else {
            //停止录制
            AudioRecordManager.getInstance().stopAudioRecord();
        }
    }

    /**
     * 播放或者暂停 音频
     *
     * @param url 如果url不为空，播放url地址音频；如果为空，检查本地是否有文件，如果有播放
     */
    public void playOrPauseAudio(String url) {
        if (AudioPlayManager.getInstance().getAUDIO_PLAY_STATUS() == ConfigConstants.AUDIO_STOP) {//停止状态
            if (!StringUtils.isBlank(url)) {
                AudioPlayManager.getInstance().initMediaPlayer(url);
            } else {
                String recordfilepath = AudioRecordManager.getInstance().getRecordFilePath();
                if (FileUtils.isFileExists(recordfilepath)) {
                    AudioPlayManager.getInstance().initMediaPlayer(recordfilepath);
                } else {
                    ToastUtils.showShortToastFromRes(R.string.audio_play_fail, ToastUtils.top);
                }
            }
        } else if (AudioPlayManager.getInstance().getAUDIO_PLAY_STATUS() == ConfigConstants.AUDIO_PAUSE) {//暂停状态
            AudioPlayManager.getInstance().start();
        } else {//播放状态
            AudioPlayManager.getInstance().pause();
        }
    }


    /**
     * 关闭弹框
     */
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
            window.getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);

            //注销删除图片监听
            BroadcastManager.getInstance(mContext).unregisterReceiver(broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }


    /**
     * 重置
     */
    public void resetStatus() {
        AudioRecordManager.getInstance().clear();
        AudioPlayManager.getInstance().clear();
        record_starttime = 0L;
        audioRecordTime = "";
        audioRecordDuration = 0L;
        AudioRecordManager.getInstance().setRecordFileName("");
        AudioRecordManager.getInstance().setRecordFilePath("");


        cl_replay.setVisibility(View.GONE);
        cl_carryout.setVisibility(View.GONE);
        tv_myaudio.setVisibility(View.GONE);
        tv_carryout.setVisibility(View.GONE);
        tv_record_status_prompt.setText(R.string.audiorecord_ready_start);

    }

    /**
     * 保存录制数据信息
     */
    public AudioEntinty saveAudioData2() {
        AudioEntinty audioEntinty = new AudioEntinty();
        audioEntinty.setLocalFileName(AudioRecordManager.getInstance().getRecordFileName());
        audioEntinty.setLocalFilePath(AudioRecordManager.getInstance().getRecordFilePath());
        audioEntinty.setAudioRecordTime(audioRecordTime);
        audioEntinty.setDuration(audioRecordDuration);

        return audioEntinty;
    }

    /**
     * 当用户确定不保存的时候，删除录制的数据
     */
    public void deleteAudioData() {
        if (FileUtils.isFileExists(AudioRecordManager.getInstance().getRecordFilePath())) {
            FileUtils.deleteFile(AudioRecordManager.getInstance().getRecordFilePath());
        }
    }

    /**
     * 动态设置Shape  RECTANGLE   上下左右角度设置
     */
    private void setDynamicShapeRECTANGLE(Context mContext, View view, float CornerRadius, int strokewidth, String strokeColor, String bgcolor) {
        GradientDrawable drawable = new GradientDrawable();
        //设置shape的形状
        drawable.setShape(GradientDrawable.RECTANGLE);

        //设置shape的圆角度数
        if (!StringUtils.isBlank(CornerRadius) && CornerRadius != -1) {
//            drawable.setCornerRadius(CornerRadius);
            if (ScreenTools.getInstance().isPad(mContext)) {
                float[] radius = {CornerRadius, CornerRadius, 0f, 0f, 0f,
                        0f, CornerRadius, CornerRadius};
                drawable.setCornerRadii(radius);
            } else {
                float[] radius = {CornerRadius, CornerRadius, CornerRadius, CornerRadius, 0f,
                        0f, 0f, 0f};
                drawable.setCornerRadii(radius);
            }
        }

        //设置shape的边的宽度和颜色
        if (!StringUtils.isBlank(strokewidth) && strokewidth != -1
                && !StringUtils.isBlank(strokeColor)) {
//            drawable.setStroke(strokewidth, ContextCompat.getColor(mContext, R.color.appblack));
            drawable.setStroke(strokewidth, Color.parseColor(strokeColor));
        }

        //设置shape的背景色
        if (!StringUtils.isBlank(bgcolor)) {
//            drawable.setColor(ContextCompat.getColor(mContext, bgcolor));
            drawable.setColor(Color.parseColor(bgcolor));
        }
        view.setBackground(drawable);
    }


    /**
     * 麦克风状态更新
     *
     * @param db
     */
    @Override
    public void onMicStatusUpdate(double db) {
//        LogUtils.i(ConfigConstants.TAG_ALL, "db =-= " + db);
//        int level = (int) db;
//        Drawable drawable = iv_mic.getDrawable();
//        drawable.setLevel((int) (3000 + 6000 * db / 100));

        long imageIndex = 1;

        if (db > 30) {
            imageIndex = Math.round((db - 20) / 10);
        }
        if (imageIndex >= 5) {
            imageIndex = 6;
        }
//        LogUtils.i(ConfigConstants.TAG_ALL, "imageIndex =-= " + imageIndex, "db =-= " + db);
        iv_mic.setImageDrawable(ContextCompat.getDrawable(mContext, arrayImageId[(int) imageIndex]));
    }

    /**
     * 开始录制的状态
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void startAudioRecordStatus(boolean isSuccess, String msg) {
        if (isSuccess) {
            cl_replay.setVisibility(View.GONE);
            cl_carryout.setVisibility(View.GONE);
            tv_myaudio.setVisibility(View.GONE);
            tv_carryout.setVisibility(View.GONE);


            record_starttime = System.currentTimeMillis();
            tv_time.setBase(SystemClock.elapsedRealtime());
            tv_time.start();
            tv_time.setVisibility(View.VISIBLE);

            tv_record_status_prompt.setText(msg);

            cp_mic_animation.reStart();
            cp_mic_animation.setVisibility(View.VISIBLE);

//            LogUtils.i(ConfigConstants.TAG_ALL, "=-= 录制开始 =-=");
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    /**
     * 结束录制状态
     *
     * @param isSuccess
     * @param msg
     */
    @Override
    public void endAudioRecordStatus(boolean isSuccess, String msg) {
        if (isSuccess) {
            cl_replay.setVisibility(View.VISIBLE);
            cl_carryout.setVisibility(View.VISIBLE);
            tv_myaudio.setVisibility(View.VISIBLE);
            tv_carryout.setVisibility(View.VISIBLE);


            tv_time.setBase(SystemClock.elapsedRealtime());
            tv_time.stop();
            tv_time.setText("00:00");
            tv_time.setVisibility(View.INVISIBLE);

            tv_record_status_prompt.setText(msg);

            iv_mic.setImageDrawable(ContextCompat.getDrawable(mContext, arrayImageId[0]));


            cp_mic_animation.stop();
            cp_mic_animation.setVisibility(View.INVISIBLE);

            //            LogUtils.i(ConfigConstants.TAG_ALL, "=-= 录制结束 =-=");
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    /**
     * Chronometer 录制计时器监听
     *
     * @param chronometer
     */
    @Override
    public void onChronometerTick(Chronometer chronometer) {
        long temp = System.currentTimeMillis();
        long diff = temp - record_starttime;
//        LogUtils.i(ConfigConstants.TAG_ALL, "diff =-= " + diff,
//                "DateUtil.stringForTime(diff) =-= " + DateUtil.stringForTime(diff));
//        audioRecordDuration = (diff / 1000);
        audioRecordDuration = diff;
        audioRecordTime = DateUtil.stringForTime(diff, 1);
//        LogUtils.i(ConfigConstants.TAG_ALL, "audioRecordTime =-= " + audioRecordTime, "audioRecordDuration =-= " + audioRecordDuration);
        tv_time.setText(DateUtil.stringForTime(diff, 1));
    }

    /**
     * 更新音频播放进度条
     *
     * @param currenttime
     * @param totalTime
     */
    @Override
    public void updateTimeStatus(int currenttime, int totalTime) {
//        LogUtils.i(ConfigConstants.TAG_ALL, "currenttime =-= " + currenttime, "totalTime =-= " + totalTime);
    }

    /**
     * 音频播放状态改变
     */
    @Override
    public void changePlayStatus() {
        if (AudioPlayManager.getInstance().getAUDIO_PLAY_STATUS() == ConfigConstants.AUDIO_STOP || AudioPlayManager.getInstance().getAUDIO_PLAY_STATUS() == ConfigConstants.AUDIO_PAUSE) {
            setReplayBG(false);
        } else {
            setReplayBG(true);
        }
    }

    @Override
    public void allPermissionsGranted(int mark) {
        if (mark == ConfigConstants.PERMISSIONS_GRANTED_STARTAUDIORECORD) {
            //开始录制
            AudioRecordManager.getInstance().startAudioRecord(mContext, "", ConfigConstants.RECORD_MAX_DURATION, this);
        }
    }

    /**
     * 权限拒绝 回调
     */
    @Override
    public void permissionsDenied(int mark) {

    }


    /**
     * 评星监听
     *
     * @param bindObject
     * @param RatingScore
     */
    @Override
    public void onRating(Object bindObject, int RatingScore) {
//        LogUtils.i(ConfigConstants.TAG_ALL, "RatingScore =-= " + RatingScore);


        setStarRating(RatingScore);
    }

    /**
     * 设置评星
     */
    private void setStarRating(int startcount) {
        //默认评分是3个星
        rbv_comment.setStar(startcount, false);
        ratingscore = startcount - 1;
        if (ratingscore == 3) {
            tv_comment_description.setText(R.string.homeworkcomment_comment_description_excellent);
        } else if (ratingscore == 1) {
            tv_comment_description.setText(R.string.homeworkcomment_comment_description_medium);
        } else if (ratingscore == 2) {
            tv_comment_description.setText(R.string.homeworkcomment_comment_description_good);
        } else if (ratingscore == 0) {
            tv_comment_description.setText(R.string.homeworkcomment_comment_description_failed);
        }
    }

    /**
     * 设置数据
     */
    public void setCommentDatas(StudentAvatarEntity studentAvatarEntitySelected, HomeworkStudentDetailEntity.Remark remark) {
        this.studentAvatarEntitySelected = studentAvatarEntitySelected;
        this.remark = remark;
        String content = String.format(mContext.getResources().getString(R.string.homeworkcomment_comment_title), studentAvatarEntitySelected.getStudentname());
        tv_comment_title.setText(content);

        //初始化快捷语数据
//        getshortcutDatas(studentAvatarEntitySelected.getSerial());
        getshortcutDatas(studentAvatarEntitySelected.getHomeworkid());

        //初始化点评数据
        if (!StringUtils.isBlank(remark)) {

            this.isFirstRemark = false;

            //初始化评分
            setStarRating(remark.getRank() + 1);
            //初始化评论语
            if (!StringUtils.isBlank(remark.getContent())) {
                et_comment_content.setText(remark.getContent());
            }

            //初始化音频文件
            if (!StringUtils.isBlank(remark.getResources()) && remark.getResources().size() > 0) {
                for (TKHomeworkResourceEntity entity : remark.getResources()) {
                    String str = entity.getUrl().toLowerCase();
                    if (str.endsWith("aac") || str.endsWith("mp3") || str.endsWith("wav")) {

                        audio = new AudioEntinty();
                        audio.setSource(entity.getSource());
                        audio.setId(entity.getId());
                        audio.setLocalFileName(entity.getName());
                        audio.setLocalFilePath(entity.getUrl());
                        audio.setDuration(entity.getDuration() * 1000L);
                        audio.setAudioRecordTime(DateUtil.stringForTime(entity.getDuration() * 1000L, 1));
                        showLocalAudio(audio);
                    } else if (str.endsWith("jpeg") || str.endsWith("jpg") || str.endsWith("png")) {
                        String mimeType = PictureMimeType.getMimeTypeFromMediaContentUri(mContext, Uri.parse(entity.getUrl()));

                        LocalMedia media = new TKLocalMedia();
                        media.setPath(entity.getUrl());
                        media.setRealPath(entity.getUrl());
                        media.setCompressed(true);
                        media.setCompressPath(entity.getUrl());
                        media.setDuration(0);
                        media.setMimeType(mimeType);
                        media.setId(Integer.parseInt(entity.getId()));

                        showLocalMedia(media);
                    }
                }

            }
        }
    }


    /**
     * 评论内容EditTextCustomize 内容变化监听
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 评论内容EditTextCustomize 内容变化监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 评论内容EditTextCustomize 内容变化监听
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (StringUtils.isBlank(s)) {
            iv_shortcut.setVisibility(View.INVISIBLE);
            tv_shortcut.setVisibility(View.INVISIBLE);
            if (jav_audio.getVisibility() != View.VISIBLE && mImgList.size() == 0) {
                setConfirmBtnBG(VariableConfig.color_button_unselected);
            }
        } else {
            setConfirmBtnBG(VariableConfig.color_button_selected);
            //搜索快捷语集合，如果编辑文字存在，就不显示保存快捷语提示
            if (!StringUtils.isBlank(expressions) && expressions.size() > 0) {
                for (String shortcut : expressions) {
                    if (shortcut.equals(s.toString())) {
                        return;
                    }
                }
            }
            iv_shortcut.setVisibility(View.VISIBLE);
            tv_shortcut.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 评论内容EditTextCustomize 焦点监听
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        LogUtils.i(ConfigConstants.TAG_ALL, "hasFocus =-= " + hasFocus);
    }

    /**
     * 设置快捷评语状态
     */
    private void setShortcutStatus() {
        if (HomeWorkCommentShortcutStatus) {
            HomeWorkCommentShortcutStatus = false;
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mContext, iv_shortcut, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120x), "#00000000");
            iv_shortcut.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.login_userprivacyagreement_unselected));
        } else {
            HomeWorkCommentShortcutStatus = true;
            PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mContext, iv_shortcut, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
            iv_shortcut.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.login_userprivacyagreement_selected));
        }
    }


    /**
     * 设置快捷语或键盘切换确定按钮的背景
     */
    private void setSelecteHideDetermineBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_selecte_hide, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_20x), -1, "", VariableConfig.color_button_selected);
    }

    /**
     * 设置提交确定按钮背景
     */
    private void setConfirmBtnBG(String color) {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_confirm, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", color);
    }

    /**
     * 设置提交确定按钮的高度
     */
    public void setConfirmBtnHeight(View view) {
        int height = 0;
        if (view.getVisibility() == View.VISIBLE) {
            height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_54x);
        } else {
            height = 0;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 获取软件盘的高度
     */
    //记录原始窗口高度
    private int mWindowHeight = 0;
    //软件盘的高度
    private int softKeyboardHeight = 0;
    //软键盘是否显示
    private boolean softinputisshow = false;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前窗口实际的可见区域
            window.getDecorView().getWindowVisibleDisplayFrame(r);
            int height = r.height();
//            LogUtils.i(ConfigConstants.TAG_ALL, "mWindowHeight =-= " + mWindowHeight, "height =-= " + height);

            if (mWindowHeight == 0) {
                //一般情况下，这是原始的窗口高度
                mWindowHeight = height;
                if (layoutParams.height > mWindowHeight) {
                    animator_padding = layoutParams.height - mWindowHeight;
                }
            } else {
                if (mWindowHeight == height) {//软件键盘隐藏
                    if (shortcut_softinput_model) {
                        disappearShortcutAndSoftinputmode();
                    }
                    softinputisshow = false;
//                    softKeyboardHeight = 0;
//                    LogUtils.i(ConfigConstants.TAG_ALL, "softinputisshow =-= " + softinputisshow, "softKeyboardHeight =-= " + softKeyboardHeight);
                } else {//软件键盘显示
                    softinputisshow = true;
                    //两次窗口高度相减，就是软键盘高度
                    softKeyboardHeight = mWindowHeight - height;

                    showSoftinputmode();

//                    LogUtils.i(ConfigConstants.TAG_ALL, "softinputisshow =-= " + softinputisshow, "softKeyboardHeight =-= " + softKeyboardHeight);
                }

            }
        }
    };

    /**
     * 显示快捷语
     */
    private void showShortcut() {
//        loopView.postDelayed(() -> {
//            setShortcutHeight(loopView, softKeyboardHeight);
//            loopView.setVisibility(View.VISIBLE);
//            cl_shortcut_softinputmode.setVisibility(View.VISIBLE);
//        }, 100);

        cl_audiorecord.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);


        if (ScreenTools.getInstance().isPad(mContext)) {
            if (phonemodel.equals("SM-T870")) {
                shortCutTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight - animator_padding);
            } else {
                shortCutTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight - animator_padding / 2 + BarHeight / 2);
            }
        } else {
            shortCutTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight - animator_padding);
        }


//        loopView.setVisibility(View.VISIBLE);
//        setShortcutHeight(loopView, softKeyboardHeight);
    }


    /**
     * 快捷语弹出动画
     *
     * @param view
     * @param translationdistance
     */
    private void shortCutTranslationYAnimator(View view, float translationdistance) {
        float y = view.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", y, 0);
        animator.setDuration(1);
        animator.setRepeatCount(0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();


        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                    LogUtils.i(ConfigConstants.TAG_ALL, "onAnimationEnd down=-= ");
                isShowSoftinputAnimator = true;

                setShortcutHeight(loopView, softKeyboardHeight);
                if (expressions.size() > 0) {
                    loopView.setVisibility(View.VISIBLE);
                } else {
                    loopView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 显示软键盘
     */
    //是否显示软件盘弹出动画
    private boolean isShowSoftinputAnimator = true;

    private void showSoftinputmode() {
//        setShortcutHeight(loopView, softKeyboardHeight);

        if (isShowSoftinputAnimator && shortcut_softinput_model) {
            cl_audiorecord.setVisibility(View.GONE);
            tv_confirm.setVisibility(View.GONE);

            if (ScreenTools.getInstance().isPad(mContext)) {
                if (phonemodel.equals("SM-T870")) {
                    softinputTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight + animator_padding);
                } else {
                    softinputTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight + animator_padding / 2 - BarHeight / 2);
                }
            } else {
                softinputTranslationYAnimator(cl_shortcut_softinputmode, softKeyboardHeight + animator_padding);
            }


//            if (expressions.size() > 0) {
//                cl_shortcut_softinputmode.setVisibility(View.VISIBLE);
//            }

            cl_shortcut_softinputmode.setVisibility(View.VISIBLE);
            loopView.setVisibility(View.GONE);
        }
    }


    /**
     * 软件盘弹出动画
     */
    //动画偏移补差
    int animator_padding = 0;

    private void softinputTranslationYAnimator(View view, float translationdistance) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0, -translationdistance);
        animator.setDuration(1);
        animator.setRepeatCount(0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                    LogUtils.i(ConfigConstants.TAG_ALL, "onAnimationEnd up=-= ");
                isShowSoftinputAnimator = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 快捷语和键盘切换模块  消失
     */
    private void disappearShortcutAndSoftinputmode() {
        loopView.setVisibility(View.GONE);
        cl_shortcut_softinputmode.setVisibility(View.GONE);

        if (jav_audio.getVisibility() != View.VISIBLE) {
            cl_audiorecord.setVisibility(View.VISIBLE);
        } else {
            cl_audiorecord.setVisibility(View.GONE);
        }

        tv_confirm.setVisibility(View.VISIBLE);

        isShowSoftinputAnimator = true;
    }

    /**
     * 设置快捷语控件高度
     */
    public void setShortcutHeight(View view, int height) {
//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.height = height;
//        view.setLayoutParams(layoutParams);
        ConstraintUtil.ConstraintBegin begin = constraintUtil.begin();
        begin.setHeight(R.id.loopView, height);
        begin.commit();
    }

    /**
     * 显示本地录音
     */
    private AudioEntinty audio;

    private void showLocalAudio(AudioEntinty audio) {
        if (!StringUtils.isBlank(audio)) {
            jav_audio.setJobAudioData(audio);
            jav_audio.setCurrentPosition(0);
            jav_audio.setVisibility(View.VISIBLE);
            cl_audiorecord.setVisibility(View.GONE);
            setConfirmBtnBG(VariableConfig.color_button_selected);


            //删除录音
            jav_audio.jobAudioDel(v -> {
                //删除录音文件
                if (FileUtils.isFileExists(audio.getLocalFilePath())) {
                    FileUtils.deleteFile(audio.getLocalFilePath());
                }
                jav_audio.closeAudio();
                jav_audio.setVisibility(View.GONE);
                cl_audiorecord.setVisibility(View.VISIBLE);

                this.audio = null;

                if (StringUtils.isBlank(et_comment_content.getText().toString()) || mImgList.size() == 0) {
                    setConfirmBtnBG(VariableConfig.color_button_unselected);
                }
            });
        }
    }

    /**
     * 快捷语数据
     */
    String test_shortcut[] = {"很优秀哦～", "作业完成的非常棒哦，请继续保持！", "完成的非常认真，给你点个赞！", "写的很赞，为你的努力鼓掌", "全班最棒", "你太优秀了"};

    private void getshortcutDatas(String homework_id) {
        List<String> list = new ArrayList<>();
        //添加默认数据
//        for (int i = 0; i < test_shortcut.length; i++) {
//            list.add(test_shortcut[i]);
//        }

        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.usefulExpressions(homework_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mContext, false, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        try {
                            String msg = apiResponseResponse.body().getMsg();
                            String data = apiResponseResponse.body().getData().toString();
//                            JSONObject jsonObject_data = new JSONObject(data);
//                            boolean exists = jsonObject_data.optBoolean("exists", false);
                            JSONArray jsonArray = new JSONArray(data);
                            if (!StringUtils.isBlank(jsonArray)) {
                                list.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String expression = jsonArray.getJSONObject(i).optString("expression");
                                    list.add(expression);
                                }
                            }
                            initshortcutDatas(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            initshortcutDatas(list);
                        }
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        initshortcutDatas(list);
                    }
                });

    }

    /**
     * 初始化快捷语数据
     */
    private void initshortcutDatas(List<String> expressionList) {
        if (expressionList.size() == 0) {//没有数据
            expressions.clear();
        } else {
            //全局数据赋值
            expressions.addAll(expressionList);

            /** 初始化快捷语的数据 **/
            //设置原始数据
            loopView.setItems(expressionList);
            //设置初始位置
            int start_index = ((expressionList.size() / 2) == 0) ? 0 : (expressionList.size() / 2) - 1;
            loopView.setInitPosition(start_index);
            //设置背景色
//            loopView.setPaintCenterTextBGColor(ContextCompat.getColor(mContext, R.color.appwhite));
            loopView.setDividerColor(ContextCompat.getColor(mContext, R.color.transparent));
            //设置文字边距
            loopView.setTextLeftPadding(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x));
            //点击监听
            loopView.setOnItemClickListenter(index -> {
                LogUtils.i(ConfigConstants.TAG_ALL, "index =-= " + index);
                shortcut_index = index;

                String shortcut_content = expressions.get(shortcut_index);
                //先获取输入框的值
                String et_comment_content1 = et_comment_content.getText().toString();
                et_comment_content.setText(et_comment_content1 + shortcut_content);
                et_comment_content.setFocusable(true);
                et_comment_content.setFocusableInTouchMode(true);
                et_comment_content.setSelection(et_comment_content.getText().toString().length());

                shortcut_softinput_model = true;
                iv_shortcut_softinputmode.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.homeworkcomment_shortcut));

                disappearShortcutAndSoftinputmode();
            });
        }
    }

    /**
     * 显示图片和视频
     */
    private void showLocalMedia(LocalMedia localMedia) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginEnd(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_12x));
        TKVideoImage videoImg = new TKVideoImage(mContext);
        videoImg.setLocalMedia(localMedia);
        videoImg.setLayoutParams(layoutParams);

        videoImg.setOnItemClickListener(new Function1<LocalMedia, Unit>() {
            @Override
            public Unit invoke(LocalMedia media) {
                // 打开预览页面
                openImgPreview(media);
                return null;
            }
        });


        //添加图片视图
        if (PictureConfig.TYPE_IMAGE == PictureMimeType.getMimeType(localMedia.getMimeType())) {
            mImgList.add(localMedia);
            mJobImageContainerLl.addView(videoImg);
        }

        mJobMediaContainerLl.setVisibility(View.VISIBLE);

        // 设置已选的图片
        List<LocalMedia> mVideoList = new ArrayList<>();
        mJobOperateView.setSelectedImgList(mImgList, mVideoList);
    }

    /**
     * 打开图片和视频的预览
     */
    private void openImgPreview(LocalMedia media) {
        if (mPictureParameterStyle == null) {
            mPictureParameterStyle = PictureParameterStyle.ofSelectTotalStyle();
            mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = isEditable;
        }

        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back_arrow;

        mineType = PictureMimeType.getMimeType(media.getMimeType());
        if (mineType == PictureConfig.TYPE_IMAGE) {
            int position = mImgList.indexOf(media);
            PictureSelector.create((Activity) mContext)
                    .setPictureStyle(mPictureParameterStyle)
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .openExternalPreview(position, mImgList);
        }
    }


    /**
     * 图片删除广播监听
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isBlank(action)) {
                return;
            }

            Bundle extras = intent.getExtras();

            if (!StringUtils.isBlank(extras)) {
                if (action.equals(BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)) {
                    if (mineType == PictureConfig.TYPE_IMAGE) {
                        int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                        mJobImageContainerLl.removeViewAt(position);
                        mImgList.remove(mImgList.get(position));

                        if (mImgList.size() == 0 &&
                                StringUtils.isBlank(et_comment_content.getText().toString()) &&
                                jav_audio.getVisibility() != View.VISIBLE) {
                            setConfirmBtnBG(VariableConfig.color_button_unselected);
                        }
                    }
                }
            }
        }
    };

    /**
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getRawX();
            int y = (int) ev.getRawY();
            if (shortcut_softinput_model) {//键盘模式
                View[] views = new View[4];
                views[0] = iv_shortcut_softinputmode;
                views[1] = et_comment_content;
                views[2] = cl_shortcut_softinputmode;
                views[3] = iv_close;
                if (!PublicPracticalMethodFromJAVA.getInstance().isTouchPointInView(views, x, y)) {
                    KeyBoardUtil.getInstance().hideKeyBoard(mContext, et_comment_content);
                }
            } else {//快捷语模式
                View[] views = new View[4];
                views[0] = iv_shortcut_softinputmode;
                views[1] = et_comment_content;
                views[2] = cl_shortcut_softinputmode;
                views[3] = iv_close;
                if (!PublicPracticalMethodFromJAVA.getInstance().isTouchPointInView(views, x, y)) {
                    et_comment_content.setFocusable(true);
                    et_comment_content.setFocusableInTouchMode(true);
                    disappearShortcutAndSoftinputmode();

                    shortcut_softinput_model = true;
                    iv_shortcut_softinputmode.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.homeworkcomment_shortcut));
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private OnRecordListener recordListener;

    public interface OnRecordListener {
        void onRecordComplete(AudioEntinty audio);

        void onConfirm(AudioRecordDialog dialog, AudioEntinty audio, List<LocalMedia> mImgList, TeacherCommentEntity teachercomment);
    }

    public void setOnRecordListener(OnRecordListener listener) {
        this.recordListener = listener;
    }
}
