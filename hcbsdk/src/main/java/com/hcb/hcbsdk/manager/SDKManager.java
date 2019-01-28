package com.hcb.hcbsdk.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.hcb.hcbsdk.activity.AboutActivity;
import com.hcb.hcbsdk.activity.ActivityCollector;
import com.hcb.hcbsdk.activity.LoginActivity;
import com.hcb.hcbsdk.activity.RechargeGoldActivity;
import com.hcb.hcbsdk.activity.TestLottieAnimaActivity;
import com.hcb.hcbsdk.activity.UserAuthenticationActivity;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.HCBPushService;
import com.hcb.hcbsdk.service.SendTask;
import com.hcb.hcbsdk.service.ThreadPoolFactory;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.service.msgBean.Order;
import com.hcb.hcbsdk.service.msgBean.OrderForm;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.socketio.listener.SocketPushDataListener;
import com.hcb.hcbsdk.util.BarcodeUtils;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.C;
import com.hcb.hcbsdk.util.CheckUtil;
import com.hcb.hcbsdk.util.DataCleanManager;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
import com.hcb.logsdk.manager.LOGSDKManager;

import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.hcb.hcbsdk.util.C.IS_NEED_LOG;
import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.SERVICE_NAME;
import static com.hcb.hcbsdk.util.L.deviceNo;

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
    private SendTask sendTask;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private ScheduledExecutorService uploadLogScheduler = Executors.newScheduledThreadPool(5);
    private ScheduledExecutorService checkSocketConnectScheduler = Executors.newScheduledThreadPool(5);
    public static String API_URL = "";//线上
    private AlertDialog.Builder alert;
    private HCBPushService mPushService;

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


    public void init(Context ctx, String deviceNo, boolean isDebug) {
        if (ctx == null) {
            L.info("", "WARNING ============>> ctx is null,service start failed...");
            return;
        }
        L.debug = isDebug;
        L.deviceNo = deviceNo;
        this.ctx = ctx;
        initImServices();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(IConstants.SERVICE_STOP);
        ctx.registerReceiver(mReceiver, mFilter);

        if (IS_NEED_LOG) {
            LOGSDKManager.getInstance().setApiUrl("http://39.107.107.82:3000");
            LOGSDKManager.getInstance().init(ctx);
        }

    }

    public Context getCtx() {
        return ctx;
    }

    public void initImServices() {

        Intent intent = new Intent(ctx, HCBPushService.class);
        ctx.bindService(intent, conn, ctx.BIND_AUTO_CREATE);
    }

    public void startServices() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(IConstants.SERVICE_STOP);
        ctx.registerReceiver(mReceiver, mFilter);
        Intent intent = new Intent(ctx, HCBPushService.class);
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

            mPushService = ((HCBPushService.LocalBinder) service).getService();
            if (mPushService != null) {

                mPushService.mPushConn.setSDKManager(SDKManager.this);
                startconnect();
            }
        }
    };


    public ScheduledExecutorService getScheduler() {
        if (scheduler.isTerminated())
            scheduler = Executors.newScheduledThreadPool(5);
        return scheduler;
    }

    public ScheduledExecutorService getUploadLogScheduler() {
        if (uploadLogScheduler.isTerminated())
            uploadLogScheduler = Executors.newScheduledThreadPool(5);
        return uploadLogScheduler;
    }

    public ScheduledExecutorService getCheckSocketConnectScheduler() {
        if (checkSocketConnectScheduler.isTerminated())
            checkSocketConnectScheduler = Executors.newScheduledThreadPool(5);
        return checkSocketConnectScheduler;
    }


    public String getSocketURL() {
        if (L.debug)
            return C.getDebugSocketURL();
        else
            return C.getSocketURL();
    }

    public void runPayScheduledTask(String snNo) {
        mPushService.mPushConn.runPayScheduledTask(snNo);
    }

    public void runGoldPayScheduledTask(String snNo, int orderType) {
        mPushService.mPushConn.runGoldPayScheduledTask(snNo, orderType);
    }

    public void runLoginScheduledTask(String deviceNo) {
        mPushService.mPushConn.runLoginScheduledTask(ctx, deviceNo);
    }

    public void cancleScheduledTask() {

        mPushService.mPushConn.cancleScheduledTask();

    }


    private int QR_WIDTH = 300;
    private int QR_HEIGHT = 300;

    /**
     * 兑奖码专用
     * @param content
     * @param width
     * @param height
     * @return
     */
    public Bitmap createDMBarcode(String content, int width, int height) {

        if (width != 0)
            QR_HEIGHT = width;
        if (height != 0)
            QR_HEIGHT = height;

        Bitmap bitmap = BarcodeUtils.createBarcode(content, null, QR_WIDTH, QR_HEIGHT, BarcodeFormat.DATA_MATRIX, Color.BLACK, Color.WHITE);
        return bitmap;
    }

    /**
     * 支付/登陆专用
     * @param content
     * @return
     */
    public Bitmap createQRCode(String content) {
        Bitmap bitmap = Utils.createQRImage(content);
        return bitmap;
    }


    public LoginReslut.User getUser() {

        try {

            if (mPushService == null) {
                L.error("pushService", "--------------PushService is null");
                return null;
            }

            LoginReslut.User user = mPushService.mPushConn.getUser();

            if (user != null)
                return user;
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void setUser(LoginReslut.User user) {
        mPushService.mPushConn.setUser(user);
    }


    /**
     * 异步发送日志
     *
     * @param key
     * @param num
     * @param
     */
    public void sendBattleLog2Server(String key, int num) {

        L.info("PushService", "异步发送分数... " + num);

        sendTask = new SendTask(key, num, deviceNo);
        ThreadPoolFactory.getNormalPool().execute(sendTask);
    }

    public void sendBattleLog2Server(int num) {

        L.info("PushService", "异步发送分数... " + num);

        sendTask = new SendTask("ZUIGAOFEN", num, deviceNo);
        ThreadPoolFactory.getNormalPool().execute(sendTask);
    }


    public void startSendLog(String deviceNo) {
        if (IS_NEED_LOG)
            LOGSDKManager.getInstance().startSendLog(deviceNo);
    }


    public void endSendLog(String deviceNo) {
        if (IS_NEED_LOG)
            LOGSDKManager.getInstance().endSendLog(deviceNo);
    }

    public void sendLog(String msg) {

        if (IS_NEED_LOG && LOGSDKManager.getInstance().LogSocketConnect())
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
                C.SOCKET_RECONNECT = false;
            }
        }
    };

    public void startLoginPage() {
        mPushService.startLoginPage();
    }

    public void startLoginPage(Activity activity) {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(LoginActivity.class)) return;

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    public void startAboutPage(Activity activity) {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(AboutActivity.class)) return;

        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    public void startUserAuPage(Activity activity) {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(UserAuthenticationActivity.class)) return;

        Intent intent = new Intent(activity, UserAuthenticationActivity.class);
        activity.startActivity(intent);
    }

    public void startTestLottieAnimaPage(Activity activity) {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(TestLottieAnimaActivity.class)) return;

        Intent intent = new Intent(activity, TestLottieAnimaActivity.class);
        activity.startActivity(intent);
    }


    /**
     * 金豆充值页面
     * @param activity
     * @param appid
     */
    public void startRechargeGoldPage(Activity activity, String appid) {

        if (Utils.isFastClick(1000)) {
            return;
        }

        if (ActivityCollector.isActivityExist(RechargeGoldActivity.class)) return;

        Intent intent = new Intent(activity, RechargeGoldActivity.class);
        intent.putExtra("appid", appid);
        activity.startActivity(intent);
    }

    /**
     * 支付/充值 调用
     * @param orderId
     * @param authorizeUrl
     * @param orderType orderType = 0（金豆消耗）；1（彩票支付）；2（金豆充值 废弃）；3（金豆消耗）(不够显示充值二维码)
     * @param consumeGoldCoinCount
     */
    public void startPayPage(String orderId, String authorizeUrl, int orderType, String consumeGoldCoinCount) {
        mPushService.startPayPage("", orderId, authorizeUrl, orderType, consumeGoldCoinCount, null, 0);
    }

    /**
     * 好运来一包/好运来一张 调用 需显示彩票包号票号
     * @param orderId
     * @param authorizeUrl
     * @param orderType
     * @param consumeGoldCoinCount
     * @param ticketNum
     * @param numType 0（单张购买）；1（整包购买）
     */
    public void startPayPage(String orderId, String authorizeUrl, int orderType, String consumeGoldCoinCount, String ticketNum, int numType) {
        mPushService.startPayPage("", orderId, authorizeUrl, orderType, consumeGoldCoinCount, ticketNum, numType);
    }

    /**
     * 游戏支付充值 调用（金豆消耗）(不够显示充值二维码)
     * @param appid
     * @param consumeGoldCoinCount
     */
    public void startGamePayPage(String appid, int consumeGoldCoinCount) {
        mPushService.startPayPage(appid, "", "", 3, consumeGoldCoinCount + "", null, 0);
    }


    public void changeModle(boolean isDebug, String deviceNo) {
        L.changeModle = true;
        L.debug = isDebug;
        L.deviceNo = deviceNo;
        stopconnect();
        L.info("PushService", " 切换模式   L.debug:" + L.debug);
    }


    public void startconnect() {
        C.IS_SOCKET_CLOSE = false;
        if (L.debug) {
            this.API_URL = C.getDebugapiURL();//测试
//            mPushService.push_connect(0, deviceNo);
        } else {
            this.API_URL = C.getAPIURL();//线上
//            mPushService.push_connect(2, deviceNo);
        }

        L.info("PushService", " 当前环境   L.debug:  " + L.debug + "   API_URL: " + API_URL);
    }

    public void stopconnect() {
        mPushService.close_connect();
    }

    public void offEmitterListener() {
        mPushService.offEmitterListener();
    }

    public void killServer() {
        destroy();
//        offEmitterListener();
        C.IS_SOCKET_CLOSE = true;
    }

    public void checkRebuildService(Context ctx, String deviceNo, boolean isDebug) {
        L.info("", " 检查服务 =========  isServiceWorked   " + CheckUtil.isServiceWorked(ctx, SERVICE_NAME));
        if (!CheckUtil.isServiceWorked(ctx, SERVICE_NAME)) {
            L.info("", "服务已死，重启服务。。。");
            init(ctx, deviceNo, isDebug);
        }

    }


    public void destroy() {
        if (IS_NEED_LOG)
            LOGSDKManager.getInstance().destroy();
        ctx.unbindService(conn);
        L.info("PushService", "销毁OR重启  C.SOCKET_RECONNECT...  " + C.SOCKET_RECONNECT);
        if (!C.SOCKET_RECONNECT) {
            ctx.unregisterReceiver(mReceiver);
        }

    }

    public void logOut() {

        RequestCenter.userLogout(DeviceUtil.getDeviceId2Ipad(ctx), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        TuitaData.getInstance().setUser(null);
                        DataCleanManager.deleteFolderFile(FileUtil.getSDDir(KEY_DIR_NAME), true);
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.LOGIN_OUT, null);
                    } else
                        Utils.showToastCenter(ctx, ((JSONObject) responseObj).getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(ctx, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });

    }

    public void order() {

        RequestCenter.order(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        Order mOrder = new Gson().fromJson(((JSONObject) responseObj).toString(), Order.class);
                        if (mOrder != null)
                            startPayPage(mOrder.getData().getOrderId() + "", mOrder.getData().getUrl(), 1, "0");

                    } else
                        Utils.showToastCenter(ctx, ((JSONObject) responseObj).getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(ctx, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });

    }


    public void ticketLock() {

        RequestCenter.ticketLock(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        order();

                    } else
                        Utils.showToastCenter(ctx, ((JSONObject) responseObj).getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(ctx, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });

    }

    public void orderList() {

        RequestCenter.orderList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                    } else
                        Utils.showToastCenter(ctx, ((JSONObject) responseObj).getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(ctx, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });

    }

    public void delOrderList() {

        RequestCenter.delOrderList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                    } else
                        Utils.showToastCenter(ctx, ((JSONObject) responseObj).getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(ctx, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });

    }

    public void clearServerUserData() {
        RequestCenter.clearServerUserData(DeviceUtil.getDeviceId2Ipad(ctx), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        L.info("PushService", "登陆成功...清除服务器缓存成功！！！  ");
                    } else
                        L.info("PushService", "登陆成功...清除服务器缓存失败！！！  ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                L.info("PushService", ((OkHttpException) reasonObj).getMsg() + "");
            }
        });
    }

    public void goldCoinAddOrLess(String appId, String goldNum,String type) {
        RequestCenter.goldCoinAddOrLess(appId, goldNum, type, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        JSONObject data = ((JSONObject) responseObj).getJSONObject("data");
                        int goldCoin = data.getInt("goldCoin");
                        SDKManager.getInstance().getUser().setGoldCoin(goldCoin);

                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.CHESS_GOLD_SUCCESS, null);

                        L.info("PushService", "象棋游戏金豆...成功！！！  goldCoin: "+goldCoin);
                        L.info("PushService", "象棋游戏金豆...成功！！！  user_goldCoin: "+SDKManager.getInstance().getUser().getGoldCoin());
                    } else {
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.CHESS_GOLD_FAIL, null);

                        L.info("PushService", "象棋游戏金豆...失败！！！  ");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.CHESS_GOLD_FAIL, null);

                L.info("PushService", ((OkHttpException) reasonObj).getMsg() + "");
            }
        });
    }
}
