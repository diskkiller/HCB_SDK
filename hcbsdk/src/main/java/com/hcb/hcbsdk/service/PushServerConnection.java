package com.hcb.hcbsdk.service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.hcb.hcbsdk.activity.ActivityCollector;
import com.hcb.hcbsdk.activity.LoginActivity;
import com.hcb.hcbsdk.activity.PayActivityC;
import com.hcb.hcbsdk.logutils.LogUtil;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.manager.schedulerTask.CheckSocketConnectScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.LoginScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.PayScheduledExecutor;
import com.hcb.hcbsdk.manager.schedulerTask.UploadLogScheduledExecutor;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.socketio.listener.IEmitterListener;
import com.hcb.hcbsdk.socketio.listener.SocketPushDataListener;
import com.hcb.hcbsdk.socketio.socket.AppSocket;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.socket.client.Manager;
import io.socket.client.Socket;

import static com.hcb.hcbsdk.util.C.IS_LAUNCHER;

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
 public class PushServerConnection  implements IEmitterListener {
    private static final String LOGTAG = "PushService";
    private final FileUtil fileUtil;
    Context ctx;
    private Handler mHandler = new Handler();
    private SDKManager sdkManager;
    private TuitaData mTuitaData;

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private boolean isTaskRuning = false;
    private boolean isLogTaskRuning = false;

    public PushServerConnection(Context ctx) {
        this.ctx = ctx;
        fileUtil = new FileUtil();
//        IntentFilter mFilter = new IntentFilter();
//        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        ctx.registerReceiver(mReceiver, mFilter);

        checkSocketConect();

    }

    private void checkSocketConect() {
        checkSocketConnectScheduledTask();
    }

    /**
     * 监听网络变化广播 做出相应的提示
     *//*
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    if(AppSocket.getInstance()!=null&&!AppSocket.getInstance().isConnected()&&sdkManager!=null)
                        sdkManager.startconnect();
                } else {

                }
            }
        }
    };*/
    public SocketPushDataListener mListener;
    public void setSocketPushDataListener(SocketPushDataListener mListener){
        this.mListener = mListener;
    }
    public void setSocketPushDataListener(){
    }
    public void setSDKManager(SDKManager sdkManager){
        this.sdkManager = sdkManager;
    }

    //子线程
    @Override
    public void emitterListenerResut(String key, Object... args) {
        switch (key) {
            case Manager.EVENT_TRANSPORT:

                break;

            case Socket.EVENT_CONNECT_ERROR:
                L.info(LOGTAG, "EVENT_CONNECT_ERROR");
                break;

            case Socket.EVENT_CONNECT_TIMEOUT:
                L.info(LOGTAG, "EVENT_CONNECT_TIMEOUT");
                break;

            // Socket连接成功
            case Socket.EVENT_CONNECT:
                L.isConnected = true;
                C.SOCKET_CONNECT_COUNT = 0;
                if(L.changeModle){
                    if(L.debug){
                        L.info(LOGTAG, " 切换模式成功 当前模式为 测试");
                        mHandler.post(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                            @Override
                            public void run() {
                                Utils.showToastCenter(ctx," 切换模式成功 当前模式为 测试");
                            }
                        });
                    }
                    else{
                        L.info(LOGTAG, " 切换模式成功 当前模式为 线上");
                        mHandler.post(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                            @Override
                            public void run() {
                                Utils.showToastCenter(ctx," 切换模式成功 当前模式为 线上");
                            }
                        });
                    }
                    L.changeModle = !L.changeModle;
                }

                BroadcastUtil.sendBroadcastToUI(ctx,IConstants.EVENT_CONNECT,"Socket连接成功!!!");

                L.info(LOGTAG, "Socket连接成功!!!  sdkManager.appid::  "+sdkManager.appid);


                /*if(!isLogTaskRuning)
                    runLogScheduledTask();*/

                if(!isTaskRuning)
                    return;
                mHandler.removeCallbacksAndMessages(null);
                isTaskRuning = false;
                L.info(LOGTAG, "Socket连接成功,取消延时连接任务-------------");
                break;

            // Socket断开连接
            case Socket.EVENT_DISCONNECT:
                offEmitterListener();
                L.isConnected = false;
                BroadcastUtil.sendBroadcastToUI(ctx,Socket.EVENT_DISCONNECT,"Socket断开连接!!!");
                L.info(LOGTAG, "Socket断开连接-----------isConnected()   "+AppSocket.getInstance().isConnected()+"   L.isConnected  "+L.isConnected);
                if(L.changeModle)
                    sdkManager.startconnect();
                break;

            // Socket连接错误
            case Socket.EVENT_ERROR:
                L.info(LOGTAG, "EVENT_ERROR");
                break;

            // Socket重新连接
            case Socket.EVENT_RECONNECT:
                L.info(LOGTAG, "Socket重新连接");
                break;

            case Socket.EVENT_RECONNECT_ATTEMPT:
                L.info(LOGTAG, "EVENT_RECONNECT_ATTEMPT");
                if(isTaskRuning)
                    return;

                L.info(LOGTAG, "Socket试图重新连接,执行延时连接任务--------------");
                 mHandler.postDelayed(new Runnable() {//将Runnable发送到Handler所在线程（一般是主线程）的消息队列中
                    @Override
                    public void run() {

                        if(L.isConnected){
                            L.info("PushService", "Socket已经连接！！！--------------");
                            return;
                        }

                        //开始连接
                        sdkManager.startconnect();
                        isTaskRuning = false;
                        L.info(LOGTAG, "开始执行延时连接任务--------------");
                    }
                },(5*60*1000)+(10*1000));
                isTaskRuning = true;
                break;

            case Socket.EVENT_RECONNECT_ERROR:
                L.info(LOGTAG, "EVENT_RECONNECT_ERROR");
                break;

            case Socket.EVENT_RECONNECT_FAILED:
                L.info(LOGTAG, "EVENT_RECONNECT_FAILED");
                break;

            case Socket.EVENT_RECONNECTING:
                L.info(LOGTAG, "EVENT_RECONNECTING");
                break;
            case IConstants.EVENT_TEST:
//                final JSONObject testData = (JSONObject) args[0];
//                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_TEST,testData.toString());
//                Log.e("ScheduledTask", "EVENT_TEST---->"+testData.toString());
                break;
            case IConstants.EVENT_USER_REFUND:
                final JSONObject user_refund = (JSONObject) args[0];
                L.info(LOGTAG,"收到退款  "+user_refund.toString());
                int status = 0;
                final String data_msg;
                try {
                    status = user_refund.getInt("status");
                    if(status == 10000) {
                        data_msg = user_refund.getString("data");
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_USER_REFUND,data_msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case IConstants.EVENT_SEND_LOG:
                LogUtil.getInstance().upload(ctx);
                break;
            case IConstants.EVENT_USER_CHANGE:
                if(!IS_LAUNCHER)
                    return;

                final JSONObject userData = (JSONObject) args[0];

                FileUtil.writeFile
                        (FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",userData.toString(), false);
                LoginReslut userReslut = new Gson().fromJson(userData.toString(), LoginReslut.class);
                if(userReslut!=null && userReslut.getStatus() == 10000)
                    TuitaData.getInstance().setUser(userReslut.getData());

                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_USER_CHANGE,userData.toString());
                L.info(LOGTAG, "EVENT_USER_CHANGE---->"+userData.toString());
                break;
            case IConstants.EVENT_WINNER:

                if(!IS_LAUNCHER)
                    return;

                final JSONObject winnerData = (JSONObject) args[0];

                FileUtil.writeFile
                        (FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",winnerData.toString(), false);
                LoginReslut winnerReslut = new Gson().fromJson(winnerData.toString(), LoginReslut.class);
                if(winnerReslut!=null && winnerReslut.getStatus() == 10000)
                    TuitaData.getInstance().setUser(winnerReslut.getData());

                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.EVENT_WINNER,winnerData.toString());
                L.info(LOGTAG, "EVENT_WINNER---->"+winnerData.toString());
                break;
            case IConstants.COUNT_BOUNS:
                if(!IS_LAUNCHER)
                    return;


                final JSONObject countdata = (JSONObject) args[0];
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.COUNT_BOUNS,countdata.toString());
                L.info("count_bouns", countdata.toString());

                break;
            case IConstants.PAY_NOTIFY:
                L.info(LOGTAG, "扫码支付接收。。。。。。");

                cancleScheduledTask();

                final JSONObject paydata = (JSONObject) args[0];
                L.info(LOGTAG, "扫码支付接收数据--->"+paydata.toString());
                try {
                    JSONObject pay = paydata.getJSONObject("pay");
                    L.info(LOGTAG, "扫码支付成功   pay--->"+pay.toString());
                    if(sdkManager.appid!=null&&!pay.getString("appId").equals(sdkManager.appid)) return;

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(LOGTAG, e.toString());
                }
                FileUtil.writeFile
                        (FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",paydata.toString(), false);
                LoginReslut payReslut = new Gson().fromJson(paydata.toString(), LoginReslut.class);
                TuitaData.getInstance().setUser(payReslut.getData());
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS,paydata.toString());
                L.info(LOGTAG, "扫码支付成功--->"+paydata.toString());

                break;

            case IConstants.LOGIN:

                if(!IS_LAUNCHER)
                    return;

                final JSONObject data = (JSONObject) args[0];

                try {
                    L.info(LOGTAG, "登录返回数据----" + data.toString());

                    SDKManager.getInstance().cancleScheduledTask();

                    LoginReslut loginReslut = new Gson().fromJson(data.toString(), LoginReslut.class);
                    if (loginReslut.getStatus() == 10000) {

                        FileUtil.writeFile
                                (FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",data.toString(), false);
                        L.info(LOGTAG, "登录成功");
                            if (sdkManager != null) {
                                TuitaData.getInstance().setUser(loginReslut.getData());
                                BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN,data.toString());
                            }
                    }else{

                        BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN_ERROR,data.toString());
                    }
                } catch (Exception e) {
                    L.info(LOGTAG, "登录异常返回数据----" + e.toString());
                    return;
                }

                break;



        }
    }

    private static final long INITIALDELAY = 6;//初始化延时
    private static final long PERIOD = 6;//两次开始执行最小间隔时间
    private static final long AWAITTIME = 1;
    public void runPayScheduledTask(String pid){
        sdkManager.getScheduler().scheduleWithFixedDelay(new PayScheduledExecutor(pid), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }
    public void runLoginScheduledTask(String deviceNo){
        sdkManager.getScheduler().scheduleWithFixedDelay(new LoginScheduledExecutor(deviceNo), INITIALDELAY, PERIOD, TimeUnit.SECONDS);
    }
    public void runLogScheduledTask(){
        isLogTaskRuning = true;
        sdkManager.getUploadLogScheduler().scheduleWithFixedDelay(new UploadLogScheduledExecutor(ctx), 10,600, TimeUnit.SECONDS);
    }
    public void checkSocketConnectScheduledTask(){
        SDKManager.getInstance().getCheckSocketConnectScheduler().scheduleWithFixedDelay(new CheckSocketConnectScheduledExecutor(ctx), INITIALDELAY,30, TimeUnit.SECONDS);
    }
    public void cancleScheduledTask(){

        try {
            sdkManager.getScheduler().shutdown();

            if(!sdkManager.getScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)){
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                sdkManager.getScheduler().shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            sdkManager.getScheduler().shutdownNow();
        }

        L.info(LOGTAG, "结束任务----");

    }
    public void cancleUploadLogScheduledTask(){

        try {
            sdkManager.getUploadLogScheduler().shutdown();

            if(!sdkManager.getUploadLogScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)){
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
    public void cancleCheckSocketConnectScheduledTask(){

        try {
            SDKManager.getInstance().getCheckSocketConnectScheduler().shutdown();

            if(!SDKManager.getInstance().getCheckSocketConnectScheduler().awaitTermination(AWAITTIME, TimeUnit.SECONDS)){
                // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                SDKManager.getInstance().getCheckSocketConnectScheduler().shutdownNow();
            }
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            SDKManager.getInstance().getCheckSocketConnectScheduler().shutdownNow();
        }
        L.info(LOGTAG, "监控Socket连接任务结束----");

    }




    public LoginReslut.User getUser(){

        LoginReslut loginReslut;
        LoginReslut.User user = TuitaData.getInstance().getUser();
        L.info(LOGTAG, "读取内存数据  "+user);
        if(user == null){
            L.info(LOGTAG, "内存数据为空  读取本地用户文件");
            String data = FileUtil.read(FileUtil.getSDDir("sdk_user") + "/sdk_user.txt");
            if(data != null){
                loginReslut = new Gson().fromJson(data.toString(), LoginReslut.class);
                if(loginReslut != null){
                    user = loginReslut.getData();
                    if(user !=null){
                        setUser(user);
                        L.info(LOGTAG, "读取本地用户文件成功  "+user.toString());
                    }
                    else
                        L.info(LOGTAG, "读取本地用户文件失败--"+data.toString());
                }
            }else{
                L.info(LOGTAG, "读取本地用户文件失败  ");
            }
        }

        return user;
    }
    public void setUser(LoginReslut.User user){
        TuitaData.getInstance().setUser(user);
    }

    public void startLoginPage() {

        if(Utils.isFastClick(1000)) {
            return;
        }

        if(ActivityCollector.isActivityExist(LoginActivity.class))return;

        Intent intent = new Intent(ctx, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
    public void startPayPage(String payId) {
         if(Utils.isFastClick(1000)) {
            return;
        }
        if(ActivityCollector.isActivityExist(PayActivityC.class))return;

        Intent intent = new Intent(ctx, PayActivityC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("payId",payId);
        ctx.startActivity(intent);
    }

    @Override
    public void requestSocketResult(String key, Object... args) {

    }

    private void openConnection(String url,String appid) {
        initAppSocket(url,appid);
    }
    /**
     * 初始化Socket
     */
    public void initAppSocket(String url,String appid) {
        String uid = "";
        if(getUser()!=null)
            uid = getUser().getUid();
        AppSocket.Builder builder;
        if(appid == null)
            builder = new AppSocket.Builder(url+ DeviceUtil.getDeviceId2Ipad(ctx)).setEmitterListener(this);
        else
            builder = new AppSocket.Builder(url+ DeviceUtil.getDeviceId2Ipad(ctx)+"&appid="+appid+"&uid="+uid).setEmitterListener(this);
        AppSocket.init(builder).connect();
        C.CUR_SOCKET_URL = builder.socketHost;
    }
    public void closeConnection() {
        L.info(LOGTAG, "closeConnection-------------");
        AppSocket.getInstance().disConnnect();
    }
    private void offEmitterListener() {
        L.info(LOGTAG, "offEmitterListener------------- ");
        AppSocket.getInstance().offEmitterListener();
    }

    public void push_connect(int test, String appid) {
        if (test == 0) {
            openConnection(C.getDebugSocketURL(),appid);
        } else if (test == 1) {
        } else {
            openConnection(C.getSocketURL(),appid);
        }
    }

    public void stopConnection(){
        mHandler.removeCallbacksAndMessages(null);
        closeConnection();
        cancleScheduledTask();
        cancleUploadLogScheduledTask();
        cancleCheckSocketConnectScheduledTask();
    }
}
