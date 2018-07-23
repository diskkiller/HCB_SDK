package com.hcb.hcbsdk.okhttp.listener;

/**
 * 监听下载进度
 * 时间：2017/9/25
 */
public interface DisposeDownloadListener extends DisposeDataListener {

    /**
     * 当下载进度改变时回调
     *
     * @param progress
     *         下载进度
     */
    void onProgress(int progress);
}
