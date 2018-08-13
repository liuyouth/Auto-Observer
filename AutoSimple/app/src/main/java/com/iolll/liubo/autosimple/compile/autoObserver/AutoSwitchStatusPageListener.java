package com.iolll.liubo.autosimple.compile.autoObserver;

/**
 * Created by LiuBo on 2018/6/15.
 */
public interface AutoSwitchStatusPageListener {
    void onSuccess();
    void onError();
    void onEmpty();
    void onTimeout();
    void onLoading();
}
