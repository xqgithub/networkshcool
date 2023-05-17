package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.ScreenTools;

/**
 * Created by XQ on 2017/4/15.
 * 权限获取页面
 */
public class PermissionsActivity extends Activity {

    private PermissionsChecker mChecker; // 权限检测器
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠

    private static PermissionsListener mPermissionsListener;
    //允许权限后，执行方法标记
    private static int MARK = 0;


    //弹框退出显示
    private static int negative = 0;
    //弹框确定显示
    private static int positive = 0;
    //弹框标题
    private static int title = 0;
    //弹框内容
    private static int message = 0;


    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity,
                                              int requestCode,
                                              int[] dailogcontent,
                                              String[] permissions, PermissionsListener permissionsListener, int mark) {
        if (dailogcontent.length > 0) {
            negative = dailogcontent[0];
            positive = dailogcontent[1];
            title = dailogcontent[2];
            message = dailogcontent[3];
        }

        mPermissionsListener = permissionsListener;
        MARK = mark;

        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(ConfigConstants.EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1.判断是pad还是phone，设置横屏或者竖屏
        if (ScreenTools.getInstance().isPad(this)) {
            //设置横屏
            ScreenTools.getInstance().setLandscape(this);
        } else {
            //设置竖屏
            ScreenTools.getInstance().setPortrait(this);
        }


//        if (getIntent() == null || !getIntent().hasExtra(ConfigConstants.EXTRA_PERMISSIONS)) {
//            throw new RuntimeException("----->PermissionsActivity需要使用静态startActivityForResult方法启动!");
//        }

        setContentView(R.layout.activity_permissions);

        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (mChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions); // 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(ConfigConstants.EXTRA_PERMISSIONS);
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, ConfigConstants.PERMISSIONS_INIT_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {

        if (mPermissionsListener != null) {
            mPermissionsListener.allPermissionsGranted(MARK);
        }

        setResult(ConfigConstants.PERMISSIONS_GRANTED);
        finish();
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConfigConstants.PERMISSIONS_INIT_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            setResult(ConfigConstants.PERMISSIONS_DENIED);
            VariableConfig.IsRefuseAccessRequest = true;

            //判断用户是否点击了，禁止不再提示按钮
            for (int i = 0; i < permissions.length; i++) {
                boolean notprompt = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                if (!notprompt) {
                    showMissingPermissionDialog();
                    return;
                }
            }
            if (mPermissionsListener != null) {
                mPermissionsListener.permissionsDenied(MARK);
            }
            PublicPracticalMethodFromJAVA.getInstance().activityFinish(this, R.anim.activity_xhold);
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);

        // 拒绝, 退出应用
        builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(ConfigConstants.PERMISSIONS_DENIED);
                finish();
//                System.exit(0);
                VariableConfig.IsRefuseAccessRequest = true;
            }
        });

        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(ConfigConstants.PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

    public interface PermissionsListener {
        void allPermissionsGranted(int mark);

        void permissionsDenied(int mark);
    }
}
