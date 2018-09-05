package com.hcb.hcbsdk.okhttp.listener;

/**
 * 请求回调接口
 * 时间：2017/9/17
 */
public interface SDKGoldDisposeDataListener {

    /**
     * 请求成功回调事件处理
     */
    void onSuccess();

    /**
     * 请求失败回调事件处理
     */
    void onFailure();

}
