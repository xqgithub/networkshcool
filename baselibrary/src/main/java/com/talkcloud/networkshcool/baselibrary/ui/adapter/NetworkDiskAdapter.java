package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

import java.util.List;

/**
 * Date:2021/6/30
 * Time:15:31
 * author:joker
 * 企业网盘 数据 适配器
 */
public class NetworkDiskAdapter extends RecyclerView.Adapter<NetworkDiskAdapter.NetworkDiskHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private List<NetworkDiskEntity.DataBean> networkDiskBeans;
    //被选择的数据集合
    private List<NetworkDiskEntity.DataBean> networkDiskBeansSelected;
    private NetworkDiskAdapterListener networkDiskAdapterListener;

    public NetworkDiskAdapter(Activity activity) {
        this.mActivity = activity;
        inflater = LayoutInflater.from(mActivity);
    }

    public class NetworkDiskHolder extends RecyclerView.ViewHolder {

        private ImageView iv_icon;
        private TextView tv_file_name;
        private TextView tv_file_size;
        private ImageView iv_selected;
        private ImageView iv_arrowright;


        public NetworkDiskHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            tv_file_name = itemView.findViewById(R.id.tv_file_name);
            tv_file_size = itemView.findViewById(R.id.tv_file_size);
            iv_selected = itemView.findViewById(R.id.iv_selected);
            iv_arrowright = itemView.findViewById(R.id.iv_arrowright);
        }
    }

    @NonNull
    @Override
    public NetworkDiskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_networkdisk, parent, false);
        NetworkDiskHolder holder = new NetworkDiskHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkDiskHolder holder, int position) {

        holder.tv_file_name.setText(networkDiskBeans.get(position).getName());

        setIconFromType(holder.iv_icon, holder.iv_selected, holder.iv_arrowright, holder.tv_file_size,
                networkDiskBeans.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (networkDiskBeans.get(position).getType_id() == 1 ||
                    (networkDiskBeans.get(position).getType_id() == 2 && networkDiskBeans.get(position).getStatus() == 1)) {
                networkDiskAdapterListener.itemOnclick(networkDiskBeans.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (StringUtils.isBlank(networkDiskBeans)) {
            return 0;
        } else {
            return networkDiskBeans.size();
        }
    }


    /**
     * 设置了setHasStableIds  需要重写该方法，方式数据混乱和重复
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 设置监听
     */
    public void setNetworkDiskAdapterListener(NetworkDiskAdapterListener networkDiskAdapterListener) {
        this.networkDiskAdapterListener = networkDiskAdapterListener;
    }

    /**
     * 设置数据
     */
    public void setDatas(List<NetworkDiskEntity.DataBean> networkDiskBeans) {
        this.networkDiskBeans = networkDiskBeans;
    }

    public void setDatas2(List<NetworkDiskEntity.DataBean> networkDiskBeansSelected) {
        this.networkDiskBeansSelected = networkDiskBeansSelected;
    }


    /**
     * 根据文件类型 设置文件的icon
     *
     * @param iv_icon
     * @param iv_selected
     * @param iv_arrowright
     */
    private void setIconFromType(ImageView iv_icon, ImageView iv_selected, ImageView iv_arrowright, TextView tv_file_size,
                                 NetworkDiskEntity.DataBean dataBean) {
        if (dataBean.getType_id() == 2) {//文件
            if (dataBean.getType().equals("pdf")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_pdf));
            } else if (dataBean.getType().equals("ppt") || dataBean.getType().equals("pptx")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_ppt));
            } else if (dataBean.getType().equals("doc") || dataBean.getType().equals("docx")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_word));
            } else if (dataBean.getType().equals("txt")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_txt));
            } else if (dataBean.getType().equals("xls") || dataBean.getType().equals("xlsx")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_excle));
            } else if (dataBean.getType().equals("mp3")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_music));
            } else if (dataBean.getType().equals("mp4")) {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_video));
            } else if (dataBean.getType().equals("jpg") || dataBean.getType().equals("jpeg") || dataBean.getType().equals("png")) {
//                if (StringUtils.isBlank(iv_icon.getTag())) {
//                    iv_icon.setTag(dataBean.getDownloadpath());
//                    Glide.with(mActivity).load(dataBean.getDownloadpath())
//                            .error(R.mipmap.networkdisk_file_img)
//                            .into(new CustomTarget<Drawable>() {//防止RecyclerView 中 使用glide 重复加载图像出现忽大忽小的现象
//                                @Override
//                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                    iv_icon.setImageDrawable(resource);
//                                }
//
//                                @Override
//                                public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                                }
//                            });
//                }

                Glide.with(mActivity).load(dataBean.getDownloadpath())
                        .error(R.mipmap.networkdisk_file_img)
                        .into(new CustomTarget<Drawable>() {//防止RecyclerView 中 使用glide 重复加载图像出现忽大忽小的现象
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                iv_icon.setImageDrawable(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

            } else {
                iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_file_zip));
            }

            if (dataBean.getStatus() == 1) {//转化完成
                setItemStatus(iv_selected, dataBean);
                iv_selected.setVisibility(View.VISIBLE);
                tv_file_size.setTextColor(Color.parseColor("#8F92A1"));

                if ("0kb".equals(dataBean.getSize().toLowerCase())) {
                    tv_file_size.setVisibility(View.GONE);
                    tv_file_size.setText("");
                } else {
                    tv_file_size.setVisibility(View.VISIBLE);
                    tv_file_size.setText(dataBean.getSize());
                }

            } else if (dataBean.getStatus() == 2) {//转化失败
                iv_selected.setVisibility(View.GONE);

                tv_file_size.setVisibility(View.VISIBLE);
                tv_file_size.setText(mActivity.getResources().getString(R.string.networkdisk_file_status2));
                tv_file_size.setTextColor(Color.parseColor("#FF2855"));
            } else if (dataBean.getStatus() == 3) {//转化中
                iv_selected.setVisibility(View.GONE);
                tv_file_size.setVisibility(View.VISIBLE);
                tv_file_size.setText(mActivity.getResources().getString(R.string.networkdisk_file_status3));
                tv_file_size.setTextColor(Color.parseColor("#FF2855"));
            } else {//排队中
                iv_selected.setVisibility(View.GONE);
                tv_file_size.setVisibility(View.VISIBLE);
                tv_file_size.setText(mActivity.getResources().getString(R.string.networkdisk_file_status));
                tv_file_size.setTextColor(Color.parseColor("#FF2855"));
            }

            iv_arrowright.setVisibility(View.GONE);
        } else {//文件夹
            iv_icon.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.networkdisk_dir));
            iv_selected.setVisibility(View.GONE);
            iv_arrowright.setVisibility(View.VISIBLE);

            tv_file_size.setVisibility(View.GONE);
            tv_file_size.setText("");
        }
    }

    /**
     * 设置是否被选中状态
     */
    private void setItemStatus(ImageView iv_selected, NetworkDiskEntity.DataBean dataBean) {

        PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, iv_selected, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), "#00000000");
        iv_selected.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_unselected));

        if (!StringUtils.isBlank(networkDiskBeansSelected) && networkDiskBeansSelected.size() > 0) {
            for (NetworkDiskEntity.DataBean dataBeanselected : networkDiskBeansSelected) {
                if (dataBeanselected.getId().equals(dataBean.getId())) {
                    PublicPracticalMethodFromJAVA.getInstance().setDynamicShapeOVAL(mActivity, iv_selected, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_120x), VariableConfig.color_button_selected);
                    iv_selected.setImageDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.login_userprivacyagreement_selected));
                }
            }
        }
    }

    public interface NetworkDiskAdapterListener {
        void itemOnclick(NetworkDiskEntity.DataBean dataBean, int position);
    }


}
