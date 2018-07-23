package com.hcb.hcbsdk.socketio.listener;

/**
 * 请求回调接口
 * <br/>
 * 作者：裴云飞
 * <br/>
 * 时间：2017/9/17
 */
public interface SocketPushDataListener {

    /**
     * 请求成功回调事件处理
     */
    void onResponse(String responseCode, Object responseObj);

    /**
     * 请求失败回调事件处理
     */
    void onFailure(String responseCode, Object reasonObj);
}
