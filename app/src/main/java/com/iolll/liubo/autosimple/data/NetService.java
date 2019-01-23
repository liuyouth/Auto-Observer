package com.iolll.liubo.autosimple.data;

import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetService {
    final String BASE_URL = "http://app.hxhz.com/";

    @GET("http://www.wanandroid.com/banner/json")
    Observable<ApiResult> getBanner();

    @GET("http://140.143.242.232:8080/EchoSong/name/")
    Observable<ApiResult<ArrayList<EchoSearch>>> echoSearchByName(@Query("name") String name, @Query("page") int page, @Query("limit") int limit );
    @Multipart
    @POST("http://192.168.1.118:6066/static/img/upload")
    Observable<ApiResult<String>> up(@Part("id") String id, @Part MultipartBody.Part[] file);
}