package com.hcb.hcbsdk.okhttp.exception;
public class OkHttpException extends Exception {

    // 异常码
    private int code;
    // 异常信息
    private Object msg;

    public OkHttpException() {
    }

    public OkHttpException(int code, Object msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public Object getMsg() {
        return msg;
    }
}
