package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.views.CountryAreaView;

import java.util.List;

/**
 * Date:2021/5/13
 * Time:10:21
 * author:joker
 * 国家区域号 适配器
 */
public class CountryAreaAdapter extends RecyclerView.Adapter<CountryAreaAdapter.CountryAreaHolder> {

    private LayoutInflater inflater;
    private Activity mActivity;
    private List<CountryAreaEntity> dataBeans;
    private CountryAreaView countryAreaView;

    public CountryAreaAdapter(Activity mActivity, CountryAreaView countryAreaView) {
        inflater = LayoutInflater.from(mActivity);
        this.mActivity = mActivity;
        this.countryAreaView = countryAreaView;
    }


    public class CountryAreaHolder extends RecyclerView.ViewHolder {
        private TextView tv_countryname;
        private TextView tv_countrycode;


        public CountryAreaHolder(@NonNull View itemView) {
            super(itemView);
            tv_countryname = itemView.findViewById(R.id.tv_countryname);
            tv_countrycode = itemView.findViewById(R.id.tv_countrycode);
        }
    }

    /**
     * 设置数据
     */
    public void setDatas(List<CountryAreaEntity> dataBeans) {
        this.dataBeans = dataBeans;
    }


    @NonNull
    @Override
    public CountryAreaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_countryarea, parent, false);
        CountryAreaHolder viewHolder = new CountryAreaHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAreaHolder holder, int position) {
        holder.tv_countryname.setText(dataBeans.get(position).getCountry());
        holder.tv_countrycode.setText("+" + dataBeans.get(position).getCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryAreaView.countryAreaitemClick(dataBeans.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (StringUtils.isBlank(dataBeans)) {
            return 0;
        } else {
            return dataBeans.size();
        }
    }


}
