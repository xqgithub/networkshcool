package com.talkcloud.networkshcool.baselibrary.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.talkcloud.corelibrary.TKJoinBackRoomModel;
import com.talkcloud.corelibrary.TKJoinRoomModel;
import com.talkcloud.corelibrary.TKSdkApi;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.adapter.LessonListForMainAdapter;
import com.talkcloud.networkshcool.baselibrary.adapter.OnRoomItemClickListener;
import com.talkcloud.networkshcool.baselibrary.base.BaseJoinRoomFragment;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.LessonFilesEntity;
import com.talkcloud.networkshcool.baselibrary.entity.LessonInfoEntity;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.presenters.LessonListPresenter;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.NormalListDialog;
import com.talkcloud.networkshcool.baselibrary.ui.webview.UserAgreementWebView;
import com.talkcloud.networkshcool.baselibrary.utils.AppPrefsUtil;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenUtil;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.views.LessonListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LessonListFragment extends BaseJoinRoomFragment implements LessonListView {

    private String dateTime;
    private TextView tvNodata;

    private LessonListForMainAdapter lessonListAdapter;
    private RecyclerView courseList;
    private RefreshLayout refreshLayout;
    List<LessonInfoEntity> list = new ArrayList<>();
    private boolean isFromMain = true; //用于判断是否是主页的课节列表，主页课节列表刷新 需要同步刷新最近日期接口，其他模块不需要

    private LessonListPresenter presenter;

    private boolean isAutoRefresh = true;

    public static LessonListFragment newInstance(String date) {
        LessonListFragment fg = new LessonListFragment();
        Bundle agrs = new Bundle();
        agrs.putString("date", date);
        fg.setArguments(agrs);
        return fg;
    }

    public static LessonListFragment newInstance(String date, boolean isFromMain) {
        LessonListFragment fg = new LessonListFragment();
        Bundle agrs = new Bundle();
        agrs.putString("date", date);
        agrs.putBoolean("isFromMain", isFromMain);
        fg.setArguments(agrs);
        return fg;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public int getLayoutId() {
        return R.layout.lesson_list_fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void initView() {
        if (getArguments() != null) {
            dateTime = getArguments().getString("date");
            isFromMain = getArguments().getBoolean("isFromMain", true);
        }
        refreshLayout = mView.findViewById(R.id.refreshLayout);
   /*     ClassicsHeader mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();
        mClassicsHeader = (ClassicsHeader) refreshLayout.getRefreshHeader();


//        mDrawableProgress = mClassicsHeader.getProgressView().getDrawable();
        Drawable mDrawableProgress = ((ImageView) mClassicsHeader.findViewById(ClassicsHeader.ID_IMAGE_PROGRESS)).getDrawable();
        if (mDrawableProgress instanceof LayerDrawable) {
            mDrawableProgress = ((LayerDrawable) mDrawableProgress).getDrawable(0);
        }*/

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (isFromMain) {
                    //发送检查身份的事件，todo 后期刷新方式可能会调整，这种有点low。无身份状态用户 就去检查是否有变化 否则无需检查
                    String identy = MySPUtilsUser.getInstance().getUserIdentity();
                    if (StringUtils.isBlank(identy)) {
                        if (!isAutoRefresh) {  //第一次进入主界面自动刷新 会和主MainMenuActivity 中resume 刷新方法冲突 导致token过期 退出
                            if (getActivity() != null) {
                                PublicPracticalMethodFromJAVA.getInstance().checkIdentity(getActivity());
                            }
                        }
                    } else {
                        //发送刷新事件,刷新近期课程 日期接口 如果有新课程，则重新加载
                        MessageEvent event = new MessageEvent();
                        event.setMessage_type(EventConstant.RECENTCOURSE_DATE_REFRESH);
                        EventBus.getDefault().post(event);

                        refresh(dateTime, true);
                    }
                }
            }
        });

        courseList = mView.findViewById(R.id.rv_course);
        tvNodata = mView.findViewById(R.id.tv_no_data);
        if (getActivity() != null) {
            lessonListAdapter = new LessonListForMainAdapter(getActivity(), list);
            presenter = new LessonListPresenter(this, getActivity());
        } else {
            return;
        }

        courseList.setLayoutManager(new LinearLayoutManager(getContext()));
        //  courseList.addItemDecoration(new SpaceItemDecoration(5));
        courseList.setAdapter(lessonListAdapter);

        presenter.getLessonList(dateTime);
        refreshLayout.autoRefresh();
        //  courseList.setNestedScrollingEnabled(false);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();

        // TODO 链接直接进教室
        String h5RoomId = AppPrefsUtil.getFormH5RoomId();

        NSLog.d("onResume h5RoomId " + h5RoomId);

        if (!TextUtils.isEmpty(h5RoomId)) {
            String roomId = h5RoomId;
            // 清空数据
            AppPrefsUtil.saveFormH5RoomId("");

            TKJoinRoomModel model = new TKJoinRoomModel();

            model.setUserId(AppPrefsUtil.getUserId());
            model.setNickName(AppPrefsUtil.getUserName());

            int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
            model.setUserRole(role);

            joinRoomPresenter.requestJoinRoom2(roomId, model);


        }
    }


    @Override
    public void initListener() {

        lessonListAdapter.setOnRoomItemClickListener(new OnRoomItemClickListener() {

            @Override
            public void onJoinRoom(String serialId, TKJoinRoomModel model) {
                if (!isJoiningRoom) {
                    isJoiningRoom = true;

                    if (model == null) {

                        model = new TKJoinRoomModel();

                        model.setUserId(AppPrefsUtil.getUserId());
                        model.setNickName(AppPrefsUtil.getUserName());

                        int role = AppPrefsUtil.getUserIdentity().equals(ConfigConstants.IDENTITY_TEACHER) ? 0 : 2;
                        model.setUserRole(role);

                    }

                    joinRoomPresenter.requestJoinRoom(serialId, model);
                }
            }


            @Override
            public void onJoinPlayBackRoom(String serialId) {
                joinRoomPresenter.requestJoinPlaybackRoom(serialId);
            }

            @Override
            public void onLessonPreparation(String serialId) {
                presenter.getLessonFiles(serialId);
            }
        });
    }


    @Override
    public void joinRoom(@NotNull TKJoinRoomModel joinRoomModel) {

        // 1未开始 2进行中 3已结束 4已过期
//        if ("3".equals(joinRoomModel.getState())) {
//
//        } else if ("4".equals(joinRoomModel.getState())) {
//
//        }

        AppPrefsUtil.saveFormH5RoomId("");

        Log.d("goyw", "joinRoom : " + joinRoomModel.toString());

        mSerialId = joinRoomModel.getSerialId();

        TKSdkApi.joinRoom(mActivity, joinRoomModel);
    }


    @Override
    public void joinPlaybackRoom(@NotNull List<? extends TKJoinBackRoomModel> backRoomModelList) {
        if (backRoomModelList.isEmpty()) {
//            ToastsKt.longToast(mActivity, R.string.join_playback_room_desc);
            ToastUtils.showShortTop(getString(R.string.join_playback_room_desc));
            return;
        }
//        Log.d("goyw", backRoomModelList.get(0).toString());

        // 只有一个直接进入
        if (backRoomModelList.size() == 1) {

            TKSdkApi.joinPlayBackRoom(mActivity, backRoomModelList.get(0));
        } else {

            String[] nameArr = new String[backRoomModelList.size()];
            for (int i = 0; i < backRoomModelList.size(); i++) {
                nameArr[i] = getString(R.string.reply) + (i + 1);
            }
            // 弹出列表
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(getString(R.string.join_playback_list_title)).setItems(nameArr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TKSdkApi.joinPlayBackRoom(mActivity, backRoomModelList.get(which));
                }
            });
            builder.create().show();
        }
    }

    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lesson_list_fragment, container, false);
//
//        courseList = view.findViewById(R.id.rv_course);
//        tvNodata = view.findViewById(R.id.tv_no_data);
//        courseListAdapter = new LessonListForMainAdapter(getActivity(), list);
//        courseList.setLayoutManager(new LinearLayoutManager(getContext()));
//        courseList.addItemDecoration(new SpaceItemDecoration(30));
//        courseList.setAdapter(courseListAdapter);
//        presenter = new LessonListPresenter(this, getActivity());
//        presenter.getLessonList(dateTime);
//
//        initListener();
//
//        return view;
//    }


    @Override
    public void lessonListCallback(boolean isSuccess, Object data) {
        refreshLayout.finishRefresh();

        if (isSuccess) {
            if (data != null) {
                list.clear();
                list.addAll((List<LessonInfoEntity>) data);
                if (((List<LessonInfoEntity>) data).size() > 0) {
                    //保存userid和nickname
                    MySPUtilsUser.getInstance().saveUserID(((List<LessonInfoEntity>) data).get(0).getUser().getUserid());
                    MySPUtilsUser.getInstance().saveNickName(((List<LessonInfoEntity>) data).get(0).getUser().getNickname());
//                    List<LessonInfoEntity> entity = (List<LessonInfoEntity>) data;
//                    for (int i = 0; i < entity.size(); i++) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "userid =-= " + entity.get(i).getUser().getUserid(),
//                                "nickname =-= " + entity.get(i).getUser().getNickname());
//                    }

                    //多添加一个item 用于显示底部没有数据
                    LessonInfoEntity info = new LessonInfoEntity();
                    list.add(info);
                }
            }

            tvNodata.setVisibility(View.GONE);

            if (list != null && list.size() > 0) {
                lessonListAdapter.notifyDataSetChanged();
            } else {

                if (getActivity() != null && !isFromMain && !ScreenTools.getInstance().isPad(getActivity())) { //在课程表界面
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tvNodata.getLayoutParams();
                    lp.topMargin = ScreenUtil.dp2px(getActivity(), 70);
                    tvNodata.setLayoutParams(lp);
                }
                tvNodata.setVisibility(View.VISIBLE);
            }
        } else {
            if (getActivity() != null && !isFromMain && !ScreenTools.getInstance().isPad(getActivity())) { //在课程表界面
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tvNodata.getLayoutParams();
                lp.topMargin = ScreenUtil.dp2px(getActivity(), 70);
                tvNodata.setLayoutParams(lp);
            }
            tvNodata.setVisibility(View.VISIBLE);
        }

        isAutoRefresh = false;
    }

    @Override
    public void lessonFilesCallback(boolean isSuccess, Object data, String msg) {
        if (isSuccess) {
            List<LessonFilesEntity> lessonFilesEntities = (List<LessonFilesEntity>) data;
            if (lessonFilesEntities.size() > 0) {
                if (lessonFilesEntities.size() == 1) {
                    //只有一个预览文件，直接展示
                    LessonFilesEntity lessonFilesEntity = lessonFilesEntities.get(0);
                    //跳转到webView
                    Bundle bundle = new Bundle();
                    bundle.putString("title", lessonFilesEntity.getFilename());
                    bundle.putString("linkurl", lessonFilesEntity.getPreview_url());
                    bundle.putInt("type", ConfigConstants.LESSONPREVIEW);
                    PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
                } else {
                    //有多个预览文件，显示展示弹框
                    NormalListDialog normalListDialog;
                    if (ScreenTools.getInstance().isPad(mActivity)) {
                        normalListDialog = new NormalListDialog(mActivity, R.style.dialog_style, WindowManager.LayoutParams.MATCH_PARENT);
                    } else {
                        normalListDialog = new NormalListDialog(mActivity);
                    }
                    normalListDialog.setItemContentList(lessonFilesEntities);
                    normalListDialog.setSelectItemClickListener(o -> {
                        LessonFilesEntity lessonFilesEntity = (LessonFilesEntity) o;
                        //跳转到webView
                        Bundle bundle = new Bundle();
                        bundle.putString("title", lessonFilesEntity.getFilename());
                        bundle.putString("linkurl", lessonFilesEntity.getPreview_url());
//                            bundle.putString("linkurl", lessonFilesEntity.getDownload_url());
                        bundle.putInt("type", ConfigConstants.LESSONPREVIEW);
                        PublicPracticalMethodFromJAVA.getInstance().intentToJump(mActivity, UserAgreementWebView.class, -1, bundle, false, R.anim.activity_right_in, R.anim.activity_xhold);
                        return null;
                    });
                    normalListDialog.show();
                }
            } else {
                String user_identity = MySPUtilsUser.getInstance().getUserIdentity();
                String content = "";
                if (user_identity.equals(ConfigConstants.IDENTITY_TEACHER)) {
                    content = mActivity.getResources().getString(R.string.lesson_preparation);
                } else {
                    content = mActivity.getResources().getString(R.string.lesson_preview);
                }
                ToastUtils.showShortToastFromText(mActivity.getResources().getString(R.string.lesson_preview_preparation_no_data, content), ToastUtils.top);
            }
        } else {
            ToastUtils.showShortToastFromText(msg, ToastUtils.top);
        }
    }

    public void refresh(String time, boolean isAutoRefresh) {
        if (isAutoRefresh) {
            refreshLayout.autoRefresh();
        }
        dateTime = time;
//        list.clear();
        lessonListAdapter.cancelAllTimers();
//        lessonListAdapter.notifyDataSetChanged();
        presenter.getLessonList(time);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lessonListAdapter != null) {
            lessonListAdapter.cancelAllTimers();
        }
    }


    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // 作业发布成功 刷新数据  TODO 这里有几个tab 就好请求几次
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handlerData(MessageEvent messageEvent) {

        NSLog.d("event type : " + messageEvent.getMessage_type());

        if (EventConstant.EVENT_PUBLISH_HOMEWORK_SUCCESS.equals(messageEvent.getMessage_type())) {

            // 有当前课节的tab 才刷新
            Map<String, String> map = (Map<String, String>) messageEvent.getMessage();
            String lessonId = map.get("lessonId");

            LessonInfoEntity entity = new LessonInfoEntity(lessonId);

//            NSLog.d("list : " + list);

            if (list.contains(entity)) {
                NSLog.d("event type lessonId: " + lessonId);
                refresh(dateTime, false);
            }
        } else if (messageEvent.getMessage_type().equals(EventConstant.CHANGE_IDENTITY_REFRESH) ||
                messageEvent.getMessage_type().equals(EventConstant.NO_IDENTITY_REFRESH)) {//1. check身份后刷新接口, 2.无身份刷新
            VariableConfig.checkIdentityFlag = false;
            presenter.getLessonList(dateTime);
        }
    }
}
