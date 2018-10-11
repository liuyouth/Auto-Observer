package com.iolll.liubo.autosimple.data;

import com.iolll.liubo.autosimple.compile.autoObserver.RxTransformer;
import com.iolll.liubo.iolllmusic.data.model.echo.EchoSearch;

import java.util.ArrayList;

import io.reactivex.Observable;

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

}
