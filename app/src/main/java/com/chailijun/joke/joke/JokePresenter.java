package com.chailijun.joke.joke;

import android.support.annotation.NonNull;

import com.chailijun.joke.api.ApiManage;
import com.chailijun.joke.base.BaseData;
import com.chailijun.joke.data.Item;
import com.chailijun.joke.data.JokeBean;
import com.chailijun.joke.data.RandData;
import com.chailijun.joke.utils.Constants;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class JokePresenter implements JokeContract.Presenter{

    @NonNull
    private JokeContract.View mJokeView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    public JokePresenter(@NonNull JokeContract.View jokeView) {
        mJokeView = jokeView;

        mSubscriptions = new CompositeSubscription();
        mJokeView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void loadJokeNewest(int page, int pagesize) {
        Subscription subscription = ApiManage.getInstence().getApiHost()
                .getJokeNewest(page, pagesize,Constants.KEY)
                .map(new Func1<BaseData<JokeBean>, JokeBean>() {
                    @Override
                    public JokeBean call(BaseData<JokeBean> jokeBeanBaseData) {
                        if (jokeBeanBaseData.getError_code() == 0 &&
                                jokeBeanBaseData.getReason().equalsIgnoreCase("Success")){
                            return jokeBeanBaseData.getResult();
                        }
                        throw new RuntimeException("Request joke list error!");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JokeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mJokeView.showLoadNewestError(e.getMessage());
                    }

                    @Override
                    public void onNext(JokeBean jokeBean) {
                        if (jokeBean.getData() != null && jokeBean.getData().size() > 0){
                            mJokeView.showJokeNewest(jokeBean.getData());
                        }else {
                            mJokeView.showNoNewestJoke();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadRandJoke() {
        Subscription subscription = ApiManage.getInstence().getApiHost2()
                .getRandJoke("", Constants.KEY)
                .map(new Func1<RandData, List<Item>>() {
                    @Override
                    public List<Item> call(RandData randData) {
                        if (randData.getError_code() == 0 &&
                                randData.getReason().equalsIgnoreCase("success")){
                            return randData.getResult();
                        }
                        throw new RuntimeException("Request rand joke error!");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mJokeView.showLoadRandError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        if (items != null && items.size() > 0){
                            mJokeView.showJokeRand(items);
                        }else {
                            mJokeView.showNoJokeRand();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }
}
