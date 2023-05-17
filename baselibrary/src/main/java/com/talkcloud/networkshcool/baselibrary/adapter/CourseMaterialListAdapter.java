package com.talkcloud.networkshcool.baselibrary.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.entity.ResourseEntity;

import java.util.List;


public class CourseMaterialListAdapter extends BaseQuickAdapter<ResourseEntity, BaseViewHolder> {

    public CourseMaterialListAdapter(int layoutResId, @Nullable List<ResourseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ResourseEntity bean) {

        TextView tv_file_name = helper.getView(R.id.tv_file_name);
        TextView tv_file_size = helper.getView(R.id.tv_file_size);
        ImageView img_file_type = helper.getView(R.id.img_file_type);
        if (bean.getFilename() != null) {
            tv_file_name.setText(bean.getFilename());
        }
        if (bean.getSize() != null) {
            tv_file_size.setText(bean.getSize());
        }
        if (bean.getFiletype() != null) {
            String type = bean.getFiletype();
            //类型  xls,xlsx,ppt,pptx,doc,docx,txt,pdf,jpg,gif,jpeg,png,bmp,mp3,mp4,zip  为空代表是未知格式
            if (type.equals("jpg") || type.equals("gif") || type.equals("jpeg") || type.equals("png") || type.equals("bmp")) {
                img_file_type.setImageResource(R.mipmap.ic_img);
            } else if (type.equals("xls") || type.equals("xlsx")) {
                img_file_type.setImageResource(R.mipmap.ic_excel);
            } else if (type.equals("ppt") || type.equals("pptx")) {
                img_file_type.setImageResource(R.mipmap.ic_ppt);
            } else if (type.equals("doc") || type.equals("docx")) {
                img_file_type.setImageResource(R.mipmap.ic_word);
            } else if (type.equals("txt")) {
                img_file_type.setImageResource(R.mipmap.ic_txt);
            } else if (type.equals("pdf")) {
                img_file_type.setImageResource(R.mipmap.ic_pdf);
            } else if (type.equals("mp3")) {
                img_file_type.setImageResource(R.mipmap.ic_mp3);
            } else if (type.equals("mp4")) {
                img_file_type.setImageResource(R.mipmap.ic_mp4);
            } else if (type.equals("zip")) {
                img_file_type.setImageResource(R.mipmap.ic_zip);
            }
        } else {
            img_file_type.setImageResource(R.mipmap.ic_unknown);
        }
    }
}
