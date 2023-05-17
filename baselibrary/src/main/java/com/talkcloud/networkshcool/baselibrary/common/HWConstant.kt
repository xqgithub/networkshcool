package com.talkcloud.networkshcool.baselibrary.common


/**
 * Author  guoyw
 * Date    2021/7/2
 * Desc    作业相关常量
 */
object HWConstant {
    // 作业状态
    class HWStatus {
        companion object {
            const val STATUS_DRAFT = "0"
            const val STATUS_NOT_SUBMIT = "1"
            const val STATUS_SUBMIT = "2"
            const val STATUS_REVIEW = "3"
        }
    }

    //草稿状态
    class DraftStatus {
        companion object {
            const val IS_DRAFT = "1"
            const val IS_NOT_DRAFT = "0"
        }
    }
}