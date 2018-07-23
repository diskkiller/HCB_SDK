package com.hcb.hcbsdk.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hcb.hcbsdk.socketio.listener.IConstants;

import java.util.List;

/**
 * 发送IM广播
 * 
 * @author iamzl
 */
public class BroadcastUtil {

	private static Intent intent;
	private static String ACTION = "com.hcb.hcbsdk";

    public static String ACTION_HCB_MSG = ACTION + ".hcbmsg"; //调用广播
    public static String ACTION_HCB_KICKEDOUT = ACTION + ".hcbkickedout" ; //退出，单点登录


	public static String ACTION_MSG = ACTION + ".msg";// 用于监听广播
	public static String ACTION_MSG_SEND_SUCCESS = ACTION + ".msg.success";// 发送成功
	public static String ACTION_MSG_SEND_FAIL = ACTION + ".msg.fail";// 发送失败
	public static String ACTION_MSG_SEND_ERROR_NOTFRIEND = ACTION+ ".msg.errornotfriend";// 发送失败
	public static String ACTION_CONNECT_FAIL = ACTION + ".connect.fail";
	public static String ACTION_CONNECT_SUCCESS = ACTION + ".connect.success";
	public static String ACTION_CLRAR_MESSAGE_BUBBLE = ACTION+ ".clear.msgbubble";


    //消息加载广播
    public static String ACTION_TAB_LOADING = ACTION + "msg.loading";//消息加载广播


	public static void sendBroadcastToUI(Context context, String action,String data) {
		intent = new Intent();
		intent.setAction(action);
		if (data != null)
			intent.putExtra(IConstants.EXTRA_MESSAGE, data);
		if (context != null) {
			context.sendBroadcast(intent);
		}
		L.info("BPushService", "sendBroadcast success " + action
				+ "------------->" + data);
	}
	public static void sendLogBroadcastToUI(Context context,String data) {
		intent = new Intent();
		intent.setAction(IConstants.EXTRA_LOG_ACTION);
		if (data != null)
			intent.putExtra(IConstants.EXTRA_LOG_MESSAGE, data);
		if (context != null) {
			context.sendBroadcast(intent);
		}
	}



	/**
	 * 判断前后台运行
	 * 
	 * @return
	 */
	private static boolean isAppOnForeground(Context context) {
		String packageName = context.getPackageName();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

}
