package com.iolll.liubo.autosimple.compile.autoObserver;

/**
 * Created by LiuBo on 2018/8/30.
 */

public interface AutoDialogListener {
    void show();
    void dismiss();
    void setCanceledOnTouchOutside(boolean b);
}
