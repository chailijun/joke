package com.chailijun.joke.joke;

import com.chailijun.joke.base.BasePresenter;
import com.chailijun.joke.base.BaseView;
import com.chailijun.joke.data.Item;

import java.util.List;

public interface JokeContract {

    interface View extends BaseView<Presenter> {

        /**
         * 显示加载最新笑话
         */
        void showJokeNewest(List<Item> jokes);

        /**
         * 显示加载最新笑话错误
         */
        void showLoadNewestError(String msg);

        /**
         * 显示没有最新笑话
         */
        void showNoNewestJoke();

        /**
         * 显示加载随机笑话
         */
        void showJokeRand(List<Item> jokes);

        /**
         * 显示加载随机笑话错误
         */
        void showLoadRandError(String msg);

        /**
         * 显示没有随机笑话
         */
        void showNoJokeRand();

    }

    interface Presenter extends BasePresenter {

        /**
         * 加载最新笑话
         * @param page
         * @param pagesize
         */
        void loadJokeNewest(int page, int pagesize);

        /**
         * 加载随机笑话
         */
        void loadRandJoke();

    }
}
