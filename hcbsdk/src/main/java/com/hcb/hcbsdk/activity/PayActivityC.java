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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.logutils.save.imp.LogWriter;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.service.msgBean.OrderForm;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
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
public class PayActivityC extends JKCBaseActivity {
    public CheckBox mCheckBox;
    private OrderForm mOrder;
    private String payType;
    private TextView tv_title,tv_chang1,tv_need_pay,tv_chang2,tv_chang3,tv_has_to_pay,mTv_voucher;
    private double hasPayNum,totalpayNum;
    private boolean charge;//true充值  false支付
    private LinearLayout rl_pay,rl_weixin_pay;
    private ImageView mSweepIV;
    private TextView tv_weixin_need_pay;
    private String payId;
    private MyRecever mRecever;
    private View activity_sdk_pay;
    private LinearLayout iv_net_error;
    private TextView tx_tips;
    private TextView tv_yuan;
    private Button rb_confim,rb_cancel;
    private LinearLayout ll_has_to_pay,ll_voucher;

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
        mRecever = new MyRecever();
        this.registerReceiver(mRecever,filter);


        if(getIntent()!=null) {
            payId = getIntent().getStringExtra("payId");
        }


        initUI();

        queryPayInfo();


        rb_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOrder.getData().getCash()>0){//用户金币不够，需充值剩余金币
                    SDKManager.getInstance().runPayScheduledTask(payId);
                    Bitmap bitmap = Utils.createQRImage(mOrder.getData().getQrcode());
                    rl_pay.setVisibility(View.GONE);
                    rl_weixin_pay.setVisibility(View.VISIBLE);
                    mSweepIV.setImageBitmap(bitmap);
                    if(bitmap == null){
                        tx_tips.setText("二维码已过期");
                        tv_yuan.setVisibility(View.GONE);
                        tv_weixin_need_pay.setVisibility(View.GONE);
                    }else{
                        tv_weixin_need_pay.setVisibility(View.VISIBLE);
                        tv_weixin_need_pay.setText(mOrder.getData().getCash()+"");
                    }
                }else{//直接去支付
                    userPayOrder(mOrder.getData().getPayId());
                }
            }
        });

        rb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initUI() {
        iv_net_error = (LinearLayout) findViewById(R.id.ll_net_error);
        tx_tips = (TextView) findViewById(R.id.tx_tips);
        tv_yuan = (TextView) findViewById(R.id.tv_yuan);
        tv_weixin_need_pay = (TextView) findViewById(R.id.tv_weixin_need_pay);
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



        activity_sdk_pay = findViewById(R.id.activity_sdk_pay);
        activity_sdk_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        tv_chang3 = (TextView) findViewById(R.id.tv_chang3);
        tv_has_to_pay = (TextView) findViewById(R.id.tv_has_to_pay);
        ll_has_to_pay = (LinearLayout) findViewById(R.id.ll_has_to_pay);
        mSweepIV = (ImageView) findViewById(sweepIV);


        mTv_voucher = (TextView) findViewById(R.id.tv_voucher);
        ll_voucher = (LinearLayout) findViewById(R.id.ll_voucher);

        mCheckBox = (CheckBox) findViewById(R.id.child_checkbox);


        rb_confim = (Button)findViewById(R.id.rb_confim);
        rb_cancel = (Button)findViewById(R.id.rb_cancel);
    }

    private void initData(OrderForm mOrder) {
        payType = mOrder.getData().getType();
        hasPayNum = mOrder.getData().getCash();
        totalpayNum = mOrder.getData().getTotal();
        charge = mOrder.getData().getCharge();


        tv_need_pay.setText(""+(int)totalpayNum);

        if(charge){//兑换
            rl_pay.setVisibility(View.GONE);
            rl_weixin_pay.setVisibility(View.VISIBLE);
        }else{//直接显示支付页面
            rl_pay.setVisibility(View.VISIBLE);
            rl_weixin_pay.setVisibility(View.GONE);
        }

        if(payType.equals("gold")){//金币
            tv_chang3.setVisibility(View.GONE);
            if(hasPayNum>0) {//用户金币不够，需充值剩余金币
                tv_title.setText("金豆不足");
                tv_chang1.setText("还需充值 ");
                tv_need_pay.setText(""+(int)( (hasPayNum)*100));
            }else{
                tv_title.setText("金豆支付");
                tv_chang1.setText("确认支付 ");
            }
            tv_chang2.setText("金豆");

        }else if(payType.equals("point")){//积分
            tv_chang3.setVisibility(View.GONE);
            if(hasPayNum>0) {//用户积分不够，需充值剩余积分
                tv_title.setText("积分不足");
                tv_chang1.setText("还需兑换 ");
                tv_need_pay.setText(""+(int)( (hasPayNum)*100));
            }else{
                tv_title.setText("积分兑换");
                tv_chang1.setText("确认兑换 ");
            }
            tv_chang2.setText("积分");

        }
    }

    //查询订单信息
    private void queryPayInfo() {



        showProgress("请求中...");

        RequestCenter.queryPayInfo(payId,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismissProgress();

                try {
                    int status =((JSONObject)responseObj).getInt("status");
                    if(status == 10000){
                        iv_net_error.setVisibility(View.GONE);
                        mOrder = new Gson().fromJson(((JSONObject)responseObj).toString(), OrderForm.class);
                        initData(mOrder);

                        mCheckBox.setEnabled(false);
                        if(mOrder.getData().getBalance()>0){//有已使用的奖金
                            mCheckBox.setChecked(true);
                            mTv_voucher.setText(" "+mOrder.getData().getBalance()+" 元");
                        }else{//用户没有奖金
                            mCheckBox.setChecked(false);
                            mTv_voucher.setText(" 0.00 元");
                        }


                        if(mOrder.getData().getCash()==0&&mOrder.getData().getBalance()==0){//用户金币足够
                            tv_has_to_pay.setText("0.00");
                            ll_voucher.setVisibility(View.GONE);
                            ll_has_to_pay.setVisibility(View.GONE);
                        }else{//用户金币不够，需充值剩余金币
                            tv_has_to_pay.setText(mOrder.getData().getCash()+"");
                            ll_voucher.setVisibility(View.VISIBLE);
                            ll_has_to_pay.setVisibility(View.VISIBLE);
                            if(charge){//直接充值
                                tv_weixin_need_pay.setText(mOrder.getData().getCash()+"");
                                Bitmap bitmap = Utils.createQRImage(mOrder.getData().getQrcode());
                                rl_pay.setVisibility(View.GONE);
                                rl_weixin_pay.setVisibility(View.VISIBLE);
                                mSweepIV.setImageBitmap(bitmap);
                                SDKManager.getInstance().runPayScheduledTask(payId);
                            }
                        }
                    }
                    else{
                        Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
                        iv_net_error.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
                iv_net_error.setVisibility(View.VISIBLE);
            }
        });

    }
    //支付订单
    private void userPayOrder(String pid) {


        if(SDKManager.getInstance().getUser()==null){
            Utils.showToastCenter(PayActivityC.this,"请先登录！");
            return;
        }

        showProgress("支付中...");

        RequestCenter.userPayOrder(SDKManager.getInstance().getUser().getUid(),DeviceUtil.getDeviceId2Ipad(this),
                pid,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                dismissProgress();

                try {
                    int status =((JSONObject)responseObj).getInt("status");
                    LoginReslut loginReslut = new Gson().fromJson(((JSONObject)responseObj).toString(), LoginReslut.class);
                    if(status == 10000){

                        FileUtil.writeFile
                                (FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",((JSONObject)responseObj).toString(), false);

                        TuitaData.getInstance().setUser(loginReslut.getData());

                        BroadcastUtil.sendBroadcastToUI(PayActivityC.this, IConstants.PAY_SUCCESS,((JSONObject)responseObj).toString());

                        Utils.showToastCenter(PayActivityC.this, "支付成功");
                        finish();
                    }
                    else
                        Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(PayActivityC.this, "网络差，无法完成该操作~");
            }
        });

    }



    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.PAY_SUCCESS.equals(intent.getAction())){
                Utils.showToastCenter(PayActivityC.this, "支付成功");
                LogWriter.writeLog("PushService", "sdk支付成功返回数据  payActivity----"+SDKManager.getInstance().getUser().toString());
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
}
