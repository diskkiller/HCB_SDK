/*
package com.hcb.hcbsdk.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.msgBean.OrderData;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

*/
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
 *//*

public class RechargeGoldActivityC extends JKCBaseActivity {


    RadioGroup rgChoose;
    EditText etCustom;
    Button btnSure, btnCancle;
    String rechargeNum = "";
    String totalMoney = "";
    TextView tvNeedMoney;
    private View btnClose;
    private OrderData mOrderData;
    private String appid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);

        setContentView(R.layout.sdk_activity_recharge_gold);

        if (getIntent() != null) {
            appid = getIntent().getStringExtra("appid");
        }

        initUI();
        initRadioGroup();

    }

    private void initUI() {

        btnClose = findViewById(R.id.btn_recharge_gold_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvNeedMoney = (TextView) findViewById(R.id.tv_recharge_gold_need_money);


        rgChoose = (RadioGroup) findViewById(R.id.rg_recharge_gold_choose);

        etCustom = (EditText) findViewById(R.id.et_recharge_gold_custom);
        etCustom.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNeedMoney.setVisibility(View.INVISIBLE);
                rgChoose.check(-1);
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
                        tvNeedMoney.setVisibility(View.INVISIBLE);
//                        Utils.showToastCenter(RechargeGoldActivity.this,"请输入正确的金额！");
                    } else {
                        tvNeedMoney.setVisibility(View.VISIBLE);
                        tvNeedMoney.setText("需支付 " + (double) (Integer.parseInt(rechargeNum) / 100) + " 元");
                    }
                }

            }
        });


        btnSure = (Button) findViewById(R.id.btn_recharge_gold_sure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rechargeNum.equals("") || !L.debug && Integer.parseInt(rechargeNum) < 100 || Integer.parseInt(rechargeNum) % 100 != 0) {
                    Utils.showToastCenter(RechargeGoldActivityC.this, "请输入正确的金额！");
                } else {

                    charge();

                }
            }
        });
        btnCancle = (Button) findViewById(R.id.btn_charge_gold_cancel);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void charge() {


        if (SDKManager.getInstance().getUser() == null) {
            Utils.showToastCenter(RechargeGoldActivityC.this, "请先登录！");
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
                        }
                        SDKManager.getInstance().startPayPage(orderId + "", authorizeUrl, 2, (Integer.parseInt(rechargeNum) / 100) + "");
                        finish();
                    } else {
                        Utils.showToastCenter(RechargeGoldActivityC.this, ((JSONObject) responseObj).getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(RechargeGoldActivityC.this, ((OkHttpException) reasonObj).getMsg() + "");
            }
        });
    }


    private void initRadioGroup() {
        rgChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton edt = (RadioButton) findViewById(checkedId);

                if (edt != null) {
                    etCustom.setText("");
                    rechargeNum = edt.getText().toString().replace("金豆", "").trim();
                    tvNeedMoney.setVisibility(View.VISIBLE);
                    tvNeedMoney.setText("需支付 " + (double) (Integer.parseInt(rechargeNum) / 100) + " 元");
                }
            }
        });
        rgChoose.check(R.id.rb_recharge_gold1);
    }
}
*/
