package com.talkcloud.networkshcool.baselibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.entity.TeacherOrAssitorEntity;
import com.talkcloud.networkshcool.baselibrary.weiget.RoundImageView;

import java.util.List;


public class TeacherListAdapter extends RecyclerView.Adapter<TeacherListAdapter.ViewHolder> {

    private Context mcontext;
    private List<TeacherOrAssitorEntity> mlist;

    private ClickListener listener;


    public void setOnItemClick(ClickListener listener) {
        this.listener = listener;
    }

    public TeacherListAdapter(Context context, List<TeacherOrAssitorEntity> list, ClickListener listener) {
        mcontext = context;
        mlist = list;
        this.listener = listener;
    }

    public TeacherListAdapter(Context context, List<TeacherOrAssitorEntity> list) {
        mcontext = context;
        mlist = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_teacher, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TeacherOrAssitorEntity bean = mlist.get(position);
        if (bean.getAvatar() != null && !bean.getAvatar().equals(""))
            Glide.with(mcontext).load(bean.getAvatar()).error(R.drawable.icon_default_head_img).into(holder.img_avator);
        if (bean.getName() != null)
            holder.tv_name.setText(bean.getName());
        if (bean.getType() != null && bean.getType().equals("1")) {
            holder.tv_type.setText(mcontext.getResources().getString(R.string.teacher));
        } else {
            holder.tv_type.setText(mcontext.getResources().getString(R.string.assistantor));
        }


        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(bean);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_type;
        private TextView tv_name;
        private RoundImageView img_avator;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_avator = itemView.findViewById(R.id.img_avator);

        }
    }

    public interface ClickListener {
        void onClick(TeacherOrAssitorEntity bean);
    }
}
