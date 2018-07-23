package com.hcb.hcbsdk.socketio.listener;

/**
 * @description 常量
 */
public interface IConstants {

    // 该地址为Socket.IO官方测试地址，实际项目中请更换  http://pornhub-x.net/
//    String CHAT_SERVER_URL = "https://socket-io-chat.now.sh/";


    String ACTION = "hcb_";


    // 登录
    String LOGIN = ACTION+"login";
    String LOGIN_OUT = ACTION+"login_out";
    String LOGIN_OUT_EQUIP = ACTION+"logoutEquip";
    String ORDER_CANCEL = ACTION+"order_cancle";
    String LOGIN_SUCCESS = ACTION+"login_success";
    String LOGIN_ERROR = ACTION+"hcb_login_error";
    String SERVICE_STOP = ACTION+"push_service_stop";

    //支付
    String PAY_SUCCESS = ACTION+"pay_success";
    String PAY_FAIL = ACTION+"pay_fail";

    //扫码支付
    String PAY_NOTIFY = ACTION+"pay";


    //金豆充值
    String COIN_PAY = ACTION+"coin_pay";

    //金豆消耗
    String COIN_CONSUME = ACTION+"coin_consume";


    String BATTLE = ACTION+"battle";


    //象棋游戏金豆
    String CHESS_GOLD_SUCCESS = ACTION+"chess_gold_success";
    String CHESS_GOLD_FAIL = ACTION+"chess_gold_fail";




    String EXTRA_MESSAGE = ACTION+"message";


    String EVENT_CONNECT = ACTION+"connect";
    String EVENT_TEST = ACTION+"test";
    String EVENT_SEND_LOG = ACTION+"send_log";
    String EVENT_USER_REFUND = ACTION+"user_refund";


    //====================LOG==========================================


    String LOG_EVENT_CONNECT = ACTION+"log_connect";
    String LOG_EVENT_DISCONNECT = ACTION+"log_disconnect";

    String EXTRA_LOG_MESSAGE = ACTION+"log_message";
    String EXTRA_LOG_ACTION = ACTION+"log_message_action";

    String EVENT_RECEIVE_LOG  = "SENDING_LOG";
}
