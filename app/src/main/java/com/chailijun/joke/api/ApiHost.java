package com.chailijun.joke.api;

import com.chailijun.joke.base.BaseData;
import com.chailijun.joke.data.JokeBean;
import com.chailijun.joke.data.PictureBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiHost {

    //按更新时间查询笑话
    @GET("content/list.from")
    Observable<BaseData<JokeBean>> getJokeList(@Query("sort") String sort,
                                               @Query("page") int page,
                                               @Query("pagesize") int pagesize,
                                               @Query("time") String time,
                                               @Query("key") String key);

    //最新笑话
    @GET("content/text.from")
    Observable<BaseData<JokeBean>> getJokeNewest(@Query("page") int page,
                                                 @Query("pagesize") int pagesize,
                                                 @Query("key") String key);

    //按更新时间查询趣图
    @GET("img/list.from")
    Observable<BaseData<PictureBean>> getPictureList(@Query("sort") String sort,
                                                     @Query("page") int page,
                                                     @Query("pagesize") int pagesize,
                                                     @Query("time") String time,
                                                     @Query("key") String key);

    //最新趣图
    @GET("img/text.from")
    Observable<BaseData<PictureBean>> getPictureNewest(@Query("page") int page,
                                                       @Query("pagesize") int pagesize,
                                                       @Query("key") String key);


}
