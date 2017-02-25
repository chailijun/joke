package com.chailijun.joke;

import android.app.Application;
import android.content.Context;

public class JokeApp extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
