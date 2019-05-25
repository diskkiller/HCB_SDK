package com.hcb.hcbsdk.okhttp.request;

import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.client.CommonOkHttpClient;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataHandle;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.service.msgBean.VersionManage;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.L;

import java.io.FileNotFoundException;

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
    public static void postOblectRequest(String url, RequestParams params,
                                   DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createPostOblectRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
    }
    public static void postLogRequest(String url, RequestParams params,
                                   DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createPostRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
    }
    public static void getRequest(String url, RequestParams params,
                                  DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.request(CommonRequest.
                createGetRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener, clazz));
    }
    public static void getRequest(String url, RequestParams params,
                                  DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createGetRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
    }
    public static void gettestRequest(String url, RequestParams params,
                                  DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createGetRequest("http://60.205.228.117:8080/"+url, params), new DisposeDataHandle(listener));
    }
public static void deletRequest(String url, RequestParams params,
                                  DisposeDataListener listener) {
        CommonOkHttpClient.request(CommonRequest.
                createDeletRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
    }

    /**
     * 获取验证码请求
     */
    public static void getVercode(String mobile,String type, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mobile", mobile);
        params.put("type", type);
        RequestCenter.getRequest(C.API_SERVER_VERCODE_URL, params, listener);
    }
    /**
     * 二维码请求
     */
    public static void getAuthorize(String deviceNo, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("snNo", deviceNo);
        RequestCenter.getRequest(C.API_SERVER_AUTHORIZE_URL, params, listener);
    }

    /**
     * 支付宝二维码请求
     */
    public static void getAliAuthorize( DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        RequestCenter.getRequest(C.API_SERVER_ALI_AUTHORIZE_URL, params, listener);
    }



    /**
     *
     */
    public static void testUpdata(DisposeDataListener listener) {

        RequestParams params = new RequestParams();

        VersionManage versionManage = new VersionManage();
         versionManage.setSnCode("28BE03829C59011");
         versionManage.setVersionNo("1.1.8");
         versionManage.setType(2019);
         versionManage.setCntn("我我。。。yyyyyy。。。");

        try {
            params.put("VersionManage", versionManage);
            params.put("versionNo", "1.1.8");
            params.put("snCode", "28BE03829C590");
            params.put("type", "2011");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestCenter.postOblectRequest(C.API_TEST_UPDATA_URL, params, listener);
    }

    /**
     * 用户登录请求
     */
    public static void userLogin(String mobile, String code,String snNo,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("mobile", mobile);
        params.put("smsCode", code);
        params.put("snNo", snNo);
        RequestCenter.postRequest(C.API_SERVER_LOGIN_URL, params, listener);
    }


    /**
     * 用户登录请求
     */
    public static void bindTel(String mobile, String code,String token,String openId, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("token", token);
        params.put("openId", openId);
        params.put("mobile", mobile);
        params.put("smsCode", code);
        RequestCenter.getRequest(C.API_SERVER_BIND_TEL_URL, params, listener);
    }


    /**
     * 用户退出登录请求
     */
    public static void userLogout(String snNo,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("snNo", snNo);
        RequestCenter.postRequest(C.API_SERVER_LOGIN_OUT_URL, params, listener);
    }







    /**
     * 清除服务器用户缓存请求
     */
    public static void clearServerUserData(String snNo,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("snNo", snNo);
        RequestCenter.postRequest(C.API_SERVER_CLEAR_SERVER_USERDATA_URL, params, listener);
    }

    /**
     * 查询订单
     */
    public static void queryPayInfo(String orderId,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.getRequest(C.API_SERVER_QUERY_DETAIL_URL, params, listener);
    }

    /**
     * 取消订单
     */
    public static void cancelOrder(String orderId,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("orderId", orderId);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_CANCEL_ORDER_URL, params, listener);
    }


    /**
     * 微信金豆充值下单
     */
    public static void goldCoinCharge(String appid,String deviceNo,String goldCoinCount,String totalMoney,DisposeDataListener listener) {
        L.info("","consumeGoldCoinCount totalMoney------------: "+totalMoney);

        RequestParams params = new RequestParams();
        params.put("appId", appid);
        params.put("snNo", deviceNo);
        params.put("totalMoney", totalMoney);
        params.put("goldCoinCount", goldCoinCount);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_GOLDCOINCHARGE_URL, params, listener);
    }
    /**
     * 微信金豆消耗
     */
    public static void consumeGoldCoin(String appid,String deviceNo,String consumeGoldCoinCount,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("appId", appid);
        params.put("snNo", deviceNo);
        params.put("consumeGoldCoinCount", consumeGoldCoinCount);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_CONSUMEGOLDCOIN_URL, params, listener);
    }

    /**
     * 实名认真
     */
    public static void auUser(String name,String idCard,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("idCard", idCard);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_USER_AUTHENTICATION_URL, params, listener);
    }


    /**
     * 轮询——支付
     * @param snNo
     * @param disposeDataListener
     */
    public static void confirm_payInfo(String snNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("snNo", snNo);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_USER_PAY_CONFIRM_PAYINFO, params, disposeDataListener);
    }
/**
     * 支付宝轮询——支付
     * @param outTradeNo
     * @param disposeDataListener
     */
    public static void confirm_aliPayInfo(String outTradeNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("outTradeNo", outTradeNo);

        RequestCenter.getRequest(C.API_USER_ALIPAY_CONFIRM_PAYINFO, params, disposeDataListener);
    }

    /**
     * 轮询——支付游戏
     * @param snNo
     * @param disposeDataListener
     */
    public static void confirm_gamePay(String snNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("snNo", snNo);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_GAME_PAY_CONFIRM_PAYINFO, params, disposeDataListener);
    }
    /**
     * 轮询——支付
     * @param snNo
     * @param disposeDataListener
     */
    public static void confirm_goldPayInfo(String snNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("snNo", snNo);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_COINPOLLING_PAY_URL, params, disposeDataListener);
    }

    /**
     * 轮询——登陆
     * @param deviceNo
     * @param disposeDataListener
     */
    public static void confirm_login(String deviceNo, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("queryCode", deviceNo);
        RequestCenter.getRequest(C.API_USER_PAY_CONFIRM_LOGIN, params, disposeDataListener);
    }
    /**
     * 轮询——支付宝登陆
     * @param queryCode
     * @param disposeDataListener
     */
    public static void confirm_AliLogin(String queryCode, DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("queryCode", queryCode);
        RequestCenter.getRequest(C.API_USER_ALI_CONFIRM_LOGIN, params, disposeDataListener);
    }

    /**
     * 象棋游戏金豆（+ - ）
     * @param goldNum
     * @param type 1(增加) 2减少
     * @param disposeDataListener
     */
    public static void goldCoinAddOrLess(String appId,String goldNum, String type,DisposeDataListener disposeDataListener) {
        RequestParams params = new RequestParams();
        params.put("appId", appId);
        params.put("goldNum", goldNum);
        params.put("type", type);
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        RequestCenter.postRequest(C.API_SERVER_USER_GOLDCOINADDORLESS_URL, params, disposeDataListener);
    }



    /**
     *
     */
    public static void order(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("snNo", L.deviceNo);
        params.put("singles", "1");
        params.put("amount", "1");
        params.put("lotteryId", "1");
        RequestCenter.postRequest(C.API_SERVER_USER_ORDER_URL, params, listener);
    }
    public static void give_caipiao(DisposeDataListener listener,String ticketType,String gameId) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("snNo", L.deviceNo);
        params.put("ticketType", ticketType);
        params.put("gameId", gameId);
        RequestCenter.postRequest(C.API_SERVER_OINTU_GIVE_ORDER_URL, params, listener);
    }
public static void game_info(String info,DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("snNo", L.deviceNo);
        params.put("info", info);
        RequestCenter.postRequest(C.API_GAME_INFO_URL, params, listener);
    }

    public static void gameOrder(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("snNo", L.deviceNo);
        params.put("gameId", "20");
        params.put("amount", "1");
        params.put("type", "11");
        RequestCenter.postRequest(C.API_SERVER_GAME_ORDER_URL, params, listener);
    }
    /**
     * 锁票
     * 彩票id
     * 1:整包 2:单张
     */
    public static void ticketLock(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("id", "2599");
        params.put("type", "2");
        RequestCenter.postRequest(C.API_SERVER_USER_LOCK_URL, params, listener);
    }

    public static void orderList(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("start", "1");
        params.put("type", "1");
        params.put("count", "10");
        RequestCenter.getRequest(C.API_SERVER_USER_OEDER_URL, params, listener);
    }

    public static void delOrderList(DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        if(SDKManager.getInstance().getUser()!=null)
            params.put("token", SDKManager.getInstance().getUser().getToken());
        params.put("id", "1017");
        RequestCenter.deletRequest(C.API_SERVER_USER_OEDER_URL, params, listener);
    }
}
