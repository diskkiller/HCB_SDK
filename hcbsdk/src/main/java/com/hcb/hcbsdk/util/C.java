package com.hcb.hcbsdk.util;

/**
 * @author wangguowei
 * @ClassName: C
 * @Description: 常量类
 * @date 15/10/31 下午9:00
 */
public class C {

    static {
        System.loadLibrary("hcb-lib");
    }

    public static native String getSocketURL();
    public static native String getDebugSocketURL();
    public static native String getAPIURL();
    public static native String getDebugapiURL();

    public static final boolean IS_LAUNCHER = true;
    public static final boolean IS_NEED_LOG = false;


    public static final String DM_URL = "http://www.efittech.com/dm.aspx";
    public static String CUR_SOCKET_URL= "";
    public static int SOCKET_CONNECT_COUNT= 0;
    public static boolean SOCKET_RECONNECT= false;


    public static final String API_SERVER_VERCODE_URL = "/user/code";
    public static final String API_SERVER_AUTHORIZE_URL = "/user/authorize";
    public static final String API_SERVER_LOGIN_URL = "/user/login";
    public static final String API_SERVER_LOGOUT_URL = "/user/logOut";
    public static final String API_TEST = "/bn/test";
    public static final String API_SEND_LOG = "/bn/send";
    public static final String API_USER_PAY_CONFIRM_PAYINFO = "/user/pay/confirm/payInfo";
    public static final String API_USER_PAY_CONFIRM_LOGIN = "/user/confirm/login";
    public static final String API_SERVER_QUERY_PAY_INFO_URL = "/user/pay/info";
    public static final String API_SERVER_QUERY_PAY_CHARGE_URL = "/game/zobi/pay";
    public static final String API_SERVER_USER_PAY_ORDER_URL = "/user/pay/check";
    public static final String API_SERVER_USER_USER_GOLD_URL = "/user/gold";
    public static final String API_SERVER_USER_RECHARGE_GOLD_URL = "/user/charge";
    public static final String key = "HCB123";




}
