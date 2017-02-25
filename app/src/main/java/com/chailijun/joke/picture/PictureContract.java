package com.chailijun.joke.picture;

import com.chailijun.joke.base.BasePresenter;
import com.chailijun.joke.base.BaseView;
import com.chailijun.joke.data.Item;

import java.util.List;


public interface PictureContract {

    interface View extends BaseView<Presenter> {

        /**
         * 显示加载最新图片
         */
        void showPictureNewest(List<Item> pictures);

        /**
         * 显示加载最新图片错误
         */
        void showLoadNewestError(String msg);

        /**
         * 显示没有最新图片
         */
        void showNoPictureNewest();

        /**
         * 显示加载随机图片
         */
        void showPictureRand(List<Item> pictures);

        /**
         * 显示加载随机图片错误
         */
        void showLoadRandError(String msg);

        /**
         * 显示没有随机图片
         */
        void showNoPictureRand();
    }

    interface Presenter extends BasePresenter {

        /**
         * 加载最新图片
         */
        void loadPictureNewest(int page, int pagesize);

        /**
         * 加载随机图片
         */
        void loadPictureRand();
    }
}
