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
    String LOGIN_BIND_TEL = ACTION+"login_bind_tel";
    String LOGIN_OUT = ACTION+"login_out";
    String LOGIN_OUT_EQUIP = ACTION+"logoutEquip";
    String ORDER_CANCEL = ACTION+"order_cancle";
    String LOGIN_SUCCESS = ACTION+"login_success";
    String LOGIN_ERROR = ACTION+"hcb_login_error";
    String SERVICE_STOP = ACTION+"push_service_stop";

    /**
     * 节日活动广播
     */
    String HCB_HAPPYDAY_WINDOW = ACTION+"happyday_window";

    //支付
    String PAY_SUCCESS = ACTION+"pay_success";
    //拼图支付二维码
    String PINTU_PAY_CODE = ACTION+"pintu_pay_code";
    String PINTU_PAY_SUCCESS = ACTION+"pintu_pay_success";
    String PINTU_GIVE_SUCCESS = ACTION+"pintu_give_success";
    String PINTU_GAME_INFO = ACTION+"pintu_game_info";


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


    //购买彩票通知的socket事件名
    String EVENT_HCB_LOTTERY_PURCHASE_ALL = ACTION+"lottery_purchase_all";


    //====================LOG==========================================


    String SOCTET_EVENT_LOG_BEGIN_SEND = "hcb_log_begin_send";

    String SOCTET_EVENT_LOG_STOP_SEND = "hcb_log_stop_send";


    String LOG_EVENT_CONNECT = ACTION+"log_connect";
    String LOG_EVENT_DISCONNECT = ACTION+"log_disconnect";

    String SOCTET_EVENT_CLIENT_LOG_MESSAGE = "hcb_client_log_message";
    String EVENT_START_LOG = "SEND_START";

    String EXTRA_LOG_MESSAGE = ACTION+"log_message";
    String EXTRA_LOG_ACTION = ACTION+"log_message_action";

    String EVENT_RECEIVE_LOG  = "get_log_message";
}
