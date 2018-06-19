package com.iolll.liubo.autosimple.compile.autoObserver.exception;

/**
 * 服务器异常
 * Created by LiuBo on 2018/5/18.
 */

public class ServerException extends RuntimeException {
    private int code;
    private String msg;

    public ServerException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
