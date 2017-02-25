package com.chailijun.joke.joke;

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

public class JokeFragment extends BaseFragment implements JokeContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ARGUMENT = "argument";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private JokeAdapter mAdapter;

    private JokeContract.Presenter mPresenter;
    private int page = 1;
    private int pagesize = 10;

    private View loadingView;
    private View errorView;
    private boolean isFirst = true;

    public JokeFragment() {
    }

    public static JokeFragment newInstance(int sectionNumber) {
        JokeFragment fragment = new JokeFragment();
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
        return R.layout.fragment_joke;
    }

    @Override
    public void initView(View view) {

        mSwipeRefreshLayout = $(view, R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorTheme));
        mSwipeRefreshLayout.setEnabled(false);

        mRecyclerView = $(view, R.id.rv_joke);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new JokeAdapter(null);
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
        mPresenter.loadJokeNewest(page++,pagesize);
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void showJokeNewest(List<Item> jokes) {

        isFirst = false;

        mSwipeRefreshLayout.setEnabled(true);
        mAdapter.addData(jokes);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showLoadNewestError(String msg) {
        Logger.e(TAG,"Request joke newest error:"+msg);
        mSwipeRefreshLayout.setEnabled(true);

        if (isFirst){
            mAdapter.setEmptyView(errorView);
        }else {
            mAdapter.loadMoreFail();
        }
        page = page-- < 0 ? 1 : page;
    }

    @Override
    public void showNoNewestJoke() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void showJokeRand(List<Item> jokes) {
        mAdapter.setNewData(jokes);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadRandError(String msg) {
        Logger.e(TAG,"Request rand joke error:"+msg);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNoJokeRand() {
        Logger.e(TAG,"No rand joke!");
    }

    @Override
    public void setPresenter(JokeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadJokeNewest(page++,pagesize);
    }

    @Override
    public void onRefresh() {
        Logger.e(TAG,"Load rand joke!");

        isFirst = true;
        page = 1;
        mPresenter.loadRandJoke();
    }
}
