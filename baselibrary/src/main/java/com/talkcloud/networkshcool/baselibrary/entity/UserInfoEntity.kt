package com.talkcloud.networkshcool.baselibrary.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfoEntity(
    val account: String? = "",
    val avatar: String? = "",
    val locale: String? = "",
    val mobile: String? = "",
    var userid: String? = "",
    val username: String? = ""
) : Parcelable