package com.talkcloud.networkshcool.baselibrary.entity

/**
 * Date:2021/10/12
 * Time:17:43
 * author:joker
 * 图文验证 返回参数
 */
data class Captcha(
    var ticket: String? = "",
    var randstr: String? = ""
) {
}