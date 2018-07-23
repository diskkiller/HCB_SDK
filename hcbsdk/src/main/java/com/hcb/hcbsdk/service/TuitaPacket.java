package com.hcb.hcbsdk.service;

import com.google.gson.Gson;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.service.msgBean.BattleLog;
import com.hcb.hcbsdk.service.msgBean.LoginBean;
import com.hcb.hcbsdk.service.msgBean.LoginOutBean;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;

import java.io.Serializable;

/**
 * @author
 */

public class TuitaPacket implements Serializable {
	protected static final String TUITA_PACK_PING = "{}";
	protected static final int TUITA_MSG_TYPE_CONNECT = 1;
	protected static final int TUITA_MSG_TYPE_RECEIVE_NOTGM = 2;
	protected static final int TUITA_MSG_TYPE_RECEIVE_ISGM = 3;
	protected static final int TUITA_MSG_TYPE_PING = 4;
	protected static final int TUITA_MSG_TYPE_SOUYUE_LOGINOUT = 5;

	protected static final int TUITA_MSG_TYPE_IM_CONNECT = 100;
	protected static final int TUITA_MSG_TYPE_IM_MESSAGE_ACK = 101;
	protected static final int TUITA_MSG_TYPE_IM_MESSAGE_OFFLINE = 102;
	protected static final int TUITA_MSG_TYPE_IM_MESSAGE_ONLINE = 103;
	protected static final int TUITA_MSG_TYPE_IM_MESSAGE = 104;
	protected static final int TUITA_MSG_TYPE_IM_MESSAGE_HISTORY = 105;
	protected static final int TUITA_MSG_TYPE_IM_RELATION_USER = 106;
	protected static final int TUITA_MSG_TYPE_IM_RELATION_GROUP = 107;
	protected static final int TUITA_MSG_TYPE_IM_UPDATE = 108;
	protected static final int TUITA_MSG_TYPE_IM_INFO = 109;
	protected static final int TUITA_MSG_TYPE_IM_LOGOUT = 118;
	protected static final int TUITA_MSG_TYPE_IM_CONTACTS_STATUS = 111;
	protected static final int TUITA_MSG_TYPE_IM_USER_SEARCH = 112;
	protected static final int TUITA_MSG_TYPE_IM_ZSB_GIFT = 113;
	protected static final int TUITA_MSG_TYPE_IM_ZSB_CHARGE = 114;
	protected static final int TUITA_MSG_TYPE_IM_CONTACTS_UPLOAD = 115;
	protected static final int TUITA_MSG_TYPE_IM_KICKED_OUT = 116;



	private String type,data;


	protected TuitaPacket() {

	}

	protected TuitaPacket(String data) {
		this.data = data;
	}
	protected TuitaPacket(String type, String data) {
		this.type = type;
		this.data = data;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public static String createLogintPacket(String phone, String vercode,String eventCode) {

		return new Gson().toJson(new LoginBean(phone,vercode,eventCode)).toString();
	}
	public static String createLogoutPacket() {

		return new Gson().toJson(new LoginOutBean(DeviceUtil.getDeviceId2Ipad(SDKManager.getInstance().getCtx()))).toString();
	}
	public static String createBattleLogPacket(String key, int num,String appId) {

		BattleLog.DataBean dataBean;

		if(SDKManager.getInstance().getUser()!=null){

			dataBean = new BattleLog.DataBean(key,num,appId,
					DeviceUtil.getDeviceId2Ipad(SDKManager.getInstance().getCtx()),
					SDKManager.getInstance().getUser().getUid());
		}else{
			L.info("PushService", "第三方发送数据----user  null");
			return null;
		}


		return new Gson().toJson(new BattleLog("BATTLETRACE",dataBean)).toString();
	}



}
