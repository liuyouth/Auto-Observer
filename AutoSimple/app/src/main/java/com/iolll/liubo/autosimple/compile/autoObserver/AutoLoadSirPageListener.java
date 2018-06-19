package com.iolll.liubo.autosimple.compile.autoObserver;


import com.iolll.liubo.autosimple.compile.loadSirCallback.EmptyCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.ErrorCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.LoadingCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.TimeoutCallback;
import com.kingja.loadsir.core.LoadService;

/**
 * Created by LiuBo on 2018/6/15.
 */
public class AutoLoadSirPageListener implements AutoSwitchStatusPageObserver {
    private LoadService loadService;

    public AutoLoadSirPageListener(LoadService loadService) {
        this.loadService = loadService;
    }

    @Override
    public void onSuccess() {
        loadService.showSuccess();
    }

    @Override
    public void onError( ) {
        loadService.showCallback(ErrorCallback.class);
    }

    @Override
    public void onEmpty( ) {
        loadService.showCallback(EmptyCallback.class);
    }

    @Override
    public void onTimeout( ) {
        loadService.showCallback(TimeoutCallback.class);
    }

    @Override
    public void onLoading( ) {
        loadService.showCallback(LoadingCallback.class);
    }
}
