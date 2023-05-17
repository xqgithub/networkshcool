package com.talkcloud.networkshcool.baselibrary.ui.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.luck.picture.lib.broadcast.BroadcastManager;
import com.talkcloud.networkshcool.baselibrary.R;
import com.talkcloud.networkshcool.baselibrary.TKExtManage;
import com.talkcloud.networkshcool.baselibrary.common.BaseConstant;
import com.talkcloud.networkshcool.baselibrary.common.ConfigConstants;
import com.talkcloud.networkshcool.baselibrary.entity.DownloadEntity;
import com.talkcloud.networkshcool.baselibrary.help.MySPUtilsUser;
import com.talkcloud.networkshcool.baselibrary.utils.ConvertUtils;
import com.talkcloud.networkshcool.baselibrary.utils.GlideUtils;
import com.talkcloud.networkshcool.baselibrary.utils.NSLog;
import com.talkcloud.networkshcool.baselibrary.utils.PublicPracticalMethodFromJAVA;
import com.talkcloud.networkshcool.baselibrary.utils.TKPrintUtil;
import com.talkcloud.networkshcool.baselibrary.utils.X5WebView;
import com.talkcloud.networkshcool.baselibrary.weiget.TKDocPreView;
import com.talkcloud.networkshcool.baselibrary.weiget.TKShareBottomView;
import com.talkcloud.networkshcool.baselibrary.weiget.dialog.TKShareDialog;

public class BrowserActivity extends FragmentActivity {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private ImageView icBack;
    private ImageView icDelete;
    private boolean isShowDelete = false;


    private static final String mHomeUrl = "https://static-demo.talk-cloud.net/static/preview_1.0/index.html?url=/cospath/bd2f6e05f0d31fab7f827389cdc174b1/newppt.html&filetype=pptx&pagenum=0&fileId=114438&showTip=true";
    private static final String TAG = "BrowserActivity";


    private String mIntentUrl;

    // 保证删除有效
    private String mId;

    // 标题
    TextView mTitleTv;

    // HeaderBar
    RelativeLayout mHeaderBarRl;
    // 是否显示标题
    private boolean isShowTitle = true;

    private TKShareDialog mShareDialog;

    private DownloadEntity downloadEntity;

    // 预览图片
    private ImageView mPreviewIv;

    // 文件
    private TKDocPreView mPreviewDocView;

    private boolean isPrintPhoto = false;

    private TKShareBottomView mShareBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            WebView.enableSlowWholeDocumentDraw();
//        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        PublicPracticalMethodFromJAVA.getInstance()
                .transparentStatusBar(
                        this,
                        true, true,
                        R.color.appwhite
                );

        setContentView(R.layout.activity_x5webview);
        mWebView = findViewById(R.id.webview_1);
        icBack = findViewById(R.id.img_back);
        icDelete = findViewById(R.id.img_delete);

        mTitleTv = findViewById(R.id.wv_title_tv);
        mTitleTv.setText(R.string.file_view);

        mHeaderBarRl = findViewById(R.id.wv_header_bar_rl);

        mPreviewIv = findViewById(R.id.mPreviewIv);

        mPreviewDocView = findViewById(R.id.mPreviewDocView);

        mShareBottomView = findViewById(R.id.mShareBottomView);

        Intent intent = getIntent();
        if (intent != null) {
            mIntentUrl = intent.getStringExtra("url");
            isShowDelete = intent.getBooleanExtra("isShowDelete", false);
            mId = intent.getStringExtra("id");

            isShowTitle = intent.getBooleanExtra("isShowTitle", true);

            downloadEntity = intent.getParcelableExtra(BaseConstant.KEY_PARAM1);
        }

        if (isShowTitle) {
            mTitleTv.setText(R.string.file_view);
            mTitleTv.setVisibility(View.VISIBLE);
            icBack.setImageResource(R.mipmap.ic_back_black);
            icBack.setPadding(0, 0, 0, 0);
        } else {
            mTitleTv.setVisibility(View.GONE);
            icBack.setImageResource(R.drawable.picture_icon_back);
            icBack.setPadding(ConvertUtils.dp2px(5f), 0, 0, 0);

            mHeaderBarRl.setBackgroundColor(getColor(android.R.color.black));
            PublicPracticalMethodFromJAVA.getInstance()
                    .transparentStatusBar(
                            this,
                            true, false,
                            android.R.color.black
                    );
        }

        NSLog.d("url : " + mIntentUrl);

        if (!isShowDelete) {
            if (ConfigConstants.IDENTITY_STUDENT.equals(MySPUtilsUser.getInstance().getUserIdentity())) {

                // TODO 定制不用友盟分享 注：sharelibrary 可以去掉
                if ("menke".equals(TKExtManage.getInstance().getUserAgreementUrl())) {
                    //  学生才有下载 分享
                    icDelete.setVisibility(View.VISIBLE);
                    icDelete.setImageResource(R.mipmap.ic_more_vertical);

                    mShareBottomView.setVisibility(View.GONE);
                } else {
                    icDelete.setVisibility(View.GONE);

                    if (downloadEntity != null) {
                        mShareBottomView.setVisibility(View.VISIBLE);
                        mShareBottomView.setDownloadInfo(downloadEntity);
                        mShareBottomView.setOnPrintListener(() -> {
                            doPrint();
                            return null;
                        });
                    }
                }
            } else {
                icDelete.setVisibility(View.GONE);
            }
        } else {
            icDelete.setVisibility(View.VISIBLE);
            icDelete.setImageResource(R.drawable.icon_trash_del);
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

        /*
         * getWindow().addFlags(
         * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */

        // 打印图片用
        if (downloadEntity != null) {
            if (downloadEntity.getDownloadPath().endsWith(".png") || downloadEntity.getDownloadPath().contains(".jpg") ||
                    downloadEntity.getDownloadPath().endsWith(".jpeg") || downloadEntity.getDownloadPath().contains(".bmp") || downloadEntity.getDownloadPath().endsWith(".jpeg")) {
                isPrintPhoto = true;
                GlideUtils.loadImageFitCenter(this, downloadEntity.getDownloadPath(), mPreviewIv);
            }
        }

        // 临时方案 后期会转化为可预览地址
        if (mIntentUrl.endsWith("zip") || mIntentUrl.endsWith(".pdf") || mIntentUrl.endsWith(".txt") ||
                mIntentUrl.endsWith("doc") || mIntentUrl.endsWith(".docx") ||
                mIntentUrl.endsWith("ppt") || mIntentUrl.endsWith(".pptx") ||
                mIntentUrl.endsWith("xls") || mIntentUrl.endsWith(".xlsx")) {
            if (downloadEntity != null) {
                mPreviewDocView.setPreviewInfo(downloadEntity);
                mPreviewDocView.setVisibility(View.VISIBLE);
            }

        } else {
            mPreviewDocView.setVisibility(View.GONE);
            if (mIntentUrl != null) {
                mWebView.loadUrl(mIntentUrl);
            }
        }

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isShowDelete) {

                    if (downloadEntity != null) {
                        //表明已经有这个权限了
                        if (mShareDialog == null) {
                            mShareDialog = new TKShareDialog(BrowserActivity.this);
                        }
                        mShareDialog.setDownloadInfo(downloadEntity);
                        mShareDialog.show();

                    } else {

                    }

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(BaseConstant.EXTRA_DELETE_EXT_ID, mId);
                    BroadcastManager.getInstance(BrowserActivity.this)
                            .action(BaseConstant.ACTION_DELETE_EXT)
                            .extras(bundle).broadcast();

                    finish();
                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {

        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }


    // 打印webview
    public void doPrint() {
        if (isPrintPhoto) {
            printPhoto();
        } else {
            printWeb();
        }
    }


    private void printWeb() {
//        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
//        PrintDocumentAdapter printAdapter = mWebView.createPrintDocumentAdapter();
//        String jobName = getString(R.string.app_name) + " Document";
//        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

        TKPrintUtil.doPhotoPrint2(this, mWebView);

    }

    public void printPhoto() {
        TKPrintUtil.doPhotoPrint(this, mPreviewIv);
    }

}
