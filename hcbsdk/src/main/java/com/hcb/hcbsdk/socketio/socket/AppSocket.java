package com.hcb.hcbsdk.socketio.socket;


import android.util.Log;

import com.hcb.hcbsdk.logutils.save.imp.LogWriter;
import com.hcb.hcbsdk.service.TuitaPacket;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.L;

/**
 * @author silencezwm on 2017/8/25 上午11:12
 * @email silencezwm@gmail.com
 * @description AppSocket
 */
public class AppSocket extends BaseSocket {

    private static volatile AppSocket INSTANCE = null;
    private Builder builder;

    public static AppSocket getInstance() {
        if (INSTANCE == null) {
//            throw new NullPointerException("must first call the build() method");
            return null;
        }
        return INSTANCE;
    }

    public static AppSocket init(Builder builder) {
        return new AppSocket(builder);
    }

    private AppSocket(Builder builder) {
        super(builder);
        INSTANCE = this;
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    /**
     * 登录
     *
     */
    public void sdk_login(String phone,String vercode,String eventCode) {

        String msg = TuitaPacket.createLogintPacket(phone,vercode,eventCode);
        L.info("PushService", "发送登录数据----" + msg);
        mSocket.emit(IConstants.LOGIN, msg);
    }
//    /**
//     * 退出
//     *
//     */
//    public void sdk_logout() {
//
//        String msg = TuitaPacket.createLogoutPacket();
//        LogWriter.writeLog("PushService", "发送退出登录数据----" + msg);
//        mSocket.emit(IConstants.EVENT_USER_LOGOUT, msg);
//    }

    /**
     * 第三方发送日志
     * @param key
     * @param num
     * @param appId
     */
    public void sendBattleLog2Server(String key, int num,String appId) {

        String msg = TuitaPacket.createBattleLogPacket(key,num,appId);
        if(msg!=null){
            L.info("PushService", "第三方发送数据----" + msg);
            mSocket.emit(IConstants.BATTLE, msg);
        }else{
            L.info("PushService", "第三方发送数据----null");
        }
    }


    public void sendLog2Server(String event,String msg) {

        Log.i("PushLogService","是否发送日志：----------"+ C.START_SEND_LOG);

        if(!C.START_SEND_LOG) return;

        if(msg!=null){
            Log.i("PushLogService","msg：----------"+ msg);
            mSocket.emit(event, msg);
        }
    }

    public void startSendLog2Server(String event,String msg) {

        Log.i("PushLogService","开启发送日志：----------"+ C.START_SEND_LOG);

        if(msg!=null){
            Log.i("PushLogService","msg：----------"+ msg);
            mSocket.emit(event, msg);
        }
    }


}
