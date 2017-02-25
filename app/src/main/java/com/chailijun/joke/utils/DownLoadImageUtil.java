package com.chailijun.joke.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.chailijun.joke.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class DownLoadImageUtil implements Runnable {

    private String url;
    private Context context;
    private ImageDownLoadCallBack callBack;
    private File currentFile;

    public DownLoadImageUtil(Context context, String url, ImageDownLoadCallBack callBack) {
        this.url = url;
        this.callBack = callBack;
        this.context = context;
    }

    @Override
    public void run() {


        /*if (url.endsWith(".gif")){
            //保存gif动图
            GifDrawable gif = Imager.getGif(context, url);
        }else {

        }*/

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            if (bitmap != null){
                // 在这里执行图片保存方法
                saveImageToGallery(context,bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            if (bitmap != null && currentFile.exists()) {
                callBack.onDownLoadSuccess(bitmap);
                callBack.onDownLoadSuccess(currentFile);
            } else {
                callBack.onDownLoadFailed();
            }
        }
    }

    private void saveImageToGallery(Context context, Bitmap bmp) {
        // 图片保存路径
        File file = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsoluteFile();

        String fileName = context.getString(R.string.image_download_foldername);//文件夹
        File appDir = new File(file ,fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //图片的名称
        int lastIndex = url.lastIndexOf("/");
        fileName = url.substring(lastIndex+1);

        currentFile = new File(appDir, fileName);

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));

    }

    public interface ImageDownLoadCallBack {

        void onDownLoadSuccess(File file);

        void onDownLoadSuccess(Bitmap bitmap);

        void onDownLoadFailed();
    }
}
