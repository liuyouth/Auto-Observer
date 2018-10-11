package com.iolll.liubo.autosimple.compile.autoObserver;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iolll.liubo.autosimple.R;
import com.iolll.liubo.autosimple.compile.loadSirCallback.EmptyCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.ErrorCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.LoadingCallback;
import com.iolll.liubo.autosimple.compile.loadSirCallback.StatusSetting;
import com.iolll.liubo.autosimple.compile.loadSirCallback.TimeoutCallback;
import com.iolll.liubo.autosimple.utils.MyUtils;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.Transport;

/**
 * 自动加载状态页面监听
 * tag 永远处于有值状态才可运行
 * Created by LiuBo on 2018/6/15.
 */
public class AutoLoadSirPageListener implements AutoSwitchStatusPageListener {
    private LoadService loadService;
    private String tag = "Defult";
    public AutoLoadSirPageListener(LoadService loadService) {
        this.loadService = loadService;
    }
    public AutoLoadSirPageListener(LoadService loadService, String tag) {
        this.loadService = loadService;
        this.tag = tag;
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
        loadService.setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                StatusSetting.EmptyBean emptyBean = StatusSetting.emptyMap.get(tag);
                if (emptyBean != null) {
                    ((TextView) view.findViewById(R.id.empty_show_msg)).setText(emptyBean.msg);
                    ((ImageView) view.findViewById(R.id.empty_img)).setImageDrawable(MyUtils.getDrawable(emptyBean.imgId));
                }
            }
        });
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
