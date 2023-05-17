package com.talkcloud.networkshcool.baselibrary.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKBaseApplication;
import com.talkcloud.networkshcool.baselibrary.common.ErrorCodeConstants;
import com.talkcloud.networkshcool.baselibrary.common.VariableConfig;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.jpush.TagAliasOperatorHelper;
import com.talkcloud.networkshcool.baselibrary.ui.activities.LoginPlusActivity;
import com.talkcloud.networkshcool.baselibrary.ui.dialog.CustomProgressDialog;
import com.talkcloud.networkshcool.baselibrary.utils.LogUtils;
import com.talkcloud.networkshcool.baselibrary.utils.NetworkUtils;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.StringUtils;
import com.talkcloud.networkshcool.baselibrary.utils.ToastUtils;
import com.tencent.mmkv.MMKV;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Headers;
import retrofit2.Response;

/**
 * Date:2021/5/10
 * Time:19:17
 * author:joker
 * 带有loading框的,在home键后台，接口能可以调用
 */
public abstract class BaseSubscriber<T> implements Observer<T> {

    public abstract void onSuccess(T t);

    public abstract void onFailure(String message, int error_code);

    private Context mContext;
    //是否显示弹框标识
    private boolean showdialogflag;
    //是否可以点击取消标识
    private boolean cancelableflag;

    public Disposable disposable;

    /**
     * @param mContext       Activity 上下文
     * @param showdialogflag 是否显示loading框
     * @param cancelableflag 物理返回键 是否消失
     */
    public BaseSubscriber(Context mContext,
                          boolean showdialogflag,
                          boolean cancelableflag) {
        this.mContext = mContext;
        this.showdialogflag = showdialogflag;
        this.cancelableflag = cancelableflag;
    }


    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;

        if (!NetworkUtils.isConnected()) {
            disposable.dispose();
            onFailure(TKBaseApplication.myApplication.getApplicationContext().getString(R.string.error_nonetwork), 800);
        } else {
            try {
                progressdialog(mContext, "",
                        showdialogflag,
                        cancelableflag,
                        true);
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    }

    @Override
    public void onNext(T t) {
//        Log.d("goyw", "onnext: " + t.toString());
        if (t instanceof Response) {
            Response response = (Response) t;

            int code = response.code();
            if (code == ErrorCodeConstants.TOKEN_ERROR) {//token错误，清空token，跳转到登录页面

                //清除极光推送别名
                if (mContext != null) {
                    JPushInterface.deleteAlias(mContext, 1);
                    MMKV.defaultMMKV().putString(TagAliasOperatorHelper.ALIAS_DATA, "");
                }

                VariableConfig.login_process = VariableConfig.login_process_normal;
                MySPUtilsUser.getInstance().saveUserToken("");
                PublicPracticalMethodFromJAVA.getInstance().intentToJump((Activity) mContext, LoginPlusActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK, null, true, R.anim.activity_xhold, R.anim.activity_xhold);
                ToastUtils.showShortToastFromRes(R.string.token_is_error, ToastUtils.top);
                return;
            }

            //获取Headers
            Headers headers = response.headers();
//            String headers_ = headers.toString();
            String Authorization = headers.get("Authorization");
            if (!StringUtils.isBlank(Authorization)) {
//                LogUtils.i(ConfigConstants.TAG_ALL, "更新过后的Authorization =-= " + Authorization);
                //保存token值
                MySPUtilsUser.getInstance().saveUserToken(Authorization);
            }


            ApiResponse br = (ApiResponse) response.body();
            if (br == null) {
                onFailure(TKBaseApplication.myApplication.getApplicationContext().getString(R.string.prompt_requestfailed), ExceptionHandle.INTERNAL_SERVER_ERROR);
                return;
            }
            if (br.getResult() == 0) {
                onSuccess(t);
            } else {
                onFailure(br.getMsg(), br.getResult());
            }
        } else if (t instanceof ApiResponse) {
            ApiResponse br = (ApiResponse) t;
            if (br == null) {
                onFailure(TKBaseApplication.myApplication.getApplicationContext().getString(R.string.prompt_requestfailed), ExceptionHandle.INTERNAL_SERVER_ERROR);
                return;
            }
            if (br.getResult() == 0) {
                onSuccess(t);
            } else {
                onFailure(br.getMsg(), br.getResult());
            }
        }

    }

    /**
     * 事件队列完成
     */
    @Override
    public void onComplete() {
        try {
            progressdialog(mContext, "",
                    showdialogflag,
                    cancelableflag,
                    false);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        } finally {
            disposable.dispose();
        }
    }

    /**
     * 事件队列异常
     */
    @Override
    public void onError(Throwable e) {
        try {
            LogUtils.e(e.getMessage());
            //1.发送错误信息
            onFailure(TKBaseApplication.myApplication.getApplicationContext().getString(R.string.prompt_busynetwork) + ExceptionHandle.handleException(e).code, 777);
//            2. 启动轮询弹框
//            StaticStateUtils.detectDomainWhetherNormal(App.getApplication());
            //3.取消掉loading框
            progressdialog(mContext, "",
                    showdialogflag,
                    cancelableflag,
                    false);
        } catch (Exception e1) {
            LogUtils.e(e1.getMessage());
        } finally {
            disposable.dispose();
        }
    }

    /**
     * 开始加载进度条
     *
     * @param context        上下文
     * @param msg            进度条内容
     * @param showdialog     是否创建dialog标识
     * @param cancelableflag dialog弹出后会点击屏幕或物理返回键是否有效果
     * @param showflag       adapter.show或者dialog.diss
     */
    private CustomProgressDialog cProgressDialog;

    private void progressdialog(Context context, String msg, boolean showdialog,
                                boolean cancelableflag, boolean showflag) {
        if (showdialog) {
            if (!((Activity) context).isFinishing()) {
                if (showflag) {
                    if (cProgressDialog == null) {
                        cProgressDialog = CustomProgressDialog
                                .createDialog(context, cancelableflag);
                        cProgressDialog.setMessage(msg);
                    }
                    cProgressDialog.show();
                } else {//停止加载
                    if (cProgressDialog != null && cProgressDialog.isShowing()) {
                        cProgressDialog.dismiss();
                        cProgressDialog = null;
                    }
                }
            } else {//停止加载
                if (cProgressDialog != null && cProgressDialog.isShowing()) {
                    cProgressDialog.dismiss();
                    cProgressDialog = null;
                }
            }
        }
    }


}
