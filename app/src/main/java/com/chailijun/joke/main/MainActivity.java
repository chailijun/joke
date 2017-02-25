package com.chailijun.joke.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chailijun.joke.R;
import com.chailijun.joke.base.BaseActivity;
import com.chailijun.joke.joke.JokeFragment;
import com.chailijun.joke.joke.JokePresenter;
import com.chailijun.joke.picture.PictureFragment;
import com.chailijun.joke.picture.PicturePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private long firstTime = 0;

    private PagerAdapter mSectionsPagerAdapter;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragments;
    private List<String> mTitles;

    private JokePresenter mJokePresenter;
    private PicturePresenter mPicturePresenter;


    @Override
    public void initParms(Bundle parms) {
        setSteepStatusBar(false);
        setSetNavigationBar(false);
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            //隐藏默认标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setIcon(R.drawable.logo);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mFragments = new ArrayList<>();
        PictureFragment pictureFragment = PictureFragment.newInstance(1);
        JokeFragment jokeFragment = JokeFragment.newInstance(2);

        mPicturePresenter = new PicturePresenter(pictureFragment);
        mJokePresenter = new JokePresenter(jokeFragment);

        mFragments.add(pictureFragment);
        mFragments.add(jokeFragment);


        mTitles = new ArrayList<>();
        mTitles.add("趣图");
        mTitles.add("段子");

        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(),mFragments,mTitles);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void setListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void onBackPressed() {

        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            showToast(getString(R.string.leave_app)+getString(R.string.app_name));
            firstTime = secondTime;//更新firstTime
        }else {
            super.onBackPressed();
        }
    }
}
