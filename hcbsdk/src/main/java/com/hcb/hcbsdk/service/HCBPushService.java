package com.hcb.hcbsdk.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hcb.hcbsdk.logutils.LogUtil;
import com.hcb.hcbsdk.logutils.save.imp.CrashWriter;
import com.hcb.hcbsdk.logutils.upload.email.EmailReporter;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.L;

/**
 * @author
 *
 */
public class HCBPushService extends Service{
	private static final String LOGTAG = HCBPushService.class.getSimpleName();
	private static final String ACTION_PREFIX = "com.tuita.sdk";
	private static final String ACTION_START = ACTION_PREFIX + ".START";
	private static final String ACTION_STOP = ACTION_PREFIX + ".STOP";
	private static final String ACTION_SET_TEST = ACTION_PREFIX + ".SET_TEST";
	public PushServerConnection mPushConn;

	public static void setTest(Context ctx, int ifTest) {
		Intent i = new Intent(ctx, HCBPushService.class);
		i.setAction(ACTION_SET_TEST);
		i.putExtra("test", ifTest);
		ctx.startService(i);
	}
	public static void startService(Context ctx) {
		Intent i = new Intent(ctx, HCBPushService.class);
		i.setAction(ACTION_START);
		ctx.startService(i);
	}
	public static void stopService(Context ctx) {
		Intent i = new Intent(ctx, HCBPushService.class);
		i.setAction(ACTION_STOP);
		ctx.stopService(i);
	}


	@Override
	public IBinder onBind(Intent intent) {
		return new LocalBinder();
	}

	public final class LocalBinder extends Binder {
		public HCBPushService getService() {
			return HCBPushService.this;
		}
	}


	@Override
	public void onCreate() {
		super.onCreate();
		mPushConn = new PushServerConnection(getApplicationContext());
		startForeground();
//		initCrashReport();
	}


	private void initCrashReport() {
		LogUtil.getInstance()
				.setCacheSize(100 * 1024)//支持设置缓存大小，超出后清空
				.setLogDir(getApplicationContext(), "sdcard/" + this.getString(this.getApplicationInfo().labelRes) + "/")//定义路径为：sdcard/[app name]/
				.setWifiOnly(false)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
				.setLogSaver(new CrashWriter(getApplicationContext()))//支持自定义保存崩溃信息的样式
				.setLogLeve(LogUtil.LOG_LEVE_INFO)
				.setLogDebugModel(true) //设置是否显示日志信息
				//.setLogContent(LogUtil.LOG_LEVE_CONTENT_NULL)  //设置是否在邮件内容显示附件信息文字
				//.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
				.init(getApplicationContext());
		initEmailReporter();
	}

	/**
	 * 使用EMAIL发送日志
	 */
	private void initEmailReporter() {

		EmailReporter email = new EmailReporter(this);
		email.setReceiver("diskkiller@163.com");//收件人
		email.setSender("diskkiller@163.com");//发送人邮箱
		email.setSendPassword("220161178wang");//邮箱密码
		email.setSMTPHost("smtp.163.com");//SMTP地址
		email.setPort("465");//SMTP 端口
		LogUtil.getInstance().setUploadType(email);
	}



	@Override
	public void onDestroy() {
		Log.i(LOGTAG, "onDestroy");
//		stopForeground(true);
		stop();
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent != null) {
			String action = intent.getAction();
			if (action != null) {
				if (action.equals(ACTION_STOP)) {
					L.info(LOGTAG,"PushService stop");
				} else if (action.equals(ACTION_START)) {
					L.info(LOGTAG,"PushService start");
				} else if (action.equals(ACTION_SET_TEST)) {
					// 2 线上 1 预上线 0 测试
//					int test = intent.getIntExtra("test", 2);
//					push_connect(test);
				}
			}
		}
	}

	public void startLoginPage() {
		mPushConn.startLoginPage();
	}
	public void startPayPage(String appid,String aliPayQueryId,String orderId,String authorizeUrl,int orderType,String consumeGoldCoinCount,String ticketNum,int numType,String payType) {
		mPushConn.startPayPage(appid,aliPayQueryId,orderId,authorizeUrl,orderType,consumeGoldCoinCount,ticketNum,numType,payType);
	}

	public void push_connect(int code, String deviceNo) {
		mPushConn.push_connect(code,deviceNo);
	}
	public void stop_connect() {
		mPushConn.stopConnection();
	}
	public void close_connect() {
		mPushConn.closeConnection();
	}
	public void offEmitterListener(){
		mPushConn.offEmitterListener();
	}

	private void startForeground() {
		Notification note = new Notification(0, null, System.currentTimeMillis());
		note.flags |= Notification.FLAG_NO_CLEAR;
		if (android.os.Build.VERSION.SDK_INT < 18)
			startForeground(134138, note);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		L.info(LOGTAG, "unbindService");
		return super.onUnbind(intent);
	}


	public void stop() {
		L.info(LOGTAG, "service stop");
		mPushConn.stopConnection();
		BroadcastUtil.sendBroadcastToUI(this, IConstants.SERVICE_STOP,null);
	}

}