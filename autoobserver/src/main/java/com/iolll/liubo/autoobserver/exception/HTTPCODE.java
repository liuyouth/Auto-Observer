package com.iolll.liubo.autoobserver.exception;

/**
 * Created by LiuBo on 2018/5/18.
 * 与服务器约定好的异常
 */
public class HTTPCODE {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = 1000;
    public static final int NULL_ERROR = 4044;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = 1001;
    /**
     * 网络错误
     */
    public static final int NETWORD_ERROR = 1002;
    /**
     * 协议出错
     */
    public static final int HTTP_ERROR = 1003;

    public static final int UNINTERCEPT = 999;
    /**正常**/
    public static final int HTTP_OK = 200;

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;
}