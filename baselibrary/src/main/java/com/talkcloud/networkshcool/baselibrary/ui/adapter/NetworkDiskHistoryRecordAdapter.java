package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;

import java.util.List;

/**
 * Date:2021/7/5
 * Time:9:44
 * author:joker
 * 企业网盘历史记录 适配器
 */
public class NetworkDiskHistoryRecordAdapter extends RecyclerView.Adapter<NetworkDiskHistoryRecordAdapter.NetworkDiskHistoryRecordHolder> {


    private LayoutInflater inflater;
    private Activity mActivity;
    private List<String> historyRecords;
    private NetworkDiskHistoryRecordAdapterListener networkDiskHistoryRecordAdapterListener;

    public NetworkDiskHistoryRecordAdapter(Activity activity, NetworkDiskHistoryRecordAdapterListener networkDiskHistoryRecordAdapterListener) {
        this.mActivity = activity;
        inflater = LayoutInflater.from(mActivity);
        this.networkDiskHistoryRecordAdapterListener = networkDiskHistoryRecordAdapterListener;
    }

    public class NetworkDiskHistoryRecordHolder extends RecyclerView.ViewHolder {

        private ImageView iv_record_icon;
        private TextView tv_record_name;

        public NetworkDiskHistoryRecordHolder(@NonNull View itemView) {
            super(itemView);
            iv_record_icon = itemView.findViewById(R.id.iv_record_icon);
            tv_record_name = itemView.findViewById(R.id.tv_record_name);
        }
    }

    @NonNull
    @Override
    public NetworkDiskHistoryRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_networkdisk_historyrecord, parent, false);
        NetworkDiskHistoryRecordHolder holder = new NetworkDiskHistoryRecordHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkDiskHistoryRecordHolder holder, int position) {
        holder.tv_record_name.setText(historyRecords.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (!StringUtils.isBlank(networkDiskHistoryRecordAdapterListener)) {
                networkDiskHistoryRecordAdapterListener.historyRecordItemOnclick(historyRecords.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (StringUtils.isBlank(historyRecords)) {
            return 0;
        } else {
            return historyRecords.size();
        }
    }


    /**
     * 设置数据
     */
    public void setDatas(List<String> historyRecords) {
        this.historyRecords = historyRecords;
    }

    public interface NetworkDiskHistoryRecordAdapterListener {
        void historyRecordItemOnclick(String keywords);
    }


}
