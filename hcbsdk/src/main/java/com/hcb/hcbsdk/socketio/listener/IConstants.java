package com.hcb.hcbsdk.socketio.listener;

/**
 * @description 常量
 */
public interface IConstants {

    // 该地址为Socket.IO官方测试地址，实际项目中请更换
//    String CHAT_SERVER_URL = "https://socket-io-chat.now.sh/";


    String ACTION = "hcb_";


    // 登录
    String LOGIN = ACTION+"login";
    String LOGIN_ERROR = ACTION+"hcb_login_error";
    String SERVICE_STOP = ACTION+"service_stop";

    //支付
    String PAY_SUCCESS = ACTION+"pay_success";

    //扫码支付
    String PAY_NOTIFY = ACTION+"pay_notify";

    /**
     *
     * 第三方发送日志
     *
     * {
     action:"BATTLETRACE",
     data:{
     key:"zhandou",
     num:1000,
     appId:"asdfasdfasdf",
     deviceNo:"0123456789ABCDE",
     uid:"sdfasdfassadf"
     }
     }
     */
    String BATTLE = ACTION+"battle";


    String COUNT_BOUNS = ACTION+"count_bouns";//奖金总额


    String EXTRA_MESSAGE = ACTION+"message";

    String LOGIN_SEND_MESSAGE_ACTION = ACTION+"login_send_message_action";

    String EVENT_CONNECT = ACTION+"connect";
    String EVENT_WINNER = ACTION+"winner";
    String EVENT_TEST = ACTION+"test";
    String EVENT_USER_CHANGE = ACTION+"user_change";
    String EVENT_USER_LOGOUT = ACTION+"user_logout";
    String EVENT_SEND_LOG = ACTION+"send_log";
    String EVENT_USER_REFUND = ACTION+"user_refund";


    //====================LOG==========================================


    String LOG_EVENT_CONNECT = ACTION+"log_connect";
    String LOG_EVENT_DISCONNECT = ACTION+"log_disconnect";

    String EXTRA_LOG_MESSAGE = ACTION+"log_message";
    String EXTRA_LOG_ACTION = ACTION+"log_message_action";

    String EVENT_RECEIVE_LOG  = "SENDING_LOG";
}
