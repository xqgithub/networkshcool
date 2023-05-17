package com.talkcloud.networkshcool.baselibrary.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.EventConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.MessageEvent;
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity;
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskPageEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsNDHRecord;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.NetworkDiskAdapter;
import com.talkcloud.networkshcool.baselibrary.ui.adapter.NetworkDiskHistoryRecordAdapter;
import com.talkcloud.networkshcool.baselibrary.utils.KeyBoardUtil;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.talkcloud.networkshcool.baselibrary.weiget.EditTextCustomize;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.AutoSizeConfig;
import retrofit2.Response;

/**
 * Date:2021/6/30
 * Time:14:08
 * author:joker
 * 企业网盘
 */
public class NetworkDiskDialog extends Dialog implements NetworkDiskAdapter.NetworkDiskAdapterListener, NetworkDiskHistoryRecordAdapter.NetworkDiskHistoryRecordAdapterListener, View.OnClickListener {

    private WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private View mDialogView;
    private Window window;

    private ConstraintLayout cl_dialog;
    private ImageView iv_close;
    private TextView tv_title;
    private TextView tv_confirm;
    private EditTextCustomize et_search;

    //文件列表
    private RecyclerView rv_networkdisk;
    private NetworkDiskAdapter networkDiskAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RefreshLayout refreshLayout;

    //空数据
    private LinearLayout rootEmptyLy;
    private TextView tv_empty_view_text;

    //历史记录
    private RecyclerView rv_historyrecord;
    private NetworkDiskHistoryRecordAdapter networkDiskHistoryRecordAdapter;
    private LinearLayoutManager linearLayoutManager_historyRecord;
    private ImageView iv_historyrecord_close;
    private TextView tv_historyrecord_title;

    //网盘数据集合
    private List<NetworkDiskEntity.DataBean> networkDiskBeans;
    //企业网盘被选择的数据集合(文件)
    private List<NetworkDiskEntity.DataBean> networkDiskBeansSelected;
    //网盘搜索数据
    private List<NetworkDiskEntity.DataBean> searchnetworkDiskBeans;
    //图片类型最大数
    private int max_num_pics = 9;
    //视频类型最大数
    private int max_num_videos = 3;
    //音频类型最大数
    private int max_num_audios = 3;
    //文件类型最大数
    private int max_num_files = 3;
    //网盘可以选择的文件的总数
    private int total_files = -1;


    //企业网盘 数据状态  正常；搜索
    private int networkDiskBeansStatus = ConfigConstants.NETWORKDISKBEANS_NORMAL;

    private Map<String, NetworkDiskPageEntity> map_page;

    //网盘索引标识
    private String index = "-1111111";
    //课件ID
    private String lesson_id;
    //目录ID
    private String dir_id;
    //搜索文字内容
    private String keywords;
    //每页显示多少行
    private int per_page = 15;

    //弹框动画默认值
    private int windowanimations = R.style.AnimBottom;


    public NetworkDiskDialog(Context context) {
        super(context, R.style.dialog_style);
        this.mContext = context;
        this.window = getWindow();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initUIView();
        initData();
        initListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        layoutParams = window.getAttributes();

        if (ScreenTools.getInstance().isPad(mContext)) {
            layoutParams.gravity = Gravity.RIGHT;
            window.setWindowAnimations(windowanimations);  //添加动画
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_395x);

            int height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = height;

            //隐藏导航栏
            PublicPracticalMethodFromJAVA.getInstance().hideNavigationBar(window);
        } else {
            layoutParams.gravity = Gravity.BOTTOM;
            window.setWindowAnimations(windowanimations);  //添加动画
            layoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_375x);

//            int height = mContext.getResources().getDimensionPixelSize(R.dimen.dimen_748x);
            int height = (int) (0.92 * AutoSizeConfig.getInstance().getScreenHeight());
            layoutParams.height = height;
        }
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setAttributes(layoutParams);


        //获取企业网盘的数据
        getResourceDatas(lesson_id);
    }

    /**
     * 初始化UI
     */
    private void initUIView() {
        mDialogView = View.inflate(mContext, R.layout.dailog_networkdisk, null);
        setContentView(mDialogView);

        cl_dialog = mDialogView.findViewById(R.id.cl_dialog);
        iv_close = mDialogView.findViewById(R.id.iv_close);
        et_search = mDialogView.findViewById(R.id.et_search);
        rv_networkdisk = mDialogView.findViewById(R.id.rv_networkdisk);
        rootEmptyLy = mDialogView.findViewById(R.id.rootEmptyLy);
        tv_empty_view_text = mDialogView.findViewById(R.id.tv_empty_view_text);
        tv_title = mDialogView.findViewById(R.id.tv_title);
        tv_confirm = mDialogView.findViewById(R.id.tv_confirm);
        refreshLayout = mDialogView.findViewById(R.id.refreshLayout);
        rv_historyrecord = mDialogView.findViewById(R.id.rv_historyrecord);
        iv_historyrecord_close = mDialogView.findViewById(R.id.iv_historyrecord_close);
        tv_historyrecord_title = mDialogView.findViewById(R.id.tv_historyrecord_title);


        tv_empty_view_text.setTextSize(14);
        tv_empty_view_text.setText(R.string.emptydata_text);

        setDialogBG();
        setEtSearchBG();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        networkDiskBeans = new ArrayList<>();
        networkDiskBeansSelected = new ArrayList<>();
        searchnetworkDiskBeans = new ArrayList<>();
        map_page = new HashMap<>();

        total_files = max_num_pics + max_num_videos + max_num_audios + max_num_files;


        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);

        //初始化文件列表RecyclerView
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_networkdisk.setLayoutManager(linearLayoutManager);
        networkDiskAdapter = new NetworkDiskAdapter((Activity) mContext);
        networkDiskAdapter.setNetworkDiskAdapterListener(this);
        networkDiskAdapter.setHasStableIds(true);
        rv_networkdisk.getItemAnimator().setChangeDuration(0);
        rv_networkdisk.setAdapter(networkDiskAdapter);

        //初始化历史记录RecyclerView
        linearLayoutManager_historyRecord = new LinearLayoutManager(mContext);
        linearLayoutManager_historyRecord.setOrientation(LinearLayoutManager.VERTICAL);
        rv_historyrecord.setLayoutManager(linearLayoutManager_historyRecord);
        networkDiskHistoryRecordAdapter = new NetworkDiskHistoryRecordAdapter((Activity) mContext, this);
        rv_historyrecord.setAdapter(networkDiskHistoryRecordAdapter);
        rv_historyrecord.setVisibility(View.GONE);
        iv_historyrecord_close.setVisibility(View.GONE);
        tv_historyrecord_title.setVisibility(View.GONE);

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //关闭按钮点击
        iv_close.setOnClickListener(this);
        tv_title.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        iv_historyrecord_close.setOnClickListener(this);


        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore(refreshLayout, map_page);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });

        et_search.setOnEditTextListener(new EditTextCustomize.onEditTextListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                LogUtils.i(ConfigConstants.TAG_ALL, "hasFocus =-=" + hasFocus);
                if (hasFocus) {
                    showNetworkDiskHistoryRecordDatas();
                }
            }
        });

        //软键盘搜索监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    LogUtils.i(ConfigConstants.TAG_ALL, "我要开始搜索 =-=" + v.getText().toString().trim());
                    searchNetworkDiskDatas(v.getText().toString().trim());
                }
                return false;
            }
        });

        //RecyclerView滑动监听
//        rv_networkdisk.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });
    }

    private NetworkDiskDialogListener networkDiskDialogListener;

    public void setNetworkDiskDialogListener(NetworkDiskDialogListener networkDiskDialogListener) {
        this.networkDiskDialogListener = networkDiskDialogListener;
    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
//            networkDiskBeansSelected.clear();
            dismissDialog();
        } else if (id == R.id.tv_confirm) {
            eventBusConfirmData();
        } else if (id == R.id.iv_historyrecord_close) {
            networkDiskBeansStatus = ConfigConstants.NETWORKDISKBEANS_NORMAL;
            et_search.setText("");
            this.keywords = "";

            if (networkDiskBeans.size() > 0) {
                networkDiskAdapter.setDatas(networkDiskBeans);
                networkDiskAdapter.notifyDataSetChanged();
                rootEmptyLy.setVisibility(View.GONE);
            } else {
                getResourceDatas(lesson_id);
            }
            searchnetworkDiskBeans.clear();
            closeNetworkDiskHistoryRecordDatas();
            iv_historyrecord_close.setVisibility(View.GONE);
        }
    }


    /**
     * 设置弹框的背景
     */
    private void setDialogBG() {
        setDynamicShapeRECTANGLE(mContext, cl_dialog, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
    }

    /**
     * 设置搜索栏的背景
     */
    private void setEtSearchBG() {
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, et_search, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
    }


    /**
     * 设置确定按钮
     */
    private void setConfirm(boolean isvisible) {
        //背景色
        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeRECTANGLE(mContext, tv_confirm, mContext.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);
        if (networkDiskBeansSelected.size() > 0) {
            String content = String.format(mContext.getResources().getString(R.string.networkdisk_confirm), networkDiskBeansSelected.size() + "", total_files + "");
            tv_confirm.setText(content);
            if (isvisible) {
                tv_confirm.setVisibility(View.VISIBLE);
            } else {
                tv_confirm.setVisibility(View.GONE);
            }
        } else {
            tv_confirm.setVisibility(View.GONE);
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
     * 设置初始数据
     *
     * @param lesson_id                课节id
     * @param networkDiskBeansSelected 企业网盘被选择数据
     */
    public void setInitDatas(String lesson_id, List<NetworkDiskEntity.DataBean> networkDiskBeansSelected,
                             int pics_selected, int videos_selected, int audios_selected, int files_selected) {
//        total_files = (max_num_pics - pics_selected) + (max_num_videos - videos_selected) + (max_num_audios - audios_selected) + (max_num_files - files_selected);
        Map<String, Integer> map = getFilesNums(networkDiskBeansSelected);
        //设置各种文件可以选择的最大数量
        max_num_pics = max_num_pics - pics_selected + map.get(ConfigConstants.NETWORKDISK_PICS_SELECTED);
        max_num_videos = max_num_videos - videos_selected + map.get(ConfigConstants.NETWORKDISK_VIDEOS_SELECTED);
        max_num_audios = max_num_audios - audios_selected + map.get(ConfigConstants.NETWORKDISK_AUDIOS_SELECTED);
        max_num_files = max_num_files - files_selected + map.get(ConfigConstants.NETWORKDISK_FILES_SELECTED);
        total_files = max_num_pics + max_num_videos + max_num_audios + max_num_files;

        this.lesson_id = lesson_id;

        if (ScreenTools.getInstance().isPad(mContext)) {
            this.windowanimations = R.style.AnimRight;
        } else {
            this.windowanimations = R.style.AnimBottom;
        }

        // TODO 使用添加进集合 防止指向同一个引用
        if (!StringUtils.isBlank(networkDiskBeansSelected) && networkDiskBeansSelected.size() > 0) {
            for (NetworkDiskEntity.DataBean data : networkDiskBeansSelected) {
                this.networkDiskBeansSelected.add(data);
            }
//            this.networkDiskBeansSelected.addAll(networkDiskBeansSelected);
        }

        this.dir_id = "";
        tv_title.setText(R.string.networkdisk_title);
        iv_close.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.countryarea_close));


        //初始化请求的页数数据
        NetworkDiskPageEntity networkDiskPageEntity = new NetworkDiskPageEntity();
        networkDiskPageEntity.setCurrent_page(1);
        networkDiskPageEntity.setPer_page(per_page);
        networkDiskPageEntity.setTotal(1);
        networkDiskPageEntity.setLast_page(1);
        networkDiskPageEntity.setDir_id(dir_id);
        map_page.put(index, networkDiskPageEntity);
    }


    /**
     * 设置进入文件夹数据
     *
     * @param lesson_id                课节id
     * @param dataBean                 被选中的企业网盘数据(文件夹)
     * @param networkDiskBeansSelected 被选中的企业网盘数据(文件)
     * @param windowanimations         弹框动画
     */
    private void setDirDatas(String lesson_id, NetworkDiskEntity.DataBean dataBean, List<NetworkDiskEntity.DataBean> networkDiskBeansSelected, int windowanimations,
                             int max_num_pics, int max_num_videos, int max_num_audios, int max_num_files, int total_files) {

        this.max_num_pics = max_num_pics;
        this.max_num_videos = max_num_videos;
        this.max_num_audios = max_num_audios;
        this.max_num_files = max_num_files;
        this.total_files = total_files;


        this.lesson_id = lesson_id;
        this.windowanimations = windowanimations;

        this.dir_id = dataBean.getId();
        tv_title.setText(dataBean.getName());
        iv_close.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.vcodeinput_return));

        this.networkDiskBeansSelected.addAll(networkDiskBeansSelected);


        //初始化请求的页数数据
        NetworkDiskPageEntity networkDiskPageEntity = new NetworkDiskPageEntity();
        networkDiskPageEntity.setCurrent_page(1);
        networkDiskPageEntity.setPer_page(per_page);
        networkDiskPageEntity.setTotal(1);
        networkDiskPageEntity.setLast_page(1);
        networkDiskPageEntity.setDir_id(dir_id);
        map_page.put(index, networkDiskPageEntity);


//        if (windowanimations != -1) {
//            this.windowanimations = windowanimations;
//        } else {
//            if (ScreenTools.getInstance().isPad(mContext)) {
//                this.windowanimations = R.style.AnimRight;
//            } else {
//                this.windowanimations = R.style.AnimBottom;
//            }
//        }


//        if (!StringUtils.isBlank(dataBean)) {
//            this.dir_id = dataBean.getId();
//            tv_title.setText(dataBean.getName());
//            iv_close.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.vcodeinput_return));
//        } else {
//            this.dir_id = "";
//            tv_title.setText(R.string.networkdisk_title);
//            iv_close.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.countryarea_close));
//        }

//        if (!StringUtils.isBlank(networkDiskBeansSelected)) {
//            this.networkDiskBeansSelected.addAll(networkDiskBeansSelected);
//        }
    }


    /**
     * 获取企业网盘的数据
     *
     * @param lesson_id 课件id
     */
    private void getResourceDatas(String lesson_id) {
        Map<String, Object> options = new HashMap<>();
        options.put("page", map_page.get(index).getCurrent_page());
        options.put("rows", map_page.get(index).getPer_page());
        options.put("lesson_id", lesson_id);
        if (!StringUtils.isBlank(map_page.get(index).getDir_id())) options.put("dir_id", map_page.get(index).getDir_id());
        if (!StringUtils.isBlank(keywords)) options.put("keywords", keywords);


        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.TEACHERS_STUDENTS, VariableConfig.V1);
        apiService.resource(options)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse<NetworkDiskEntity>>>(mContext, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse<NetworkDiskEntity>> apiResponseResponse) {
                        try {
                            String msg = apiResponseResponse.body().getMsg();
                            String data = apiResponseResponse.body().getData().toString();
                            int result = apiResponseResponse.body().getResult();
                            if (0 == result) {
                                if (!StringUtils.isBlank(apiResponseResponse.body().getData())) {
                                    List<NetworkDiskEntity.DataBean> networkDiskEntities = apiResponseResponse.body().getData().getData();
                                    if (!StringUtils.isBlank(networkDiskEntities) && networkDiskEntities.size() > 0) {

                                        //1.给网盘数据页数类赋值
                                        NetworkDiskPageEntity networkDiskPageEntity = new NetworkDiskPageEntity();
                                        networkDiskPageEntity.setCurrent_page(apiResponseResponse.body().getData().getCurrent_page());
                                        networkDiskPageEntity.setPer_page(apiResponseResponse.body().getData().getPer_page());
                                        networkDiskPageEntity.setTotal(apiResponseResponse.body().getData().getTotal());
                                        networkDiskPageEntity.setLast_page(apiResponseResponse.body().getData().getLast_page());
                                        map_page.put(index, networkDiskPageEntity);

                                        if (networkDiskBeansStatus == ConfigConstants.NETWORKDISKBEANS_NORMAL) {
                                            //集合总数据
                                            networkDiskBeans.addAll(networkDiskEntities);
                                            networkDiskAdapter.setDatas(networkDiskBeans);
                                        } else {
                                            searchnetworkDiskBeans.addAll(networkDiskEntities);
                                            networkDiskAdapter.setDatas(searchnetworkDiskBeans);
                                        }
                                        networkDiskAdapter.setDatas2(networkDiskBeansSelected);
                                        networkDiskAdapter.notifyDataSetChanged();

                                        closeNetworkDiskHistoryRecordDatas();

                                        refreshLayout.setEnableLoadMore(true);
                                        rootEmptyLy.setVisibility(View.GONE);
                                        setConfirm(true);
                                        return;
                                    }
                                }
                            }
                            closeNetworkDiskHistoryRecordDatas();
                            rootEmptyLy.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            closeNetworkDiskHistoryRecordDatas();
                            rootEmptyLy.setVisibility(View.VISIBLE);
                        }
                        setConfirm(false);
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
//                        LogUtils.i(ConfigConstants.TAG_ALL, "error_code =-= " + error_code, "message =-= " + message);
                        closeNetworkDiskHistoryRecordDatas();
                        rootEmptyLy.setVisibility(View.VISIBLE);
                    }
                });
    }


    /**
     * 加载更多数据
     */
    private void loadMore(RefreshLayout refreshLayout, Map<String, NetworkDiskPageEntity> map_page) {
        int current_page = map_page.get(index).getCurrent_page();
        int total = map_page.get(index).getTotal();
        if (networkDiskBeans.size() < total) {
            current_page = current_page + 1;
            NetworkDiskPageEntity networkDiskPageEntity = new NetworkDiskPageEntity();
            networkDiskPageEntity.setCurrent_page(current_page);
            networkDiskPageEntity.setPer_page(per_page);
            networkDiskPageEntity.setTotal(1);
            networkDiskPageEntity.setLast_page(1);
            networkDiskPageEntity.setDir_id(dir_id);
            this.map_page.put(index, networkDiskPageEntity);
            getResourceDatas(lesson_id);
            refreshLayout.finishLoadMore();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }


    /**
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void EventBusNetworkDiskBeansSelectedMessage(MessageEvent messageEvent) {
        if (EventConstant.EVENT_NETWORKDISKBEANSSELECTED == messageEvent.getMessage_type()) {
            networkDiskBeansSelected = ((List<NetworkDiskEntity.DataBean>) messageEvent.getMessage());
            setConfirm(true);

            networkDiskAdapter.setDatas2(networkDiskBeansSelected);
            networkDiskAdapter.notifyDataSetChanged();
        } else if (EventConstant.EVENT_NETWORKDISKCONFIRM == messageEvent.getMessage_type()) {
            if (!StringUtils.isBlank(networkDiskDialogListener)) {
                Map<String, Integer> map = getFilesNums(networkDiskBeansSelected);
                networkDiskDialogListener.onNetworkDiskConfirmCallback(networkDiskBeansSelected,
                        map.get(ConfigConstants.NETWORKDISK_PICS_SELECTED),
                        map.get(ConfigConstants.NETWORKDISK_VIDEOS_SELECTED),
                        map.get(ConfigConstants.NETWORKDISK_AUDIOS_SELECTED),
                        map.get(ConfigConstants.NETWORKDISK_FILES_SELECTED)
                );
            }
            dismissDialog();
        }
    }

    /**
     * 推送数据
     */
    private void eventBusPostData(List<NetworkDiskEntity.DataBean> networkDiskBeansSelected) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMessage_type(EventConstant.EVENT_NETWORKDISKBEANSSELECTED);
        messageEvent.setMessage(networkDiskBeansSelected);
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 提交数据
     */
    private void eventBusConfirmData() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMessage_type(EventConstant.EVENT_NETWORKDISKCONFIRM);
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 显示企业网盘搜索历史记录数据
     */
    private void showNetworkDiskHistoryRecordDatas() {
        List<String> networkDiskHistorys = MySPUtilsNDHRecord.getInstance().getRecordDatas();
        networkDiskHistoryRecordAdapter.setDatas(networkDiskHistorys);
        networkDiskHistoryRecordAdapter.notifyDataSetChanged();
        rv_historyrecord.setVisibility(View.VISIBLE);
        iv_historyrecord_close.setVisibility(View.VISIBLE);
        tv_historyrecord_title.setVisibility(View.VISIBLE);

    }

    /**
     * 关闭企业网盘搜索历史记录数据
     */
    private void closeNetworkDiskHistoryRecordDatas() {
        rv_historyrecord.setVisibility(View.GONE);
        tv_historyrecord_title.setVisibility(View.GONE);

        //隐藏软键盘
        KeyBoardUtil.getInstance().hideKeyBoard(mContext, et_search);
    }

    /**
     * 搜索网盘数据
     */
    private void searchNetworkDiskDatas(String keywords) {
        networkDiskBeansStatus = ConfigConstants.NETWORKDISKBEANS_SEARCH;

        NetworkDiskPageEntity networkDiskPageEntity = new NetworkDiskPageEntity();
        networkDiskPageEntity.setCurrent_page(1);
        networkDiskPageEntity.setPer_page(per_page);
        networkDiskPageEntity.setTotal(1);
        networkDiskPageEntity.setLast_page(1);
        networkDiskPageEntity.setDir_id(dir_id);
        this.map_page.put(index, networkDiskPageEntity);
        this.keywords = keywords;
        if (!StringUtils.isBlank(keywords)) {

            MySPUtilsNDHRecord.getInstance().saveRecordName(keywords, keywords);
        }
        searchnetworkDiskBeans.clear();
        getResourceDatas(lesson_id);
    }

    /**
     * 搜索历史记录item点击回调
     *
     * @param keywords
     */
    @Override
    public void historyRecordItemOnclick(String keywords) {
        searchNetworkDiskDatas(keywords);
    }


    /**
     * 根据 networkDiskBeansSelected 获取已经选择的图片数量、视频数量、音频数量、文件数量
     */
    private Map<String, Integer> getFilesNums(List<NetworkDiskEntity.DataBean> mNetworkDiskBeansSelected) {
        int pics_selected = 0;
        int videos_selected = 0;
        int audios_selected = 0;
        int files_selected = 0;
        Map<String, Integer> map_files = new HashMap<>();
        for (NetworkDiskEntity.DataBean dataBean : mNetworkDiskBeansSelected) {
            if (dataBean.getType().equals("jpg") || dataBean.getType().equals("jpeg") || dataBean.getType().equals("png")) {//图片
                pics_selected++;
            } else if (dataBean.getType().equals("mp4")) {//视频
                videos_selected++;
            } else if (dataBean.getType().equals("mp3")) {//音频
                audios_selected++;
            } else if (dataBean.getType().equals("xls") || dataBean.getType().equals("xlsx") ||
                    dataBean.getType().equals("txt") ||
                    dataBean.getType().equals("doc") || dataBean.getType().equals("docx") ||
                    dataBean.getType().equals("ppt") || dataBean.getType().equals("pptx") ||
                    dataBean.getType().equals("pdf")) {//文件
                files_selected++;
            }
        }
        map_files.put(ConfigConstants.NETWORKDISK_PICS_SELECTED, pics_selected);
        map_files.put(ConfigConstants.NETWORKDISK_VIDEOS_SELECTED, videos_selected);
        map_files.put(ConfigConstants.NETWORKDISK_AUDIOS_SELECTED, audios_selected);
        map_files.put(ConfigConstants.NETWORKDISK_FILES_SELECTED, files_selected);
        return map_files;
    }


    /**
     * 关闭弹框
     */
    public void dismissDialog() {
        if (isShowing()) {

            if (!StringUtils.isBlank(networkDiskBeans) && networkDiskBeans.size() > 0) {
                networkDiskBeans.clear();
            }
            if (!StringUtils.isBlank(map_page) && map_page.size() > 0) {
                map_page.clear();
            }

//            if (!StringUtils.isBlank(networkDiskBeansSelected) && networkDiskBeansSelected.size() > 0) {
//                networkDiskBeansSelected.clear();
//            }
            //注销EventBus
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }

            dismiss();
        }
    }


    /**
     * 企业网盘item点击事件
     *
     * @param dataBean
     */
    @Override
    public void itemOnclick(NetworkDiskEntity.DataBean dataBean, int position) {
        if (dataBean.getType_id() == 2) {//文件
            if (!StringUtils.isBlank(networkDiskBeansSelected) && networkDiskBeansSelected.size() > 0) {
                for (int i = 0; i < networkDiskBeansSelected.size(); i++) {
                    if (networkDiskBeansSelected.get(i).getId().equals(dataBean.getId())) {
                        networkDiskBeansSelected.remove(i);
                        eventBusPostData(networkDiskBeansSelected);
                        networkDiskAdapter.notifyItemChanged(position);
                        return;
                    }
                }

                if (networkDiskBeansSelected.size() < total_files) {
                    Map<String, Integer> map = getFilesNums(networkDiskBeansSelected);

                    if (dataBean.getType().equals("jpg") || dataBean.getType().equals("jpeg") || dataBean.getType().equals("png")) {//图片
                        if (map.get(ConfigConstants.NETWORKDISK_PICS_SELECTED) >= max_num_pics) {
                            ToastUtils.showLongToastFromText(String.format(mContext.getResources().getString(R.string.networkdisk_pics_selected_max), max_num_pics + ""), ToastUtils.top);
                            return;
                        }
                    } else if (dataBean.getType().equals("mp4")) {//视频
                        if (map.get(ConfigConstants.NETWORKDISK_VIDEOS_SELECTED) >= max_num_videos) {
                            ToastUtils.showLongToastFromText(String.format(mContext.getResources().getString(R.string.networkdisk_videos_selected_max), max_num_videos + ""), ToastUtils.top);
                            return;
                        }
                    } else if (dataBean.getType().equals("mp3")) {//音频
                        if (map.get(ConfigConstants.NETWORKDISK_AUDIOS_SELECTED) >= max_num_audios) {
                            ToastUtils.showLongToastFromText(String.format(mContext.getResources().getString(R.string.networkdisk_audios_selected_max), max_num_audios + ""), ToastUtils.top);
                            return;
                        }
                    } else if (dataBean.getType().equals("xls") || dataBean.getType().equals("xlsx") ||
                            dataBean.getType().equals("txt") ||
                            dataBean.getType().equals("doc") || dataBean.getType().equals("docx") ||
                            dataBean.getType().equals("ppt") || dataBean.getType().equals("pptx") ||
                            dataBean.getType().equals("pdf")) {//文件
                        if (map.get(ConfigConstants.NETWORKDISK_FILES_SELECTED) >= max_num_files) {
                            ToastUtils.showLongToastFromText(String.format(mContext.getResources().getString(R.string.networkdisk_files_selected_max), max_num_files + ""), ToastUtils.top);
                            return;
                        }
                    }
                    networkDiskBeansSelected.add(dataBean);
                } else {
                    ToastUtils.showLongToastFromText(String.format(mContext.getResources().getString(R.string.networkdisk_morequantity), total_files + ""), ToastUtils.top);
                    return;
                }
            } else {
                networkDiskBeansSelected.add(dataBean);
            }
            eventBusPostData(networkDiskBeansSelected);
//            networkDiskAdapter.notifyItemChanged(position);
        } else {//文件夹
            NetworkDiskDialog networkDiskDialog = new NetworkDiskDialog(mContext);
            networkDiskDialog.setDirDatas(lesson_id, dataBean, networkDiskBeansSelected, R.style.NoAnimation, max_num_pics, max_num_videos, max_num_audios, max_num_files, total_files);
            if (!networkDiskDialog.isShowing()) {
                networkDiskDialog.show();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            boolean isHide = KeyBoardUtil.getInstance().isShouldHideKeyboard(v, ev);
//            LogUtils.i(ConfigConstants.TAG_ALL, "isHide =-=" + isHide);
            if (!isHide) {
                showNetworkDiskHistoryRecordDatas();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface NetworkDiskDialogListener {
        void onNetworkDiskConfirmCallback(List<NetworkDiskEntity.DataBean> networkDiskBeansSelected, int pics_selected, int videos_selected, int audios_selected, int files_selected);
    }
}
