package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.dodo.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.KEY_FILE_NAME;

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
public class LoginScheduledExecutor implements Runnable {


    private final String deviceNo;
    private Context ctx;

    public LoginScheduledExecutor(Context ctx, String deviceNo) {
        this.deviceNo = deviceNo;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        L.info("PushService", "登录检查开始任务----  "+Thread.currentThread().getName());


        login();
    }


    private void login() {

        RequestCenter.confirm_login(deviceNo,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i("PushService", "登录----定时请求成功  "+responseObj.toString());
                JSONObject data = (JSONObject) responseObj;
                try {
                    if(data.get("data").equals("")) return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LoginReslut loginReslut = new Gson().fromJson(responseObj.toString(), LoginReslut.class);
                if (loginReslut.getStatus() == 1) {

                    FileUtil.writeFile
                            (FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME,responseObj.toString(), false);
                    L.info("PushService", "登录成功");
                    TuitaData.getInstance().setUser(loginReslut.getData());
                    BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN,responseObj.toString());
                }else{

                    BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN_ERROR,responseObj.toString());
                }            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i("PushService", "登录-----定时请求失败  ");
            }
        });
    }


}
