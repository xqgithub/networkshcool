package com.talkcloud.networkshcool.baselibrary.views;

import com.talkcloud.networkshcool.baselibrary.entity.CountryAreaEntity;

import java.util.List;

/**
 * Date:2021/5/13
 * Time:9:18
 * author:joker
 * 国家区域号
 */
public interface CountryAreaView {
    void CountryCodeCallback(boolean isSuccess, List<CountryAreaEntity> countryAreaEntityList, String msg);

    void countryAreaitemClick(CountryAreaEntity bean, int position);
}
