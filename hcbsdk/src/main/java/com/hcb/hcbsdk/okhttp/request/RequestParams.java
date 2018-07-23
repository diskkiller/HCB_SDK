package com.hcb.hcbsdk.okhttp.request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 封装所有的请求参数
 * 时间：2017/9/17
 */
public class RequestParams {

    public ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<>();

    public RequestParams() {
        this((Map<String, String>) null);
    }

    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }

    /**
     * 添加请求参数
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    /**
     * 添加请求参数
     */
    public void put(String key, Object value) throws FileNotFoundException {
        if (key != null && value != null) {
            fileParams.put(key, value);
        }
    }

    /**
     * 判断是否有参数
     */
    public boolean hasParams() {
        if (urlParams.size() > 0 || fileParams.size() > 0) {
            return true;
        }
        return false;
    }
}
