package com.iolll.liubo.autosimple.data;

/**
 * Created by LiuBo on 2018/5/3.
 */


    /**
     * 统一API响应结果封装
     */
    public class ApiResult<T> {

        //    public static final int SUCCESS = 200;
//    public static final int DATA_NULL = 400;
        private int code;
        private String message;
        private T data;

        public int getCode() {
            return code;
        }

        public ApiResult setCode(int code) {
            this.code = code;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public ApiResult setMessage(String message) {
            this.message = message;
            return this;
        }

        public T getData() {
            return data;
        }

        public ApiResult setData(T data) {
            this.data = data;
            return this;
        }


    }