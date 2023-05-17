package com.talkcloud.networkshcool.baselibrary.help;

import com.talkcloud.networkshcool.baselibrary.common.SPConstants;
import com.talkcloud.networkshcool.baselibrary.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2021/7/5
 * Time:10:58
 * author:joker
 * networkdiskhistoryrecord.xml SharedPreferences 工具类
 */
public class MySPUtilsNDHRecord {

    private volatile static MySPUtilsNDHRecord mysputilsndhrecord;

    private SPUtils sputils_ndhrecord;

    public static MySPUtilsNDHRecord getInstance() {
        if (mysputilsndhrecord == null) {//第一次检查，避免不必要的同步
            synchronized (MySPUtilsNDHRecord.class) {//synchronized对MySPUtilsNDHRecord加全局锁，保证每次只要一个线程创建实例
                if (mysputilsndhrecord == null) {//第二次检查，为null时才创建实例
                    mysputilsndhrecord = new MySPUtilsNDHRecord();
                }
            }
        }
        return mysputilsndhrecord;
    }

    public MySPUtilsNDHRecord() {
        sputils_ndhrecord = new SPUtils(SPConstants.HISTORYRECORD_NAME);
    }


    /**
     * 保存历史记录的名称
     */
    public void saveRecordName(String key, String name) {
        sputils_ndhrecord.put(key, name);
    }

    /**
     * 获取该文件中所有的值
     */
    public List<String> getRecordDatas() {
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map = (Map<String, String>) sputils_ndhrecord.getAll();
        if (map.size() > 0) {
            for (String kk : map.keySet()) {
                list.add(map.get(kk));
            }
        }
        return list;
    }

    /**
     * 清空数据
     */

    public void clear() {
        sputils_ndhrecord.clear();
    }


}
