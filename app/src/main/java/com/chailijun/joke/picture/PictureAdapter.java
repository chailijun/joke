package com.chailijun.joke.picture;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.joke.R;
import com.chailijun.joke.data.Item;
import com.chailijun.joke.fullpicture.FullPictureActivity;
import com.chailijun.joke.utils.Constants;
import com.chailijun.joke.utils.Imager;

import java.util.List;


public class PictureAdapter extends BaseQuickAdapter<Item,BaseViewHolder>{

    public PictureAdapter(List<Item> data) {
        super(R.layout.fragment_pic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Item item) {

//        int height = mediaItem.getImage().getHeight() <= DensityUtil.getScreenHeight() * 0.75 ?
//                mediaItem.getImage().getHeight() : (int) (DensityUtil.getScreenHeight() * 0.75);
//        Bitmap bitmap = Imager.getBitmap(item.getUrl());
//        Logger.e("TAG","Height==="+bitmap.getHeight());
//        Logger.e("TAG","Width===="+bitmap.getWidth());

        helper.setText(R.id.tv_content,item.getContent());

        if (!TextUtils.isEmpty(item.getUpdatetime())){
            helper.setText(R.id.tv_updatetime,item.getUpdatetime());
        }else {
            helper.setText(R.id.tv_updatetime,mContext.getString(R.string.recommend));
        }

        final String url = item.getUrl();
        ImageView imageView = helper.getView(R.id.iv_img);
        if (url.endsWith(".gif")){
            //Gif动态
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else {
            //静态图片
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    (int)(DensityUtil.getScreenWidth()*0.9),(int)(DensityUtil.getScreenWidth()*0.8));
//            imageView.setLayoutParams(params);
        }

        Imager.load(mContext,url, (ImageView) helper.getView(R.id.iv_img));
        helper.addOnClickListener(R.id.iv_img);
        helper.getView(R.id.iv_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FullPictureActivity.class);
                intent.putExtra(Constants.PIC_URL,url);
                mContext.startActivity(intent);
            }
        });

    }
}
