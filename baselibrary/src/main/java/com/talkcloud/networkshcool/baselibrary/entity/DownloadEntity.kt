package com.talkcloud.networkshcool.baselibrary.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadEntity(
    var fileName: String? = "",
    var downloadPath: String? = "",
    var size: String? = ""
) : Parcelable
