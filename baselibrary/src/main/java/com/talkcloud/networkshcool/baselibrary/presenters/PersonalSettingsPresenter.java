package com.talkcloud.networkshcool.baselibrary.presenters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.api.ApiResponse;
import com.talkcloud.networkshcool.baselibrary.api.ApiService;
import com.talkcloud.networkshcool.baselibrary.api.BaseSubscriber;
import com.talkcloud.networkshcool.baselibrary.api.CallManager;
import com.talkcloud.networkshcool.baselibrary.api.RetrofitServiceManager;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.entity.NetworkDiskEntity;
import com.talkcloud.networkshcool.baselibrary.entity.SysVersionEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.ui.activities.PermissionsActivity;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.AudioRecordDialog;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.ChoiceDialog;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.NetworkDiskDialog;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.SysVersionUpdateDialog;
import com.talkcloud.networkshcool.baselibrary.utils.BarUtils;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PermissionsChecker;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.UpdateAppUtils;
import com.talkcloud.networkshcool.baselibrary.utils.audiohelp.AudioPlayManager;
import com.talkcloud.networkshcool.baselibrary.utils.audiohelp.AudioRecordManager;
import com.talkcloud.networkshcool.baselibrary.views.PersonalSettingsView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import retrofit2.Response;

/**
 * Date:2021/5/18
 * Time:20:26
 * author:joker
 * 个人设置页面 Presenter
 */
public class PersonalSettingsPresenter implements NetworkDiskDialog.NetworkDiskDialogListener {

    private Activity mActivity;
    private PersonalSettingsView personalSettingsView;
    private PermissionsActivity.PermissionsListener permissionsListener;

    public PersonalSettingsPresenter(Activity activity, PersonalSettingsView personalSettingsView, PermissionsActivity.PermissionsListener permissionsListener) {
        this.mActivity = activity;
        this.personalSettingsView = personalSettingsView;
        this.permissionsListener = permissionsListener;
    }

    /**
     * 设置状态栏的高度
     */
    public void setBarHeight(View view) {
        //获取状态的高度
        int BarHeight = BarUtils.getStatusBarHeight(mActivity);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height = BarHeight;
        view.setLayoutParams(layoutParams);
    }


    /**
     * 申请权限，开始调节手机亮度
     */
    public void requestWriteSettings(int REQUEST_CODE_WRITE_SETTINGS, boolean isChecked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(mActivity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.putExtra("isChecked", isChecked);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                mActivity.startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            } else {
                //保存数据
                MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(isChecked);
                PublicPracticalMethodFromJAVA.getInstance().setScrennManualMode(mActivity, isChecked);
//                int ScreenBrightness = PublicPracticalMethodFromJAVA.getInstance().getScreenBrightness(mActivity);
//                LogUtils.i(ConfigConstants.TAG_ALL, "ScreenBrightness =-= " + ScreenBrightness);
//                PublicPracticalMethodFromJAVA.getInstance().saveScreenBrightness(mActivity, 180);
            }
        }
    }

    /**
     * 申请权限，判断是否有悬浮框，如果有开启护眼模式
     */
    public void eyeProtectionOperating(boolean isChecked) {
        //判断是否有悬浮窗的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mActivity)) {//没有该权限
                eyeProtectionDiadlog();
                return;
            } else {//有该权限
//                EyeProtectionUtil.openSuspensionWindow(mActivity, isChecked);
            }
        } else {
//            EyeProtectionUtil.openSuspensionWindow(mActivity, isChecked);
        }
        //保存数据
        MySPUtilsUser.getInstance().saveUserEyeProtectionStatus(isChecked);

        //这里的状态栏设置
        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        mActivity,
                        false, true,
                        R.color.appwhite
                );
    }


    /**
     * 护眼模式权限申请弹框
     */
    public void eyeProtectionDiadlog() {
        ChoiceDialog choiceDialog = new ChoiceDialog(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_375x));

        String title = mActivity.getResources().getString(R.string.logout_title);
        String content = mActivity.getResources().getString(R.string.eyeprotection_content);
        String no = mActivity.getResources().getString(R.string.logout_no);
        String yes = mActivity.getResources().getString(R.string.eyeprotection_yes);

        choiceDialog.setTextInformation(title, content, no, yes);
        choiceDialog.setDialogBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
        choiceDialog.setCancelBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
        choiceDialog.setConfirmBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);

        choiceDialog.confirmButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismissDialog();
                personalSettingsView.eyeProtectionCallback(true);
            }
        });

        choiceDialog.notSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismissDialog();
                personalSettingsView.eyeProtectionCallback(false);
            }
        });

        choiceDialog.closeWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismissDialog();
                personalSettingsView.eyeProtectionCallback(false);
            }
        });

        if (!choiceDialog.isShowing()) {
            choiceDialog.show();
        }
    }


    /**
     * 显示退出登录的弹框
     */
    public void showLogOutDialog() {
        ChoiceDialog choiceDialog = new ChoiceDialog(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_375x));

        String title = mActivity.getResources().getString(R.string.logout_title);
        String content = mActivity.getResources().getString(R.string.logout_content);
        String no = mActivity.getResources().getString(R.string.logout_no);
        String yes = mActivity.getResources().getString(R.string.logout_yes);
        choiceDialog.setTextInformation(title, content, no, yes);
        choiceDialog.setDialogBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
        choiceDialog.setCancelBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
        choiceDialog.setConfirmBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);

        choiceDialog.confirmButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                choiceDialog.dismissDialog();
            }
        });

        choiceDialog.notSure(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismissDialog();
            }
        });

        choiceDialog.closeWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDialog.dismissDialog();
            }
        });

        if (!choiceDialog.isShowing()) {
            choiceDialog.show();
        }
    }

    /**
     * 用户退出
     */

    private void logout() {
        ApiService apiService = RetrofitServiceManager.getInstance().getApiService(VariableConfig.UrlPathHelp.USER, VariableConfig.V1);
        apiService.logout()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
                    @Override
                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
                        personalSettingsView.logoutCallback(true, apiResponseResponse.body().getMsg());
                    }

                    @Override
                    public void onFailure(String message, int error_code) {
                        personalSettingsView.logoutCallback(false, message);
                    }
                });
//        ApiService apiService = RetrofitServiceManager.getInstance(VariableConfig.base_url_user, ApiService.logout).getApiService();
//        apiService.logout()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseSubscriber<Response<ApiResponse>>(mActivity, true, false) {
//                    @Override
//                    public void onSuccess(Response<ApiResponse> apiResponseResponse) {
//                        personalSettingsView.logoutCallback(true, apiResponseResponse.body().getMsg());
//                    }
//
//                    @Override
//                    public void onFailure(String message, int error_code) {
//                        personalSettingsView.logoutCallback(false, message);
//                    }
//                });
    }


    /**
     * 获取系统版本接口
     */
    public void sysversion() {
        Map<String, String> map_bodys = new HashMap<>();
        map_bodys.put("companydomain", TKExtManage.getInstance().getCompanyDomain());//企业域名 非定制默认www
        map_bodys.put("source", "4");//企业来源 4：门课
        /**
         * 0：Mac、
         * 1：PC、
         * 3：iOS iPad、
         * 5：iOS iPhone、
         * 6：TV盒子、
         * 7：新学问PC、
         * 8：新学问TV、
         * 9：Android、
         * 10：会议Android、
         * 11：会议iOS
         */
        map_bodys.put("type", "9");
        map_bodys.put("version", VariableConfig.getVersionUpdateVersion());//版本号 拓课云APP端通行用法为：封包日期+版本号，如：20210621100
        Call call = CallManager.getInstance(VariableConfig.getVersionUpdateUrl()).getPostRequest(map_bodys);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                LogUtils.i(ConfigConstants.TAG_ALL, "e =-= " + e.getMessage());
//                personalSettingsView.sysversionCallback(false, null, e.getMessage());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    if (!StringUtils.isBlank(response.body())) {
                        String jsonString = StringUtils.byte2String(response.body().bytes());
                        JSONObject jsonObject_data = new JSONObject(jsonString);
                        int result = jsonObject_data.optInt("result", -1);
                        if (result == 0) {
                            SysVersionEntity sysVersionEntity = new SysVersionEntity();
                            sysVersionEntity.setVersion(jsonObject_data.optString("version", ""));
                            sysVersionEntity.setFilename(jsonObject_data.optString("filename", ""));
                            sysVersionEntity.setFiletype(jsonObject_data.optString("filetype", ""));
                            sysVersionEntity.setIsupdate(jsonObject_data.optString("isupdate", ""));
                            sysVersionEntity.setUpdateflag(jsonObject_data.optString("updateflag", ""));
                            sysVersionEntity.setUrl(jsonObject_data.optString("url", ""));
                            sysVersionEntity.setApptype(jsonObject_data.optString("apptype", ""));
                            sysVersionEntity.setUpdateaddr(jsonObject_data.optString("updateaddr", ""));
                            sysVersionEntity.setSetupaddr(jsonObject_data.optString("setupaddr", ""));
                            sysVersionEntity.setVersionnum(jsonObject_data.optString("versionnum", ""));
                            personalSettingsView.sysversionCallback(true, sysVersionEntity, "");
                        } else {
                            personalSettingsView.sysversionCallback(true, null, "");
                        }
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                personalSettingsView.sysversionCallback(false, null, mActivity.getResources().getString(R.string.data_wrong));
            }
        });
    }


    /**
     * 升级更新APP
     */
    public void updateAPP(SysVersionEntity sysVersionEntity) {
        if (!StringUtils.isBlank(sysVersionEntity)) {
            SysVersionUpdateDialog sysVersionUpdateDialog = new SysVersionUpdateDialog(mActivity);
            sysVersionUpdateDialog.setInitDatas(sysVersionEntity);
            sysVersionUpdateDialog.confirmButton(v -> {
                //权限判断
                PermissionsChecker mPermissionsChecker = new PermissionsChecker(mActivity);
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (mPermissionsChecker.lacksPermissions(permissions)) {
                    //去申请权限
                    PublicPracticalMethodFromJAVA.getInstance().startPermissionsActivity(mActivity, permissions, permissionsListener, ConfigConstants.PERMISSIONS_GRANTED_UPDATEAPP);
                } else {
                    //初始化下载
                    UpdateAppUtils.getInstance().initUtils(mActivity, sysVersionEntity);
                    //准备开始下载apk
                    UpdateAppUtils.getInstance().readyDownloadApk(mActivity.getResources().getString(TKExtManage.getInstance().getAppNameRes(mActivity)), mActivity.getResources().getString(R.string.download_progress) + "0%");
                    sysVersionUpdateDialog.dismissDialog();
                }
            });

            if (!sysVersionUpdateDialog.isShowing()) {
                sysVersionUpdateDialog.show();
            }
        }
    }

    /**
     * 销毁处理
     */
    public void onDestroy() {
        UpdateAppUtils.getInstance().onDestroy();
    }


    /**
     * 显示音频录制弹框
     */
    public void showAudioRecordDialog() {

        AudioRecordDialog audioRecordDialog = new AudioRecordDialog(mActivity);

        //右上角X按钮
        audioRecordDialog.closeWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioRecordManager.getInstance().clear();
                AudioPlayManager.getInstance().clear();
                audioRecordDialog.dismissDialog();
                showAudioRecordConfirmDialog(audioRecordDialog);
            }
        });

        //麦克风录制按钮
        audioRecordDialog.micClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordDialog.startOrEndRecord();
            }
        });

        //我的录音按钮
        audioRecordDialog.myAudioClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordDialog.playOrPauseAudio("");
            }
        });

        //完成按钮
        audioRecordDialog.carryOutClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存录音文件
                audioRecordDialog.saveAudioData2();

                audioRecordDialog.resetStatus();
                audioRecordDialog.dismissDialog();
            }
        });


        if (!audioRecordDialog.isShowing()) {
            audioRecordDialog.show();
        }
    }

    /**
     * 显示音频录制 是否需要保存文件的选择弹框
     *
     * @param audioRecordDialog
     */
    public void showAudioRecordConfirmDialog(AudioRecordDialog audioRecordDialog) {
        if (!StringUtils.isBlank(AudioRecordManager.getInstance().getRecordFilePath())) {
            ChoiceDialog choiceDialog = new ChoiceDialog(mActivity, mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_375x));

            String title = mActivity.getResources().getString(R.string.audiorecord_title);
            String content = mActivity.getResources().getString(R.string.audiorecord_content);
            String no = mActivity.getResources().getString(R.string.logout_no);
            String yes = mActivity.getResources().getString(R.string.logout_yes);
            choiceDialog.setTextInformation(title, content, no, yes);
            choiceDialog.setDialogBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_30x), -1, "", "#FFFFFF");
            choiceDialog.setCancelBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", "#F7F8F9");
            choiceDialog.setConfirmBtnBG(mActivity.getResources().getDimensionPixelSize(R.dimen.dimen_15x), -1, "", VariableConfig.color_button_selected);

            choiceDialog.confirmButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //保存录音文件
                    audioRecordDialog.saveAudioData2();

                    audioRecordDialog.resetStatus();

                    choiceDialog.dismissDialog();
                    audioRecordDialog.dismissDialog();
                }
            });

            choiceDialog.notSure(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //不保存录音文件
                    audioRecordDialog.deleteAudioData();

                    audioRecordDialog.resetStatus();
                    choiceDialog.dismissDialog();
                    audioRecordDialog.dismissDialog();
                }
            });

            choiceDialog.closeWindow(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //不保存录音文件
                    audioRecordDialog.deleteAudioData();

                    audioRecordDialog.resetStatus();
                    choiceDialog.dismissDialog();
                    audioRecordDialog.dismissDialog();
                }
            });

            if (!choiceDialog.isShowing()) {
                choiceDialog.show();
            }
        } else {
            audioRecordDialog.dismissDialog();
        }
    }

    /**
     * 显示企业网盘弹框
     */
    public void showNetworkDiskDialog() {
        NetworkDiskDialog networkDiskDialog = new NetworkDiskDialog(mActivity);
//        networkDiskDialog.setInitDatas("3319", networkDiskBeansSelected);
        networkDiskDialog.setInitDatas("3424", networkDiskBeansSelected, 0, 0, 0, 0);
        networkDiskDialog.setNetworkDiskDialogListener(this);
        if (!networkDiskDialog.isShowing()) {
            networkDiskDialog.show();
        }
    }


    /**
     * 企业网盘确定提交回调
     *
     * @param networkDiskBeansSelected
     */

    private List<NetworkDiskEntity.DataBean> networkDiskBeansSelected;

    @Override
    public void onNetworkDiskConfirmCallback(List<NetworkDiskEntity.DataBean> networkDiskBeansSelected, int pics_selected, int videos_selected, int audios_selected, int files_selected) {
        LogUtils.i(ConfigConstants.TAG_ALL, "networkDiskBeansSelected =-= " + networkDiskBeansSelected.size());
        this.networkDiskBeansSelected = networkDiskBeansSelected;
    }
}
