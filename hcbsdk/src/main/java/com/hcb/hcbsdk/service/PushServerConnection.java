package com.hcb.hcbsdk.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.google.gson.Gson;
import com.hcb.hcbsdk.activity.ActivityCollector;
import com.hcb.hcbsdk.activity.BindTelActivity;
import com.hcb.hcbsdk.activity.Login_weichat_alipay_Activity;
import com.hcb.hcbsdk.activity.NetErrorActivity;
import com.hcb.hcbsdk.activity.PayActivityC;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.manager.schedulerTask.AliLoginScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.CheckSocketConnectScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.GoldPayScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.WeiChatLoginScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.PayGameScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.PayScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.UploadLogScheduledExecutor;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.service.msgBean.User;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.socketio.listener.IEmitterListener;
import com.hcb.hcbsdk.socketio.listener.SocketPushDataListener;
import com.hcb.hcbsdk.socketio.socket.AppSocket;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;

import java.util.concurrent.TimeUnit;

import io.socket.client.Manager;
import io.socket.client.Socket;

import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.KEY_FILE_NAME;

/**
 * @author WangGuoWei
 * @time 2018/1/5 17:59
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
public class PushServerConnection implements IEmitterListener {
    private static final String LOGTAG = "PushService";
    private final FileUtil fileUtil;
    Context ctx;
    private Handler mHandler = new Handler();
    private SDKManager sdkManager;

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private boolean isTaskRuning = false;
    private boolean isLogTaskRuning = false;

    public PushServerConnection(Context ctx) {
        this.ctx = ctx;
        fileUtil = new FileUtil();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ctx.registerReceiver(mReceiver, mFilter);

        checkSocketConect();

    }

    private void checkSocketConect() {
        checkSocketConnectScheduledTask();
    }

    /**
     * 监听网络变化广播 做出相应的提示
     */

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
//                    if(AppSocket.getInstance()!=null&&!AppSocket.getInstance().isConnected()&&sdkManager!=null)
//                        sdkManager.startconnect();
                } else {
                        startNetErrorPage();
                }
            }
        }
    };
    public SocketPushDataListener mListener;

    public void setSocketPushDataListener(SocketPushDataListener mListener) {
        this.mListener = mListener;
    }

    public void setSocketPushDataListener() {
    }

    public void setSDKManager(SDKManager sdkManager) {
        this.sdkManager = sdkManager;
    }

    //子线程
    @Override
    public void emitterListenerResut(String key, Object... args) {
        L.info(LOGTAG, "接收到Socket推送消息----"+key);
        switch (key) {
            case Manager.EVENT_TRANSPORT:

                break;

            case Manager.EVENT_CONNECT_CANCLE:
                L.info(LOGTAG, "Socket连接取消----");
                break;

            case Socket.EVENT_CONNECT_ERROR:
                L.info(LOGTAG, "Socket连接错误");
                break;

            case Socket.EVENT_CONNECT_TIMEOUT:
                L.info(LOGTAG, "Socket连接超时");
                break;

            // Socket连接成功
            case Socket.EVENT_CONNECT:
                L.isConnected = true;
                C.SOCKET_CONNECT_COUNT = 0;
                if (L.changeModle) {
                    if (L.debug) {
                        L.info(LOGTAG, " 切换模式成功 当前模式为 测试");
                        mHandler.post(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                            @Override
                            public void run() {
                                Utils.showToastCenter(ctx, " 切换模式成功 当前模式为 测试");
                            }
                        });
                    } else {
                        L.info(LOGTAG, " 切换模式成功 当前模式为 线上");
                        mHandler.post(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                            @Override
                            public void run() {
                                Utils.showToastCenter(ctx, " 切换模式成功 当前模式为 线上");
                            }
                        });
                    }
                    L.changeModle = !L.changeModle;
                }

                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_CONNECT, "Socket连接成功!!!");

                L.info(LOGTAG, "Socket连接成功!!!  sdkManager.deviceNo::  " + L.deviceNo);


                /*if(!isLogTaskRuning)
                    runLogScheduledTask();*/

                if (!isTaskRuning)
                    return;
                mHandler.removeCallbacksAndMessages(null);
                isTaskRuning = false;
                L.info(LOGTAG, "Socket连接成功,取消延时连接任务-------------");
                break;

            // Socket断开连接
            case Socket.EVENT_DISCONNECT:
                offEmitterListener();
                L.isConnected = false;
                BroadcastUtil.sendBroadcastToUI(ctx, Socket.EVENT_DISCONNECT, "Socket断开连接!!!");
                L.info(LOGTAG, "Socket断开连接-----------isConnected()   " + AppSocket.getInstance().isConnected() + "   L.isConnected  " + L.isConnected);
                if (L.changeModle)
                    sdkManager.startconnect();
                break;

            // Socket连接错误
            case Socket.EVENT_ERROR:
                L.info(LOGTAG, "Socket错误");
                break;

            // Socket重新连接
            case Socket.EVENT_RECONNECT:
                L.info(LOGTAG, "Socket重新连接");
                break;

            case Socket.EVENT_RECONNECT_ATTEMPT:
                L.info(LOGTAG, "Socket试图重新连接");
                if (isTaskRuning)
                    return;

                L.info(LOGTAG, "Socket试图重新连接,执行延时连接任务--------------");
                mHandler.postDelayed(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                    @Override
                    public void run() {

                        if (L.isConnected) {
                            L.info("PushService", "Socket已经连接！！！--------------");
                            return;
                        }

                        //开始连接
                        sdkManager.startconnect();
                        isTaskRuning = false;
                        L.info(LOGTAG, "开始执行延时连接任务--------------");
                    }
                }, (5 * 60 * 1000) + (10 * 1000));
                isTaskRuning = true;
                break;

            case Socket.EVENT_RECONNECT_ERROR:
                L.info(LOGTAG, "Socket重新连接错误");
                break;

            case Socket.EVENT_RECONNECT_FAILED:
                L.info(LOGTAG, "Socket重新连接失败");
                break;

            case Socket.EVENT_RECONNECTING:
                L.info(LOGTAG, "Socket重新连接...");
                break;
            case IConstants.EVENT_TEST:
//                final JSONObject testData = (JSONObject) args[0];
//                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_TEST,testData.toString());
//                Log.e("ScheduledTask", "EVENT_TEST---->"+testData.toString());
                break;
            case IConstants.EVENT_USER_REFUND:
                /*final JSONObject user_refund = (JSONObject) args[0];
                L.info(LOGTAG, "收到退款  " + user_refund.toString());
                int status = 0;
                final String data_msg;
                try {
                    status = user_refund.getInt("status");
                    if (status == 10000) {
                        data_msg = user_refund.getString("data");
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_USER_REFUND, data_msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
                break;
            case IConstants.SOCTET_EVENT_LOG_BEGIN_SEND:

                L.info(LOGTAG, "接收到Socket推送消息----日志开启--"+IConstants.SOCTET_EVENT_LOG_BEGIN_SEND);
                C.START_SEND_LOG = true;
                break;

            case IConstants.SOCTET_EVENT_LOG_STOP_SEND:

                L.info(LOGTAG, "接收到Socket推送消息----日志关闭--"+IConstants.SOCTET_EVENT_LOG_STOP_SEND);
                C.START_SEND_LOG = false;

                break;
            case IConstants.SOCTET_EVENT_SOCKET_LOGIN:

                L.info(LOGTAG, "接收到Socket推送消息----设备登录--"+IConstants.SOCTET_EVENT_SOCKET_LOGIN);
                L.info(LOGTAG, "设备登录=================>>>>  " + args[0].toString());
                break;
            case IConstants.SOCTET_EVENT_SOCKET_LOGOUT:

                L.info(LOGTAG, "接收到Socket推送消息----设备下线--"+IConstants.SOCTET_EVENT_SOCKET_LOGOUT);
                L.info(LOGTAG, "设备下线=================>>>>  " + args[0].toString());
                break;
            case IConstants.SOCTET_EVENT_ALL_CLIENT_LOGIN:

                L.info(LOGTAG, "接收到Socket推送消息----获取在线设备--"+IConstants.SOCTET_EVENT_ALL_CLIENT_LOGIN);
                L.info(LOGTAG, "获取在线设备=================>>>>  " + args[0].toString());
                break;


            case IConstants.COIN_PAY://金豆充值


               /* L.info(LOGTAG, "推送扫码--金豆充值接收=================>>>>  " + args[0].toString());

//                cancleScheduledTask();

                LoginReslut coinReslut = new Gson().fromJson(args[0].toString(), LoginReslut.class);
                if (coinReslut.getStatus() == 1) {

                    FileUtil.writeFile(FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME, args[0].toString(), false);

                    TuitaData.getInstance().setUser(coinReslut.getData());

                    BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS, "2");
                } else
                    BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL, "2");*/

                break;


            case IConstants.COIN_CONSUME://金豆消耗


                /*L.info(LOGTAG, "推送扫码--金豆消耗接收=================>>>>  " + args[0].toString());

                cancleScheduledTask();

                LoginReslut consumeReslut = new Gson().fromJson(args[0].toString(), LoginReslut.class);
                if (consumeReslut.getStatus() == 1) {

                    FileUtil.writeFile(FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME, args[0].toString(), false);

                    TuitaData.getInstance().setUser(consumeReslut.getData());

                    BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS, "3");
                } else
                    BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL, "3");*/
                break;


            case IConstants.PAY_NOTIFY:
                /*L.info(LOGTAG, "推送扫码支付接收=================>>>>  " + args[0].toString());

                cancleScheduledTask();

                final JSONObject paydata = (JSONObject) args[0];

                try {
                    if (paydata.get("data").equals("success")) {
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS, null);
                        L.info("PushService", "推送支付----定时请求支付成功-----  " + paydata.toString());
                    } else if (paydata.get("data").equals("fail")) {
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL, null);
                        L.info("PushService", "推送支付----定时请求支付失败-----  " + paydata.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
                break;

            case IConstants.LOGIN_OUT_EQUIP:
                /*L.info(LOGTAG, "退出登录接收。。。。。。");
                TuitaData.getInstance().setUser(null);
                DataCleanManager.deleteFolderFile(FileUtil.getSDDir(KEY_DIR_NAME), true);

                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.LOGIN_OUT, null);*/

                break;


             case IConstants.EVENT_HCB_LOTTERY_PURCHASE_ALL:
                /* L.info(LOGTAG, "购买彩票通知的socket事件=================>>>>  " + args[0].toString());

                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_HCB_LOTTERY_PURCHASE_ALL, args[0].toString());*/

                break;

            case IConstants.LOGIN:

                /*if (!IS_LAUNCHER)
                    return;

                final JSONObject data = (JSONObject) args[0];

                try {
                    L.info(LOGTAG, "登录返回数据----" + args[0].toString());

                    SDKManager.getInstance().cancleScheduledTask();

                    LoginReslut loginReslut = new Gson().fromJson(data.toString(), LoginReslut.class);
                    if (loginReslut.getStatus() == 1) {

                        FileUtil.writeFile
                                (FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME, data.toString(), false);
                        L.info(LOGTAG, "登录成功");
                        if (sdkManager != null) {
                            TuitaData.getInstance().setUser(loginReslut.getData());
                            BroadcastUtil.sendBroadcastToUI(ctx, IConstants.LOGIN, data.toString());
                            sdkManager.clearServerUserData();
                        }
                    } else {

                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.LOGIN_ERROR, data.toString());
                    }
                } catch (Exception e) {
                    L.info(LOGTAG, "登录异常返回数据----" + e.toString());
                    return;
                }*/

                break;


        }
    }

    private static final long INITIALDELAY = 6;//初始化延时
    private static final long PERIOD = 6;//两次开始执行最小间隔时间
    private static final long AWAITTIME = 1;

    public void runPayScheduledTask(String snNo,String orderId,String payType) {
        sdkManager.getScheduler().scheduleWithFixedDelay(new PayScheduledExecutor(snNo, ctx,orderId,payType), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }
    public void runGamePayScheduledTask(String snNo) {
        sdkManager.getScheduler().scheduleWithFixedDelay(new PayGameScheduledExecutor(snNo, ctx), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }

    public void runGoldPayScheduledTask(String snNo, int orderType) {
        sdkManager.getScheduler().scheduleWithFixedDelay(new GoldPayScheduledExecutor(snNo, ctx, orderType), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }

    public void runLoginScheduledTask(Context ctx, String deviceNo) {
        sdkManager.getScheduler().scheduleWithFixedDelay(new WeiChatLoginScheduledExecutor(ctx, deviceNo), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }
    public void runAliLoginScheduledTask(Context ctx, String queryCode) {
        sdkManager.getScheduler().scheduleWithFixedDelay(new AliLoginScheduledExecutor(ctx, queryCode), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }

    public void runLogScheduledTask() {
        isLogTaskRuning = true;
        sdkManager.getUploadLogScheduler().scheduleWithFixedDelay(new UploadLogScheduledExecutor(ctx), 10, 600, TimeUnit.SECONDS);
    }

    public void checkSocketConnectScheduledTask() {
        SDKManager.getInstance().getCheckSocketConnectScheduler().scheduleWithFixedDelay(new CheckSocketConnectScheduledExecutor(ctx), INITIALDELAY, 30, TimeUnit.SECONDS);
    }

    public void cancleScheduledTask() {

        try {
            sdkManager.getScheduler().shutdown();

            if (!sdkManager.getScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                sdkManager.getScheduler().shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            sdkManager.getScheduler().shutdownNow();
        }

        L.info(LOGTAG, "结束轮询 登陆/支付 任务----");

    }

    public void cancleUploadLogScheduledTask() {

        try {
            sdkManager.getUploadLogScheduler().shutdown();

            if (!sdkManager.getUploadLogScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                sdkManager.getUploadLogScheduler().shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            sdkManager.getUploadLogScheduler().shutdownNow();
        }
        isLogTaskRuning = false;
        L.info(LOGTAG, "上传日志结束任务----");

    }

    public void cancleCheckSocketConnectScheduledTask() {

        try {
            SDKManager.getInstance().getCheckSocketConnectScheduler().shutdown();

            if (!SDKManager.getInstance().getCheckSocketConnectScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)) {
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                SDKManager.getInstance().getCheckSocketConnectScheduler().shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            SDKManager.getInstance().getCheckSocketConnectScheduler().shutdownNow();
        }
        L.info(LOGTAG, "监控Socket连接任务结束----");

    }


    public User getUser() {

        LoginReslut loginReslut;
       User user = TuitaData.getInstance().getUser();
        L.debug("userdata", "读取内存数据  " + user);
        if (user == null) {
            L.debug("userdata", "内存数据为空  读取本地用户文件");
            String data = FileUtil.read(FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME);
            if (data != null) {
                loginReslut = new Gson().fromJson(data.toString(), LoginReslut.class);
                if (loginReslut != null) {
                    user = loginReslut.getData();
                    if (user != null) {
                        setUser(user);
                        L.debug("userdata", "读取本地用户文件成功  " + user.toString());
                    } else
                        L.debug("userdata", "读取本地用户文件失败--" + data.toString());
                }
            } else {
                L.debug("userdata", "读取本地用户文件失败  ");
            }
        }

        return user;
    }

    public void setUser(User user) {
        TuitaData.getInstance().setUser(user);
    }

    public void startLoginPage() {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(Login_weichat_alipay_Activity.class)) return;

        Intent intent = new Intent(ctx, Login_weichat_alipay_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

public void startNetErrorPage() {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(NetErrorActivity.class)) return;

        Intent intent = new Intent(ctx, NetErrorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public void startPayPage(String appid, String orderId,String aliPayQueryId, String authorizeUrl, int orderType, String consumeGoldCoinCount, String ticketNum, int numType,String payType) {
        if (Utils.isFastClick(1000)) {
            return;
        }
        if (ActivityCollector.isActivityExist(PayActivityC.class)) return;

        Intent intent = new Intent(ctx, PayActivityC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("appid", appid);
        intent.putExtra("orderId", orderId);
        intent.putExtra("aliPayQueryId", aliPayQueryId);
        intent.putExtra("authorizeUrl", authorizeUrl);
        intent.putExtra("orderType", orderType);
        intent.putExtra("consumeGoldCoinCount", consumeGoldCoinCount);
        intent.putExtra("ticketNum", ticketNum);
        intent.putExtra("numType", numType);
        intent.putExtra("payType", payType);
        ctx.startActivity(intent);
    }

    public void startBindPage(String token, String openId) {
        if (Utils.isFastClick(1000)) {
            return;
        }
        if (ActivityCollector.isActivityExist(BindTelActivity.class)) return;

        Intent intent = new Intent(ctx, BindTelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("token", token);
        intent.putExtra("openId", openId);
        ctx.startActivity(intent);
    }

    @Override
    public void requestSocketResult(String key, Object... args) {

    }

    private void openConnection(String url, String deviceNo) {
        initAppSocket(url, deviceNo);
    }

    /**
     * 初始化Socket
     */
    public void initAppSocket(String url, String deviceNo) {
        String uid = "1234567890";
        AppSocket.Builder builder;
        /*if(appid == null)
            builder = new AppSocket.Builder(url+ DeviceUtil.getDeviceId2Ipad(ctx)).setEmitterListener(this);
        else*/
        builder = new AppSocket.Builder(url + deviceNo).setEmitterListener(this);
        AppSocket.init(builder).connect();
        C.CUR_SOCKET_URL = builder.socketHost;
    }

    public void closeConnection() {
        L.info(LOGTAG, "closeConnection-------------");
        if(AppSocket.getInstance()!=null)
            AppSocket.getInstance().disConnnect();
    }

    public void offEmitterListener() {
        L.info(LOGTAG, "offEmitterListener------------- ");
        AppSocket.getInstance().offEmitterListener();
    }

    public void push_connect(int test, String deviceNo) {
        if (test == 0) {
            openConnection(C.getDebugSocketURL(), deviceNo);
        } else if (test == 1) {
        } else {
            openConnection(C.getSocketURL(), deviceNo);
        }
    }

    public void stopConnection() {
        mHandler.removeCallbacksAndMessages(null);
        if(mReceiver!=null)
            ctx.unregisterReceiver(mReceiver);

        closeConnection();
        cancleScheduledTask();
        cancleUploadLogScheduledTask();
        cancleCheckSocketConnectScheduledTask();
    }
}
