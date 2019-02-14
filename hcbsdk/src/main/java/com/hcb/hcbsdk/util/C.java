package com.hcb.hcbsdk.util;

/**
 * @author wangguowei
 * @ClassName: C
 * @Description: 常量类
 * @date 15/10/31 下午9:00
 */
public class C {

    public static boolean IS_SOCKET_CLOSE = false;

    /*static {
        System.loadLibrary("hcb-lib");
    }*/


    public static final String SOCKET = "http://123.56.11.82:9999?snNo=";
//    public static final String DEBUG_SOCKET = "http://mtest.zthuacai.com:9999?snNo=";
    public static final String DEBUG_SOCKET = "http://47.104.104.40:9999?snNo=";
    public static final String APIURL = "https://m.zthuacai.com";
    public static final String DEBUG_APIURL = "http://mtest.zthuacai.com";


    public static String getSocketURL(){
        return SOCKET;
    };

    public static  String getDebugSocketURL(){
        return DEBUG_SOCKET;
    };

    public static String getAPIURL(){
        return  APIURL;
    };

    public static  String getDebugapiURL(){
        return DEBUG_APIURL;
    };

    public static final boolean IS_LAUNCHER = true;
    public static final boolean IS_NEED_LOG = false;


    public static final String DM_URL = "http://www.efittech.com/dm.aspx";
    public static String CUR_SOCKET_URL = "";
    public static int SOCKET_CONNECT_COUNT = 0;
    public static boolean SOCKET_RECONNECT = false;


    /**
     * 用户登录
     */
    public static final String API_SERVER_LOGIN_URL = "/api/mbr/user/code/login";
    /**
     * 清除服务器用户缓存请求
     */
    public static final String API_SERVER_CLEAR_SERVER_USERDATA_URL = "/api/common/logout";
    /**
     * 用户退出
     */
    public static final String API_SERVER_LOGIN_OUT_URL = "/api/common/user/logout";
    /**
     * 发送验证码
     */
    public static final String API_SERVER_VERCODE_URL = "/api/common/code";
    /**
     * 获取二维码链接
     */
    public static final String API_SERVER_AUTHORIZE_URL = "/api/common/qrcode/url";
    /**
     * 轮询支付结果
     */
    public static final String API_USER_PAY_CONFIRM_PAYINFO = "/api/common/polling/pay";
    /**
     * 游戏轮询支付结果
     */
    public static final String API_GAME_PAY_CONFIRM_PAYINFO = "/api/common/game/polling/pay";
    /**
     * 轮询登录
     */
    public static final String API_USER_PAY_CONFIRM_LOGIN = "/api/common/polling/login";
    /**
     * 查询订单信息
     */
    public static final String API_SERVER_QUERY_DETAIL_URL = "/api/common/order/detail/wgw";
    /**
     * 取消订单
     */
    public static final String API_SERVER_CANCEL_ORDER_URL = "/api/mbr/user/order/cancel";
    /**
     * 微信金豆充值下单
     */
    public static final String API_SERVER_GOLDCOINCHARGE_URL = "/api/common/wechat/goldCoinCharge";
    /**
     * 微信金豆消耗
     */
    public static final String API_SERVER_CONSUMEGOLDCOIN_URL = "/api/common/wechat/consumeGoldCoin";

    /**
     * 微信金豆轮询支付
     */
    public static final String API_SERVER_COINPOLLING_PAY_URL = "/api/common/coinPolling/pay";

    /**
     * 微信金豆轮询支付
     */
    public static final String API_SERVER_USER_AUTHENTICATION_URL = "/api/mbr/user/authentication";

    /**
     * 象棋游戏金豆（+ - ）
     */
    public static final String API_SERVER_USER_GOLDCOINADDORLESS_URL = "/api/common/chess/goldCoinAddOrLess";
    /**
     *
     */
    public static final String API_SERVER_USER_ORDER_URL = "/api/common/wechat/order";


    /**
     *拼图送彩票
     */
    public static final String API_SERVER_OINTU_GIVE_ORDER_URL = "/api/common/give/order";


    /**
     *
     */
    public static final String API_GAME_INFO_URL = "/api/game/add/info";


    /**
     *游戏下单
     */
    public static final String API_SERVER_GAME_ORDER_URL = "/api/common/game/order";

    /**
     *
     */
    public static final String API_SERVER_USER_LOCK_URL = "/api/common/ticket/lock";
/**
     *
     */
    public static final String API_SERVER_USER_OEDER_URL = "/api/mbr/user/lottery/order";
//    public static final String API_SERVER_USER_OEDER_URL = "/merApi/user/userBuyTickets";
    public static final String API_SERVER_USER_DEL_OEDER_URL = "/api/mbr/user/lottery/order";


    public static final String SERVICE_NAME = "com.hcb.hcbsdk.service.HCBPushService";
    public static final String KEY_DIR_NAME = "hcb_sdk_user";
    public static final String KEY_FILE_NAME = "/hcb_sdk_user.txt";
    public static final String PUSH_SERVICE_LOG_TAG = "huacaisdk";


    public static final String API_SEND_LOG = "/bn/send";
    public static final String API_SERVER_USER_PAY_ORDER_URL = "/user/pay/check";
    public static final String API_SERVER_USER_USER_GOLD_URL = "/user/gold";
    public static final String API_SERVER_USER_RECHARGE_GOLD_URL = "/user/charge";
    public static final String key = "HCB123";


}
