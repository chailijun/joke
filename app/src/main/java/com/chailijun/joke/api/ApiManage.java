package com.chailijun.joke.api;

import com.chailijun.joke.JokeApp;
import com.chailijun.joke.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManage {

    private static final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (NetWorkUtil.isNetWorkAvailable(JokeApp.getContext())) {
                int maxAge = 1; // 在线缓存在1秒内可读取
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("User-Agent")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 离线时缓存保存4周
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
    public static ApiManage apiManage;
    private static File httpCacheDirectory = new File(JokeApp.getContext().getCacheDir(), "joke_Cache");
    private static Cache cache = new Cache(httpCacheDirectory, 1024 * 1024 * 100);//100M
    private final Object monitor = new Object();
    private static final long DEFAULT_TIMEOUT = 5000;
    private static OkHttpClient mOkHttpClient;

    private ApiHost apiHost;
    private ApiHost2 apiHost2;

    private static void initOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (ApiManage.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .cache(cache)
                            .build();
                }
            }
        }

    }

    public static ApiManage getInstence() {
        if (apiManage == null) {
            synchronized (ApiManage.class) {
                if (apiManage == null) {
                    apiManage = new ApiManage();
                }
            }
        }
        return apiManage;
    }

    public ApiHost getApiHost() {
        if (apiHost == null) {
            synchronized (monitor) {
                if (apiHost == null) {
                    initOkHttpClient();
                    apiHost = new Retrofit.Builder()
                            .baseUrl("http://japi.juhe.cn/joke/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ApiHost.class);
                }
            }
        }
        return apiHost;
    }

    public ApiHost2 getApiHost2() {
        if (apiHost2 == null) {
            synchronized (monitor) {
                if (apiHost2 == null) {
                    initOkHttpClient();
                    apiHost2 = new Retrofit.Builder()
                            .baseUrl("http://v.juhe.cn/joke/")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build().create(ApiHost2.class);
                }
            }
        }
        return apiHost2;
    }
}
