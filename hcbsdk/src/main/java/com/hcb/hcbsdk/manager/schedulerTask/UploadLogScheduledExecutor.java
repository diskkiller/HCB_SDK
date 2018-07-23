package com.hcb.hcbsdk.manager.schedulerTask;

import android.content.Context;

import com.hcb.hcbsdk.logutils.LogUtil;
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
public class UploadLogScheduledExecutor implements Runnable {


    private Context ctx;

    public UploadLogScheduledExecutor(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        L.info("ScheduledTask", "开始上传日志任务----  "+Thread.currentThread().getName());


        test();
    }


    private void test() {

        LogUtil.getInstance().upload(ctx);
    }


}
