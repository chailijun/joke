package com.chailijun.joke.fullpicture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chailijun.joke.R;
import com.chailijun.joke.base.BaseActivity;
import com.chailijun.joke.utils.Constants;
import com.chailijun.joke.utils.DownLoadImageUtil;
import com.chailijun.joke.utils.Imager;
import com.chailijun.joke.utils.Logger;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

public class FullPictureActivity extends BaseActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    private SubsamplingScaleImageView mImageView;
    private ImageView mImageViewGif;
    private ProgressBar mProgressBar;

    private String url;

    @Override
    public void initParms(Bundle parms) {
        Intent intent = getIntent();
        url = intent.getStringExtra(Constants.PIC_URL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_full_picture;
    }

    @Override
    public void initView(View view) {
        mProgressBar = $(R.id.progressbar);
        mImageViewGif = $(R.id.iv_gif);

        mImageView = $(R.id.imageView);
        mImageView.setMinScale(0.5F);//最小显示比例
        mImageView.setMaxScale(10.0F);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）
    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (url.endsWith(".gif")){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImageViewGif.setVisibility(View.VISIBLE);
                            mImageView.setVisibility(View.GONE);

                            Imager.loadGif(FullPictureActivity.this,url,mImageViewGif);

                            mProgressBar.setVisibility(View.GONE);
                        }
                    });


                }else {
                    mImageViewGif.setVisibility(View.GONE);
                    mImageView.setVisibility(View.VISIBLE);

                    final Bitmap bitmap = Imager.getBitmap(url);

                    if (bitmap != null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImage(ImageSource.bitmap(bitmap)
                                        ,new ImageViewState(1.0F, new PointF(0, 0), 0));

                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                downloadImage();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片
     */
    private void downloadImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        DownLoadImageUtil downLoadImageUtil = new DownLoadImageUtil(getApplicationContext(), url,
                new DownLoadImageUtil.ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(final File file) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片保存到:"+file);
                            }
                        });
                    }

                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        Logger.d(TAG,"图片加载成功!");
                    }

                    @Override
                    public void onDownLoadFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("图片保存失败");
                            }
                        });
                    }
                });

        //启动图片下载线程
        new Thread(downLoadImageUtil).start();
    }
}
