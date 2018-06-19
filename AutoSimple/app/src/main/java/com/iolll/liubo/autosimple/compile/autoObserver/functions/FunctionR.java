package com.iolll.liubo.autosimple.compile.autoObserver.functions;


import com.iolll.liubo.autosimple.data.ApiResult;

/**
 * Rxjava Zip操作符 第三个参数
 * Created by LiuBo on 2018/5/30.
 */

public class FunctionR<T1, T2> extends ApiResult<FunctionR<T1,T2>> {
    public T1 a = null;
    public T2 b = null;

    public FunctionR(T1 a, T2 b) {
        this.a = a;
        this.b = b;
        super.setCode(200);
    }
}
