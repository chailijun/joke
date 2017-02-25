package com.chailijun.joke.api;

import com.chailijun.joke.data.RandData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiHost2 {

    @GET("randJoke.php")
    Observable<RandData> getRandJoke(@Query("type") String type, @Query("key") String key);
}
