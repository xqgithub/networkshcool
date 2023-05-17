package com.talkcloud.networkshcool.baselibrary.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 * Author  guoyw
 * Date    2021/5/12
 * Desc    今日头条屏幕适配方案，修改系统density，需要在view之前调用，如activity在setContentView前调用
 */
class TKDensityUtil {

    companion object {
        // 用来参照的的width  设计的效果图的宽度 pad的宽度
        private var designPadWidth = 1024f

        // 设计图手机的宽度
        private var designPhoneWidth = 375f

        // 屏幕的密度density
        private var appDensity = 0f

        // 字体的缩放比例 默认为appDensity
        private var appScaledDensity = 0f

        private var targetDensity = 0f

        private var mIsPad = true

        //    private var application: Application? = null
        @JvmStatic
        fun setDensity(activity: Activity, application: Application, isPad: Boolean = true) {
//            designWidth = width
            mIsPad = isPad

            setDensity(activity, application)
        }

        /**
         * @param activity 当前activity
         */
        @JvmStatic
        fun setDensity(activity: Activity, application: Application) {
//        application = activity.application

            // 获取当前app屏幕的显示信息
            val appDisplayMetrics = application.resources.displayMetrics
            if (appDensity == 0f) {
                //初始化的时候赋值
                appDensity = appDisplayMetrics.density
                appScaledDensity = appDisplayMetrics.scaledDensity
            }

            // 添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })

            // 计算目标density
            var widthPixels =
                Math.max(appDisplayMetrics.widthPixels, appDisplayMetrics.heightPixels)
            var designWidth = designPadWidth
            if (!mIsPad) {
                // 手机
                widthPixels =
                    Math.min(appDisplayMetrics.widthPixels, appDisplayMetrics.heightPixels)
                designWidth = designPhoneWidth
            }
            targetDensity = widthPixels / designWidth

//            NSLog.d("goyw", " widthPixels : $widthPixels  ,  designWidth: $designWidth")
//            NSLog.d("goyw", " targetDensity : $targetDensity")
            val targetScaledDensity = targetDensity * (appScaledDensity / appDensity)
            val targetDensityDpi = (160 * targetDensity).toInt()

            // 修改Activity的density值
            val activityDisplayMetrics = activity.resources.displayMetrics
            activityDisplayMetrics?.density = targetDensity
            activityDisplayMetrics?.scaledDensity = targetScaledDensity
            activityDisplayMetrics?.densityDpi = targetDensityDpi
        }


        // 使用修改了density转化后的
//        internal fun dp2px(dpValue: Float): Float {
//            var scale =
//                MyApplication.myApplication.applicationContext.resources.displayMetrics.density
//            if (targetDensity != 0f) {
//                scale = targetDensity
////                TKLog.d("goyw", " scale : $scale")
//            }
//            return dpValue * scale + 0.5f
//        }
    }
}

