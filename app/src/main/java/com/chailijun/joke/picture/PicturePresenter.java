package com.chailijun.joke.picture;

import android.support.annotation.NonNull;

import com.chailijun.joke.api.ApiManage;
import com.chailijun.joke.base.BaseData;
import com.chailijun.joke.data.Item;
import com.chailijun.joke.data.PictureBean;
import com.chailijun.joke.data.RandData;
import com.chailijun.joke.utils.Constants;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class PicturePresenter implements PictureContract.Presenter{

    @NonNull
    private PictureContract.View mPictureView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    public PicturePresenter(@NonNull PictureContract.View pictureView) {
        mPictureView = pictureView;

        mSubscriptions = new CompositeSubscription();
        mPictureView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadPictureNewest(int page, int pagesize) {
        Subscription subscription = ApiManage.getInstence()
                .getApiHost().getPictureNewest(page, pagesize,Constants.KEY)
                .map(new Func1<BaseData<PictureBean>, PictureBean>() {
                    @Override
                    public PictureBean call(BaseData<PictureBean> pictureBeanData) {
                        if (pictureBeanData.getError_code() == 0 &&
                                pictureBeanData.getReason().equalsIgnoreCase("Success")){
                            return pictureBeanData.getResult();
                        }
                        throw new RuntimeException("Request picture newest error!");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PictureBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPictureView.showLoadNewestError(e.getMessage());
                    }

                    @Override
                    public void onNext(PictureBean pictureBean) {
                        if (pictureBean.getData() != null && pictureBean.getData().size() > 0){
                            mPictureView.showPictureNewest(pictureBean.getData());
                        }else {
                            mPictureView.showNoPictureNewest();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadPictureRand() {
        Subscription subscription = ApiManage.getInstence().getApiHost2()
                .getRandJoke(Constants.TYPE_PIC, Constants.KEY)
                .map(new Func1<RandData,List<Item>>() {
                    @Override
                    public List<Item> call(RandData randData) {
                        if (randData.getError_code() == 0 &&
                                randData.getReason().equalsIgnoreCase("success")){
                            return randData.getResult();
                        }
                        throw new RuntimeException("Request rand picture error!");
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
                        mPictureView.showLoadRandError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        if (items != null && items.size() > 0){
                            mPictureView.showPictureRand(items);
                        }else {
                            mPictureView.showNoPictureRand();
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }
}
