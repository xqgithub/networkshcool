package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.talkcloud.corelibrary.TKSdkApi;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.adapter.CourseMaterialListAdapter;
import com.talkcloud.networkshcool.baselibrary.base.BaseActivity;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity;
import com.talkcloud.networkshcool.baselibrary.entity.ResourseEntity;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:wsf
 * createTime:2021/5/18
 * description: 课程资料
 */
public class CourseMaterialActivity extends BaseActivity {
    public static final String TAG = "CourseMaterialActivity";


    private RecyclerView rv_courseMaterialList;

    private CourseMaterialListAdapter courseMaterialListAdapter;

    private List<ResourseEntity> list = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_material;
    }

    @Override
    protected void initUiView() {


    }

    @Override
    protected void initData() {
        rv_courseMaterialList = findViewById(R.id.rv_course_material);
        list = (List<ResourseEntity>) getIntent().getSerializableExtra("materialList");
        rv_courseMaterialList.setLayoutManager(new LinearLayoutManager(this));
        courseMaterialListAdapter = new CourseMaterialListAdapter(R.layout.item_course_material, list);
        rv_courseMaterialList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rv_courseMaterialList.setAdapter(courseMaterialListAdapter);
        if (list == null || list.size() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_empty_list_view, null);
            TextView v = view.findViewById(R.id.tv_empty_view_text);
            v.setText(getResources().getString(R.string.nomatrial));
            courseMaterialListAdapter.setEmptyView(view);
        }
        courseMaterialListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TBSFileViewActivity.viewFile(CourseMaterialActivity.this, "https://hwdemocdn.talk-cloud.net/cospath/3554502bd4ed00954e8da8c383105ad5-1.png");
                ResourseEntity info = (ResourseEntity) adapter.getItem(position);
//                if (info.getFiletype().equals("mp3") || info.getFiletype().equals("mp4")) {
//                    TKSdkApi.joinPlayMp4Back(CourseMaterialActivity.this, info.getDownloadpath());
//                } else {
                    Intent intent = new Intent(CourseMaterialActivity.this, BrowserActivity.class);
                    String url = "";

                    url = info.getPreview_url();
                    if (url.trim().equals("")) {
                        ToastUtils.showShortToastFromText(getResources().getString(R.string.datagenerate), ToastUtils.top);
                        return;
                    }
                    intent.putExtra("url", url);

                    intent.putExtra(BaseConstant.KEY_PARAM1, new DownloadEntity(info.getFilename(), info.getDownloadpath(), info.getSize()));
                    startActivity(intent);
                }
//            }
        });

    }

    @Override
    protected void initListener() {
    }


    @Override
    protected void onBeforeSetContentLayout() {
      //  TKDensityUtil.setDensity(this, MyApplication.myApplication);
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        true, true,
                        R.color.appwhite
                );

    }

}


