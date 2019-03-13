package com.hcb.hcbsdk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author WangGuoWei
 * @time 2018/2/1 19:38
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
public class WifiStateTXView extends android.support.v7.widget.AppCompatTextView {

    /**
     * WifiManager wifi_service = (WifiManager)getSystemService(WIFI_SERVICE);
     WifiInfo wifiInfo = wifi_service.getConnectionInfo();
     其中WifiManager是管理wifi的最重要的类，详细请参考
     http://developer.android.com/reference/android/net/wifi/WifiManager.html
     其中wifiInfo有以下的方法：
     wifiinfo.getBSSID()；
     wifiinfo.getSSID()；
     wifiinfo.getIpAddress()；获取IP地址。
     wifiinfo.getMacAddress()；获取MAC地址。
     wifiinfo.getNetworkId()；获取网络ID。
     wifiinfo.getLinkSpeed()；获取连接速度，可以让用户获知这一信息。
     wifiinfo.getRssi()；获取RSSI，RSSI就是接受信号强度指示。
                        在这可以直 接和华为提供的Wi-Fi信号阈值进行比较来提供给用户，
                        让用户对网络或地理位置做出调整来获得最好的连接效果。

     这里得到信号强度就靠wifiinfo.getRssi()；这个方法。得到的值是一个0到-100的区间值，
     是一个int型数据，其中0到-50表示信号最好，-50到-70表示信号偏差，小于-70表示最差，
     有可能连接不上或者掉线，一般Wifi已断则值为-200。
     */

    public static final String TAG = "WifiStateView";
    private static final int LEVEL_DGREE = 5;
    //定义wifi信号等级
    private static final int LEVEL_0 = 0;
    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;
    private static final int LEVEL_4 = 4;
    private static final int LEVEL_NONE = -1;

    WifiManager mWifiManager;
    WifiHandler mWifiHandler;
    ConnectivityManager mCM;

    //将handle设定为static，防止内存泄漏
    private static class WifiHandler extends Handler {
        WeakReference<WifiStateTXView> mView;
        public WifiHandler(WifiStateTXView view){
            mView = new WeakReference<WifiStateTXView>(view);

        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mView.get() == null){
                return;
            }
            WifiStateTXView view = mView.get();
//            Log.i(TAG, "handleMessage speed " + msg.what);
            view.setTextColor(Color.RED);
            view.setTextSize(20);
            view.setText(msg.what+" /"+WifiInfo.LINK_SPEED_UNITS);
//            Log.i(TAG, "handleMessage view " + view.getText().toString());
        }

    }


    private BroadcastReceiver mWifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //WifiManager.WIFI_STATE_CHANGED_ACTION
//            Log.i(TAG, "onReceive "+action);
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                //如果wifi没有连接成功，则显示wifi图标无连接的状态
                if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                    mWifiHandler.sendEmptyMessage(LEVEL_NONE);
                    return;
                }

            }else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){

                NetworkInfo netInfo =  mCM.getActiveNetworkInfo();
                //当前网络无连接，当前网络不是wifi连接,当前网络是wifi但是没有连接，wifi图标都显示无连接
                if(netInfo == null || netInfo.getType() != ConnectivityManager.TYPE_WIFI){
                    mWifiHandler.sendEmptyMessage(LEVEL_NONE);
                }else if(netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI && !netInfo.isConnected()){
                    mWifiHandler.sendEmptyMessage(LEVEL_NONE);

                }

            }else if(action.equals(WifiManager.RSSI_CHANGED_ACTION)){
                //当信号的rssi值发生变化时，在这里处理
                if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                    mWifiHandler.sendEmptyMessage(LEVEL_NONE);
                    return;
                }
                WifiInfo info = mWifiManager.getConnectionInfo();
             //算wifi的信号强度
                int speed = info.getLinkSpeed();

//                Log.i(TAG,"wifi rssi "+info.getRssi());
                mWifiHandler.sendEmptyMessage(speed);
            }
        }
    };

    public WifiStateTXView(Context context) {
        this(context, null);
    }

    public WifiStateTXView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WifiStateTXView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mCM = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiHandler = new WifiHandler(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter mFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        // mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        //注册广播接收器
        getContext().registerReceiver(mWifiStateReceiver, mFilter);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        mWifiHandler.removeCallbacksAndMessages(null);
        //注销广播接收器
//        getContext().unregisterReceiver(mWifiStateReceiver);
    }

    public void destroyReceiver(){
        mWifiHandler.removeCallbacksAndMessages(null);
        getContext().unregisterReceiver(mWifiStateReceiver);
        mWifiStateReceiver = null;
    }



}
