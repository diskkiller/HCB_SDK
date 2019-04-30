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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.GoldOrderData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.service.msgBean.OrderForm;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
import com.hcb.hcbsdk.util.dodo.NetStatus;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hcb.hcbsdk.R.id.sweepIV;
import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.KEY_FILE_NAME;

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
    private OrderForm mOrder;
    private String payType;
    private TextView tv_title, tv_chang1, tv_need_pay, tv_chang2, tv_has_to_pay, tv_jkc_name, tv_jkc_num;
    private double hasPayNum, totalpayNum;
    private boolean charge;//true充值  false支付
    private ImageView mSweepIV, code_sweepIV;
    private TextView tv_weixin_need_pay;
    private String orderId, authorizeUrl, consumeGoldCoinCount;
    private MyRecever mRecever;
    private View activity_sdk_pay;
    private TextView tx_tips;
    private TextView tv_yuan;
    private Button rb_confim, rb_cancel;
    private LinearLayout ll_has_to_pay;
    private TextView tv_jkc_paynum, tv_jkc_ticket_name, tv_jkc_ticket_num;
    private TasktimerView tv_jkc_paytime;
    private ImageButton ib_closed;
    private int orderType, numType;
    private GoldOrderData mGoldOrder;
    private int gameMustCoin, userLeftCoin;
    private String appid, ticketNum;
    private AnimationSet animationSet;
    private long mDuration = 1000;
    private ImageButton ib_error_closed;
    private ImageButton ib_gold_closed;
    private ImageButton ib_code_closed;
    private TextView tv_code_weixin_need_pay;
    private TextView tv_price;
    private LinearLayout ll_jkc_ticket_content;
    private String aliPayScheduledId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);

        setContentView(R.layout.sdk_new_pay);


        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.PAY_SUCCESS);
        filter.addAction(IConstants.PAY_FAIL);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever, filter);


        if (getIntent() != null) {
            appid = getIntent().getStringExtra("appid");

            authorizeUrl = getIntent().getStringExtra("authorizeUrl");
            consumeGoldCoinCount = getIntent().getStringExtra("consumeGoldCoinCount");
            orderType = getIntent().getIntExtra("orderType", 1);

            ticketNum = getIntent().getStringExtra("ticketNum");
            numType = getIntent().getIntExtra("numType", 0);
            payType = getIntent().getStringExtra("payType");

            orderId = getIntent().getStringExtra("orderId");

            aliPayScheduledId = getIntent().getStringExtra("aliPayQueryId");


            L.info("", "consumeGoldCoinCount ------------: " + consumeGoldCoinCount);
        }


        initUI();

        if (orderType == 1) {//即开采支付
            if (numType != 0) {
                if (numType == 1)
                    tv_jkc_ticket_name.setText("包号：");
                else
                    tv_jkc_ticket_name.setText("票号：");
                tv_jkc_ticket_num.setText(ticketNum);
            }else{
                ll_jkc_ticket_content.setVisibility(View.GONE);
            }


            queryPayInfo();
        } else if (orderType == 2) {//金豆充值

        } else {//金豆消耗
            consumeGoldCoin(consumeGoldCoinCount);
        }


    }


    private void showVisibleView(int viewID) {
        if (viewID == R.id.sdk_gold_buy) {
            sdk_gold_buy.setVisibility(View.VISIBLE);
            sdk_jkc_pay.setVisibility(View.GONE);
            sdk_pay_error.setVisibility(View.GONE);
            sdk_authorize_code.setVisibility(View.GONE);

        } else if (viewID == R.id.sdk_jkc_pay) {
            sdk_gold_buy.setVisibility(View.GONE);
            sdk_jkc_pay.setVisibility(View.VISIBLE);
            sdk_pay_error.setVisibility(View.GONE);
            sdk_authorize_code.setVisibility(View.GONE);

        } else if (viewID == R.id.sdk_pay_error) {
            sdk_gold_buy.setVisibility(View.GONE);
            sdk_jkc_pay.setVisibility(View.GONE);
            sdk_pay_error.setVisibility(View.VISIBLE);
            sdk_authorize_code.setVisibility(View.GONE);

        } else if (viewID == R.id.sdk_authorize_code) {
            sdk_gold_buy.setVisibility(View.GONE);
            sdk_jkc_pay.setVisibility(View.GONE);
            sdk_pay_error.setVisibility(View.GONE);
            sdk_authorize_code.setVisibility(View.VISIBLE);

        }

        startAnimation(findViewById(viewID));
    }


    private void startAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(mDuration * 3 / 2);
        Animation scale = new ScaleAnimation((float) 0.5, 1, (float) 0.5, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ib_closed.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationSet);
    }


    private void consumeGoldCoin(String consumeGoldCoinCount) {
        showProgress("");
        RequestCenter.consumeGoldCoin(appid, L.deviceNo, consumeGoldCoinCount, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismissProgress();

                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        mGoldOrder = new Gson().fromJson(((JSONObject) responseObj).toString(), GoldOrderData.class);
                        if (mGoldOrder != null)
                            initGoldData(mGoldOrder);

                    } else {
                        Utils.showToastCenter(PayActivityC.this, ((JSONObject) responseObj).getString("message"));
                        showVisibleView(R.id.sdk_pay_error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(PayActivityC.this, ((OkHttpException) reasonObj).getMsg() + "");
                showVisibleView(R.id.sdk_pay_error);
            }
        });


    }

    private void initGoldData(GoldOrderData mGoldOrder) {


        if (!mGoldOrder.getData().isPlayCoin()) {//用户金币不够，需充值剩余金币

            showVisibleView(R.id.sdk_gold_buy);

            tv_chang2.setText("金豆");
            tv_title.setText("金豆不足");
            tv_chang1.setText("还需充值 ");

            gameMustCoin = mGoldOrder.getData().getGameMustCoin();//游戏所需消耗金豆
            userLeftCoin = mGoldOrder.getData().getUserLeftCoin();//用户现剩余金豆
            authorizeUrl = mGoldOrder.getData().getUrl();//微信支付二维码
            orderId = mGoldOrder.getData().getOrderId() + "";
            hasPayNum = gameMustCoin - userLeftCoin;


            tv_need_pay.setText("" + hasPayNum);

            tv_has_to_pay.setText((double) (hasPayNum / 100) + "");

        } else {

            BroadcastUtil.sendBroadcastToUI(this, IConstants.PAY_SUCCESS, orderType + "");


            Utils.showToastCenter(this, "支付成功！");

            finish();

        }


    }

    private View sdk_gold_buy, sdk_jkc_pay, sdk_pay_error, sdk_authorize_code;

    private void initUI() {

        sdk_gold_buy = findViewById(R.id.sdk_gold_buy);
        sdk_jkc_pay = findViewById(R.id.sdk_jkc_pay);
        sdk_pay_error = findViewById(R.id.sdk_pay_error);
        sdk_authorize_code = findViewById(R.id.sdk_authorize_code);


        tx_tips = (TextView) findViewById(R.id.tx_tips);
        tv_yuan = (TextView) findViewById(R.id.tv_yuan);
        tv_weixin_need_pay = (TextView) findViewById(R.id.tv_weixin_need_pay);
        tv_code_weixin_need_pay = (TextView) findViewById(R.id.tv_code_weixin_need_pay);
        if (!NetStatus.getNetStatus(this)) {
            Utils.showToastCenter(this, "您的网络已断开，请检查网络！");
            showVisibleView(R.id.sdk_pay_error);
        }

        ib_closed = (ImageButton) findViewById(R.id.ib_closed);
        ib_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(orderId);
            }
        });
        ib_error_closed = (ImageButton) findViewById(R.id.ib_error_closed);
        ib_error_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ib_gold_closed = (ImageButton) findViewById(R.id.ib_gold_closed);
        ib_gold_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ib_code_closed = (ImageButton) findViewById(R.id.ib_code_closed);
        ib_code_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_jkc_paynum = (TextView) findViewById(R.id.tv_jkc_paynum);


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_chang1 = (TextView) findViewById(R.id.tv_chang1);
        tv_need_pay = (TextView) findViewById(R.id.tv_need_pay);
        tv_chang2 = (TextView) findViewById(R.id.tv_chang2);

        tv_has_to_pay = (TextView) findViewById(R.id.tv_has_to_pay);
        ll_has_to_pay = (LinearLayout) findViewById(R.id.ll_has_to_pay);
        mSweepIV = (ImageView) findViewById(sweepIV);
        code_sweepIV = (ImageView) findViewById(R.id.code_sweepIV);

        rb_confim = (Button) findViewById(R.id.rb_confim);
        rb_cancel = (Button) findViewById(R.id.rb_cancel);

        tv_jkc_paytime = (TasktimerView) findViewById(R.id.tv_jkc_paytime);//即开采倒计时

        ll_jkc_ticket_content = (LinearLayout)findViewById(R.id.ll_jkc_ticket_content);//随机一包/张 布局
        tv_price = (TextView) findViewById(R.id.tv_price);//单价
        tv_jkc_name = (TextView) findViewById(R.id.tv_jkc_name);//票种
        tv_jkc_num = (TextView) findViewById(R.id.tv_jkc_num);//票种数量
        tv_jkc_paynum = (TextView) findViewById(R.id.tv_jkc_paynum);//即开彩支付金额
        tv_jkc_ticket_name = (TextView) findViewById(R.id.tv_jkc_ticket_name);//随机一包/张
        tv_jkc_ticket_num = (TextView) findViewById(R.id.tv_jkc_ticket_num);//随机一包/张  号



        rb_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKManager.getInstance().runGoldPayScheduledTask(DeviceUtil.getDeviceId2Ipad(PayActivityC.this), orderType);
                Bitmap bitmap = Utils.createQRImage(authorizeUrl);
                showVisibleView(R.id.sdk_authorize_code);
                code_sweepIV.setImageBitmap(bitmap);
                tv_code_weixin_need_pay.setText(((double) hasPayNum / 100) + "");
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


        showVisibleView(R.id.sdk_jkc_pay);

        tv_jkc_name.setText(mOrder.getData().getLotteryName() + " ");


        tv_jkc_paynum.setText(mOrder.getData().getMoney() + "");

        tv_jkc_num.setText(mOrder.getData().getCount() + "");

        tv_price.setText(mOrder.getData().getPrice() + "");

        Bitmap bitmap = Utils.createQRImage(authorizeUrl);
        mSweepIV.setImageBitmap(bitmap);
        if(payType.equals("2"))
            SDKManager.getInstance().runPayScheduledTask(DeviceUtil.getDeviceId2Ipad(this),aliPayScheduledId,payType);
        else
            SDKManager.getInstance().runPayScheduledTask(DeviceUtil.getDeviceId2Ipad(this),orderId,payType);


        //初始化时间
        tv_jkc_paytime.initTime(0, 3, 0);
        tv_jkc_paytime.start();

        tv_jkc_paytime.setOnTimeCompleteListener(new TasktimerView.OnTimeCompleteListener() {
            @Override
            public void onTimeComplete() {
//                cancelOrder(orderId);
                BroadcastUtil.sendBroadcastToUI(PayActivityC.this, IConstants.ORDER_CANCEL, null);
                finish();
            }
        });


    }

    private void cancelOrder(String orderId) {

        if (StringUtils.isEmpty(orderId)) finish();

        showProgress("");

        RequestCenter.cancelOrder(orderId, new DisposeDataListener() {

            @Override
            public void onSuccess(Object responseObj) {
                BroadcastUtil.sendBroadcastToUI(PayActivityC.this, IConstants.ORDER_CANCEL, null);
                dismissProgress();
                int status = 0;
                try {
                    status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                    } else {
                        Utils.showToastCenter(PayActivityC.this, ((JSONObject) responseObj).getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                BroadcastUtil.sendBroadcastToUI(PayActivityC.this, IConstants.ORDER_CANCEL, null);
                dismissProgress();
                Utils.showToastCenter(PayActivityC.this, ((OkHttpException) reasonObj).getMsg() + "");
                finish();
            }
        });

    }

    //查询订单信息
    private void queryPayInfo() {

        showProgress("请求中...");

        RequestCenter.queryPayInfo(orderId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                dismissProgress();

                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        mOrder = new Gson().fromJson(((JSONObject) responseObj).toString(), OrderForm.class);
                        if (mOrder != null)
                            initData(mOrder);

                    } else {
                        Utils.showToastCenter(PayActivityC.this, ((JSONObject) responseObj).getString("message"));
                        showVisibleView(R.id.sdk_pay_error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(PayActivityC.this, ((OkHttpException) reasonObj).getMsg() + "");
                showVisibleView(R.id.sdk_pay_error);
            }
        });

    }


    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.PAY_SUCCESS.equals(intent.getAction())) {
                if (mOrder != null) {
                    double mMoney = mOrder.getData().getMoney();
                    Utils.showToastCenter(PayActivityC.this, "感谢您为体育公益事业贡献了 " + (mMoney * 0.2) + " 元公益金");
                } else {
                    Utils.showToastCenter(PayActivityC.this, "支付成功");
                }
                finish();
            } else if (IConstants.PAY_FAIL.equals(intent.getAction())) {
                Utils.showToastCenter(PayActivityC.this, "订单异常，请重新下单");
                finish();
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
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
