package com.iolll.liubo.autosimple.compile.autoObserver;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * Created by LiuBo on 2018/6/15.
 */
public class AutoSmartRefreshLayoutListener implements AutoRefreshListener {
    private RefreshLayout refreshLayout;

    public AutoSmartRefreshLayoutListener(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void finishRefresh(boolean success) {
        refreshLayout.finishRefresh(success);
    }

    @Override
    public void finishRefresh(boolean success, boolean hasNext) {
        refreshLayout.setNoMoreData(!hasNext);
        refreshLayout.finishRefresh(success);
    }

    @Override
    public void finishRefresh() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void finishLoadMore(boolean success) {
        refreshLayout.finishLoadMore(success);
    }

    @Override
    public void finishLoadMore(boolean success, boolean hasNext) {
        refreshLayout.finishLoadMore(0,success,!hasNext);
    }

    @Override
    public void finishLoadMore() {
        refreshLayout.finishLoadMore();
    }
}
