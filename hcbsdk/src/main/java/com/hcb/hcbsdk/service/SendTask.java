package com.hcb.hcbsdk.service;

import com.hcb.hcbsdk.socketio.socket.AppSocket;
import com.hcb.hcbsdk.util.L;

public class SendTask implements Runnable {


    public int num;
    public String key;
    public String appId;

    public SendTask(String key,int num,String appId) {
        this.num = num;
        this.key = key;
        this.appId = appId;
    }

    @Override
    public void run() {
        //appid:5a7c51c66b4d8b27a8557488
        //key:ZUIGAOFEN
        L.info("PushService", "SendTask异步发送分数... "+num);
        AppSocket.getInstance().sendBattleLog2Server(key,num,appId);


    }


}
