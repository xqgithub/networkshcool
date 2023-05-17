package com.talkcloud.networkshcool.baselibrary.views

import io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess

/**
 * Date:2021/9/16
 * Time:16:23
 * author:joker
 */
interface NoticeInAppView {
    fun <T> noticeListCallback(isSuccess: Boolean, data: T, msg: String)

    fun readNotice(datas: IntArray, position: Int)

    fun <T> readNoticeCallback(isSuccess: Boolean, data: T, msg: String)
}