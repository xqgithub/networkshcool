package com.talkcloud.networkshcool.baselibrary.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.talkcloud.networkshcool.baselibrary.R
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils

/**
 * Date:2021/5/11
 * Time:9:14
 * author:joker
 * 检查用户是否具有相应的权限页面
 */
class CheckPermissionsActivity : Activity() {

    //权限检测器
    lateinit var mPermissionsChecker: PermissionsChecker

    /**
     * 所需检查的权限
     */
//    val PERMISSIONS = arrayOf<String>(
////        Manifest.permission.READ_PHONE_STATE,
////        Manifest.permission.WRITE_EXTERNAL_STORAGE,
////        Manifest.permission.READ_EXTERNAL_STORAGE,
////        Manifest.permission.RECORD_AUDIO,
////        Manifest.permission.ACCESS_COARSE_LOCATION,
////        Manifest.permission.ACCESS_FINE_LOCATION,
////        Manifest.permission.CAMERA
////    )
    val PERMISSIONS = arrayOf<String>(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        //判断pad或者手机，设置横屏或者竖屏
//        requestedOrientation = if (PublicPracticalMethodFromJAVA.getInstance().isPad(this)) {
//            //设置横屏
//            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
//        } else {
//            //设置竖屏
//            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
//        }
        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this)
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this)
        }

        setContentView(R.layout.activity_checkpermissions)
//        PublicPracticalMethodFromJAVA.getInstance().restartApp(this@CheckPermissionsActivity)
        initData()
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        mPermissionsChecker = PermissionsChecker(this)
    }

    override fun onResume() {
        super.onResume()

        if (mPermissionsChecker.lacksPermissions(PERMISSIONS) && !VariableConfig.IsRefuseAccessRequest) {//缺少权限的时候，进入权限配置页面
            startPermissionsActivity()
        } else {//不缺少权限的时候，跳转到指定页面
            var token: String? = MySPUtilsUser.getInstance().userToken
            var intent: Intent? = null
            intent = if (!StringUtils.isBlank(token)) {
                Intent(
                    this@CheckPermissionsActivity,
                    MainMenuActivity::class.java
                )
            } else {
                Intent(
                    this@CheckPermissionsActivity,
                    LoginActivity::class.java
                )
            }
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.activity_xhold, R.anim.activity_xhold)
        }
    }

    /**
     * 启动权限管理类
     */
    private fun startPermissionsActivity() {
        val dailogcontent = intArrayOf(
            R.string.quit,
            R.string.settings,
            R.string.help,
            R.string.string_help_text
        )
        PermissionsActivity.startActivityForResult(
            this@CheckPermissionsActivity,
            ConfigConstants.PERMISSIONS_INIT_REQUEST_CODE, dailogcontent, PERMISSIONS, null, 0
        )
    }
}