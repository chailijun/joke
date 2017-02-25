package com.chailijun.joke.picture;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chailijun.joke.R;
import com.chailijun.joke.base.BaseFragment;
import com.chailijun.joke.data.Item;
import com.chailijun.joke.utils.Logger;

import java.util.List;

public class PictureFragment extends BaseFragment implements PictureContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private PictureAdapter mAdapter;

    private PictureContract.Presenter mPresenter;

    private int page = 1;
    private int pagesize = 10;

    private View loadingView;
    private View errorView;

    private boolean isFirst = true;

    public PictureFragment() {
    }

    public static PictureFragment newInstance(int sectionNumber) {
        PictureFragment fragment = new PictureFragment();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_pic;
    }

    @Override
    public void initView(View view) {
        mSwipeRefreshLayout = $(view, R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorTheme));
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView = $(view, R.id.rv_pic);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PictureAdapter(null);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);

        //加载中
        loadingView = getActivity().getLayoutInflater()
                .inflate(R.layout.loading, (ViewGroup) mRecyclerView.getParent(), false);
        //加载错误
        errorView = getActivity().getLayoutInflater()
                .inflate(R.layout.loading_failed, (ViewGroup) mRecyclerView.getParent(), false);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                onRefresh();
                doBusiness(getActivity());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void doBusiness(Context mContext) {

        mAdapter.setEmptyView(loadingView);
        mPresenter.loadPictureNewest(page++,pagesize);
    }

    @Override
    public void widgetClick(View v) {

    }


    @Override
    public void showPictureNewest(List<Item> pictures) {

        isFirst = false;

        mSwipeRefreshLayout.setEnabled(true);
        mAdapter.addData(pictures);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadNewestError(String msg) {
        Logger.e(TAG,"Request picture list error:"+msg);
        mSwipeRefreshLayout.setEnabled(true);

        if (isFirst){
            mAdapter.setEmptyView(errorView);
        }else {
            mAdapter.loadMoreFail();
        }
        page = page-- < 0 ? 1 : page;
    }

    @Override
    public void showNoPictureNewest() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showPictureRand(List<Item> pictures) {
        mAdapter.setNewData(pictures);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadRandError(String msg) {
        Logger.e(TAG,"Request rand picture error:"+msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoPictureRand() {
        Logger.e(TAG,"No rand picture!");
    }

    @Override
    public void setPresenter(PictureContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onLoadMoreRequested() {

        mPresenter.loadPictureNewest(page++,pagesize);
    }

    @Override
    public void onRefresh() {
        Logger.e(TAG,"Load rand picture!");

        page = 1;
        isFirst = true;

        mPresenter.loadPictureRand();
    }
}
