package com.iolll.liubo.autosimple.compile.autoObserver.functions;

import io.reactivex.disposables.Disposable;

/**
 * 无异常 Consumer
 * Created by LiuBo on 2018/5/25.
 */

public interface OnSubscribe {
    boolean accept(Disposable d);
}
