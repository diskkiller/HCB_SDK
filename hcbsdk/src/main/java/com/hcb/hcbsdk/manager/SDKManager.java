package com.hcb.hcbsdk.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hcb.hcbsdk.activity.ActivityCollector;
import com.hcb.hcbsdk.activity.LoginActivity;
import com.hcb.hcbsdk.activity.RechargeGoldActivity;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.listener.SDKDisposeDataListener;
import com.hcb.hcbsdk.okhttp.listener.SDKGoldDisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.PushService;
import com.hcb.hcbsdk.service.SendTask;
import com.hcb.hcbsdk.service.ThreadPoolFactory;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.socketio.listener.SocketPushDataListener;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.DataCleanManager;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
import com.hcb.logsdk.manager.LOGSDKManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.hcb.hcbsdk.util.C.IS_NEED_LOG;

/**
 * @author WangGuoWei
 * @time 2018/1/4 15:18
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
public class SDKManager {
    private static final int IM_PUSH_TEST = 0;
    private static final int IM_PUSH_ONLINE = 2;

    private SocketPushDataListener mListener;
    private Context ctx;
    private static SDKManager INSTANCE;
    public String appid;
    private SendTask sendTask;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private ScheduledExecutorService uploadLogScheduler = Executors.newScheduledThreadPool(5);
    private ScheduledExecutorService checkSocketConnectScheduler = Executors.newScheduledThreadPool(5);
    public static String API_URL = "";//线上
    private AlertDialog.Builder alert;
    private PushService mPushService;

    /**
     * 提供系统调用的构造函数，
     */
    private SDKManager() {
        INSTANCE = this;
    }

    /**
     * 获得实例
     *
     * @return
     */
    public static SDKManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SDKManager();
        }
        return INSTANCE;
    }


    public void init(Context ctx,String appid) {
        initImServices(ctx,appid);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(IConstants.SERVICE_STOP);
        ctx.registerReceiver(mReceiver, mFilter);

        if(IS_NEED_LOG){
            LOGSDKManager.getInstance().setApiUrl("http://39.107.107.82:3000");
            LOGSDKManager.getInstance().init(ctx);
        }

    }

    public Context getCtx() {
        return ctx;
    }

    public void initImServices(Context cx,String appid) {
        this.ctx = cx;
        this.appid = appid;
        Intent intent = new Intent(cx, PushService.class);
        cx.bindService(intent, conn, cx.BIND_AUTO_CREATE);
    }
    public void startServices() {
        Intent intent = new Intent(ctx, PushService.class);
        ctx.bindService(intent, conn, ctx.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.info("PushService", "service conn onServiceDisconnected " + name);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.info("PushService", "service 开启成功！！！ ");
            C.SOCKET_CONNECT_COUNT = 0;

            mPushService = ((PushService.LocalBinder) service).getService();
            if (mPushService != null) {

                mPushService.mPushConn.setSocketPushDataListener();
                mPushService.mPushConn.setSDKManager(SDKManager.this);
                startconnect();
            }
        }
    };


    public ScheduledExecutorService getScheduler() {
        if(scheduler.isTerminated())
            scheduler =  Executors.newScheduledThreadPool(5);
        return scheduler;
    }
    public ScheduledExecutorService getUploadLogScheduler() {
        if(uploadLogScheduler.isTerminated())
            uploadLogScheduler =  Executors.newScheduledThreadPool(5);
        return uploadLogScheduler;
    }
    public ScheduledExecutorService getCheckSocketConnectScheduler() {
        if(checkSocketConnectScheduler.isTerminated())
            checkSocketConnectScheduler =  Executors.newScheduledThreadPool(5);
        return checkSocketConnectScheduler;
    }


    public void sendLog(String mobile,final SDKDisposeDataListener listener){
        RequestCenter.sendLog(mobile, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    if(listener!=null){
                        int status =((JSONObject)responseObj).getInt("status");
                        if(status == 10000){
                            listener.onSuccess();
                        }else{
                            listener.onFailure();
                        }

                    }
                } catch (JSONException e) {
                    listener.onFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if(listener!=null){
                    listener.onFailure();
                }
            }
        });
    }


    public String getSocketURL(){
        return C.getSocketURL();
    }

    public void runPayScheduledTask(String pid){
        mPushService.mPushConn.runPayScheduledTask(pid);
    }
    public void runLoginScheduledTask(String deviceNo){
        mPushService.mPushConn.runLoginScheduledTask(deviceNo);
    }
    public void cancleScheduledTask(){

        mPushService.mPushConn.cancleScheduledTask();

    }
    public void userLogout(final SDKDisposeDataListener listener){

        RequestCenter.userLogout(DeviceUtil.getDeviceId2Ipad(ctx), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                if(listener!=null){
                        int status =((JSONObject)responseObj).getInt("status");
                        if(status == 10000){
                            logOut();
                            listener.onSuccess();
                        }else{
                            listener.onFailure();
                        }

                    }
                } catch (JSONException e) {
                    listener.onFailure();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if(listener!=null){
                    listener.onFailure();
                }
            }
        });

    }

    /**
     *
     * @param appId
     * @param num
     * @param listener
     */
    public void payOrExpendGold(String appId,String num,final SDKGoldDisposeDataListener listener){

        if(getUser()==null){
            Utils.showToastCenter(ctx,"用户不存在.");
            return;
        }

        RequestCenter.payOrExpendGold(getUser().getUid(),DeviceUtil.getDeviceId2Ipad(ctx),appId,num,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                if(listener!=null){
                        int status =((JSONObject)responseObj).getInt("status");
                        if(status == 10000){
                            listener.onSuccess(((JSONObject)responseObj).toString());
                        }else{
                            listener.onFailure(((JSONObject)responseObj).toString());
                        }

                    }
                } catch (JSONException e) {
                    listener.onFailure(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if(listener!=null){
                    listener.onFailure(reasonObj.toString());
                }
            }
        });

    }


    public LoginReslut.User getUser(){

        try {

            if(mPushService == null){
                L.error("pushService","--------------PushService is null");
                return null;
            }

            LoginReslut.User user = mPushService.mPushConn.getUser();

            if(user != null)
                return user;
            else
                return null;
        }catch (Exception e){
            throw new Error("PushService is null");
        }
    }

    private void setUser(LoginReslut.User user){
        mPushService.mPushConn.setUser(user);
    }





    /**
     * 异步发送日志
     * @param key
     * @param num
     * @param
     */
    public void sendBattleLog2Server(String key, int num) {

        L.info("PushService", "异步发送分数... "+num);

        sendTask = new SendTask(key,num,appid);
        ThreadPoolFactory.getNormalPool().execute(sendTask);
    }
    public void sendBattleLog2Server(int num) {

        L.info("PushService", "异步发送分数... "+num);

        sendTask = new SendTask("ZUIGAOFEN",num,appid);
        ThreadPoolFactory.getNormalPool().execute(sendTask);
    }




    public void startSendLog(String deviceNo){
        if(IS_NEED_LOG)
            LOGSDKManager.getInstance().startSendLog(deviceNo);
    }



    public void endSendLog(String deviceNo){
        if(IS_NEED_LOG)
            LOGSDKManager.getInstance().endSendLog(deviceNo);
    }

    public void sendLog(String msg){

        if(IS_NEED_LOG&&LOGSDKManager.getInstance().LogSocketConnect())
            LOGSDKManager.getInstance().sendLog(msg);

    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(IConstants.SERVICE_STOP)) {
                L.info("PushService", "收到重启广播  service重启... ");
                startServices();
                C.SOCKET_CONNECT_COUNT = 0;
            }
        }
    };

    public void startLoginPage(){
        mPushService.startLoginPage();
    }
    public void startLoginPage(Activity activity){

        if(Utils.isFastClick(1000)) {
            return;
        }

        if(ActivityCollector.isActivityExist(LoginActivity.class))return;

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
    public void startRechargeGoldPage(Activity activity,String notify){

        if(Utils.isFastClick(1000)) {
            return;
        }

        if(ActivityCollector.isActivityExist(RechargeGoldActivity.class))return;

        Intent intent = new Intent(activity, RechargeGoldActivity.class);
        if(notify!=null)
            intent.putExtra("notify",notify);
        activity.startActivity(intent);
    }
    public void startPayPage(String payId){
        mPushService.startPayPage(payId);
    }


    public void changeModle(boolean isDebug,String appid){
        L.changeModle = true;
        L.debug = isDebug;
        this.appid = appid;
        stopconnect();
        L.info("PushService", " 切换模式   L.debug:"+L.debug);
    }



    public void startconnect(){
         if(L.debug){
            this.API_URL = C.getDebugapiURL();//测试
            mPushService.push_connect(0,appid);
        }else{
            this.API_URL = C.getAPIURL();//线上
            mPushService.push_connect(2,appid);
        }

        L.info("PushService", " 当前环境   L.debug:  "+L.debug+"   API_URL: "+API_URL);
    }

    public void stopconnect(){
        mPushService.close_connect();
    }

    public void destroy(){
        if(IS_NEED_LOG)
            LOGSDKManager.getInstance().destroy();
        ctx.unbindService(conn);
        L.info("PushService", "销毁OR重启  C.SOCKET_RECONNECT...  "+C.SOCKET_RECONNECT);
        if(!C.SOCKET_RECONNECT){
            ctx.unregisterReceiver(mReceiver);
            C.SOCKET_RECONNECT = false;
        }

    }

    public void logOut(){
        TuitaData.getInstance().setUser(null);
        DataCleanManager.deleteFolderFile( FileUtil.getSDDir("sdk_user"), true);
//        AppSocket.getInstance().sdk_logout();
    }
}
