package com.iolll.liubo.autosimple.data;

import com.iolll.liubo.autosimple.compile.autoObserver.RxTransformer;
import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by LiuBo on 2018/6/19.
 */
public class DataManager {
    public static Observable<ApiResult<ArrayList<EchoSearch>>> echoSearchByName(String name, int page){
        return RxRetrofitManager.getInstance().getApiService().echoSearchByName(name,page,10)
                .concatMap(RxTransformer.observableTransformer(0))
                .onErrorResumeNext(new RxTransformer.HttpResultFunc<>())//异常拦截 转换
                .compose(RxTransformer.transformer());


    }
    public static Observable<ApiResult<String>> upload(String imgPath){
        File file = new File(imgPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("multipartFile", "multipartFile.jpeg", requestFile);
        MultipartBody.Part[] files = new MultipartBody.Part[2];
        files[0] =body;
        body = MultipartBody.Part.createFormData("multipartFile", "multipartFile2.jpeg", requestFile);
        files[1] =body;
        return RxRetrofitManager.getInstance().getApiService().up("123",files)
//                .concatMap(RxTransformer.observableTransformer(0))
//                .onErrorResumeNext(new RxTransformer.HttpResultFunc<>())//异常拦截 转换
                .compose(RxTransformer.transformer());
    }

}
