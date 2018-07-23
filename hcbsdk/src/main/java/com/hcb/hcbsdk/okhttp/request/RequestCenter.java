package com.hcb.hcbsdk.okhttp.request;

import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.client.CommonOkHttpClient;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataHandle;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.util.C;

/**
 * <br/>
 * <br/>
 * 时间：2017/9/25
 */
public class RequestCenter {

    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params,
                                   DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createPostRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
    }
    public static void postLogRequest(String url, RequestParams params,
                                   DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createPostRequest("http://192.168.1.112:3000"+url, params), new DisposeDataHandle(listener));
    }
    public static void getRequest(String url, RequestParams params,
                                  DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.request(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }
    public static void getRequest(String url, RequestParams params,
                                  DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener));
    }

    /**
     *
     */
    public static void getDMcode(String code, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("t", code);
        params.put("m", "8");
        params.put("e", "8");
        RequestCenter.getRequest(C.DM_URL, params, listener);
    }
    /**
     * 获取验证码请求
     */
    public static void getVercode(String mobile, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mobile", mobile);
        RequestCenter.postRequest(C.API_SERVER_VERCODE_URL, params, listener);
    }
    /**
     * 二维码请求
     */
    public static void getAuthorize(String deviceNo, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("deviceNo", deviceNo);
        RequestCenter.postRequest(C.API_SERVER_AUTHORIZE_URL, params, listener);
    }


    public static void test(String pid,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("pid", pid);
        RequestCenter.postRequest(C.API_TEST, params, listener);
    }
    public static void sendLog(String mobile,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mobile", mobile);
        RequestCenter.postRequest(C.API_SEND_LOG, params, listener);
    }


    /**
     * 用户登录请求
     */
    public static void userLogin(String mobile, String code,String deviceNo,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("deviceNo", deviceNo);
        RequestCenter.postRequest(C.API_SERVER_LOGIN_URL, params, listener);
    }

    /**
     * 用户退出登录请求
     */
    public static void userLogout(String deviceNo,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("deviceNo", deviceNo);
        RequestCenter.postRequest(C.API_SERVER_LOGOUT_URL, params, listener);
    }
    /**
     * 查询付款单
     */
    public static void queryPayInfo(String payId,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("pid", payId);
        RequestCenter.postRequest(C.API_SERVER_QUERY_PAY_INFO_URL, params, listener);
    }
    /**
     * 生成付款单
     */
    public static void creatPayInfo(String uid,String deviceNo,String type,String num,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("deviceNo", deviceNo);
        params.put("num", num);
        params.put("launcher", "0");
        RequestCenter.postRequest(C.API_SERVER_QUERY_PAY_CHARGE_URL, params, listener);
    }
    /**
     * 金豆消耗与增加
     */
    public static void payOrExpendGold(String uid,String deviceNo,String appId,String num,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("deviceNo", deviceNo);
        params.put("num", num);
        params.put("appId", appId);
        RequestCenter.postRequest(C.API_SERVER_USER_USER_GOLD_URL, params, listener);
    }
    /**
     * 用户确认付款单支付
     */
    public static void userPayOrder(String uid,String deviceNo,String pid,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("deviceNo", deviceNo);
        params.put("pid", pid);
        RequestCenter.postRequest(C.API_SERVER_USER_PAY_ORDER_URL, params, listener);
    }
    /**
     * 用户充值
     */
    public static void userRecharge(String uid,String deviceNo,String num,String notify,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("uid", uid);
        params.put("deviceNo", deviceNo);
        params.put("type", "gold");
        params.put("num", num);
        params.put("notify", notify);
        RequestCenter.postRequest(C.API_SERVER_USER_RECHARGE_GOLD_URL, params, listener);
    }



    public static void confirm_payInfo(String pid, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("pid", pid);
        RequestCenter.postRequest(C.API_USER_PAY_CONFIRM_PAYINFO, params, disposeDataListener);
    }
    public static void confirm_login(String deviceNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("deviceNo", deviceNo);
        RequestCenter.postRequest(C.API_USER_PAY_CONFIRM_LOGIN, params, disposeDataListener);
    }
}
