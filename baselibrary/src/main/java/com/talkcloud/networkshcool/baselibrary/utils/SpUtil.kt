package com.talkcloud.networkshcool.baselibrary.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant

/**
 * Author  guoyw
 * Date    2021/5/12
 * Desc
 */
object SpUtil {
    private var sp: SharedPreferences = TKBaseApplication.myApplication.getSharedPreferences(BaseConstant.TABLE_PREFS, Context.MODE_PRIVATE)
    private var ed: Editor

    init {
        ed = sp.edit()
    }

    /*
        Boolean数据
     */
    fun putBoolean(key: String, value: Boolean) {
        ed.putBoolean(key, value)
        ed.commit()
    }

    /*
        默认 false
     */
    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }

    /*
        String数据
     */
    fun putString(key: String, value: String) {
        ed.putString(key, value)
        ed.commit()
    }

    /*
        默认 ""
     */
    fun getString(key: String): String {
        return sp.getString(key, "").toString()
    }

    /*
        Int数据
     */
    fun putInt(key: String, value: Int) {
        ed.putInt(key, value)
        ed.commit()
    }

    /*
        默认 0
     */
    fun getInt(key: String): Int {
        return sp.getInt(key, 0)
    }

    /*
        Long数据
     */
    fun putLong(key: String, value: Long) {
        ed.putLong(key, value)
        ed.commit()
    }

    /*
        默认 0
     */
    fun getLong(key: String): Long {
        return sp.getLong(key, 0)
    }


    /*
        set数据
     */
    fun putSet(key: String, value: MutableSet<String>) {
        ed.putStringSet(key, value)
        ed.commit()
    }

    /*
        默认
     */
    fun getSet(key: String): MutableSet<String>? {
        return sp.getStringSet(key, mutableSetOf(""))
    }


    /*
        删除key数据
     */
    fun remove(key: String) {
        ed.remove(key)
        ed.commit()
    }


}
