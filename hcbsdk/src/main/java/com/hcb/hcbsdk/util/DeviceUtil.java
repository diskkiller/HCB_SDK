package com.hcb.hcbsdk.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author wangguowei
 * @ClassName: DeviceUtil
 * @Description: 获取设备信息工具
 *
 *  所有的设备都可以返回一个 TelephonyManager.getDeviceId()
 *  所有的GSM设备 (测试设备都装载有SIM卡) 可以返回一个TelephonyManager.getSimSerialNumber()
 *  所有的CDMA 设备对于 getSimSerialNumber() 却返回一个空值！
 *  所有添加有谷歌账户的设备可以返回一个 ANDROID_ID
 *  所有的CDMA设备对于 ANDROID_ID 和 TelephonyManager.getDeviceId() 返回相同的值（只要在设置时添加了谷歌账户）
 *  目前尚未测试的：没有SIM卡的GSM设备、没有添加谷歌账户的GSM设备、处于飞行模式的设备。
 *
 * @date 14/12/15 下午3:07
 */
public class DeviceUtil {
	
	/**
     * 获取手机型号
     * @return
     */
    public static String getPhoneModel(Context context){

        return android.os.Build.MODEL;
    }

    /**
     * 获取SDK版本号
     * @return
     */
    @SuppressWarnings("deprecation")
	public static String getSDKVersion(Context context){

        return android.os.Build.VERSION.SDK;
    }

    public static String getDeviceId2Ipad(Context context){

        return "0123456789ABCDE";
    }

    /**
     * 获取Firmware/OS 版本号
     * @return
     */
	public static String getRelaseVersion(Context context){

        return android.os.Build.VERSION.RELEASE;
    }

	/**
     * 获取手机厂商
     * @return
     */
	public static String getManufacturer(Context context){

		return android.os.Build.MANUFACTURER;
    }
	
    /**
     * 获取设备IMEI
     *
     *  真实设备的标识，它根据不同的手机设备返回IMEI，MEID或者ESN码，但它在使用的过程中会遇 到很多问题：
     *   非手机设备： 如果只带有Wifi的设备或者音乐播放器没有通话的硬件功能的话就没有这个DEVICE_ID
     *   权限： 获取DEVICE_ID需要READ_PHONE_STATE权限，但如果我们只为了获取它，没有用到其他的通话功能，那这个权限有点大才小用
     *   bug：在少数的一些手机设备上，该实现有漏洞，会返回垃圾，如:zeros或者asterisks的产品
     *
     * @return
     */
    public static String getDeviceId(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getDeviceId();
    }


    /**
     * 获取ANDROID_ID
     *
     *  ANDROID_ID是设备第一次启动时产生和存储的64bit的一个数，当设备被wipe后该数重置
     *  它在Android <=2.1 or Android >=2.3的版本是可靠、稳定的，但在2.2的版本并不是100%可靠的
     *  在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
     *
     * @return
     */
    public static String getAndroidId(Context context){

        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取 MAC Address
     *
     *   wifi mac地址（wifi）不是所有的设备都有Wifi，并且，如果Wifi没有打开，那硬件设备无法返回MAC ADDRESS.
     *   需要加入android.permission.ACCESS_WIFI_STATE 权限，否则这个地址会为null
     *
     * @return
     */
    public static String getMacAddress(Context context){

        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        return wm.getConnectionInfo().getMacAddress();
    }

    /**
     * 获取 BT MAC Address
     *
     *   蓝牙地址, 蓝牙没有必要打开，也能读取.
     *   需要加入android.permission.BLUETOOTH 权限
     *
     * @return
     */
    public static String getBluetoothAddress(){

        BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return m_BluetoothAdapter.getAddress();
    }

    /**
     * 获取手机SIM卡的序列号
     *
     *  需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getSerialNumber(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getSimSerialNumber();
    }

    /**
     * 获取手机号
     *：
     *  GSM手机的 MSISDN.
     *  Return null if it is unavailable.
     *
     * @return
     */
    public static String getPhoneNumber(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getLine1Number();
    }

    /**
     * 获取ISO标准的国家码，即国际长途区号。
     *
     *  注意：仅当用户已在网络注册后有效。
     *       在CDMA网络中结果也许不可靠。
     *
     * @return
     */
    public static String getNetworkCountryIso(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getNetworkCountryIso();
    }

    /**
     * 当前使用的网络类型：
     * 例如： NETWORK_TYPE_UNKNOWN  网络类型未知  0
            NETWORK_TYPE_GPRS     GPRS网络  1
            NETWORK_TYPE_EDGE     EDGE网络  2
            NETWORK_TYPE_UMTS     UMTS网络  3
            NETWORK_TYPE_HSDPA    HSDPA网络  8
            NETWORK_TYPE_HSUPA    HSUPA网络  9
            NETWORK_TYPE_HSPA     HSPA网络  10
            NETWORK_TYPE_CDMA     CDMA网络,IS95A 或 IS95B.  4
            NETWORK_TYPE_EVDO_0   EVDO网络, revision 0.  5
            NETWORK_TYPE_EVDO_A   EVDO网络, revision A.  6
            NETWORK_TYPE_1xRTT    1xRTT网络  7
     *
     * @return
     */
    public static int getNetworkType(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getNetworkType();
    }

    /**
     * 手机类型：
     * 例如： PHONE_TYPE_NONE  无信号
            PHONE_TYPE_GSM   GSM信号
            PHONE_TYPE_CDMA  CDMA信号
     *
     * @return
     */
    public static int getPhoneType(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getPhoneType();
    }

    /**
     * Returns the ISO country code equivalent for the SIM provider's country code.
     * 获取ISO国家码，相当于提供SIM卡的国家码。
     *
     * @return
     */
    public static String getSimCountryIso(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getSimCountryIso();
    }

    /**
     * 服务商名称：
     * 例如：中国移动、联通
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return
     */
    public static String getSimOperatorName(Context context){

        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyMgr.getSimOperatorName();
    }

    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode(Context context){

        try {
            int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return -1;

    }

    /**
     * 获取版本名称
     * @return
     */
    public static String getVersionName(Context context){

        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }



    /**
     * 获取设备唯一编号
     *
     *  MD5(DeviceId + SerialNumber + AndroidId + UUID)
     *
     * @return
     */
    public static String getDeviceUuid(Context context){

        //return MD5Code.encode(getDeviceId(context) + getSerialNumber(context) + getAndroidId(context) + getUUID(context));
        return MD5Util.encode(getDeviceId(context) + getSerialNumber(context) + getAndroidId(context));
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context){

        String uuid = SPHelper.getValue(context,"sysMap", Context.MODE_PRIVATE,"uuid");

        if(uuid == null || "".equals(uuid)){
            uuid = UUID.randomUUID().toString();
            SPHelper.putValue(context,"sysMap", Context.MODE_PRIVATE,"uuid",uuid);
        }

        return uuid;
    }

    /**
     * 获取本地安装包信息（包名 逗号分隔）
     */
    public static String getPackageNames(Context context){

        List<PackageInfo> packageInfoList = getAllApps(context);

        String pnames = "";

        for (int i = 0; i < packageInfoList.size(); i++) {
            pnames += packageInfoList.get(i).packageName + ",";
        }
        if(pnames != null && pnames.length() > 0){
            pnames = pnames.substring(0,pnames.length()-1);
        }

        return pnames;
    }


    //获得当前应用包名
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        String contextActivity = runningActivity.substring(runningActivity.lastIndexOf(".")+1);
        return contextActivity;
    }


    /**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    private static int[] pxy = { 0, 0 };

    public static int[] getXY(Activity act) {
        if ((pxy[0] == 0) && (pxy[1] == 0)) {
            DisplayMetrics dm = new DisplayMetrics();
            act.getWindowManager().getDefaultDisplay().getMetrics(dm);
            pxy[0] = dm.widthPixels;
            pxy[1] = dm.heightPixels;
        }
        return pxy;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
