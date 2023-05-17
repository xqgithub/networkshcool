package com.talkcloud.networkshcool.baselibrary.utils

import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class TKDoubleTapHelper {

    companion object {
        /**
         * 对View的点击事件在设定的时间内进行防止二次点击，不带View参数的回调
         * @param view
         * @param debounceTime 设定的这个时间内，View点击只有一次起效，时间单位是秒
         * @param runnable  View的点击后的执行方法的回调
         */
        @JvmStatic
        fun click(view: View, debounceTime: Long, runnable: Runnable) {

            Observable.create<View> {
                var emitter = it
                view?.setOnClickListener {
//                    Log.e("DebounceHelper", "点击...")
                    emitter.onNext(it)
                }
            }.throttleFirst(debounceTime, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { runnable.run() }
        }

        /**
         * 对View的点击事件在设定的时间内进行防止二次点击，带View参数的回调
         * @param view
         * @param debounceTime 设定的这个时间内，View点击只有一次起效，时间单位是秒
         * @param listener  View的点击事件回调，带有本身View的参数
         */
        @JvmStatic
        fun click(view: View, debounceTime: Long, listener: View.OnClickListener) {
            Observable.create<View> {
                var emitter = it
                view?.setOnClickListener {
                    emitter.onNext(it)
                }
            }.throttleFirst(debounceTime, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listener.onClick(it) }
        }

        private var lastTime = 0L

        @JvmStatic
        fun doubleClick(): Boolean {

            if (System.currentTimeMillis() - lastTime < 500) {
                return true
            }
            lastTime = System.currentTimeMillis()

            return false
        }
    }
}