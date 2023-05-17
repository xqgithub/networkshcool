package com.talkcloud.networkshcool.baselibrary.common

import java.util.*
import android.util.SparseArray
import android.view.View
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils
import java.util.regex.Pattern

/**
 * Author  guoyw
 * Date    2021/5/18
 * Desc    扩展函数
 */

/**
 * 判断字符串 是否符合数字类型
 */
fun String.toNumber(): Long {
    val pattern: Pattern = Pattern.compile("[0-9]*")
    val flag = pattern.matcher(this).matches()
    return if (flag) {
        this.toLong()
    } else {
        0L
    }
}


fun String.isAudio(): Boolean {
    val path = this.toLowerCase(Locale.getDefault())
    return path.endsWith(".aac") || path.endsWith(".mp3") || path.endsWith("wav")
}


// 是否是图片
fun String.isPhoto(): Boolean {
    return this.endsWith(".jpg") || this.endsWith(".gif") || this.endsWith("jpeg") || this.endsWith(".png") || this.endsWith("bmp")
}

/**
 * Kotlin 实现 ViewHolder 的扩展函数 实现和使用起来更加方便流畅
 */
fun <T : View> View.findViewOfItem(viewId: Int): T {
    var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    tag = viewHolder
    var childView: View? = viewHolder.get(viewId)
    if (StringUtils.isBlank(childView)) {
        childView = findViewById(viewId)
        viewHolder.put(viewId, childView)
    }
    return childView as T
}
