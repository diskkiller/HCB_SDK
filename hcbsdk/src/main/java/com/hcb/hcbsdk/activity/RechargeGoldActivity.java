package com.hcb.hcbsdk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.activity.widget.dialog.NormalDialog;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.msgBean.OrderData;
import com.hcb.hcbsdk.service.msgBean.OrderForm;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.internal.Util;

/**
 * @author WangGuoWei
 * @time 2018/4/13 16:34
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
public class RechargeGoldActivity extends JKCBaseActivity {


    RadioGroup rgChoose;
    EditText etCustom;
    Button btnSure, btnCancle;
    String rechargeNum = "";
    TextView tvNeedMoney;
    private View btnClose;
    private OrderData mOrderData;
    private String appid;
    private AnimationSet animationSet;
    private View typeLayOut;
    private RadioButton edt;
    private TextInputLayout gold_input_layout;
    private NormalDialog dialog;
    private ImageView sweepIV;
    private MyRecever mRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);

        setContentView(R.layout.sdk_recharge_gold);
        dialog = new NormalDialog(this);
        if (getIntent() != null) {
            appid = getIntent().getStringExtra("appid");
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.PAY_SUCCESS);
        filter.addAction(IConstants.PAY_FAIL);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever,filter);

        SDKManager.getInstance().runGoldPayScheduledTask(DeviceUtil.getDeviceId2Ipad(this),2);


        initUI();
        initRadioGroup();

        startAnimation(findViewById(R.id.recharge_gold));

    }

    private void initUI() {

        btnClose = findViewById(R.id.btn_recharge_gold_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sweepIV = findViewById(R.id.sweepIV);

        tvNeedMoney = (TextView) findViewById(R.id.tv_recharge_gold_need_money);


        rgChoose = (RadioGroup) findViewById(R.id.rg_recharge_gold_choose);


    }




    private void initRadioGroup() {
        rgChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(edt!=null)
                    startNormalAnimation(edt);

                edt = (RadioButton) findViewById(checkedId);

                if (edt != null) {
                    startChooseAnimation(edt);

                    if(checkedId == R.id.rb_recharge_gold5){

                        showDialog();

                    }else{
                        rechargeNum = edt.getText().toString();
                        charge();
                    }

                }
            }
        });

        rgChoose.check(R.id.rb_recharge_gold1);
    }


    private void showDialog() {
        typeLayOut = View.inflate(this, R.layout
                .sdk_dialog_recharge_gold, null);
        typeLayOut.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        gold_input_layout = (TextInputLayout)typeLayOut.findViewById(R.id.gold_input_layout);
        etCustom = (EditText) typeLayOut.findViewById(R.id.et_recharge_gold_custom);
        etCustom.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rechargeNum = "";
            }
        });
        etCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rechargeNum = etCustom.getText().toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rechargeNum = s.toString();




                if (!StringUtils.isEmpty(rechargeNum)) {
                    if (!L.debug || Integer.parseInt(rechargeNum) % 100 != 0) {
                        gold_input_layout.setErrorEnabled(true);
                        gold_input_layout.setError("请输入正确的金额!");
                    } else {
                        gold_input_layout.setErrorEnabled(false);
                    }
                }

            }
        });


        btnSure = (Button) typeLayOut.findViewById(R.id.btn_recharge_gold_sure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(rechargeNum)){

                    if (rechargeNum.equals("") || !L.debug && Integer.parseInt(rechargeNum) < 100 || Integer.parseInt(rechargeNum) % 100 != 0) {
                        Utils.showToastCenter(RechargeGoldActivity.this, "请输入正确的金额！");
                    } else {
                        if(edt!=null)
                            startNormalAnimation(edt);
                        rgChoose.check(-1);
                        charge();
                    }
                }
            }
        });
        btnCancle = (Button) typeLayOut.findViewById(R.id.btn_charge_gold_cancel);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt!=null)
                    startNormalAnimation(edt);
                rgChoose.check(-1);
                rechargeNum = "";
                dialog.dismiss();
                Utils.closeKeybord(RechargeGoldActivity.this);
            }
        });


        dialog.contentView = typeLayOut;
        dialog.widthScale(0.5f);
        dialog.isKongBai = false;
        dialog.btnNum(0)
                .cornerRadius(15)
                .isTitleShow(false)
                .show();
    }

    private long mDuration = 1000;

    private void startAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation alpha = new AlphaAnimation( 0, 1);
        alpha.setDuration(mDuration * 3 / 2);
        Animation scale = new ScaleAnimation((float) 0.5, 1, (float) 0.5,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
//                ib_closed.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animationSet);
    }



    private void startChooseAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation scale = new ScaleAnimation(1, (float) 1.1, 1,(float) 1.1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(200);

        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);


        view.startAnimation(animationSet);
    }
    private void startNormalAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation scale = new ScaleAnimation((float) 1.1, 1, (float) 1.1,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scale.setDuration(200);

        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);


        view.startAnimation(animationSet);
    }




    private void charge() {


        if (SDKManager.getInstance().getUser() == null) {
            Utils.showToastCenter(RechargeGoldActivity.this, "请先登录！");
            return;
        }


        RequestCenter.goldCoinCharge(appid, DeviceUtil.getDeviceId2Ipad(this), rechargeNum, (Integer.parseInt(rechargeNum) / 100) + "", new DisposeDataListener() {


            @Override
            public void onSuccess(Object responseObj) {
                try {
                    int orderId = 0;
                    String authorizeUrl = "";
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        mOrderData = new Gson().fromJson(((JSONObject) responseObj).toString(), OrderData.class);
                        if (mOrderData != null) {
                            orderId = mOrderData.getData().getOrderId();
                            authorizeUrl = mOrderData.getData().getUrl();
                            Bitmap bitmap = Utils.createQRImage(authorizeUrl);
                            sweepIV.setImageBitmap(bitmap);
                            tvNeedMoney.setText("需支付 " + (double) (Integer.parseInt(rechargeNum) / 100) + " 元");
                        }
                        if(dialog!=null&&dialog.isShowing()){
                            dialog.dismiss();
                            Utils.closeKeybord(RechargeGoldActivity.this);
                        }
                    } else {
                        Utils.showToastCenter(RechargeGoldActivity.this, ((JSONObject) responseObj).getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(RechargeGoldActivity.this, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });


    }

    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.PAY_SUCCESS.equals(intent.getAction())){
                Utils.showToastCenter(RechargeGoldActivity.this, "支付成功");
                finish();
            }else if(IConstants.PAY_FAIL.equals(intent.getAction())){
                Utils.showToastCenter(RechargeGoldActivity.this, "订单异常，请重新下单");
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mRecever);
        SDKManager.getInstance().cancleScheduledTask();
        super.onDestroy();
    }
}
