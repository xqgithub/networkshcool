package com.talkcloud.networkshcool.baselibrary.utils

import android.text.TextUtils
import java.lang.StringBuilder

/**
 * Author  guoyw
 * Date    2021/5/12
 * Desc
 */
class AppPrefsUtil {

    companion object {
        //主色
        private const val PRIMARY_COLOR = "ns_primary_color"

        // 保存保存主色
        fun savePrimaryColor(color: String) {
            SpUtil.putString(PRIMARY_COLOR, color)
        }

        // 获取主色
        fun getPrimaryColor(): String {
            return SpUtil.getString(PRIMARY_COLOR)
        }


        //本地头像地址 TODO 可以不需要了
        private const val LOCAL_HEADER_URL = "local_header_url"

        // 保存本地头像地址
        fun saveLocalHeaderUrl(color: String) {
            SpUtil.putString(PRIMARY_COLOR, color)
        }

        // 获取本地头像地址
        fun getLocalHeaderUrl(): String {
            return SpUtil.getString(PRIMARY_COLOR)
        }


        //远程头像地址
        private const val REMOTE_HEADER_URL = "remote_header_url"

        // 保存远程头像地址
        @JvmStatic
        fun saveRemoteHeaderUrl(url: String) {
            SpUtil.putString(REMOTE_HEADER_URL, url)
        }

        // 获取本地头像地址
        @JvmStatic
        fun getRemoteHeaderUrl(): String {
            return SpUtil.getString(REMOTE_HEADER_URL)
        }


        // 用户名
        private const val KEY_USER_NAME = "key_user_name"

        @JvmStatic
        fun saveUserName(name: String) {
            SpUtil.putString(KEY_USER_NAME, name)
        }

        @JvmStatic
        fun getUserName(): String {
            return SpUtil.getString(KEY_USER_NAME)
        }


        // 用户身份
        private const val KEY_USER_IDENTITY = "key_user_identity"

        @JvmStatic
        fun saveUserIdentity(identity: String) {
            SpUtil.putString(KEY_USER_IDENTITY, identity)
        }

        @JvmStatic
        fun getUserIdentity(): String {
            return SpUtil.getString(KEY_USER_IDENTITY)
        }


        // 用户id
        private const val KEY_USER_ID = "key_user_id"

        @JvmStatic
        fun saveUserId(userid: String) {
            SpUtil.putString(KEY_USER_ID, userid)
        }

        @JvmStatic
        fun getUserId(): String {
            return SpUtil.getString(KEY_USER_ID)
        }


        // 从链接进入应用
        private const val KEY_FROM_H5_ROOM_ID = "key_from_h5_room_id"

        @JvmStatic
        fun saveFormH5RoomId(id: String) {
            SpUtil.putString(KEY_FROM_H5_ROOM_ID, id)
        }

        @JvmStatic
        fun getFormH5RoomId(): String {
            return SpUtil.getString(KEY_FROM_H5_ROOM_ID)
        }


        // 注销账号 联系电话
        private const val KEY_UNREGISTER_PHONE = "key_unregister_phone"

        @JvmStatic
        fun saveUnregisterPhone(id: String) {
            SpUtil.putString(KEY_UNREGISTER_PHONE, id)
        }

        @JvmStatic
        fun getUnregisterPhone(): String {
            return SpUtil.getString(KEY_UNREGISTER_PHONE)
        }


        // 是否显示注销账号
        private const val KEY_UNREGISTER_FLAG = "key_unregister_flag"

        @JvmStatic
        fun saveUnregisterFlag(flag: Boolean) {
            SpUtil.putBoolean(KEY_UNREGISTER_FLAG, flag)
        }

        @JvmStatic
        fun getUnregisterFlag(): Boolean {
            return SpUtil.getBoolean(KEY_UNREGISTER_FLAG)
        }


        // 搜索学生姓名集合
        private const val KEY_SEARCH_NAME = "key_search_name"

        @JvmStatic
        fun saveSearchName(name: String) {

            val sb = StringBuilder()

            sb.append(name)
            sb.append("#####")
            sb.append(getSearchName())

            SpUtil.putString(KEY_SEARCH_NAME, sb.toString())

        }

        @JvmStatic
        fun getSearchName(): String {
            return SpUtil.getString(KEY_SEARCH_NAME)
        }

        @JvmStatic
        fun getSearchNameList(): MutableList<String> {
            val nameStr = getSearchName()
            val nameList2 = mutableListOf<String>()
            if (!TextUtils.isEmpty(nameStr)) {
                val nameList = nameStr.split("#####")
                nameList.forEach { s ->
                    if (s.isNotEmpty() && !nameList2.contains(s)) {
                        nameList2.add(s)
                    }
                }
            }

            return nameList2
        }
    }

}