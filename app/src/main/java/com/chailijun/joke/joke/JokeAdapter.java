package com.chailijun.joke.joke;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chailijun.joke.R;
import com.chailijun.joke.data.Item;

import java.util.List;

public class JokeAdapter extends BaseQuickAdapter<Item,BaseViewHolder>{

    public JokeAdapter(List<Item> data) {
        super(R.layout.fragment_joke_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Item item) {
        helper.setText(R.id.tv_content,item.getContent());

        if (!TextUtils.isEmpty(item.getUpdatetime())){
            helper.setText(R.id.tv_updatetime,item.getUpdatetime());
        }else {
            helper.setText(R.id.tv_updatetime,mContext.getString(R.string.recommend));
        }
    }
}
