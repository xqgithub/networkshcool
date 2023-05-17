package com.talkcloud.networkshcool.baselibrary.weiget

import android.text.Editable
import android.text.TextWatcher

/**
 * Author  guoyw
 * Date    2021/5/18
 * Desc     默认TextWatcher，空实现
 */
open class DefaultTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
