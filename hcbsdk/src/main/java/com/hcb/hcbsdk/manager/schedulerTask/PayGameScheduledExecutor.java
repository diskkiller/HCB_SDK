package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;

import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.L;

import org.json.JSONException;
import org.json.JSONObject;

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
public class PayGameScheduledExecutor implements Runnable {


    private final String snNo;
    private Context ctx;

    public PayGameScheduledExecutor(String snNo, Context ctx) {
        this.ctx = ctx;
        this.snNo = snNo;
    }

    @Override
    public void run() {
        L.info("PushService", "游戏----支付----开始任务----  "+Thread.currentThread().getName());


        confirm_gamePay();
    }


    private void confirm_gamePay() {


        RequestCenter.confirm_gamePay(snNo,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                L.info("PushService", "游戏-------支付----定时请求成功。。。。。  "+responseObj.toString());
                JSONObject data = (JSONObject) responseObj;
                try {
                    if(data.get("data").equals("success")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PINTU_PAY_SUCCESS,null);
                        L.info("PushService", "游戏---支付----定时请求支付成功-----  "+responseObj.toString());
                    }else if(data.get("data").equals("fail")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,null);
                        L.info("PushService", "游戏---支付----定时请求支付失败-----  "+responseObj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,((OkHttpException)reasonObj).getMsg().toString());
                L.info("PushService", "游戏---支付----定时请求失败。。。。。  ");
            }
        });
    }


}
