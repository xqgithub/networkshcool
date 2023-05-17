package com.talkcloud.networkshcool.baselibrary.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsLanguage;
import com.talkcloud.networkshcool.baselibrary.views.LanguageSwitchView;

import java.util.List;
import java.util.Map;

/**
 * Date:2021/5/18
 * Time:16:34
 * author:joker
 * 切换语言适配器
 */
public class LanguageSwitchAdapter extends RecyclerView.Adapter<LanguageSwitchAdapter.LanguageSwitchHolder> {
    private LayoutInflater inflater;
    private Activity mActivity;
    private LanguageSwitchView languageSwitchView;
    private List<Map<String, String>> datas;
    private String colorbg = "";

    public LanguageSwitchAdapter(Activity activity, LanguageSwitchView languageSwitchView) {
        this.mActivity = activity;
        inflater = LayoutInflater.from(mActivity);
        this.languageSwitchView = languageSwitchView;
    }

    public class LanguageSwitchHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_namedescription;
        public ImageView iv_selected;


        public LanguageSwitchHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_namedescription = itemView.findViewById(R.id.tv_namedescription);
            iv_selected = itemView.findViewById(R.id.iv_selected);
        }
    }

    @NonNull
    @Override
    public LanguageSwitchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_languageswitch, parent, false);
        LanguageSwitchHolder viewHolder = new LanguageSwitchHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageSwitchHolder holder, int position) {
        holder.tv_name.setText(datas.get(position).get("name"));
        holder.tv_namedescription.setText(datas.get(position).get("namedescription"));
        holder.iv_selected.setBackgroundColor(Color.parseColor(colorbg));

        String locale_language = MySPUtilsLanguage.getInstance().getLocaleLanguage();
        String locale_country = MySPUtilsLanguage.getInstance().getLocaleCountry();

        if (datas.get(position).get(SPConstants.LOCALE_LANGUAGE).equals(locale_language)) {
            if (datas.get(position).get(SPConstants.LOCALE_COUNTRY).equals(locale_country)) {
                holder.iv_selected.setVisibility(View.VISIBLE);
            }
        } else {
            holder.iv_selected.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageSwitchView.LanguageSwitchAdapterOnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    /**
     * 设置数据
     */
    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    /**
     * 更新UI控件的背景色
     */

    public void updateUIbg(String colorbg) {
        this.colorbg = colorbg;
    }


}
