package com.iolll.liubo.autosimple.compile.autoObserver.exception;

/**
 * 自定义异常
 * Created by LiuBo on 2018/5/18.
 */

public class ApiException extends Exception {
    private int code;
    private String displayMessage;

    public ApiException( int code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public int getCode() {
        return code;
    }
}
