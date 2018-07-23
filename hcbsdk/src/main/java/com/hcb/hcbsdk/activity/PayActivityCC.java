package com.hcb.hcbsdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.activity.widget.TasktimerView;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.msgBean.GoldOrderData;
import com.hcb.hcbsdk.service.msgBean.OrderForm;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.NetStatus;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hcb.hcbsdk.R.id.sweepIV;

/**
 * @author WangGuoWei
 * @time 2018/2/1 12:17
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
public class PayActivityCC extends JKCBaseActivity {
    private OrderForm mOrder;
    private String payType;
    private TextView tv_title,tv_chang1,tv_need_pay,tv_chang2,tv_has_to_pay,tv_jkc_name,tv_jkc_num;
    private double hasPayNum,totalpayNum;
    private boolean charge;//true充值  false支付
    private LinearLayout rl_pay,rl_weixin_pay,ll_jkc,tv_weixinpay_title,ll_jkc_ticket_content;
    private ImageView mSweepIV;
    private TextView tv_weixin_need_pay;
    private String orderId,authorizeUrl,consumeGoldCoinCount;
    private MyRecever mRecever;
    private View activity_sdk_pay;
    private LinearLayout iv_net_error;
    private TextView tx_tips;
    private TextView tv_yuan;
    private Button rb_confim,rb_cancel;
    private LinearLayout ll_has_to_pay;
    private TextView tv_jkc_paynum,tv_jkc_ticket_name,tv_jkc_ticket_num;
    private TasktimerView tv_jkc_paytime;
    private ImageButton ib_closed;
    private int orderType,numType;
    private GoldOrderData mGoldOrder;
    private int gameMustCoin,userLeftCoin;
    private String appid,ticketNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);

        setContentView(R.layout.sdk_pay);


        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.PAY_SUCCESS);
        filter.addAction(IConstants.PAY_FAIL);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever,filter);


        if(getIntent()!=null) {
            appid = getIntent().getStringExtra("appid");
            orderId = getIntent().getStringExtra("orderId");
            authorizeUrl = getIntent().getStringExtra("authorizeUrl");
            consumeGoldCoinCount = getIntent().getStringExtra("consumeGoldCoinCount");
            orderType = getIntent().getIntExtra("orderType",1);

            ticketNum = getIntent().getStringExtra("ticketNum");
            numType = getIntent().getIntExtra("numType",0);
            L.info("","consumeGoldCoinCount ------------: "+consumeGoldCoinCount);
        }


        initUI();

        if(orderType==1){//即开采支付

            ib_closed.setVisibility(View.VISIBLE);
            if(numType!=0){
                ll_jkc_ticket_content.setVisibility(View.VISIBLE);
                if(numType == 1)
                    tv_jkc_ticket_name.setText("包号：");
                else
                    tv_jkc_ticket_name.setText("票号：");
                tv_jkc_ticket_num.setText(ticketNum);
            }


            queryPayInfo();
        }
        else if(orderType==2){//金豆充值
            ib_closed.setVisibility(View.VISIBLE);

            ll_jkc.setVisibility(View.GONE);
            SDKManager.getInstance().runGoldPayScheduledTask(DeviceUtil.getDeviceId2Ipad(PayActivityCC.this),orderType);
            Bitmap bitmap = Utils.createQRImage(authorizeUrl);
            rl_pay.setVisibility(View.GONE);
            rl_weixin_pay.setVisibility(View.VISIBLE);
            mSweepIV.setImageBitmap(bitmap);
            if(bitmap == null){
                tx_tips.setText("二维码已过期");
                tv_yuan.setVisibility(View.GONE);
                tv_weixin_need_pay.setVisibility(View.GONE);
            }else{
                tv_weixin_need_pay.setVisibility(View.VISIBLE);
                tv_weixin_need_pay.setText(((double)(Integer.parseInt(consumeGoldCoinCount)))+"");
            }

        } else{//金豆消耗
            consumeGoldCoin(consumeGoldCoinCount);
        }


    }

    private void consumeGoldCoin(String consumeGoldCoinCount) {
        showProgress("");
        RequestCenter.consumeGoldCoin(appid,L.deviceNo, consumeGoldCoinCount, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismissProgress();

                try {
                    int status =((JSONObject)responseObj).getInt("status");
                    if(status == 1){
                        iv_net_error.setVisibility(View.GONE);
                        mGoldOrder = new Gson().fromJson(((JSONObject)responseObj).toString(), GoldOrderData.class);
                        if(mGoldOrder!=null)
                            initGoldData(mGoldOrder);

                    }
                    else{
                        Utils.showToastCenter(PayActivityCC.this,((JSONObject)responseObj).getString("message"));
                        iv_net_error.setVisibility(View.VISIBLE);
                        ib_closed.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(PayActivityCC.this,((OkHttpException)reasonObj).getMsg()+"");
                iv_net_error.setVisibility(View.VISIBLE);
                ib_closed.setVisibility(View.VISIBLE);

            }
        });


    }

    private void initGoldData(GoldOrderData mGoldOrder) {

        rl_pay.setVisibility(View.VISIBLE);
        rl_weixin_pay.setVisibility(View.GONE);

            tv_chang2.setText("金豆");


            if(!mGoldOrder.getData().isPlayCoin()) {//用户金币不够，需充值剩余金币
                ib_closed.setVisibility(View.VISIBLE);
                tv_title.setText("金豆不足");
                tv_chang1.setText("还需充值 ");

                gameMustCoin = mGoldOrder.getData().getGameMustCoin();//游戏所需消耗金豆
                userLeftCoin = mGoldOrder.getData().getUserLeftCoin();//用户现剩余金豆
                authorizeUrl = mGoldOrder.getData().getUrl();//微信支付二维码
                orderId = mGoldOrder.getData().getOrderId()+"";
                hasPayNum = gameMustCoin-userLeftCoin;


                tv_need_pay.setText(""+hasPayNum);

                tv_has_to_pay.setText((double)(hasPayNum/100)+"");
                ll_has_to_pay.setVisibility(View.VISIBLE);

            }else{

                BroadcastUtil.sendBroadcastToUI(this, IConstants.PAY_SUCCESS,orderType+"");


                Utils.showToastCenter(this,"支付成功！");

                finish();

                /*tv_title.setText("金豆支付");
                tv_chang1.setText("确认支付 ");

                ll_has_to_pay.setVisibility(View.GONE);*/
            }


    }

    private void initUI() {
        iv_net_error = (LinearLayout) findViewById(R.id.ll_net_error);
        tx_tips = (TextView) findViewById(R.id.tx_tips);
        tv_yuan = (TextView) findViewById(R.id.tv_yuan);
        tv_weixin_need_pay = (TextView) findViewById(R.id.tv_weixin_need_pay);
        tv_weixinpay_title = (LinearLayout) findViewById(R.id.tv_weixinpay_title);
        if(!NetStatus.getNetStatus(this)){
            Utils.showToastCenter(this,"您的网络已断开，请检查网络！");
            iv_net_error.setVisibility(View.VISIBLE);
            tx_tips.setText("网络差，二维码加载失败");
            tv_yuan.setVisibility(View.GONE);
            tv_weixin_need_pay.setVisibility(View.GONE);
        }else{
            iv_net_error.setVisibility(View.GONE);
            tv_yuan.setVisibility(View.VISIBLE);
            tv_weixin_need_pay.setVisibility(View.VISIBLE);
            tx_tips.setText("请用微信扫码支付");
        }

        ib_closed = (ImageButton)findViewById(R.id.ib_closed);
        ib_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderType == 1)
                    cancelOrder(orderId);
                else
                    finish();
            }
        });


        tv_jkc_paynum = (TextView) findViewById(R.id.tv_jkc_paynum);

        activity_sdk_pay = findViewById(R.id.activity_sdk_pay);
        activity_sdk_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        rl_pay = (LinearLayout)findViewById(R.id.rl_pay);
        rl_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rl_weixin_pay = (LinearLayout)findViewById(R.id.rl_weixin_pay);
        rl_weixin_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_chang1 = (TextView) findViewById(R.id.tv_chang1);
        tv_need_pay = (TextView) findViewById(R.id.tv_need_pay);
        tv_chang2 = (TextView) findViewById(R.id.tv_chang2);

        tv_has_to_pay = (TextView) findViewById(R.id.tv_has_to_pay);
        ll_has_to_pay = (LinearLayout) findViewById(R.id.ll_has_to_pay);
        mSweepIV = (ImageView) findViewById(sweepIV);

        rb_confim = (Button)findViewById(R.id.rb_confim);
        rb_cancel = (Button)findViewById(R.id.rb_cancel);

        ll_jkc = (LinearLayout)findViewById(R.id.ll_jkc);
        ll_jkc_ticket_content = (LinearLayout)findViewById(R.id.ll_jkc_ticket_content);//随机一包/张 布局
        tv_jkc_paytime = (TasktimerView) findViewById(R.id.tv_jkc_paytime);//即开采倒计时

        tv_jkc_name = (TextView)findViewById(R.id.tv_jkc_name);//票种
        tv_jkc_num = (TextView)findViewById(R.id.tv_jkc_num);//票种数量
        tv_jkc_paynum = (TextView)findViewById(R.id.tv_jkc_paynum);//即开彩支付金额
        tv_jkc_ticket_name = (TextView)findViewById(R.id.tv_jkc_ticket_name);//随机一包/张
        tv_jkc_ticket_num = (TextView)findViewById(R.id.tv_jkc_ticket_num);//随机一包/张  号




        rb_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderType==1)
                    SDKManager.getInstance().runPayScheduledTask(DeviceUtil.getDeviceId2Ipad(PayActivityCC.this));
                else{
                    SDKManager.getInstance().runGoldPayScheduledTask(DeviceUtil.getDeviceId2Ipad(PayActivityCC.this), orderType);
                }
                Bitmap bitmap = Utils.createQRImage(authorizeUrl);
                    if(orderType!=1){
                        ll_jkc.setVisibility(View.GONE);
                    }
                    rl_pay.setVisibility(View.GONE);
                    rl_weixin_pay.setVisibility(View.VISIBLE);
                    mSweepIV.setImageBitmap(bitmap);
                    if(bitmap == null){
                        tx_tips.setText("二维码已过期");
                        tv_yuan.setVisibility(View.GONE);
                        tv_weixin_need_pay.setVisibility(View.GONE);
                    }else{
                        tv_weixin_need_pay.setVisibility(View.VISIBLE);
                        tv_weixin_need_pay.setText(((double)hasPayNum/100)+"");
                    }
            }
        });


        rb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(orderId);
            }
        });
    }

    private void initData(OrderForm mOrder) {



        rl_pay.setVisibility(View.GONE);
        rl_weixin_pay.setVisibility(View.VISIBLE);
        ll_jkc.setVisibility(View.VISIBLE);
        tv_weixinpay_title.setVisibility(View.INVISIBLE);
        tv_weixin_need_pay.setVisibility(View.GONE);


        tv_jkc_name.setText(mOrder.getData().getLotteryName()+"");


        tv_jkc_paynum.setText(mOrder.getData().getMoney()+"");

        tv_jkc_num.setText(mOrder.getData().getCount()+"");

        Bitmap bitmap = Utils.createQRImage(authorizeUrl);
        mSweepIV.setImageBitmap(bitmap);
        SDKManager.getInstance().runPayScheduledTask(DeviceUtil.getDeviceId2Ipad(this));


        //初始化时间
        tv_jkc_paytime.initTime(0,3,0);
        tv_jkc_paytime.start();

        tv_jkc_paytime.setOnTimeCompleteListener    (new TasktimerView.OnTimeCompleteListener() {
            @Override
            public void onTimeComplete() {
                cancelOrder(orderId);
            }
        });



    }

    private void cancelOrder(String orderId) {

        if(StringUtils.isEmpty(orderId))finish();

        showProgress("");

        RequestCenter.cancelOrder(orderId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                BroadcastUtil.sendBroadcastToUI(PayActivityCC.this,IConstants.ORDER_CANCEL,null);
                dismissProgress();
                int status = 0;
                try {
                    status = ((JSONObject)responseObj).getInt("status");
                    if(status == 1){

                    }else{
                        Utils.showToastCenter(PayActivityCC.this,((JSONObject)responseObj).getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(PayActivityCC.this,IConstants.ORDER_CANCEL,null);
                dismissProgress();
                Utils.showToastCenter(PayActivityCC.this,((OkHttpException)reasonObj).getMsg()+"");
                finish();
            }
        });

    }

    //查询订单信息
    private void queryPayInfo() {

        showProgress("请求中...");

        RequestCenter.queryPayInfo(orderId,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismissProgress();

                try {
                    int status =((JSONObject)responseObj).getInt("status");
                    if(status == 1){
                        iv_net_error.setVisibility(View.GONE);
                        mOrder = new Gson().fromJson(((JSONObject)responseObj).toString(), OrderForm.class);
                        if(mOrder!=null)
                            initData(mOrder);

                    }
                    else{
//                        Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
                        Utils.showToastCenter(PayActivityCC.this,((JSONObject)responseObj).getString("message"));
                        iv_net_error.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
//                Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
                Utils.showToastCenter(PayActivityCC.this,((OkHttpException)reasonObj).getMsg()+"");
                iv_net_error.setVisibility(View.VISIBLE);
            }
        });

    }


    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.PAY_SUCCESS.equals(intent.getAction())){
                if(mOrder!=null){
                    double mMoney = mOrder.getData().getMoney();
                    Utils.showToastCenter(PayActivityCC.this, "感谢您为体育公益事业贡献了 "+(mMoney*0.2)+" 元公益金");
                }else{
                    Utils.showToastCenter(PayActivityCC.this, "支付成功");
                }
                finish();
            }else if(IConstants.PAY_FAIL.equals(intent.getAction())){
                Utils.showToastCenter(PayActivityCC.this, "订单异常，请重新下单");
                finish();
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus)
            hideSystemUI(this);
        super.onWindowFocusChanged(hasFocus);
    }
    public void hideSystemUI(Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mRecever);
        SDKManager.getInstance().cancleScheduledTask();
        dismissProgress();
        onDetachedFromWindow();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
