package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.hcb.hcbsdk.activity.Login_weichat_alipay_Activity;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.AliLoginReslut;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.service.msgBean.User;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;
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
public class AliLoginScheduledExecutor implements Runnable {


    private final String queryCode;
    private Context ctx;

    public AliLoginScheduledExecutor(Context ctx, String queryCode) {
        this.queryCode = queryCode;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        L.info("huacaisdk", "登录检查开始任务----  "+Thread.currentThread().getName());


        login();
    }



    private void login() {

        RequestCenter.confirm_AliLogin(queryCode,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i("huacaisdk", "登录----定时请求成功  "+responseObj.toString());
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 200) {

                        AliLoginReslut loginReslut = new Gson().fromJson(responseObj.toString(), AliLoginReslut.class);

                        User user = loginReslut.getData();

                        if(user!=null){
                            if(user.isSuccess()){
                                if(StringUtils.isEmpty(user.getMobile())){
                                    L.info("huacaisdk", " 需绑定手机");
                                    BroadcastUtil.sendAliBroadcastToUI(ctx,IConstants.LOGIN_BIND_TEL,user.getToken(),user.getOpenId());
                                }else{
                                    FileUtil.writeFile
                                            (FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME,responseObj.toString(), false);
                                    L.info("huacaisdk", "登录成功");
                                    TuitaData.getInstance().setUser(loginReslut.getData());
                                    BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN,"");
                                }


                            }


                        }



                    } else{
                        BroadcastUtil.sendBroadcastToUI(ctx,IConstants.LOGIN_ERROR,responseObj.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i("huacaisdk", "登录-----定时请求失败  ");
            }
        });
    }


}
