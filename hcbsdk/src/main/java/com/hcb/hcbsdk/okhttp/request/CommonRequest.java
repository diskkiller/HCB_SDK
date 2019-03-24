package com.hcb.hcbsdk.okhttp.request;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hcb.hcbsdk.service.msgBean.VersionManage;
import com.hcb.hcbsdk.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 创建请求对象，包括get请求对象和post请求对象
 * 时间：2017/9/17
 */
public class CommonRequest {

    /**
     * 创建get请求对象
     *
     * @param url
     *         网址
     * @param params
     *         请求参数
     * @return 返回get请求对象
     */
    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    public static Request createDeletRequest(String url, RequestParams params) {
        return createDeletRequest(url, params, null);
    }

    /**
     * 可以带请求头的Get请求
     */
    public static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        L.info("PushService", "GET请求数据----url:  "+ urlBuilder.toString());
        return new Request.Builder().
                url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .headers(mHeader)
                .build();


    }

    /**
     * 可以带请求头的Get请求
     */
    public static Request createDeletRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        L.info("PushService", "GET请求数据----url:  "+ urlBuilder.toString());
        return new Request.Builder().
                url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .delete()
                .headers(mHeader)
                .build();


    }

    /**
     * 创建post请求对象
     *
     * @param url
     *         网址
     * @param params
     *         请求参数
     * @return 返回get请求对象
     */
    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    public static Request createPostOblectRequest(String url, RequestParams params) {
        return createPostOblectRequest(url, params, null);
    }


    public static void putJson(JSONObject json,String key,Object value){
        try{
            json.put(key,value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    /**可以带请求头和object参数的Post请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostOblectRequest(String url, RequestParams params, RequestParams headers) {

        RequestBody body = null;
        Gson gson = new Gson();
        JSONObject jsonObject= new JSONObject();
        if (params != null) {

            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {

                putJson(jsonObject,entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {

                VersionManage testBean = (VersionManage) entry.getValue();


                String json = gson.toJson(testBean);
                putJson(jsonObject,entry.getKey(), json);
                body = RequestBody.create(JSON,jsonObject.toString());

            }

        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        Request request = new Request.Builder().url(url).
                post(body).
                headers(mHeader)
                .build();
        L.info("PushService", "请求数据----url:  "+url+"  data:  " + jsonObject.toString());
        return request;
    }

    /**可以带请求头的Post请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeader = mHeaderBuild.build();
        Request request = new Request.Builder().url(url).
                post(mFormBody).
                headers(mHeader)
                .build();
        JSONObject jsonObject = new JSONObject(params.urlParams);
        L.info("PushService", "请求数据----url:  "+url+"  data:  " + jsonObject.toString());
        return request;
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public static Request createMonitorRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if (params != null && params.hasParams()) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
    }
}
