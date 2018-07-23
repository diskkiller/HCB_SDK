package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;

import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.socketio.socket.AppSocket;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.CheckUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.dodo.NetStatus;

import static com.hcb.hcbsdk.util.C.SERVICE_NAME;
import static com.hcb.hcbsdk.util.L.isConnected;

/**
 * @author WangGuoWei
 * @time 2018/3/1 10:48
 * @des ${TODO}
 * <p>
 * ┽
 * ┽                            _ooOoo_
 * ┽                           o8888888o
 * ┽                           88" . "88
 * ┽                           (| -_- |)
 * ┽                           O\  =  /O
 * ┽                        ____/`---'\____
 * ┽                      .'  \\|     |//  `.
 * ┽                     /  \\|||  :  |||//  \
 * ┽                    /  _||||| -:- |||||-  \
 * ┽                    |   | \\\  -  /// |   |
 * ┽                    | \_|  ''\---/''  |   |
 * ┽                    \  .-\__  `-`  ___/-. /
 * ┽                  ___`. .'  /--.--\  `. . __
 * ┽               ."" '<  `.___\_<|>_/___.'  >'"".
 * ┽              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * ┽              \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ┽         ======`-.____`-.___\_____/___.-`____.-'======
 * ┽                            `=---='
 * ┽         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * ┽                      佛祖保佑       永无BUG
 * ┽
 * ┽
 * ┽
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class CheckSocketConnectScheduledExecutor implements Runnable {

    private final Context ctx;

    public CheckSocketConnectScheduledExecutor(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public void run() {
        L.info("PushService", "开始执行Socket连接监控任务-----  "+ C.CUR_SOCKET_URL
                +"\n  ------isConnected   "+AppSocket.getInstance().isConnected()
                +"   L.isConnected  "+ isConnected+"   ConnectCount:  "+C.SOCKET_CONNECT_COUNT);

        if(isConnected){
            L.info("PushService", "Socket已经连接！！！--------------");

            if(SDKManager.getInstance().getUser()!=null)
                L.info("PushService", "登陆用户：----------->   "+
                        SDKManager.getInstance().getUser().getNickname()+"   Gold: "+SDKManager.getInstance().getUser().getGoldCoin());
            else
                L.info("PushService", "用户未登陆。。。");

            return;
        }


        /*if(C.SOCKET_CONNECT_COUNT>2){
            L.info("PushService", "Socket重连次数-------ConnectCount: "+C.SOCKET_CONNECT_COUNT
                    +"  -----isServiceWorked   "+CheckUtil.isServiceWorked(ctx,SERVICE_NAME));
            if(CheckUtil.isServiceWorked(ctx,SERVICE_NAME)){
                if(NetStatus.getNetStatus(ctx)){
                    C.SOCKET_RECONNECT = true;
                    SDKManager.getInstance().destroy();
                }
            }

            return;
        }

            C.SOCKET_CONNECT_COUNT++;

        */

        L.info("PushService", "Socket已经断开！！！开始重连....--------------ConnectCount: "+C.SOCKET_CONNECT_COUNT);
        SDKManager.getInstance().startconnect();

    }


}
