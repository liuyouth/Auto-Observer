package com.iolll.liubo.autosimple;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.iolll.liubo.autosimple.compile.loadSirCallback.CustomCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.EmptyCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.ErrorCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.LoadingCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.TimeoutCallback;
import com.iolll.liubo.autosimple.utils.MyUtils;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by LiuBo on 2018/6/19.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyUtils.init(this);
        Utils.init(this);
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())//'添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
    }
}
