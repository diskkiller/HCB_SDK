package com.hcb.hcbsdk.manager.schedulerTask;

import android.util.Log;

import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.util.L;

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

    public LoginScheduledExecutor(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    public void run() {
        L.info("PushService", "登录检查开始任务----  "+Thread.currentThread().getName());


        test();
    }


    private void test() {


        RequestCenter.confirm_login(deviceNo,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.i("PushService", "登录----定时请求成功  "+responseObj.toString());
            }

            @Override
            public void onFailure(Object reasonObj) {
                Log.i("PushService", "登录-----定时请求失败  ");
            }
        });
    }


}
