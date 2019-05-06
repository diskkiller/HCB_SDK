package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;

import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.msgBean.Order;
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
public class PayScheduledExecutor implements Runnable {


    private final String snNo;
    private Context ctx;
    private String orderId,payType;

    public PayScheduledExecutor(String snNo, Context ctx, String orderId, String payType) {
        this.ctx = ctx;
        this.snNo = snNo;
        this.orderId = orderId;
        this.payType = payType;
    }

    @Override
    public void run() {
        L.info("PushService", "支付----开始任务----  "+Thread.currentThread().getName());


        if(payType.equals("2"))
            confirm_aliPayInfo();
        else {
            confirm_payInfo();
        }
    }


    private void confirm_payInfo() {


        RequestCenter.confirm_payInfo(snNo,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                L.info("PushService", "支付----定时请求成功。。。。。  "+responseObj.toString());
                JSONObject data = (JSONObject) responseObj;
                try {
                    if(data.get("data").equals("success")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS,null);
                        L.info("PushService", "支付----定时请求支付成功-----  "+responseObj.toString());
                    }else if(data.get("data").equals("fail")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,null);
                        L.info("PushService", "支付----定时请求支付失败-----  "+responseObj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,((OkHttpException)reasonObj).getMsg().toString());
                L.info("PushService", "支付----定时请求失败。。。。。  ");
            }
        });
    }

    private void confirm_aliPayInfo() {


        RequestCenter.confirm_aliPayInfo(orderId,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                L.info("PushService", "支付宝支付----定时请求成功。。。。。  "+responseObj.toString());
                JSONObject data = (JSONObject) responseObj;
                try {
                    if(data.get("body").equals("success")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS,null);
                        L.info("PushService", "支付宝支付----定时请求支付成功-----  "+responseObj.toString());
                    }else if(data.get("body").equals("fail")){
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,null);
                        L.info("PushService", "支付宝支付----定时请求支付失败-----  "+responseObj.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL,((OkHttpException)reasonObj).getMsg().toString());
                L.info("PushService", "支付宝支付----定时请求失败。。。。。  ");
            }
        });
    }


}
