package com.iolll.liubo.autosimple.compile.autoObserver;

/**
 * Created by LiuBo on 2018/6/15.
 */
public interface AutoRefreshListener {
    void finishRefresh(boolean success);
    void finishRefresh();

    void finishLoadMore(boolean success);
    void finishLoadMore();

}