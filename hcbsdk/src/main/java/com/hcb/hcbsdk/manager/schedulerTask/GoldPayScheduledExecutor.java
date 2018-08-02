package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;

import com.google.gson.Gson;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
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
public class GoldPayScheduledExecutor implements Runnable {


    private final String snNo;
    private final int orderType;
    private Context ctx;

    public GoldPayScheduledExecutor(String snNo, Context ctx, int orderType) {
        this.ctx = ctx;
        this.snNo = snNo;
        this.orderType = orderType;
    }


    @Override
    public void run() {
        L.info("PushService", "支付----开始任务----  " + Thread.currentThread().getName());


        confirm_payInfo();
    }


    private void confirm_payInfo() {


        RequestCenter.confirm_goldPayInfo(snNo, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                L.info("PushService", "金豆支付----定时请求成功。。。。。  " + responseObj.toString());


                try {

                    JSONObject data = (JSONObject) responseObj;
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        if (data.get("data").equals("")) return;

                        LoginReslut consumeReslut = new Gson().fromJson(data.toString(), LoginReslut.class);
                        FileUtil.writeFile(FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME, data.toString(), false);
                        TuitaData.getInstance().setUser(consumeReslut.getData());

                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_SUCCESS, orderType + "");

                        SDKManager.getInstance().cancleScheduledTask();

                    } else
                        BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL, orderType + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(ctx, IConstants.PAY_FAIL, ((OkHttpException) reasonObj).getMsg().toString());
                L.info("PushService", "支付----定时请求失败。。。。。  ");
            }
        });
    }


}
