package com.talkcloud.sharelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.talkcloud.sharelibrary.model.TKShareLinkModel;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Author  guoyw
 * Date    2021/8/18
 * Desc     在应用层建一个统一管理类 方便后续管理
 */
public class TKShareApi2 {

    private static volatile TKShareApi2 mInstance;

    private Context mContext;

    private TKShareApi2() {
        if (mInstance != null) {
            throw new RuntimeException("instance is exist!");
        }
    }

    public static TKShareApi2 getInstance() {
        if (mInstance == null) {
            synchronized (TKShareApi2.class) {
                if (mInstance == null) {
                    mInstance = new TKShareApi2();
                }
            }
        }
        return mInstance;
    }

    public void preInit(Context ctx) {
        //友盟预初始化
        UMConfigure.preInit(ctx, TKShareConstants2.TK_UMENG_APP_KEY, TKShareConstants2.TK_UMENG_CHANNEL);
    }


    public void init(Context ctx) {

        this.mContext = ctx;

        UMConfigure.init(ctx, TKShareConstants2.TK_UMENG_APP_KEY, TKShareConstants2.TK_UMENG_CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE, "");

        UMConfigure.setLogEnabled(true);

        initOtherPlatform();
    }


    private void initOtherPlatform() {
        // 微信设置
        PlatformConfig.setWeixin(TKShareConstants2.TK_WX_APP_KEY, TKShareConstants2.TK_WX_SECRET);
        PlatformConfig.setWXFileProvider("com.talkcloud.networkshcool.fileprovider");
        //钉钉设置
        PlatformConfig.setDing(TKShareConstants2.TK_DING_KEY);
        PlatformConfig.setDingFileProvider("com.talkcloud.networkshcool.fileprovider");
    }


    //QQ与新浪QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中
    public void onActResult(Context ctx, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(ctx).onActivityResult(requestCode, resultCode, data);
    }


    // 分享链接
    public void shareLink(final Context ctx, final int type, TKShareLinkModel linkModel) {

        final UMWeb web = new UMWeb(linkModel.getUrl());
        web.setTitle(linkModel.getTitle());//标题
        web.setDescription(linkModel.getDesc());//描述

//        UMImage thumb = new UMImage(ctx, linkModel.getIcon());
//        web.setThumb(thumb);//缩略图

        Log.d("goyw", "share web" + web);

        new ShareAction((Activity) ctx)
                .withMedia(web)
                .setPlatform(getShareMedia(type))
                .setCallback(shareListener)
                .share();

    }


    //分享图片
    public void shareImg(Context ctx, int type, Bitmap bitmap) {

        UMImage image = new UMImage(ctx, bitmap);
        image.setThumb(image);

        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressFormat = Bitmap.CompressFormat.PNG;

        new ShareAction((Activity) ctx)
                .withMedia(image).setPlatform(getShareMedia(type))
                .setCallback(shareListener)
                .share();
    }

    // 判断是否安装
    public boolean isInstallApp(Context ctx, int type) {
        UMShareAPI mShareAPI = UMShareAPI.get(ctx);
        return mShareAPI.isInstall((Activity) ctx, getShareMedia(type));
    }


    private SHARE_MEDIA getShareMedia(int type) {
        SHARE_MEDIA mediaType = SHARE_MEDIA.WEIXIN;

        if (TKShareConstants2.TYPE_WX == type) {
            mediaType = SHARE_MEDIA.WEIXIN;
        } else if (TKShareConstants2.TYPE_DD == type) {
            mediaType = SHARE_MEDIA.DINGTALK;
        }

        return mediaType;
    }



    UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.d("goyw", "onStart");
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
//            Toast.makeText(mContext, "分享成功了", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.d("goyw", "onError: " + throwable.getMessage());
            Toast.makeText(mContext, "分享失败了", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
//            Toast.makeText(mContext, "分享取消了", Toast.LENGTH_LONG).show();
        }
    };


}
