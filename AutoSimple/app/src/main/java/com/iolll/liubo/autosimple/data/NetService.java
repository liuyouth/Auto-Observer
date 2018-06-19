package com.iolll.liubo.autosimple.data;

import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetService {
    final String BASE_URL = "http://app.hxhz.com/";

    @GET("http://www.wanandroid.com/banner/json")
    Observable<ApiResult> getBanner();

    @GET("http://140.143.242.232:8080/EchoSong/name/")
    Observable<ApiResult<ArrayList<EchoSearch>>> echoSearchByName(@Query("name") String name, @Query("page") int page, @Query("limit") int limit);
}